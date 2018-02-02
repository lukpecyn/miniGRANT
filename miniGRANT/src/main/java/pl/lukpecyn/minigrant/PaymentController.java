package pl.lukpecyn.minigrant;

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

@Controller
public class PaymentController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

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
	
	@GetMapping({"/grant/{idGrant}/document/{idDocument}/payment_form","/grant/{idGrant}/document/{idDocument}/payment/{idPayment}/payment_form"})
	public String paymentFormGet(Payment payment, 
			Model model, 
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=true) Integer idDocument,
			@PathVariable(value="idPayment", required=false) Integer idPayment) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Document document = new Document();
		
		if(idPayment!=null) {
			payment = paymentService.getPayment(idPayment);
			if(payment.getId()!=null) {
				model.addAttribute("payment", payment);
			
				document = documentService.getDocument(payment.getDocument().getId());
				model.addAttribute("document", document);
			} 
		} else {
			document = documentService.getDocument(idDocument);
			model.addAttribute("document", document);
		}
		
		Grant grant = grantService.getGrant(document.getIdGrant());
		model.addAttribute("grant", grant);

		return "payment_form";

	}

}
