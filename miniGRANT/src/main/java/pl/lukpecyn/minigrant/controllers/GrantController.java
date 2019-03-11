package pl.lukpecyn.minigrant.controllers;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.lukpecyn.minigrant.models.Beneficiary;
import pl.lukpecyn.minigrant.models.Budget;
import pl.lukpecyn.minigrant.models.Document;
import pl.lukpecyn.minigrant.models.Grant;
import pl.lukpecyn.minigrant.models.Payment;
import pl.lukpecyn.minigrant.services.BeneficiaryService;
import pl.lukpecyn.minigrant.services.BudgetService;
import pl.lukpecyn.minigrant.services.CostTypeService;
import pl.lukpecyn.minigrant.services.DocumentService;
import pl.lukpecyn.minigrant.services.DonorService;
import pl.lukpecyn.minigrant.services.GrantService;
import pl.lukpecyn.minigrant.services.GrantStatusService;
import pl.lukpecyn.minigrant.services.PaymentService;

@Controller
public class GrantController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(GrantController.class);

	@Autowired
	GrantService grantService;
	
	@Autowired
	BudgetService budgetService;

	@Autowired
	CostTypeService costTypeService;
	
	@Autowired
	DonorService donorService;

	@Autowired
	BeneficiaryService beneficiaryService;
	
	@Autowired
	GrantStatusService grantStatusService;

	@Autowired
	DocumentService documentService;

	@Autowired
	PaymentService paymentService;

	@RequestMapping(value = "/beneficiary/{idBeneficiary}/grant/{idGrant}")
	public String showGrant(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary, 
			@PathVariable(value="idGrant",required=true) Integer idGrant) throws ParseException {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && (grant.getIdBeneficiary()==beneficiary.getId())) {
			model.addAttribute("beneficiary", beneficiary);

			model.addAttribute("grant", grant);
			model.addAttribute("grantStatusList", grantStatusService.getGrantStatusList());
			
			List<Budget> budgetList = budgetService.getBudgetForGrantList(idGrant);
			model.addAttribute("budgetList", budgetList);
		
			List<Document> documentList = documentService.getDocumentForGrantList(idGrant);
			model.addAttribute("documentList", documentList);

			BigDecimal dotationPaid = new BigDecimal("0.00");
			BigDecimal contributionOwnPaid = new BigDecimal("0.00");
			BigDecimal contributionPersonalPaid = new BigDecimal("0.00");
			BigDecimal contributionInkindPaid = new BigDecimal("0.00");
			for(Budget budget : budgetList) {
				List<Payment> paymentList = paymentService.getPaymentForBudgetList(budget.getId());
				for (Payment payment : paymentList) {
					dotationPaid = dotationPaid.add(payment.getDotation());
					contributionOwnPaid = contributionOwnPaid.add(payment.getContributionOwn());
					contributionPersonalPaid = contributionPersonalPaid.add(payment.getContributionPersonal());
					contributionInkindPaid = contributionInkindPaid.add(payment.getContributionInkind());
				}
			}
			model.addAttribute("dotationPaid", dotationPaid);
			model.addAttribute("contributionOwnPaid", contributionOwnPaid);
			model.addAttribute("contributionPersonalPaid", contributionPersonalPaid);
			model.addAttribute("contributionInkindPaid", contributionInkindPaid);			
		
			return "grant";
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("/beneficiary/{idBeneficiary}/grant_form")
	public String addGrantFormGet(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true)Integer idBeneficiary) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		if (beneficiaryService.checkUser(beneficiary, principal.getName())>0) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("grantStatusList", grantStatusService.getGrantStatusList());
			model.addAttribute("donorList", donorService.getDonorList(idBeneficiary));
			Grant grant = new Grant();			
			grant.setIdBeneficiary(beneficiary.getId());
			model.addAttribute("grant", grant);
			return "grant_form";
		} else {
			return "redirect:/";
		}
	}
	
	@PostMapping("/beneficiary/{idBeneficiary}/grant_form")
	public String addGrantFormPost(Model model, Principal principal, Grant grant, 
			@PathVariable(value="idBeneficiary", required=true)Integer idBeneficiary) throws DataAccessException, ParseException {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		if(grant.getId()!=null) {
			if(grant.getId()<0) {
				if((grant.getIdBeneficiary()==idBeneficiary) && (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0)) {
					grant.setStatus(0);
					grantService.addGrant(grant);
				}
			}
		}
		return "redirect:/beneficiary/" + idBeneficiary;
	}

	@GetMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/grant_form")
	public String updateGrantFormGet(Model model, Principal principal, 
			@PathVariable(value="idBeneficiary", required=true)Integer idBeneficiary,
			@PathVariable(value="idGrant", required=true) Integer idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		if((grant.getIdBeneficiary()==idBeneficiary) && (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0)) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("grant", grant);
			model.addAttribute("grantStatusList", grantStatusService.getGrantStatusList());
			model.addAttribute("donorList", donorService.getDonorList(idBeneficiary));
			return "grant_form";
		} else {
			return "redirect:/";
		}
		
	}
	
	@PostMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/grant_form")
	public String updateGrantFormPost(Model model, Principal principal, Grant grant,  
			@PathVariable(value="idBeneficiary", required=true)Integer idBeneficiary,
			@PathVariable(value="idGrant", required=true) Integer idGrant) throws DataAccessException, ParseException {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		if((grant.getIdBeneficiary()==idBeneficiary) && (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0)) {
			grantService.updateGrant(grant);
			return "redirect:/beneficiary/" +beneficiary.getId() + "/grant/"+grant.getId();
		} else {
			return "redirect:/";
		}
	}
}
