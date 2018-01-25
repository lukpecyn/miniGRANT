package pl.lukpecyn.minigrant;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MiniGrantController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(MiniGrantController.class);

	@Autowired
	GrantService grantService;
	
	@RequestMapping("/")
	public String grantList(Model model) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		List<Grant> grantList = grantService.getGrantsList();
		model.addAttribute("grantList", grantList);
		return "index";
	}
	
	@GetMapping("/grant_form")
	public String grantFormGet(Grant grant, Model model, @RequestParam(value="id", required=false, defaultValue="-1") long idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		if(idGrant>=0){
			grant = grantService.getGrant(idGrant);
		}
		if(grant.getName()!=null) {
			model.addAttribute("grant", grant);
		}
		return "grant_form";
	}
	
	@PostMapping("/grant_form")
	public String grantFormPost(Grant grant, Model model) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		if(grant.getName()!=null){
			if(grant.getId()<0){
				grant.setStatus(0);
				grantService.addGrant(grant);
				return "redirect:/";
			}else{
				grantService.updateGrant(grant);
				return "redirect:/grant?id="+grant.getId();
			}
		}else{
			return "redirect:/";
		}
	}
}
