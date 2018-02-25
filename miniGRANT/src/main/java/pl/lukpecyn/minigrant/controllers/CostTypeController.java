package pl.lukpecyn.minigrant.controllers;

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

import pl.lukpecyn.minigrant.models.CostType;
import pl.lukpecyn.minigrant.services.BeneficiaryService;
import pl.lukpecyn.minigrant.services.BudgetService;
import pl.lukpecyn.minigrant.services.CostTypeService;
import pl.lukpecyn.minigrant.services.DocumentService;
import pl.lukpecyn.minigrant.services.DonorService;
import pl.lukpecyn.minigrant.services.GrantService;
import pl.lukpecyn.minigrant.services.GrantStatusService;
import pl.lukpecyn.minigrant.services.PaymentService;

@Controller
public class CostTypeController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(CostTypeController.class);

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
	
	@GetMapping("/admin/cost_type_form")
	public String addCostTypeFormGet(Model model) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		model.addAttribute("costType", new CostType());

		return "cost_type_form";
	}
		
	@PostMapping("/admin/cost_type_form")
	public String addCostTypeFormPost(Model model, CostType costType) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(costType.getId()!=null)
				costTypeService.addCostType(costType);

		return "redirect:/admin";
	}

	@GetMapping("/admin/cost_type/{idCostType}/cost_type_form")
	public String updateCostTypeFormGet(Model model, @PathVariable(value="idCostType", required=false) Integer idCostType) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		if(idCostType!=null){
			CostType costType = costTypeService.getCostType(idCostType);
			
			if(costType.getId()!=null) {
				model.addAttribute("costType", costType);
			}
		}
		return "cost_type_form";
	}

	@PostMapping("/admin/cost_type/{idCostType}/cost_type_form")
	public String updateCostTypeFormPost(Model model, CostType costType, @PathVariable(value="idCostType", required=false) Integer idCostType) {		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(idCostType == costType.getId())
			costTypeService.updateCostType(costType);
		else
			return "redirect:/grant";
		
		return "redirect:/admin";
	}

	@RequestMapping(value = "/admin/cost_type/{idCostType}")
	public String showDocument(Model model, 
			@PathVariable(value="idCostType", required=true) long idCostType) {

		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		CostType costType = costTypeService.getCostType(idCostType);
		model.addAttribute("costType", costType);

		return "cost_type";
	}	
}