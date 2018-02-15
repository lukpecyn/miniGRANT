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
	
	@GetMapping("/admin/donor_form")
	public String addDonorFormGet(Model model) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		model.addAttribute("donor", new Donor());

		return "donor_form";
	}
		
	@PostMapping("/admin/donor_form")
	public String addDonorFormPost(Model model, Donor donor) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(donor.getId()!=null) {
			if(donor.getId()<0) {
				donorService.addDonor(donor);
			}
		}
		return "redirect:/admin";
	}

	@GetMapping("/admin/donor/{idDonor}/donor_form")
	public String updateDonorFormGet(Model model, @PathVariable(value="idDonor", required=false) Integer idDonor) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		if(idDonor!=null){
			Donor donor = donorService.getDonor(idDonor);
			
			if(donor.getId()!=null) {
				model.addAttribute("donor", donor);
				return "donor_form";
			}
		}
		return "redirect:/admin";
	}

	@PostMapping("/admin/donor/{idDonor}/donor_form")
	public String updateDonorFormPost(Model model, Donor donor, @PathVariable(value="idDonor", required=true) Integer idDonor) {
		
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(donor.getId()!=null)
			if(donor.getId()==idDonor)
				donorService.updateDonor(donor);

		return "redirect:/admin";
	}

	@RequestMapping(value = "/admin/donor/{idDonor}")
	public String showDocument(Model model, 
			@PathVariable(value="idDonor", required=true) long idDonor) {

		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Donor donor = donorService.getDonor(idDonor);
		model.addAttribute("donor", donor);

		return "donor";
	}	
}
