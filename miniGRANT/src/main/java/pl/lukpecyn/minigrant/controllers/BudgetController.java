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

import pl.lukpecyn.minigrant.models.Beneficiary;
import pl.lukpecyn.minigrant.models.Budget;
import pl.lukpecyn.minigrant.models.CostType;
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
public class BudgetController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);

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
	
	@RequestMapping({"/beneficiary/{idBeneficiary}/grant/{idGrant}/budget/{idBudget}"})
	public String budgetShow(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary,
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idBudget", required=true) Integer idBudget) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		Budget budget = budgetService.getBudget(idBudget);
		
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && 
				(grant.getIdBeneficiary()==beneficiary.getId()) &&
				(budget.getIdGrant()==grant.getId())) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("budget", budget);
			model.addAttribute("grant", grant);
			
			List<Payment> paymentList = paymentService.getPaymentForBudgetList(idBudget);
			model.addAttribute("paymentList", paymentList);
			
			BigDecimal dotationPaid = new BigDecimal("0.00");
			BigDecimal contributionOwnPaid = new BigDecimal("0.00");
			BigDecimal contributionPersonalPaid = new BigDecimal("0.00");
			BigDecimal contributionInkindPaid = new BigDecimal("0.00");
			
			for (Payment payment : paymentList) {
				dotationPaid = dotationPaid.add(payment.getDotation());
				contributionOwnPaid = contributionOwnPaid.add(payment.getContributionOwn());
				contributionPersonalPaid = contributionPersonalPaid.add(payment.getContributionPersonal());
				contributionInkindPaid = contributionInkindPaid.add(payment.getContributionInkind());
			}
			model.addAttribute("dotationPaid", dotationPaid);
			model.addAttribute("contributionOwnPaid", contributionOwnPaid);
			model.addAttribute("contributionPersonalPaid", contributionPersonalPaid);
			model.addAttribute("contributionInkindPaid", contributionInkindPaid);
			
			return "budget";	
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/budget_form")
	public String addBudgetFormGet(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary, 
			@PathVariable(value="idGrant", required=true) Integer idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && (grant.getIdBeneficiary()==beneficiary.getId())) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("grant", grant);
			
			Budget budget = new Budget();
			budget.setIdGrant(grant.getId());
			model.addAttribute("budget", budget);
			
			List<CostType> costTypeList = costTypeService.getCostTypeList(beneficiary.getId());
			model.addAttribute("costTypeList", costTypeList);
		
			return "budget_form";
		} else {
			return "redirect:/";
		}
	}
	
	@PostMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/budget_form")
	public String addBudgetFormPost(Model model, Principal principal, Budget budget,
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary, 
			@PathVariable(value="idGrant", required=true) Integer idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && 
				(grant.getIdBeneficiary()==beneficiary.getId()) &&
				(budget.getIdGrant()==grant.getId())) {
			budgetService.addBudget(budget);
			return "redirect:/beneficiary/{idBeneficiary}/grant/{idGrant}";
		} else {
			return "redirect:/";
		}
		
	}

	@GetMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/budget/{idBudget}/budget_form")
	public String updateBudgetFormGet(Model model, Principal principal, 
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary,
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idBudget", required=true) Integer idBudget) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		Budget budget = budgetService.getBudget(idBudget);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && 
				(grant.getIdBeneficiary()==beneficiary.getId()) &&
				(budget.getIdGrant()==grant.getId())) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("grant", grant);		
			model.addAttribute("budget", budget);

			List<CostType> costTypeList = costTypeService.getCostTypeList(beneficiary.getId());
			model.addAttribute("costTypeList", costTypeList);

			return "budget_form";
		} else {
			return "redirect:/";
		}
	}

	@PostMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/budget/{idBudget}/budget_form")
	public String updateBudgetFormPost(Model model, Principal principal, Budget budget,  
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary,
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idBudget", required=true) Integer idBudget) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && 
				(grant.getIdBeneficiary()==beneficiary.getId()) &&
				(budget.getIdGrant()==grant.getId()) &&
				(budget.getId()==idBudget)) {
			BigDecimal paidDotation = paymentService.getPaymentForBudgetDotationSum(budget.getId());
			BigDecimal paidContributionOwn = paymentService.getPaymentForBudgetContributionOwnSum(budget.getId());
			BigDecimal paidContributionPersonal = paymentService.getPaymentForBudgetContributionPersonalSum(budget.getId());
			BigDecimal paidContributionInkind = paymentService.getPaymentForBudgetContributionInkindSum(budget.getId());
			
			budgetService.updateBudget(budget);
			
			return "redirect:/beneficiary/{idBeneficiary}/grant/{idGrant}/budget/{idBudget}";
		} else {
			return "redirect:/";
		}
	}

	@RequestMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/budget/{idBudget}/budget_delete")
	public String deleteDocument(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary,
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idBudget", required=true) Integer idBudget) {

		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		Budget budget = budgetService.getBudget(idBudget);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && 
				(grant.getIdBeneficiary()==beneficiary.getId()) &&
				(budget.getIdGrant()==grant.getId())) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("grant", grant);
			model.addAttribute("budget", budget);
						
			if(paymentService.getPaymentForBudgetCount(budget.getId())>0) {
				model.addAttribute("message", "Nie można usunąć pozycji kosztorysu!!! Najpierw usuń istniejące rozliczenia dla tej pozycji z listy poniżej.");
				
				List<Payment> paymentList = paymentService.getPaymentForBudgetList(idBudget);
				model.addAttribute("paymentList", paymentList);
				
				BigDecimal dotationPaid = new BigDecimal("0.00");
				BigDecimal contributionOwnPaid = new BigDecimal("0.00");
				BigDecimal contributionPersonalPaid = new BigDecimal("0.00");
				BigDecimal contributionInkindPaid = new BigDecimal("0.00");
				
				for (Payment payment : paymentList) {
					dotationPaid = dotationPaid.add(payment.getDotation());
					contributionOwnPaid = contributionOwnPaid.add(payment.getContributionOwn());
					contributionPersonalPaid = contributionPersonalPaid.add(payment.getContributionPersonal());
					contributionInkindPaid = contributionInkindPaid.add(payment.getContributionInkind());
				}
				model.addAttribute("dotationPaid", dotationPaid);
				model.addAttribute("contributionOwnPaid", contributionOwnPaid);
				model.addAttribute("contributionPersonalPaid", contributionPersonalPaid);
				model.addAttribute("contributionInkindPaid", contributionInkindPaid);

				return "budget";				
				//return "redirect:/grant/" + idGrant + "/budget/" +idBudget;
			} else {
				budgetService.deleteBudget(idBudget);
				return "redirect:/beneficiary/{idBeneficiary}/grant/" + idGrant;
			}			
		}
		return "redirect:/";
	}
}
