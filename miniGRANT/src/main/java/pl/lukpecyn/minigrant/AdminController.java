package pl.lukpecyn.minigrant;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(MiniGrantController.class);

	@Autowired
	CostTypeService costTypeService;

	@Autowired
	DonorService donorService;

	@Autowired
	BeneficiaryService beneficiaryService;

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
		
		return "admin";
	}

}