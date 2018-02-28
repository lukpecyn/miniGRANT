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
import pl.lukpecyn.minigrant.models.Budget;
import pl.lukpecyn.minigrant.models.CostType;
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
	
	@GetMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/document/{idDocument}/payment_form")
	public String addPaymentFormGet(Model model, Principal principal,
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

			Payment payment = new Payment();
			payment.setDocument(document);
			model.addAttribute("payment", payment);
				
			List<Budget> budgetList = budgetService.getBudgetForGrantList(grant.getId());
			model.addAttribute("budgetList", budgetList);

			return "payment_form";
		} else {
			return "redirect:/";
		}
	}

	@PostMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/document/{idDocument}/payment_form")
	public String addPaymentFormPost(Model model, Principal principal, Payment payment, 
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

			Budget budget = budgetService.getBudget(payment.getBudget().getId());

			BigDecimal unpaidDotation = budget.getDotation().subtract(budget.getPaidDotation());
			BigDecimal unpaidContributionOwn = budget.getContributionOwn().subtract(budget.getPaidContributionOwn());
			BigDecimal unpaidContributionPersonal = budget.getContributionPersonal().subtract(budget.getPaidContributionPersonal());
			BigDecimal unpaidContributionInkind = budget.getContributionInkind().subtract(budget.getPaidContributionInkind());
			BigDecimal unpaidDocument = document.getValue()
					.subtract(paymentService.getPaymentForDocumentDotationSum(document.getId())
							.add(paymentService.getPaymentForDocumentContributionOwnSum(document.getId())
									.add(paymentService.getPaymentForDocumentContributionPersonalSum(document.getId())
											.add(paymentService.getPaymentForDocumentContributionInkindSum(document.getId())))));

			if((unpaidDotation.subtract(payment.getDotation()).signum()==-1) ||
					(unpaidContributionOwn.subtract(payment.getContributionOwn()).signum()==-1) ||
					(unpaidContributionPersonal.subtract(payment.getContributionPersonal()).signum()==-1) ||
					(unpaidContributionInkind.subtract(payment.getContributionInkind()).signum()==-1) ||
					(unpaidDocument.subtract(payment.getDotation().add(payment.getContributionOwn().add(payment.getContributionPersonal().add(payment.getContributionInkind())))).signum()==-1)) {
				if(unpaidDotation.subtract(payment.getDotation()).signum()==-1) 
					model.addAttribute("unpaidDotation", unpaidDotation);
				if(unpaidContributionOwn.subtract(payment.getContributionOwn()).signum()==-1) 
					model.addAttribute("unpaidContributionOwn", unpaidContributionOwn);
				if(unpaidContributionPersonal.subtract(payment.getContributionPersonal()).signum()==-1)
					model.addAttribute("unpaidContributionPersonal", unpaidContributionPersonal);
				if(unpaidContributionInkind.subtract(payment.getContributionInkind()).signum()==-1)
					model.addAttribute("unpaidContributionInkind", unpaidContributionInkind);
				if(unpaidDocument.subtract(payment.getDotation().add(payment.getContributionOwn().add(payment.getContributionPersonal().add(payment.getContributionInkind())))).signum()==-1)
					model.addAttribute("unpaidDocument", unpaidDocument);
			
				model.addAttribute("payment", payment);
				model.addAttribute("document", document);
				model.addAttribute("grant", grant);
				List<Budget> budgetList = budgetService.getBudgetForGrantList(grant.getId());
				model.addAttribute("budgetList", budgetList);
				return "payment_form";
			} else {
				paymentService.addPayment(payment);
			}
			return "redirect:/beneficiary/{idBeneficiary}/grant/{idGrant}/document/{idDocument}";
		}
		return "redirect:/";
	}
