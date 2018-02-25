package pl.lukpecyn.minigrant.controllers;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@GetMapping("/grant/grant_form")
	public String addGrantFormGet(Model model, Principal principal) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		model.addAttribute("grantStatusList", grantStatusService.getGrantStatusList());
		model.addAttribute("beneficiaryList", beneficiaryService.getBeneficiaryList(principal.getName()));
		//model.addAttribute("donorList", donorService.getDonorList());
		model.addAttribute("grant", new Grant());
		return "grant_form";
	}
	
	@PostMapping("/grant/grant_form")
	public String addGrantFormPost(Model model, Grant grant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		if(grant.getName()!=null){
			if(grant.getId()<0){
				grant.setStatus(0);
				grantService.addGrant(grant);
			}
		}
		return "redirect:/grant";
	}

	@GetMapping("/grant/{idGrant}/grant_form")
	public String updateGrantFormGet(Model model, Principal principal, @PathVariable(value="idGrant", required=true) Integer idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		model.addAttribute("grantStatusList", grantStatusService.getGrantStatusList());
		model.addAttribute("beneficiaryList", beneficiaryService.getBeneficiaryList(principal.getName()));
		//model.addAttribute("donorList", donorService.getDonorList());
		
		if(idGrant!=null){
			Grant grant = grantService.getGrant(idGrant);
			if(grant.getName()!=null) {
				model.addAttribute("grant", grant);
				return "grant_form";
			}
		}
		return "redirect:/grant";
	}
	
	@PostMapping("/grant/{idGrant}/grant_form")
	public String updateGrantFormPost(Model model, Grant grant, @PathVariable(value="idGrant", required=true) Integer idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		if(grant.getName()!=null){
			if(grant.getId()==idGrant){
				grant.setStatus(0);
				grantService.updateGrant(grant);
				return "redirect:/grant/"+grant.getId();
			}
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/grant/{idGrant}")
	public String showGrant(Model model, 
			@PathVariable("idGrant") long idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		model.addAttribute("grantStatusList", grantStatusService.getGrantStatusList());
		Grant grant = grantService.getGrant(idGrant);
		model.addAttribute("grant", grant);
				
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
	}
}
