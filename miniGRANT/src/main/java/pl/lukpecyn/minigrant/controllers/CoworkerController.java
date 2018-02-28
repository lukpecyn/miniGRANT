package pl.lukpecyn.minigrant.controllers;

import java.security.Principal;

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

import pl.lukpecyn.minigrant.models.Beneficiary;
import pl.lukpecyn.minigrant.models.CostType;
import pl.lukpecyn.minigrant.models.Coworker;
import pl.lukpecyn.minigrant.services.BeneficiaryService;
import pl.lukpecyn.minigrant.services.CoworkerService;
import pl.lukpecyn.minigrant.services.UserService;

@Controller
public class CoworkerController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;
	
	@Autowired
	BeneficiaryService beneficiaryService;

	@Autowired
	CoworkerService coworkerService;
	
	@Autowired
	UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(CoworkerController.class);

	@GetMapping("/beneficiary/{idBeneficiary}/coworker_form")
	public String addCoworkerFormGet(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true)long idBeneficiary) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		if(beneficiaryService.checkUser(beneficiary, principal.getName())>0) {
			model.addAttribute("beneficiary", beneficiary);
			Coworker coworker = new Coworker("",beneficiary.getId());
			model.addAttribute("coworker", coworker);	
			return "coworker_form";
		} else {
			return "redirect:/";
		}
	}
		
	@PostMapping("/beneficiary/{idBeneficiary}/coworker_form")
	public String addCoworkerFormPost(Model model, Principal principal,Coworker coworker, 
			@PathVariable(value="idBeneficiary", required=true)long idBeneficiary) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if((coworker.getIdBeneficiary()==idBeneficiary) && (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0)) {
			//if(userService.ChceckUsernameExists(coworker.getUsername())>0)
			if((beneficiaryService.checkUser(beneficiaryService.getBeneficiary(coworker.getIdBeneficiary()), coworker.getUsername())==0) && (userService.ChceckUsernameExists(coworker.getUsername())>0)) {
				coworkerService.addCoworker(coworker);
				return "redirect:/beneficiary/" + idBeneficiary;
			} else {
				if (userService.ChceckUsernameExists(coworker.getUsername())==0)
					model.addAttribute("error", "Taki użytkownik nie istnieje!!!");
				if (beneficiaryService.checkUser(beneficiaryService.getBeneficiary(coworker.getIdBeneficiary()), coworker.getUsername())>0) 
					model.addAttribute("error", "Ten użytkownik ma już dostęp do tego beneficjenta!!!");
				
				model.addAttribute("beneficiary", beneficiaryService.getBeneficiary(idBeneficiary));
				model.addAttribute("coworker", coworker);
				return "coworker_form";
			}
		} else { 
			return "redirect:/";
		}		
	}
	
	@RequestMapping("/beneficiary/{idBeneficiary}/coworker/{username}/cancel")
	public String cancelCoworker(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true)Integer idBeneficiary,
			@PathVariable(value="username", required=true)String username) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Coworker coworker = new Coworker(username,idBeneficiary);
		if((beneficiaryService.checkUser(beneficiaryService.getBeneficiary(idBeneficiary), principal.getName())>0) && (coworkerService.ChceckCoworkerExists(coworker)>0)) {
			coworkerService.delCoworker(coworker);
			return "redirect:/beneficiary/{idBeneficiary}";
		} else {
			return "redirect:/";
		}
	}
}
