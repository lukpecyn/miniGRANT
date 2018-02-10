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

import pl.lukpecyn.minigrant.models.Budget;
import pl.lukpecyn.minigrant.models.Document;
import pl.lukpecyn.minigrant.models.Grant;
import pl.lukpecyn.minigrant.models.Payment;
import pl.lukpecyn.minigrant.services.BeneficiaryService;
import pl.lukpecyn.minigrant.services.BudgetService;
import pl.lukpecyn.minigrant.services.CostTypeService;
import pl.lukpecyn.minigrant.services.DocumentService;
import pl.lukpecyn.minigrant.services.DonorService;
import pl.lukpecyn.minigrant.services.GrantService;
import pl.lukpecyn.minigrant.services.GrantStatusService;
import pl.lukpecyn.minigrant.services.PaymentService;

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
			Document document,
			Model model, 
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=true) Integer idDocument,
			@PathVariable(value="idPayment", required=false) Integer idPayment) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
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
			
			payment.setDocument(document);
			model.addAttribute("payment", payment);
		}

		
		Grant grant = grantService.getGrant(document.getIdGrant());
		model.addAttribute("grant", grant);

		List<Budget> budgetList = budgetService.getBudgetForGrantList(grant.getId());
		model.addAttribute("budgetList", budgetList);

		return "payment_form";
	}

	@PostMapping({"/grant/{idGrant}/document/{idDocument}/payment_form","/grant/{idGrant}/document/{idDocument}/payment/{idPayment}/payment_form"})
	public String paymentFormPost(Payment payment,
			Model model, 
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=true) Integer idDocument,
			@PathVariable(value="idPayment", required=false) Integer idPayment) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		if(payment!=null) {
			Document document = documentService.getDocument(payment.getDocument().getId());
			Grant grant = grantService.getGrant(document.getIdGrant());

			if(payment.getId()>=0) {
				paymentService.updatePayment(payment);
			} else {
				paymentService.addPayment(payment);
			}
		return "redirect:/grant/" + grant.getId() + "/document/" + document.getId();
		}
		return "redirect:/";
	}

	@RequestMapping("/grant/{idGrant}/document/{idDocument}/payment/{idPayment}/payment_delete")
	public String deletePayment(Model model, 
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=true) Integer idDocument,
			@PathVariable(value="idPayment", required=true) Integer idPayment) {
		
		if(idPayment!=null) {
			Payment payment = paymentService.getPayment(idPayment);
			Document document = payment.getDocument();
			Grant grant = grantService.getGrant(document.getIdGrant());
			paymentService.deletePayment(idPayment);
			
			return "redirect:/grant/" + grant.getId() + "/document/" + document.getId();
		}
		
		return "redirect:/";
	}
}
