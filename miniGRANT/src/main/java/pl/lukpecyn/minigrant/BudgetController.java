package pl.lukpecyn.minigrant;

import java.math.BigDecimal;
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
	
	@GetMapping({"/grant/{idGrant}/budget_form","/grant/{idGrant}/budget/{idBudget}/budget_form"})
	public String budgetFormGet(Grant grant, 
			Budget budget, 
			Model model, 
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idBudget", required=false) Integer idBudget) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		if(idBudget!=null){
			budget = budgetService.getBudget(idBudget);
			
			if(budget.getId()!=null) {
				grant = grantService.getGrant(budget.getIdGrant());
			} else {
				grant = grantService.getGrant(idGrant);

			}
		} else {
			grant = grantService.getGrant(idGrant);
		}
					
		model.addAttribute("grant", grant);				
		model.addAttribute("budget", budget);
			
		List<CostType> costTypeList = costTypeService.getCostTypeList();
		model.addAttribute("costTypeList", costTypeList);
		return "budget_form";
	}

	@PostMapping({"/grant/{idGrant}/budget_form","/grant/{idGrant}/budget/{idBudget}/budget_form"})
	public String budgetFormPost(Budget budget, 
			Model model,
			@PathVariable(value="idGrant", required=true) Integer idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(budget.getId()!=null){
			if(budget.getId()<0){
				budgetService.addBudget(budget);
			}else{
				budgetService.updateBudget(budget);
			}
		}else{
			return "redirect:/";
		}
		return "redirect:/grant/"+idGrant;
	}

	@RequestMapping({"/grant/{idGrant}/budget/{idBudget}"})
	public String budgetShow(Grant grant,
			Budget budget,
			Model model,
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idBudget", required=false) Integer idBudget) {

		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		budget = budgetService.getBudget(idBudget);
		model.addAttribute("budget", budget);
		
		grant = grantService.getGrant(budget.idGrant);
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
		//model.addAttribute("unpaid", (budget.getSum().subtract(paid)));
		
		return "budget";
	}

	@RequestMapping("/grant/{idGrant}/budget/{idBudget}/budget_delete")
	public String deleteDocument(Model model, 
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idBudget", required=true) Integer idBudget) {

		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		if(idBudget!=null) {
			Budget budget = budgetService.getBudget(idBudget);
			model.addAttribute("budget", budget);
			
			Grant grant = grantService.getGrant(budget.getIdGrant());
			model.addAttribute("grant", grant);
			
			if(paymentService.getPaymentForBudgetCount(budget.getId())>0) {
				model.addAttribute("message", "Nie można usunąć dokumentu!!! Najpierw usuń istniejące rozliczenia dla tego dokumentu z listy poniżej.");
				/*
				List<Payment> paymentList = paymentService.getPaymentForDocumentList(idDocument);
				model.addAttribute("paymentList", paymentList);
				
				BigDecimal paid = new BigDecimal("0.00");
				for (Payment payment : paymentList) {
					paid = paid.add(payment.getSum());
				}
				model.addAttribute("paid", paid);
				model.addAttribute("unpaid", (document.getValue().subtract(paid)));
				return "document";
				*/
				return "redirect:/grant/" + idGrant + "/budget/" +idBudget;
			} else {
				budgetService.deleteBudget(idBudget);
				return "redirect:/grant/" + idGrant;
			}			
		}
		
		return "redirect:/";
	}
}
