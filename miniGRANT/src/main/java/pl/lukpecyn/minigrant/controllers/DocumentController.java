package pl.lukpecyn.minigrant.controllers;

import java.math.BigDecimal;
import java.security.Principal;
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

import pl.lukpecyn.minigrant.models.Beneficiary;
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
	
	@RequestMapping(value = "/beneficiary/{idBeneficiary}/grant/{idGrant}/document/{idDocument}")
	public String showDocument(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary,
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=true) Integer idDocument) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		Document document = documentService.getDocument(idDocument);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) 
				&& (grant.getIdBeneficiary()==beneficiary.getId())
				&& (document.getIdGrant()==grant.getId())) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("grant", grant);		
			model.addAttribute("document", document);

			List<Payment> paymentList = paymentService.getPaymentForDocumentList(idDocument);
			model.addAttribute("paymentList", paymentList);
		
			BigDecimal paid = new BigDecimal("0.00");
			for (Payment payment : paymentList) {
				paid = paid.add(payment.getSum());
			}
			model.addAttribute("paid", paid);
			model.addAttribute("unpaid", (document.getValue().subtract(paid)));
			return "document";
		} else {
			return "redirect:/";
		}
	}	

	@GetMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/document_form")
	public String addDocumentFormGet(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary, 
			@PathVariable(value="idGrant", required=true) Integer idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && (grant.getIdBeneficiary()==beneficiary.getId())) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("grant", grant);		
			
			Document document = new Document();
			document.setIdGrant(idGrant);
			model.addAttribute("document", document);
		
			return "document_form";
		} else {
			return "redirect:/";
		}
	}

	@PostMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/document_form")
	public String addDocumentFormPost(Model model, Principal principal, Document document, 
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary,
			@PathVariable(value="idGrant", required=true) Integer idGrant) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) && 
				(grant.getIdBeneficiary()==beneficiary.getId()) &&
				(document.getIdGrant()==grant.getId())) {
			documentService.addDocument(document);
			return "redirect:/beneficiary/{idBeneficiary}/grant/"+document.getIdGrant();
		} else {
			return "redirect:/";
		}
	}
/*poprawa dokumentu
	@GetMapping("/grant/{idGrant}/document/{idDocument}/document_form")
	public String updateDocumentFormGet(Model model, @PathVariable(value="idGrant", required=true) Integer idGrant, @PathVariable(value="idDocument", required=false) Integer idDocument) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
		
		if(idDocument!=null){
			Document document = documentService.getDocument(idDocument);
			if((document.getId()!=null) && (idGrant==document.getIdGrant())) {
				Grant grant = grantService.getGrant(document.getIdGrant());
				model.addAttribute("grant", grant);		
				model.addAttribute("document", document);
				return "document_form";
			}
		}
		return "redirect:/grant";			
	}

	@PostMapping("/grant/{idGrant}/document/{idDocument}/document_form")
	public String updateDocumentFormPost(Model model, Document document, @PathVariable(value="idGrant", required=true) Integer idGrant, @PathVariable(value="idDocument", required=true) Integer idDocument) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(document.getId()!=null){
			if(idDocument==document.getId()){
				//TODO: sprawdzanie czy kwota dokumentu nie jest niższa od sumy rozliczeń dla tego dokumentu
				documentService.updateDocument(document);
				return "redirect:/grant/"+document.getIdGrant();
			}
		}
		return "redirect:/grant";
	}
*/
	@RequestMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/document/{idDocument}/document_delete")
	public String deleteDocument(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary,
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=true) Integer idDocument) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		Document document = documentService.getDocument(idDocument);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) 
				&& (grant.getIdBeneficiary()==beneficiary.getId())
				&& (document.getIdGrant()==grant.getId())) {
			model.addAttribute("beneficiary", beneficiary);
			model.addAttribute("grant", grant);		
			model.addAttribute("document", document);

			if(paymentService.getPaymentForDocumentCount(document.getId())>0) {
				model.addAttribute("message", "Nie można usunąć dokumentu!!! Najpierw wycofaj istniejące rozliczenia dla tego dokumentu z listy poniżej.");
				
				List<Payment> paymentList = paymentService.getPaymentForDocumentList(idDocument);
				model.addAttribute("paymentList", paymentList);
				
				BigDecimal paid = new BigDecimal("0.00");
				for (Payment payment : paymentList) {
					paid = paid.add(payment.getSum());
				}
				model.addAttribute("paid", paid);
				model.addAttribute("unpaid", (document.getValue().subtract(paid)));
				return "document";
			} else {
				documentService.deleteDocument(idDocument);
				return "redirect:/beneficiary/{idBeneficiary}/grant/{idGrant}";
			}			
		}
		
		return "redirect:/";
	}
}
