package pl.lukpecyn.minigrant;

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
public class MiniGrantController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(MiniGrantController.class);

	@Autowired
	GrantService grantService;
	
	@Autowired
	BudgetService budgetService;

	@Autowired
	CostTypeService costTypeService;

	@Autowired
	GrantStatusService grantStatusService;

	@RequestMapping("/")
	public String grantList(Model model) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		model.addAttribute("grantStatusList", grantStatusService.getGrantStatusList());
		
		List<Grant> grantList = grantService.getGrantsListByDateBegin();
		model.addAttribute("grantList", grantList);
		return "index";
	}
	
	@GetMapping({"grant_form", "/grant/{idGrant}/grant_form"})
	public String grantFormGet(Grant grant, Model model, @PathVariable(value="idGrant", required=false) Integer idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		model.addAttribute("grantStatusList", grantStatusService.getGrantStatusList());
		
		if(idGrant!=null){
			grant = grantService.getGrant(idGrant);
		}
		if(grant.getName()!=null) {
			model.addAttribute("grant", grant);
		}
		return "grant_form";
	}
	
	@PostMapping({"grant_form", "/grant/{idGrant}/grant_form"})
	public String grantFormPost(Grant grant, Model model) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		if(grant.getName()!=null){
			if(grant.getId()<0){
				grant.setStatus(0);
				grantService.addGrant(grant);
				return "redirect:/";
			}else{
				grantService.updateGrant(grant);
				return "redirect:/grant/"+grant.getId();
			}
		}else{
			return "redirect:/";
		}
	}

	@RequestMapping(value = "/grant/{idGrant}")
	public String grant(Model model, @PathVariable("idGrant") long idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		model.addAttribute("grantStatusList", grantStatusService.getGrantStatusList());
		Grant grant = grantService.getGrant(idGrant);
		model.addAttribute("grant", grant);
		
		List<Budget> budgetList = budgetService.getBudgetForGrantList(idGrant);
		model.addAttribute("budgetList", budgetList);
		return "grant";
	}

	@GetMapping({"/grant/{idGrant}/budget_form","/grant/{idGrant}/{idBudget}/budget_form"})
	public String budgetFormGet(Budget budget, Model model, @PathVariable(value="idGrant", required=false) Integer idGrant, @PathVariable(value="idBudget", required=false) Integer idBudget) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		if(idGrant!=null) {
			Grant grant = grantService.getGrant(idGrant);
			model.addAttribute("grant", grant);

			if(idBudget!=null){
				budget = budgetService.getBudget(idBudget);
			}
			if(budget.getId()!=null) {
				model.addAttribute("budget", budget);
			}
			
			List<CostType> costTypeList = costTypeService.getCostTypeList();
			model.addAttribute("costTypeList", costTypeList);
			return "budget_form";
		}
		else return "/";
	}

	@PostMapping({"/grant/{idGrant}/budget_form","/grant/{idGrant}/{idBudget}/budget_form"})
	public String budgetFormPost(Budget budget, Model model) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(budget.getId()!=null){
			if(budget.getId()<0){
				budgetService.addBudget(budget);
			}else{
				//grantService.updateGrant(grant);
			}
		}else{
			return "redirect:/";
		}
		return "redirect:/grant/"+budget.getIdGrant();
	}

}
