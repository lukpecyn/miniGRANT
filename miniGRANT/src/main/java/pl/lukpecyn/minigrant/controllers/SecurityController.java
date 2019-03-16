package pl.lukpecyn.minigrant.controllers;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	@Value("${app.address}")
	public String appAddress;

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
		model.addAttribute("user", new User());

		if(userService.getCount()==0)
			model.addAttribute("infoMessage", "UWAGA!!! Nie zarejestrowano jeszcze żadnych użytkowników! Pierwszy zarejestrowany użytkownik zostanie aktywowany od razu.");
		return "login";
	}

	@PostMapping("register")
	public String register(Model model, User user, @RequestParam(value = "pwd2", required = true) String pwd2) {
		model.addAttribute("appVersion",appVersion);
		model.addAttribute("appName", appName);
		logger.info("register page");		
		logger.info("IP " + getClientIp());
		
		if(user.getPassword().equals(pwd2)) {
			Role role = new Role();
			user.setGuid(UUID.randomUUID());
			if(userService.getCount()==0) {
				user.setEnabled(true);
				role.setRole("ROLE_ADMIN");
			} else {
				user.setEnabled(false);
				role.setRole("ROLE_USER");
			}		
			role.setUsername(user.getUsername());
			user.setRole(role);
			if((userService.ChceckUsernameExists(user.getUsername())==0) && (userService.ChceckEmailExists(user.getEmail())==0)) {
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				userService.addUser(user);
				emailService.sendSimpleEmail(user.getEmail(), "Rejestracja w systemie " +appName, "Adres do potwierdzenia konta: " + appAddress + "/activation/" + user.getGuid().toString()+ " \nBrak potwierdzenia w ciągu 24 godzin spowoduje usunięcie konta!");
				String infoMessage = "Na twój adres e-mail została wysłana wiadomość z linkiem do potwierdzenia rejestracji.";
				if(userService.getCount()>1)
					infoMessage = infoMessage + " Po potwierdzeniu musisz jeszcze poczekać, aż administrator aktywuje twoje konto.";
				model.addAttribute("infoMessage", infoMessage);
				model.addAttribute("user", new User());
			} else {
				String errorMessage = "";
				if(userService.ChceckUsernameExists(user.getUsername())>0)
					errorMessage = "Nazwa użytkownika '" + user.getUsername() + "' jest już zajęta. ";
				else if(userService.ChceckEmailExists(user.getEmail())>0)
					errorMessage = "Adres e-mail '" + user.getEmail() + "' jest już zajęty. ";
				model.addAttribute("errorMessage", errorMessage);
				model.addAttribute("user", user);
			}
		} else {
			model.addAttribute("errorMessage", "Hasła się nie zgadzają");
			model.addAttribute("user", user);
		}
		return "login";
	}
	
	@RequestMapping("activation/{guid}")
	public String activation(Model model,@PathVariable(value="guid",required=true) String guid) {
		model.addAttribute("appVersion",appVersion);
		model.addAttribute("appName", appName);
		logger.info("activation page");		
		logger.info("IP " + getClientIp());
		
		User user = userService.getUser(UUID.fromString(guid));
		if(user!=null) {
			user.setConfirmed(true);
			user.setGuid(UUID.randomUUID());
			userService.updateUser(user);
			model.addAttribute("user", new User());
			model.addAttribute("infoMessage", "Adres e-mail został potwierdzony. Konto oczekuje na aktywację przez administratora.");
			return "login";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping("passwordreset")
	public String passwordReset(Model model, @RequestParam(value = "email", required = true) String email) {
		model.addAttribute("appVersion",appVersion);
		model.addAttribute("appName", appName);
		logger.info("passwordreset page");		
		logger.info("IP " + getClientIp());

		try {
			User user = userService.getUserByEmail(email);
			user.setGuid(UUID.randomUUID());
			userService.updateUser(user);
				
			emailService.sendSimpleEmail(user.getEmail(), "Reset hasła w systemie " +appName, "Adres do zresetowania hasła dla użytkownika '" + user.getUsername() +"': " + appAddress + "/passwordreset/" + user.getGuid().toString()+ "\nStare hasło pozostanie aktywne do czasu zmiany na nowe.");
			model.addAttribute("infoMessage", "Na adres <strong>" + email + "</strong>  została wysłana instrukcja resetowania hasła.");
		
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Wystąpił problem z wysyłką na adres <strong>" + email + "</strong>.");
		}
		model.addAttribute("email", email);
		return "password_reset";
	}
	
	@GetMapping("passwordreset/{guid}")
	public String passwordResetGuidGet(Model model, @PathVariable(value="guid",required=true) String guid) {
		model.addAttribute("appVersion",appVersion);
		model.addAttribute("appName", appName);
		logger.info("passwordresetguid GET page");		
		logger.info("IP " + getClientIp());

		User user = userService.getUser(UUID.fromString(guid));
		if(user!=null) {
			//model.addAttribute("user", user);
			return "password_reset_form";
		} else {
			return "login";
		}
	}
	
	@PostMapping("passwordreset/{guid}")
	public String passwordResetGuidPost(Model model, @PathVariable(value="guid", required=true) String guid, @RequestParam(value = "password1", required = true) String password1, @RequestParam(value = "password2", required = true) String password2) {
		model.addAttribute("appVersion",appVersion);
		model.addAttribute("appName", appName);
		logger.info("passwordresetguid POST page");		
		logger.info("IP " + getClientIp());

		User user = userService.getUser(UUID.fromString(guid));
		if(password1.equals(password2)) {
			user.setPassword(passwordEncoder.encode(password1));
			user.setGuid(UUID.randomUUID());
			userService.updateUser(user);
			model.addAttribute("infoMessage", "Hasło zostało zresetowane.");
			model.addAttribute("user",new User());
			return "login";
		} else {
			model.addAttribute("errorMessage", "Hasła różnią się od siebie.");
			return "password_reset_form";
		}
	}
}
