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
public class DocumentController {

	@Value("${app.version}")
	public String appVersion;
	@Value("${app.name}")
	public String appName;

	private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

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
	
	@GetMapping({"/grant/{idGrant}/document_form","/grant/{idGrant}/document/{idDocument}/document_form"})
	public String documentFormGet(Document document, 
			Model model, 
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=false) Integer idDocument) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		if(idDocument!=null){
			document = documentService.getDocument(idDocument);
			
			Grant grant = grantService.getGrant(document.getIdGrant());
			model.addAttribute("grant", grant);

			if(document.getId()!=null) {
				model.addAttribute("document", document);
			}
		} else {
			Grant grant = grantService.getGrant(idGrant);
			model.addAttribute("grant", grant);			
		}
		return "document_form";
	}

	@PostMapping({"/grant/{idGrant}/document_form","/grant/{idGrant}/document/{idDocument}/document_form"})
	public String documentFormPost(Document document, 
			Model model,
			@PathVariable(value="idGrant", required=true) Integer idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(document.getId()!=null){
			if(document.getId()<0){
				documentService.addDocument(document);
			}else{
				documentService.updateDocument(document);
			}
			return "redirect:/grant/"+document.getIdGrant();
		}else{
			return "redirect:/" + idGrant;
		}
		//return "redirect:/";
	}

	@RequestMapping(value = "/grant/{idGrant}/document/{idDocument}")
	public String showDocument(Model model, 
			@PathVariable("idGrant") long idGrant, 
			@PathVariable("idDocument") long idDocument) {

		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		Document document = documentService.getDocument(idDocument);
		model.addAttribute("document", document);

		Grant grant = grantService.getGrant(document.getIdGrant());
		model.addAttribute("grant", grant);

		List<Payment> paymentList = paymentService.getPaymentForDocumentList(idDocument);
		model.addAttribute("paymentList", paymentList);
		
		BigDecimal paid = new BigDecimal("0.00");
		for (Payment payment : paymentList) {
			paid = paid.add(payment.getSum());
		}
		model.addAttribute("paid", paid);
		model.addAttribute("unpaid", (document.getValue().subtract(paid)));
		return "document";
	}	

	@RequestMapping("/grant/{idGrant}/document/{idDocument}/document_delete")
	public String deleteDocument(Model model, 
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=true) Integer idDocument) {

		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		if(idDocument!=null) {
			Document document = documentService.getDocument(idDocument);
			model.addAttribute("document", document);
			
			Grant grant = grantService.getGrant(document.getIdGrant());
			model.addAttribute("grant", grant);
			
			if(paymentService.getPaymentForDocumentCount(document.getId())>0) {
				model.addAttribute("message", "Nie można usunąć dokumentu!!! Najpierw usuń istniejące rozliczenia dla tego dokumentu z listy poniżej.");
				
				List<Payment> paymentList = paymentService.getPaymentForDocumentList(idDocument);
				model.addAttribute("paymentList", paymentList);
				
				BigDecimal paid = new BigDecimal("0.00");
				for (Payment payment : paymentList) {
					paid = paid.add(payment.getSum());
				}
				model.addAttribute("paid", paid);
				model.addAttribute("unpaid", (document.getValue().subtract(paid)));
				return "document";
				
				//return "redirect:/grant/" + idGrant + "/document/" +idDocument;
			} else {
				documentService.deleteDocument(idDocument);
				return "redirect:/grant/" + idGrant;
			}			
		}
		
		return "redirect:/";
	}
}
