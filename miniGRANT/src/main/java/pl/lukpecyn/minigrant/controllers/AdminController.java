package pl.lukpecyn.minigrant.controllers;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.lukpecyn.minigrant.models.Beneficiary;
import pl.lukpecyn.minigrant.models.CostType;
import pl.lukpecyn.minigrant.models.Donor;
import pl.lukpecyn.minigrant.models.User;
import pl.lukpecyn.minigrant.services.BeneficiaryService;
import pl.lukpecyn.minigrant.services.CostTypeService;
import pl.lukpecyn.minigrant.services.DonorService;
import pl.lukpecyn.minigrant.services.EmailService;
import pl.lukpecyn.minigrant.services.UserService;

@Controller
public class AdminController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;
	@Value("${app.address}")
	public String appAddress;

	private static final Logger logger = LoggerFactory.getLogger(MiniGrantController.class);

	@Autowired
	EmailService emailService;
	
	@Autowired
	CostTypeService costTypeService;

	@Autowired
	DonorService donorService;

	@Autowired
	BeneficiaryService beneficiaryService;
	
	@Autowired
	UserService userService;

	@RequestMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		List<CostType> costTypeList = costTypeService.getCostTypeList();
		model.addAttribute("costTypeList", costTypeList);
		
		List<Donor> donorList = donorService.getDonorList();
		model.addAttribute("donorList", donorList);

		List<Beneficiary> beneficiaryList = beneficiaryService.getBeneficiaryList();
		model.addAttribute("beneficiaryList", beneficiaryList);
		
		List<User> userList = userService.getAllUser();
		model.addAttribute("userList", userList);
		
		return "admin";
	}

	@RequestMapping("/admin/user/{username}/enable")
	public String enableUser(@PathVariable(value= "username", required=true) String username) {		
		userService.enableUser(username);
		emailService.sendSimpleEmail(userService.getUser(username).getEmail(), "Informacja z systemu " +appName, "Twoje konto zostało włączone. Teraz możesz się zalogować wchodząc na stronę: " + appAddress);
		return "redirect:/admin";
	}

	@RequestMapping("/admin/user/{username}/disable")
	public String disableUser(@PathVariable(value= "username", required=true) String username) {
		userService.disableUser(username);
		emailService.sendSimpleEmail(userService.getUser(username).getEmail(), "Informacja z systemu " +appName, "Twoje konto zostało wyłączone.");
		return "redirect:/admin";
	}
}
