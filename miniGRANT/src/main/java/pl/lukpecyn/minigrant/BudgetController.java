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
}
