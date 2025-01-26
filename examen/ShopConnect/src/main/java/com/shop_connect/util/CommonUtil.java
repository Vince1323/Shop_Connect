package com.shop_connect.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.shop_connect.model.ProductOrder;
import com.shop_connect.model.UserDtls;
import com.shop_connect.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Classe utilitaire pour les envois d'emails.
 */
@Component
public class CommonUtil {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserService userService;


	/**
	 * Méthode pour envoyer un email de réinitialisation de mot de passe.
	 */
	public Boolean sendMail(String url, String recipientEmail) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("vincent.vanhees13@gmail.com", "Shop Connect");
		helper.setTo(recipientEmail);

		String content = "<p>Bonjour,</p>"
				+ "<p>Vous avez demandé à réinitialiser votre mot de passe.</p>"
				+ "<p>Cliquez sur le lien ci-dessous pour le changer :</p>"
				+ "<p><a href=\"" + url + "\">Réinitialiser mon mot de passe</a></p>";
		helper.setSubject("Réinitialisation du mot de passe");
		helper.setText(content, true);


		mailSender.send(message);
		return true;
	}


	/**
	 * Génère une URL basée sur la requête actuelle.
	 */
	public static String generateUrl(HttpServletRequest request) {
		String siteUrl = request.getRequestURL().toString();
		return siteUrl.replace(request.getServletPath(), "");
	}


	/**
	 * Méthode pour envoyer un email contenant le statut de commande.
	 */
	public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception {
		String msg = "<p>Bonjour [[name]],</p>"
				+ "<p>Merci pour votre commande. Statut : <b>[[orderStatus]]</b>.</p>"
				+ "<p><b>Détails du produit :</b></p>"
				+ "<p>Nom : [[productName]]</p>"
				+ "<p>Catégorie : [[category]]</p>"
				+ "<p>Quantité : [[quantity]]</p>"
				+ "<p>Prix : [[price]]</p>"
				+ "<p>Mode de paiement : [[paymentType]]</p>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("vincent.vanhees13@gmail.com", "Shop Connect");
		helper.setTo(order.getOrderAddress().getEmail());

		msg = msg.replace("[[name]]", order.getOrderAddress().getFirstName());
		msg = msg.replace("[[orderStatus]]", status);
		msg = msg.replace("[[productName]]", order.getProduct().getTitle());
		msg = msg.replace("[[category]]", order.getProduct().getCategory());
		msg = msg.replace("[[quantity]]", order.getQuantity().toString());
		msg = msg.replace("[[price]]", order.getPrice().toString());
		msg = msg.replace("[[paymentType]]", order.getPaymentType());

		helper.setSubject("Product Order Status");
		helper.setText(msg, true);

		mailSender.send(message);
		return true;
	}


	/**
	 * Récupère les détails de l'utilisateur connecté.
	 */
	public UserDtls getLoggedInUserDetails(Principal principal) {
		String email = principal.getName();
		return userService.getUserByEmail(email);
	}
}