/* poprawa rozliczenia
	@GetMapping("/grant/{idGrant}/document/{idDocument}/payment/{idPayment}/payment_form")
	public String updatePaymentFormGet(Model model, Payment payment, Document document,
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=true) Integer idDocument,
			@PathVariable(value="idPayment", required=true) Integer idPayment) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);
				
		if(idPayment!=null) {
			payment = paymentService.getPayment(idPayment);
			model.addAttribute("payment", payment);
			
			document = documentService.getDocument(payment.getDocument().getId());
			model.addAttribute("document", document);
			
			Grant grant = grantService.getGrant(document.getIdGrant());
			model.addAttribute("grant", grant);

			List<Budget> budgetList = budgetService.getBudgetForGrantList(grant.getId());
			model.addAttribute("budgetList", budgetList);

			return "payment_form";
		} 
		return "redirect:/grant";
	}

	@PostMapping("/grant/{idGrant}/document/{idDocument}/payment/{idPayment}/payment_form")
	public String updatePaymentFormPost(Model model, Payment payment,
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=true) Integer idDocument,
			@PathVariable(value="idPayment", required=true) Integer idPayment) {
		model.addAttribute("appVersion", appVersion);
		model.addAttribute("appName", appName);

		if(payment.getId()==idPayment) {
			Document document = documentService.getDocument(payment.getDocument().getId());
			Grant grant = grantService.getGrant(document.getIdGrant());
			Budget budget = budgetService.getBudget(payment.getBudget().getId());

			BigDecimal unpaidDotation = budget.getDotation().subtract(budget.getPaidDotation());
			BigDecimal unpaidContributionOwn = budget.getContributionOwn().subtract(budget.getPaidContributionOwn());
			BigDecimal unpaidContributionPersonal = budget.getContributionPersonal().subtract(budget.getPaidContributionPersonal());
			BigDecimal unpaidContributionInkind = budget.getContributionInkind().subtract(budget.getPaidContributionInkind());
			BigDecimal unpaidDocument = document.getValue()
					.subtract(paymentService.getPaymentForDocumentDotationSum(document.getId())
							.add(paymentService.getPaymentForDocumentContributionOwnSum(document.getId())
									.add(paymentService.getPaymentForDocumentContributionPersonalSum(document.getId())
											.add(paymentService.getPaymentForDocumentContributionInkindSum(document.getId())))));
			
			payment.setBudget(budget);
			model.addAttribute("payment", payment);
			
			unpaidDocument.add(payment.getSum());
			
			if((unpaidDotation.subtract(payment.getDotation()).signum()==-1) ||
					(unpaidContributionOwn.subtract(payment.getContributionOwn()).signum()==-1) ||
					(unpaidContributionPersonal.subtract(payment.getContributionPersonal()).signum()==-1) ||
					(unpaidContributionInkind.subtract(payment.getContributionInkind()).signum()==-1) ||
					(unpaidDocument.subtract(payment.getDotation().add(payment.getContributionOwn().add(payment.getContributionPersonal().add(payment.getContributionInkind())))).signum()==-1)) {
				if(unpaidDotation.subtract(payment.getDotation()).signum()==-1) 
					model.addAttribute("unpaidDotation", unpaidDotation);
				if(unpaidContributionOwn.subtract(payment.getContributionOwn()).signum()==-1) 
					model.addAttribute("unpaidContributionOwn", unpaidContributionOwn);
				if(unpaidContributionPersonal.subtract(payment.getContributionPersonal()).signum()==-1)
					model.addAttribute("unpaidContributionPersonal", unpaidContributionPersonal);
				if(unpaidContributionInkind.subtract(payment.getContributionInkind()).signum()==-1)
					model.addAttribute("unpaidContributionInkind", unpaidContributionInkind);
				if(unpaidDocument.subtract(payment.getDotation().add(payment.getContributionOwn().add(payment.getContributionPersonal().add(payment.getContributionInkind())))).signum()==-1)
					model.addAttribute("unpaidDocument", unpaidDocument);
				model.addAttribute("document", document);
				
				model.addAttribute("grant", grant);
				List<Budget> budgetList = budgetService.getBudgetForGrantList(grant.getId());
				model.addAttribute("budgetList", budgetList);
				return "payment_form";
			} else {
				paymentService.updatePayment(payment);
			}
			return "redirect:/grant/" + grant.getId() + "/document/" + document.getId();
		}
		return "redirect:/";
	}
*/
	@RequestMapping("/beneficiary/{idBeneficiary}/grant/{idGrant}/document/{idDocument}/payment/{idPayment}/payment_delete")
	public String deletePayment(Model model, Principal principal,
			@PathVariable(value="idBeneficiary", required=true) Integer idBeneficiary, 
			@PathVariable(value="idGrant", required=true) Integer idGrant, 
			@PathVariable(value="idDocument", required=true) Integer idDocument,
			@PathVariable(value="idPayment", required=true) Integer idPayment) {
		
		Beneficiary beneficiary = beneficiaryService.getBeneficiary(idBeneficiary);
		Grant grant = grantService.getGrant(idGrant);
		Document document = documentService.getDocument(idDocument);
		Payment payment = paymentService.getPayment(idPayment);
		if ((beneficiaryService.checkUser(beneficiary, principal.getName())>0) 
				&& (grant.getIdBeneficiary()==beneficiary.getId())
				&& (document.getIdGrant()==grant.getId())
				&& (payment.getDocument().getId()==document.getId())) {
			paymentService.deletePayment(idPayment);
			
			return "redirect:/beneficiary/{idBeneficiary}/grant/{idGrant}/document/{idDocument}";
		} else {
			return "redirect:/";
		}
	}
}
