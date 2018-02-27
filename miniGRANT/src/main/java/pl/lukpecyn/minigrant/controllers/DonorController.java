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
import pl.lukpecyn.minigrant.models.Donor;
import pl.lukpecyn.minigrant.services.BeneficiaryService;
import pl.lukpecyn.minigrant.services.BudgetService;
import pl.lukpecyn.minigrant.services.CostTypeService;
import pl.lukpecyn.minigrant.services.DocumentService;
import pl.lukpecyn.minigrant.services.DonorService;
import pl.lukpecyn.minigrant.services.GrantService;
import pl.lukpecyn.minigrant.services.GrantStatusService;
import pl.lukpecyn.minigrant.services.PaymentService;

@Controller
public class DonorController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(DonorController.class);

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
	
	@RequestMapping(value = "/beneficiary/{idBeneficiary}/donor/{idDonor}")
	public String showDocument(Model model, Principal principal, 
			@PathVariable(value="idBeneficiary", required=true) long idBeneficiary,
			@PathVariable(value="idDonor", required=true) long idDonor) {

		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Donor donor = donorService.getDonor(idDonor);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && (donor.getIdBeneficiary()==beneficiary.getId())) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("donor", donor);
			return "donor";
		} else {
			return "redirect:/";
		}
	}	

	@GetMapping("/beneficiary/{idBeneficiary}/donor_form")
	public String addDonorFormGet(Model model, Principal principal, @PathVariable(value="idBeneficiary", required=true) long idBeneficiary) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		
		if(beneficiaryService.checkUser(beneficiary, principal.getName())>0) {
			model.addAttribute("beneficiary", beneficiary);
			Donor donor = new Donor();
			donor.setIdBeneficiary(beneficiary.getId());
			model.addAttribute("donor", donor);
			return "donor_form";
	
		} else {
			return "redirect:/";
		}
	}
		
	@PostMapping("/beneficiary/{idBeneficiary}/donor_form")
	public String addDonorFormPost(Model model, Principal principal, Donor donor, @PathVariable(value="idBeneficiary", required=true) long idBeneficiary) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(donor.getId()!=null) {
			if(donor.getId()<0) {
				if((donor.getIdBeneficiary()==idBeneficiary) && (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0)) {
					donorService.addDonor(donor);
				}
			}
		}
		return "redirect:/beneficiary/" + idBeneficiary;
	}

	@GetMapping("/beneficiary/{idBeneficiary}/donor/{idDonor}/donor_form")
	public String updateDonorFormGet(Model model, Principal principal, 
			@PathVariable(value="idBeneficiary", required=true) long idBeneficiary,
			@PathVariable(value="idDonor", required=true) Integer idDonor) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Donor donor = donorService.getDonor(idDonor);
		if((donor.getIdBeneficiary()==beneficiary.getId()) && (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0)) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("donor", donor);
			return "donor_form";
		} else {
			return "redirect:/";
		}
	}

	@PostMapping("/beneficiary/{idBeneficiary}/donor/{idDonor}/donor_form")
	public String updateDonorFormPost(Model model, Principal principal, Donor donor,  
			@PathVariable(value="idBeneficiary", required=true) long idBeneficiary,
			@PathVariable(value="idDonor", required=true) Integer idDonor) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		if((donor.getId()==idDonor) && (donor.getIdBeneficiary()==beneficiary.getId()) && (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0)) {
			donorService.updateDonor(donor);
			return "redirect:/beneficiary/{idBeneficiary}";
		} else {
			return "redirect:/";
		}
	}
}
