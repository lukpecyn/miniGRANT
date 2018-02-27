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

	@RequestMapping(value = "/beneficiary/{idBeneficiary}/cost_type/{idCostType}")
	public String showDocument(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true)long idBeneficiary,
			@PathVariable(value="idCostType", required=true) long idCostType) {

		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		CostType costType = costTypeService.getCostType(idCostType);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && (costType.getIdBeneficiary()==beneficiary.getId())) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("costType", costType);
			return "cost_type";
		} else {
			return "redirect:/";
		}
	}	

	@GetMapping("/beneficiary/{idBeneficiary}/cost_type_form")
	public String addCostTypeFormGet(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true)long idBeneficiary) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		if(beneficiaryService.checkUser(beneficiary, principal.getName())>0) {
			model.addAttribute("beneficiary", beneficiary);
			CostType costType = new CostType();
			costType.setIdBeneficiary(beneficiary.getId());
			model.addAttribute("costType", costType);	
			return "cost_type_form";
		} else {
			return "redirect:/";
		}
	}
		
	@PostMapping("/beneficiary/{idBeneficiary}/cost_type_form")
	public String addCostTypeFormPost(Model model, Principal principal,CostType costType, 
			@PathVariable(value="idBeneficiary", required=true)long idBeneficiary) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(costType.getId()<0) {
			if((costType.getIdBeneficiary()==idBeneficiary) && (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0)) {
				costTypeService.addCostType(costType);
				logger.info("idBeneficiary=" + String.valueOf(idBeneficiary) + ", costType.getBeneficiary=" + costType.getIdBeneficiary());
			} else logger.info("costType NOT added!!!");
		}
		return "redirect:/beneficiary/" + idBeneficiary;
	}

	@GetMapping("/beneficiary/{idBeneficiary}/cost_type/{idCostType}/cost_type_form")
	public String updateCostTypeFormGet(Model model, Principal principal, 
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary,
			@PathVariable(value="idCostType", required=true) Integer idCostType) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		CostType costType = costTypeService.getCostType(idCostType);
		if((costType.getIdBeneficiary()==beneficiary.getId()) && (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0)) {
				model.addAttribute("beneficiary", beneficiary);
				model.addAttribute("costType", costType);
				return "cost_type_form";
		} else {
			return "redirect:/";
		}
	}

	@PostMapping("/beneficiary/{idBeneficiary}/cost_type/{idCostType}/cost_type_form")
	public String updateCostTypeFormPost(Model model, Principal principal, CostType costType,  
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary,
			@PathVariable(value="idCostType", required=true) Integer idCostType) {		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		if((costType.getId()==idCostType) && (costType.getIdBeneficiary()==beneficiary.getId()) && (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0)) {
			costTypeService.updateCostType(costType);			
			return "redirect:/beneficiary/{idBeneficiary}";
		} else {
			return "redirect:/";
		}
	}
}
