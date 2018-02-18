package pl.lukpecyn.minigrant.controllers;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.lukpecyn.minigrant.models.Role;
import pl.lukpecyn.minigrant.models.User;
import pl.lukpecyn.minigrant.services.EmailService;
import pl.lukpecyn.minigrant.services.UserService;

@Controller
public class SecurityController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;
	@Value("${appAddress}")
	public String appAddress;

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

	private static HttpServletRequest request;
	
	@Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    private static String getClientIp() {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }

	@RequestMapping("login")
	public String login(Model model) {		
		model.addAttribute("appVersion",appVersion);
		model.addAttribute("appName", appName);
		logger.info("login page");		
		logger.info("IP " + getClientIp());
		
		List<User> userList = userService.getAllUser();
		logger.info("User list size " + userList.size());
		for(User user : userList) {
			logger.info("User: " + user.getUsername() + ", confirmed: " + user.getConfirmed() + ", enabled: " + user.getEnabled() + ", guid: " + user.getGuid().toString());
		}
		model.addAttribute("user", new User());

		if(userService.getCount()==0)
			model.addAttribute("information", "UWAGA!!! Nie zarejestrowano jeszcze żadnych użytkowników! Pierwszy zarejestrowany użytkownik zostanie aktywowany od razu.");
		return "login";
	}

	@PostMapping("register")
	public String register(Model model, User user, @RequestParam(value = "pwd2", required = true) String pwd2) {
		model.addAttribute("appVersion",appVersion);
		model.addAttribute("appName", appName);
		
		if(user.getPassword().equals(pwd2)) {
			Role role = new Role();
			user.setGuid(UUID.randomUUID());
			if(userService.getCount()==0) {
				user.setEnabled(true);
				role.setRole("ROLE_ADMIN");
			} else {
				user.setEnabled(false);
				role.setRole("ROLE_ADMIN");
				model.addAttribute("information", "Na twój adres e-mail została wysłana wiadomość z linkiem do potwierdzenia rejestracji. "
						+ " Po potwiedzeniu musisz jeszcze poczekać, aż administrator aktywuje twoje konto.");
			}		
			role.setUsername(user.getUsername());
			user.setRole(role);
			userService.addUser(user);
			emailService.sendSimpleEmail(user.getEmail(), "Rejestracja w systemie " +appName, "Adres do aktywacji konta: " + appAddress + "/activation/" + user.getGuid().toString()+ "\n\nZ poważaniem\nAdministrator " +appName);
		
			model.addAttribute("user", new User());
		} else {
			model.addAttribute("user", user);
			model.addAttribute("information", "Hasła się nie zgadzają");
		}
		return "login";
	}
	
	@RequestMapping("activation/{guid}")
	public String activation(Model model,@PathVariable(value="guid",required=true) String guid) {
		User user = userService.getUser(UUID.fromString(guid));
		if(user!=null) {
			user.setConfirmed(true);
			userService.updateUser(user);
			model.addAttribute("user", new User());
			model.addAttribute("information", "Adres e-mail został potwierdzony");
		} else {
			return "redirect:/";
		}
		return "login";
	}
}
