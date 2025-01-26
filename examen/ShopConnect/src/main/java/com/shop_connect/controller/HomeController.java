package com.shop_connect.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop_connect.model.Category;
import com.shop_connect.model.Product;
import com.shop_connect.model.UserDtls;
import com.shop_connect.service.CartService;
import com.shop_connect.service.CategoryService;
import com.shop_connect.service.ProductService;
import com.shop_connect.service.UserService;
import com.shop_connect.util.CommonUtil;

import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Controller
public class HomeController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private CartService cartService;


	/**
	 * Méthode appelée avant chaque requête pour charger les détails de l'utilisateur.
	 *
	 * @param p Le principal contenant l'utilisateur actuellement connecté.
	 * @param m Le modèle pour ajouter des attributs utilisés dans les vues.
	 */
	@ModelAttribute
	public void getUserDetails(Principal p, Model m) {
		if (p != null) {
			// Récupère l'email de l'utilisateur connecté.
			String email = p.getName();

			// Recherche les détails de l'utilisateur par email.
			UserDtls userDtls = userService.getUserByEmail(email);

			// Ajoute les détails de l'utilisateur au modèle.
			m.addAttribute("user", userDtls);

			// Compte le nombre d'articles dans le panier de l'utilisateur.
			Integer countCart = cartService.getCountCart(userDtls.getId());
			m.addAttribute("countCart", countCart);
		}

		// Récupère toutes les catégories actives pour les afficher dans la barre de navigation.
		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys", allActiveCategory);
	}


	/**
	 * Affiche la page d'accueil avec les catégories et les produits les plus récents.
	 *
	 * @param m Le modèle pour ajouter des attributs utilisés dans la vue.
	 * @return La vue `index`.
	 */
	@GetMapping("/")
	public String index(Model m) {

		// Récupère les catégories actives (triées par ID décroissant).
		List<Category> allActiveCategory = categoryService.getAllActiveCategory().stream()
				.sorted((c1, c2) -> c2.getId().compareTo(c1.getId()))
				.toList();

		// Récupère les 12 derniers produits actifs (triés par ID décroissant).
		List<Product> allActiveProducts = productService.getAllActiveProducts("").stream()
				.sorted((p1, p2) -> p2.getId().compareTo(p1.getId()))
				.limit(12)
				.toList();

		// Ajoute les catégories et produits au modèle pour affichage.
		m.addAttribute("category", allActiveCategory);
		m.addAttribute("products", allActiveProducts);

		// Retourne la vue de la page d'accueil.
		return "index";
	}


	/**
	 * Charge la page de connexion.
	 *
	 * @return La vue `login`.
	 */
	@GetMapping("/signin")
	public String login() {
		return "login";
	}


	/**
	 * Charge la page d'inscription.
	 *
	 * @return La vue `register`.
	 */
	@GetMapping("/register")
	public String register() {
		return "register";
	}


	/**
	 * Affiche les produits avec pagination et filtrage.
	 *
	 * @param m       Le modèle pour ajouter des attributs utilisés dans la vue.
	 * @param category Le filtre pour une catégorie spécifique.
	 * @param pageNo   Le numéro de la page à afficher (par défaut : 0).
	 * @param pageSize Le nombre de produits par page (par défaut : 12).
	 * @param ch       Le mot-clé pour rechercher des produits.
	 * @return La vue `product` pour afficher les produits filtrés.
	 */
	@GetMapping("/products")
	public String products(@NotNull Model m,
						   @RequestParam(value = "category", defaultValue = "") String category,
						   @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
						   @RequestParam(name = "pageSize", defaultValue = "12") Integer pageSize,
						   @RequestParam(defaultValue = "") String ch) {

		// Récupère toutes les catégories actives pour les afficher dans le filtre.
		List<Category> categories = categoryService.getAllActiveCategory();
		m.addAttribute("paramValue", category); // La catégorie sélectionnée.
		m.addAttribute("categories", categories); // Toutes les catégories.

		// Déclare une variable pour gérer la pagination.
		Page<Product> page = null;

		// Si un mot-clé de recherche est fourni.
		if (StringUtils.isEmpty(ch)) {
			// Récupère les produits actifs d'une catégorie donnée avec pagination.
			page = productService.getAllActiveProductPagination(pageNo, pageSize, category);
		} else {
			// Recherche les produits actifs par mot-clé avec pagination.
			page = productService.searchActiveProductPagination(pageNo, pageSize, category, ch);
		}

		// Ajoute les produits paginés au modèle.
		List<Product> products = page.getContent();
		m.addAttribute("products", products);
		m.addAttribute("productsSize", products.size());

		// Ajoute les informations de pagination.
		m.addAttribute("pageNo", page.getNumber());
		m.addAttribute("pageSize", pageSize);
		m.addAttribute("totalElements", page.getTotalElements());
		m.addAttribute("totalPages", page.getTotalPages());
		m.addAttribute("isFirst", page.isFirst());
		m.addAttribute("isLast", page.isLast());

		// Retourne la vue pour afficher les produits.
		return "product";
	}


	/**
	 * Affiche les détails d'un produit en fonction de son ID.
	 *
	 * @param id L'identifiant du produit.
	 * @param m  Le modèle pour ajouter les attributs nécessaires à la vue.
	 * @return La vue `view_product` pour afficher les détails du produit.
	 */
	@GetMapping("/product/{id}")
	public String product(@PathVariable int id, Model m) {
		// Recherche le produit par son ID
		Product productById = productService.getProductById(id);
		// Ajoute le produit au modèle
		m.addAttribute("product", productById);
		// Retourne la vue pour afficher les détails du produit
		return "view_product";
	}


	/**
	 * Enregistre un nouvel utilisateur.
	 *
	 * @param user    L'objet utilisateur contenant les informations du formulaire.
	 * @param file    Le fichier image uploadé (photo de profil).
	 * @param session La session HTTP pour afficher des messages de succès ou d'erreur.
	 * @return Redirige vers la page d'inscription.
	 * @throws IOException En cas d'erreur lors de l'enregistrement de l'image.
	 */
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		// Vérifie si l'email de l'utilisateur existe déjà
		Boolean existsEmail = userService.existsEmail(user.getEmail());

		if (existsEmail) {
			// Ajoute un message d'erreur à la session si l'email existe déjà
			session.setAttribute("errorMsg", "Email already exist");
		} else {
			// Définit l'image par défaut si aucune image n'est uploadée
			String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
			user.setProfileImage(imageName);

			// Enregistre l'utilisateur dans la base de données
			UserDtls saveUser = userService.saveUser(user);

			if (!ObjectUtils.isEmpty(saveUser)) {
				// Sauvegarde l'image dans le répertoire défini
				if (!file.isEmpty()) {
					File saveFile = new ClassPathResource("static/img").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
							+ file.getOriginalFilename());
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				}
				session.setAttribute("succMsg", "Register successfully");
			} else {
				session.setAttribute("errorMsg", "something wrong on server");
			}
		}

		// Redirige vers la page d'inscription
		return "redirect:/register";
	}


	/**
	 * Affiche la page de réinitialisation de mot de passe.
	 *
	 * @return La vue `forgot_password.html`.
	 */
	@GetMapping("/forgot-password")
	public String showForgotPassword() {
		return "forgot_password.html";
	}


	/**
	 * Traite une demande de réinitialisation de mot de passe.
	 *
	 * @param email   L'email de l'utilisateur ayant demandé une réinitialisation.
	 * @param session La session HTTP pour afficher des messages.
	 * @param request L'objet de requête HTTP pour construire le lien de réinitialisation.
	 * @return Redirige vers la page de réinitialisation de mot de passe.
	 * @throws UnsupportedEncodingException En cas d'erreur d'encodage.
	 * @throws MessagingException           En cas d'erreur d'envoi d'email.
	 */
	@PostMapping("/forgot-password")
	public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {

		// Recherche l'utilisateur par email
		UserDtls userByEmail = userService.getUserByEmail(email);

		if (ObjectUtils.isEmpty(userByEmail)) {
			session.setAttribute("errorMsg", "Invalid email");
		} else {
			// Génère un token unique pour la réinitialisation
			String resetToken = UUID.randomUUID().toString();
			userService.updateUserResetToken(email, resetToken);

			// Crée l'URL de réinitialisation
			String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;

			// Envoie l'email avec le lien de réinitialisation
			Boolean sendMail = commonUtil.sendMail(url, email);

			if (sendMail) {
				session.setAttribute("succMsg", "Please check your email..Password Reset link sent");
			} else {
				session.setAttribute("errorMsg", "Something wrong on server ! Email not sent");
			}
		}

		return "redirect:/forgot-password";
	}


	/**
	 * Affiche la page de réinitialisation de mot de passe avec un token.
	 *
	 * @param token   Le token unique pour la réinitialisation.
	 * @param session La session HTTP.
	 * @param m       Le modèle pour ajouter des attributs nécessaires à la vue.
	 * @return La vue `reset_password` ou un message d'erreur.
	 */
	@GetMapping("/reset-password")
	public String showResetPassword(@RequestParam String token, HttpSession session, Model m) {
		UserDtls userByToken = userService.getUserByToken(token);

		if (userByToken == null) {
			m.addAttribute("msg", "Your link is invalid or expired !!");
			return "message";
		}
		m.addAttribute("token", token);
		return "reset_password";
	}


	/**
	 * Réinitialise le mot de passe d'un utilisateur.
	 *
	 * @param token    Le token unique pour identifier l'utilisateur.
	 * @param password Le nouveau mot de passe.
	 * @param session  La session HTTP.
	 * @param m        Le modèle pour ajouter des messages.
	 * @return La vue `message` indiquant le succès ou l'échec.
	 */
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String password, HttpSession session,
								Model m) {

		UserDtls userByToken = userService.getUserByToken(token);

		if (userByToken == null) {
			m.addAttribute("errorMsg", "Your link is invalid or expired !!");
			return "message";
		} else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null);
			userService.updateUser(userByToken);
			m.addAttribute("msg", "Password change successfully");

			return "message";
		}
	}


	/**
	 * Recherche des produits à partir d'un mot-clé.
	 *
	 * @param ch Le mot-clé pour la recherche.
	 * @param m  Le modèle pour ajouter les résultats de recherche.
	 * @return La vue `product` affichant les produits correspondants.
	 */
	@GetMapping("/search")
	public String searchProduct(@RequestParam String ch, Model m) {
		// Recherche les produits correspondant au mot-clé
		List<Product> searchProducts = productService.searchProduct(ch);
		m.addAttribute("products", searchProducts);

		// Ajoute les catégories actives pour le filtre
		List<Category> categories = categoryService.getAllActiveCategory();
		m.addAttribute("categories", categories);

		return "product";
	}
}

