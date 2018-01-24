package pl.lukpecyn.minigrant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MiniGrantController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(MiniGrantController.class);

	@RequestMapping("/")
	public String grantList(Model model) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		return "index";
	}
}
