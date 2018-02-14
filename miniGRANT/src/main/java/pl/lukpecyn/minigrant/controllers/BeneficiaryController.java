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

import pl.lukpecyn.minigrant.models.Beneficiary;
import pl.lukpecyn.minigrant.services.BeneficiaryService;
import pl.lukpecyn.minigrant.services.BudgetService;
import pl.lukpecyn.minigrant.services.CostTypeService;
import pl.lukpecyn.minigrant.services.DocumentService;
import pl.lukpecyn.minigrant.services.DonorService;
import pl.lukpecyn.minigrant.services.GrantService;
import pl.lukpecyn.minigrant.services.GrantStatusService;
import pl.lukpecyn.minigrant.services.PaymentService;

@Controller
public class BeneficiaryController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(BeneficiaryController.class);

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
	
	@GetMapping("/admin/beneficiary_form")
	public String addBeneficiaryFormGet(Model model, 
			@PathVariable(value="idBeneficiary", required=false) Integer idBeneficiary) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		model.addAttribute("beneficiary", new Beneficiary());
		
		return "beneficiary_form";
	}
		
	@PostMapping("/admin/beneficiary_form")
	public String addBeneficiaryFormPost(Model model, Beneficiary beneficiary) {		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(beneficiary.getId()!=null) {
			if(beneficiary.getId()<0) {
				beneficiaryService.addBeneficiary(beneficiary);
			} else {
				beneficiaryService.updateBeneficiary(beneficiary);
			}
		} else {
			return "redirect:/admin";
		}
		return "redirect:/admin";
	}

	@GetMapping("/admin/beneficiary/{idBeneficiary}/beneficiary_form")
	public String updateBeneficiaryFormGet(Model model, 
			@PathVariable(value="idBeneficiary", required=false) Integer idBeneficiary) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		if(idBeneficiary!=null){
			Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
			
			if(beneficiary.getId()!=null) {
				model.addAttribute("beneficiary", beneficiary);
			}
		}
		return "beneficiary_form";
	}

	@PostMapping("/admin/beneficiary/{idBeneficiary}/beneficiary_form")
	public String updateBeneficiaryFormPost(Model model, Beneficiary beneficiary, @PathVariable(value="idBeneficiary", required=true) long idBeneficiary) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		if(idBeneficiary==beneficiary.getId())
			beneficiaryService.updateBeneficiary(beneficiary);
		else 
			return "redirect:/";
		
		return "redirect:/admin";
	}

	@RequestMapping(value = "/admin/beneficiary/{idBeneficiary}")
	public String showDocument(Model model, 
			@PathVariable(value="idBeneficiary", required=true) long idBeneficiary) {

		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		model.addAttribute("beneficiary", beneficiary);

		return "beneficiary";
	}	
}
