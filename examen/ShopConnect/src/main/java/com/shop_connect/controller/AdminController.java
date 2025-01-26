package com.shop_connect.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop_connect.model.Category;
import com.shop_connect.model.Product;
import com.shop_connect.model.ProductOrder;
import com.shop_connect.model.UserDtls;
import com.shop_connect.service.CartService;
import com.shop_connect.service.CategoryService;
import com.shop_connect.service.OrderService;
import com.shop_connect.service.ProductService;
import com.shop_connect.service.UserService;
import com.shop_connect.util.CommonUtil;
import com.shop_connect.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;


	/*** Méthode exécutée avant chaque requête pour injecter des informations globales dans le modèle.
	 * @param p Principal de l'utilisateur connecté.
	 * @param m Modèle pour ajouter des attributs accessibles dans les vues.
	 */
	@ModelAttribute
	public void getUserDetails(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			UserDtls userDtls = userService.getUserByEmail(email);
			m.addAttribute("user", userDtls);
			Integer countCart = cartService.getCountCart(userDtls.getId());
			m.addAttribute("countCart", countCart);
		}

		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		m.addAttribute("categorys", allActiveCategory);
	}


	/**
	 * Page d'accueil de l'espace administrateur.
	 * @return Vue `admin/index`.
	 */
	@GetMapping("/")
	public String index() {
		return "admin/index";
	}


	/**
	 * Charge la page d'ajout d'un produit.
	 * @param m Modèle pour injecter les catégories.
	 * @return Vue `admin/add_product`.
	 */
	@GetMapping("/loadAddProduct")
	public String loadAddProduct(Model m) {
		List<Category> categories = categoryService.getAllCategory();
		m.addAttribute("categories", categories);
		return "admin/add_product";
	}


	/**
	 * Gère la pagination et l'affichage des catégories.
	 *
	 * @param m Le modèle utilisé pour transmettre des données à la vue.
	 * @param pageNo Le numéro de la page à afficher (par défaut 0).
	 * @param pageSize La taille de la page, c'est-à-dire le nombre d'éléments par page (par défaut 10).
	 * @return Retourne la vue `admin/category` pour afficher les catégories paginées.
	 */
	@GetMapping("/category")
	public String category(@org.jetbrains.annotations.NotNull Model m,
						   @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
						   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		// Appel du service pour récupérer une page de catégories avec pagination.
		Page<Category> page = categoryService.getAllCategorPagination(pageNo, pageSize);

		// Récupération de la liste des catégories à partir de la page actuelle.
		List<Category> categorys = page.getContent();

		// Ajout des catégories au modèle pour les afficher dans la vue.
		m.addAttribute("categorys", categorys);

		// Ajout des informations de pagination pour une navigation dans les pages.
		m.addAttribute("pageNo", page.getNumber());          // Numéro de la page actuelle.
		m.addAttribute("pageSize", pageSize);                // Taille de la page.
		m.addAttribute("totalElements", page.getTotalElements()); // Nombre total d'éléments.
		m.addAttribute("totalPages", page.getTotalPages());  // Nombre total de pages.
		m.addAttribute("isFirst", page.isFirst());           // Indique si c'est la première page.
		m.addAttribute("isLast", page.isLast());             // Indique si c'est la dernière page.

		// Retourne la vue `admin/category` pour afficher les données.
		return "admin/category";
	}


	/**
	 * Sauvegarde une catégorie, y compris son image associée.
	 *
	 * @param category Objet catégorie à sauvegarder.
	 * @param file Image associée à la catégorie.
	 * @param session Session HTTP pour gérer les messages de feedback.
	 * @return Redirige vers la liste des catégories après la sauvegarde.
	 * @throws IOException En cas d'erreur lors de la gestion de l'image.
	 */
	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
							   HttpSession session) throws IOException {

		// Vérifie si un fichier a été fourni, sinon attribue une image par défaut.
		String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
		category.setImageName(imageName); // Attribue le nom de l'image à l'objet catégorie.

		// Vérifie si une catégorie avec le même nom existe déjà dans la base de données.
		Boolean existCategory = categoryService.existCategory(category.getName());

		if (existCategory) {
			// Si la catégorie existe déjà, ajoute un message d'erreur à la session.
			session.setAttribute("errorMsg", "Le nom de la catégorie existe déjà.");
		} else {
			// Sauvegarde la catégorie dans la base de données.
			Category savedCategory = categoryService.saveCategory(category);

			if (ObjectUtils.isEmpty(savedCategory)) {
				// Si la sauvegarde échoue, ajoute un message d'erreur à la session.
				session.setAttribute("errorMsg", "Erreur interne lors de la sauvegarde !");
			} else {
				// Sauvegarde l'image dans le répertoire des images.
				File saveFile = new ClassPathResource("static/img").getFile();

				// Définit le chemin pour sauvegarder l'image.
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
						+ file.getOriginalFilename());

				// Copie l'image dans le répertoire défini.
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				// Ajoute un message de succès à la session.
				session.setAttribute("succMsg", "Catégorie sauvegardée avec succès !");
			}
		}

		// Redirige vers la liste des catégories.
		return "redirect:/admin/category";
	}


	/**
	 * Supprime une catégorie identifiée par son ID.
	 *
	 * @param id ID de la catégorie à supprimer.
	 * @param session Session HTTP pour gérer les messages de feedback.
	 * @return Redirige vers la liste des catégories après la suppression.
	 */
	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable int id, HttpSession session) {
		// Appelle le service pour supprimer la catégorie par son ID.
		Boolean deleteCategory = categoryService.deleteCategory(id);

		if (deleteCategory) {
			// Si la suppression réussit, ajoute un message de succès à la session.
			session.setAttribute("succMsg", "Catégorie supprimée avec succès !");
		} else {
			// Si la suppression échoue, ajoute un message d'erreur à la session.
			session.setAttribute("errorMsg", "Erreur lors de la suppression de la catégorie !");
		}

		// Redirige vers la liste des catégories.
		return "redirect:/admin/category";
	}


	/**
	 * Charge les détails d'une catégorie pour modification.
	 *
	 * @param id ID de la catégorie à modifier.
	 * @param m Modèle pour transmettre les données à la vue.
	 * @return Retourne la vue `admin/edit_category` avec les détails de la catégorie.
	 */
	@GetMapping("/loadEditCategory/{id}")
	public String loadEditCategory(@PathVariable int id, Model m) {
		// Récupère la catégorie par son ID et l'ajoute au modèle.
		m.addAttribute("category", categoryService.getCategoryById(id));

		// Retourne la vue pour la modification de la catégorie.
		return "admin/edit_category";
	}


	/**
	 * Met à jour une catégorie avec une nouvelle image si fournie.
	 * @param category Catégorie à mettre à jour.
	 * @param file Nouvelle image pour la catégorie.
	 * @param session Session HTTP pour les messages de feedback.
	 * @return Redirection vers la page de modification.
	 * @throws IOException Erreur en cas de problème lors de la sauvegarde de l'image.
	 */
	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		Category oldCategory = categoryService.getCategoryById(category.getId());
		String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

		if (!ObjectUtils.isEmpty(category)) {

			oldCategory.setName(category.getName());
			oldCategory.setIsActive(category.getIsActive());
			oldCategory.setImageName(imageName);
		}

		Category updateCategory = categoryService.saveCategory(oldCategory);

		if (!ObjectUtils.isEmpty(updateCategory)) {

			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
						+ file.getOriginalFilename());

				// System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			session.setAttribute("succMsg", "Category update success");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/admin/loadEditCategory/" + category.getId();
	}


	/*** Sauvegarde un produit avec ses détails et son image associée.
	 *
	 * @param product L'objet représentant le produit à sauvegarder.
	 * @param image L'image associée au produit, envoyée via le formulaire.
	 * @param session La session HTTP pour transmettre les messages de feedback.
	 * @return Redirige vers la page de création d'un produit après la sauvegarde.
	 * @throws IOException En cas de problème lors de la gestion du fichier d'image.
	 */
	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
							  HttpSession session) throws IOException {

		// Si aucune image n'est fournie, utilise une image par défaut.
		String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

		// Associe le nom de l'image au produit.
		product.setImage(imageName);

		// Initialise les valeurs par défaut : pas de réduction, prix inchangé.
		product.setDiscount(0); // Réduction par défaut à 0%.
		product.setDiscountPrice(product.getPrice()); // Prix après réduction = prix initial.

		// Sauvegarde les informations du produit dans la base de données.
		Product savedProduct = productService.saveProduct(product);

		// Vérifie si le produit a été sauvegardé avec succès.
		if (!ObjectUtils.isEmpty(savedProduct)) {
			// Si oui, sauvegarde l'image associée dans le répertoire approprié.
			File saveFile = new ClassPathResource("static/img").getFile();

			// Détermine le chemin complet où enregistrer l'image.
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
					+ image.getOriginalFilename());

			// Copie l'image fournie dans le répertoire défini.
			Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			// Ajoute un message de succès à la session HTTP.
			session.setAttribute("succMsg", "Produit enregistré avec succès !");
		} else {
			// Si la sauvegarde échoue, ajoute un message d'erreur à la session HTTP.
			session.setAttribute("errorMsg", "Une erreur est survenue sur le serveur !");
		}

		// Redirige l'utilisateur vers la page de création d'un produit.
		return "redirect:/admin/loadAddProduct";
	}


	/**
	 * Charge une liste paginée de produits avec une option de recherche.
	 *
	 * @param m Le modèle utilisé pour transmettre les données à la vue.
	 * @param ch Le critère de recherche (facultatif).
	 * @param pageNo Le numéro de la page à afficher (par défaut 0).
	 * @param pageSize Le nombre de produits par page (par défaut 10).
	 * @return Retourne la vue `admin/products`.
	 */
	@GetMapping("/products")
	public String loadViewProduct(Model m, @RequestParam(defaultValue = "") String ch,
								  @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
								  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		// Déclare une variable pour contenir les produits paginés.
		Page<Product> page = null;

		// Si un critère de recherche est fourni, utilise le service pour chercher les produits correspondants.
		if (ch != null && ch.length() > 0) {
			page = productService.searchProductPagination(pageNo, pageSize, ch);
		} else {
			// Sinon, récupère tous les produits avec pagination.
			page = productService.getAllProductsPagination(pageNo, pageSize);
		}

		// Ajoute la liste des produits et les détails de la pagination au modèle.
		m.addAttribute("products", page.getContent());
		m.addAttribute("pageNo", page.getNumber());
		m.addAttribute("pageSize", pageSize);
		m.addAttribute("totalElements", page.getTotalElements());
		m.addAttribute("totalPages", page.getTotalPages());
		m.addAttribute("isFirst", page.isFirst());
		m.addAttribute("isLast", page.isLast());

		// Retourne la vue pour afficher les produits.
		return "admin/products";
	}


	/**
	 * Supprime un produit identifié par son ID.
	 *
	 * @param id ID du produit à supprimer.
	 * @param session Session HTTP pour gérer les messages de feedback.
	 * @return Redirige vers la liste des produits après la suppression.
	 */
	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id, HttpSession session) {
		// Appelle le service pour supprimer le produit par son ID.
		Boolean deleteProduct = productService.deleteProduct(id);

		if (deleteProduct) {
			// Ajoute un message de succès si la suppression réussit.
			session.setAttribute("succMsg", "Produit supprimé avec succès !");
		} else {
			// Ajoute un message d'erreur en cas d'échec.
			session.setAttribute("errorMsg", "Une erreur est survenue sur le serveur !");
		}

		// Redirige vers la liste des produits.
		return "redirect:/admin/products";
	}

	/**
	 * Charge les détails d'un produit pour modification.
	 *
	 * @param id ID du produit à modifier.
	 * @param m Le modèle utilisé pour transmettre les données à la vue.
	 * @return Retourne la vue `admin/edit_product` avec les détails du produit.
	 */
	@GetMapping("/editProduct/{id}")
	public String editProduct(@PathVariable int id, @NotNull Model m) {
		// Récupère le produit par son ID et l'ajoute au modèle.
		m.addAttribute("product", productService.getProductById(id));

		// Ajoute la liste des catégories au modèle pour permettre la sélection d'une catégorie.
		m.addAttribute("categories", categoryService.getAllCategory());

		// Retourne la vue pour la modification du produit.
		return "admin/edit_product";
	}


	/**
	 * Met à jour un produit avec de nouvelles informations, y compris son image.
	 *
	 * @param product L'objet contenant les nouvelles informations du produit.
	 * @param image La nouvelle image associée au produit (facultative).
	 * @param session La session HTTP pour gérer les messages de feedback.
	 * @param m Le modèle utilisé pour transmettre les données à la vue.
	 * @return Redirige vers la page d'édition du produit après la mise à jour.
	 */
	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
								HttpSession session, Model m) {

		// Vérifie si la réduction est valide (entre 0 et 100 %).
		if (product.getDiscount() < 0 || product.getDiscount() > 100) {
			session.setAttribute("errorMsg", "Réduction invalide !");
		} else {
			// Appelle le service pour mettre à jour le produit avec les nouvelles données.
			Product updatedProduct = productService.updateProduct(product, image);

			if (!ObjectUtils.isEmpty(updatedProduct)) {
				// Ajoute un message de succès si la mise à jour réussit.
				session.setAttribute("succMsg", "Produit mis à jour avec succès !");
			} else {
				// Ajoute un message d'erreur en cas d'échec.
				session.setAttribute("errorMsg", "Une erreur est survenue sur le serveur !");
			}
		}

		// Redirige vers la page d'édition pour le produit mis à jour.
		return "redirect:/admin/editProduct/" + product.getId();
	}


	/**
	 * Affiche la liste des utilisateurs selon leur rôle (administrateur ou utilisateur).
	 *
	 * @param m Le modèle pour transmettre les données à la vue.
	 * @param type Le type d'utilisateur : 1 pour les utilisateurs, autre pour les administrateurs.
	 * @return La vue `/admin/users` contenant la liste des utilisateurs.
	 */
	@GetMapping("/users")
	public String getAllUsers(Model m, @RequestParam Integer type) {
		List<UserDtls> users = null;

		// Vérifie le type d'utilisateur pour récupérer la liste correspondante.
		if (type == 1) {
			users = userService.getUsers("ROLE_USER"); // Récupère les utilisateurs normaux.
		} else {
			users = userService.getUsers("ROLE_ADMIN"); // Récupère les administrateurs.
		}

		// Ajoute les données au modèle pour l'afficher dans la vue.
		m.addAttribute("userType", type);
		m.addAttribute("users", users);

		// Retourne la vue affichant les utilisateurs.
		return "/admin/users";
	}


	/**
	 * Met à jour le statut du compte d'un utilisateur (activé/désactivé).
	 *
	 * @param status Le nouveau statut du compte (true pour activé, false pour désactivé).
	 * @param id L'ID de l'utilisateur dont le statut doit être mis à jour.
	 * @param type Le type d'utilisateur pour rediriger correctement.
	 * @param session La session HTTP pour ajouter des messages de feedback.
	 * @return Redirige vers la liste des utilisateurs après la mise à jour.
	 */
	@GetMapping("/updateSts")
	public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id,
										  @RequestParam Integer type, HttpSession session) {

		// Met à jour le statut de l'utilisateur.
		Boolean updated = userService.updateAccountStatus(id, status);

		// Ajoute un message de succès ou d'erreur à la session.
		if (updated) {
			session.setAttribute("succMsg", "Statut du compte mis à jour avec succès !");
		} else {
			session.setAttribute("errorMsg", "Erreur lors de la mise à jour du statut du compte.");
		}

		// Redirige vers la liste des utilisateurs en fonction du type.
		return "redirect:/admin/users?type=" + type;
	}


	/**
	 * Affiche la liste paginée de toutes les commandes.
	 *
	 * @param m Le modèle pour transmettre les données à la vue.
	 * @param pageNo Le numéro de la page à afficher (par défaut 0).
	 * @param pageSize Le nombre de commandes par page (par défaut 10).
	 * @return La vue `/admin/orders` contenant la liste des commandes.
	 */
	@GetMapping("/orders")
	public String getAllOrders(Model m, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
							   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		// Récupère les commandes paginées.
		Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo, pageSize);

		// Ajoute les commandes et les détails de la pagination au modèle.
		m.addAttribute("orders", page.getContent());
		m.addAttribute("srch", false);
		m.addAttribute("pageNo", page.getNumber());
		m.addAttribute("pageSize", pageSize);
		m.addAttribute("totalElements", page.getTotalElements());
		m.addAttribute("totalPages", page.getTotalPages());
		m.addAttribute("isFirst", page.isFirst());
		m.addAttribute("isLast", page.isLast());

		// Retourne la vue affichant les commandes.
		return "/admin/orders";
	}


	/**
	 * Met à jour le statut d'une commande.
	 *
	 * @param id L'ID de la commande à mettre à jour.
	 * @param st L'ID du nouveau statut de la commande.
	 * @param session La session HTTP pour ajouter des messages de feedback.
	 * @return Redirige vers la liste des commandes après la mise à jour.
	 */
	@PostMapping("/update-order-status")
	public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {

		// Recherche le nom du statut correspondant à l'ID.
		OrderStatus[] values = OrderStatus.values();
		String status = null;
		for (OrderStatus orderStatus : values) {
			if (orderStatus.getId().equals(st)) {
				status = orderStatus.getName();
				break;
			}
		}

		// Met à jour la commande avec le nouveau statut.
		ProductOrder updatedOrder = orderService.updateOrderStatus(id, status);

		// Envoie un email de confirmation pour la commande si nécessaire.
		try {
			commonUtil.sendMailForProductOrder(updatedOrder, status);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Ajoute un message de succès ou d'erreur à la session.
		if (!ObjectUtils.isEmpty(updatedOrder)) {
			session.setAttribute("succMsg", "Statut de la commande mis à jour avec succès !");
		} else {
			session.setAttribute("errorMsg", "Erreur lors de la mise à jour du statut.");
		}

		// Redirige vers la liste des commandes.
		return "redirect:/admin/orders";
	}


	/**
	 * Recherche une commande par son identifiant.
	 *
	 * @param orderId L'ID de la commande à rechercher.
	 * @param m Le modèle pour transmettre les données à la vue.
	 * @param session La session HTTP pour ajouter des messages de feedback.
	 * @param pageNo Le numéro de la page (par défaut 0).
	 * @param pageSize Le nombre de commandes par page (par défaut 10).
	 * @return La vue `/admin/orders` contenant les résultats de la recherche.
	 */
	@GetMapping("/search-order")
	public String searchProduct(@RequestParam String orderId, Model m, HttpSession session,
								@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
								@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		if (orderId != null && orderId.length() > 0) {
			// Recherche la commande par son ID.
			ProductOrder order = orderService.getOrdersByOrderId(orderId.trim());

			if (ObjectUtils.isEmpty(order)) {
				// Ajoute un message d'erreur si la commande n'existe pas.
				session.setAttribute("errorMsg", "Identifiant de commande incorrect !");
				m.addAttribute("orderDtls", null);
			} else {
				m.addAttribute("orderDtls", order);
			}

			m.addAttribute("srch", true);
		} else {
			// Si aucun ID n'est fourni, affiche la liste paginée des commandes.
			Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo, pageSize);
			m.addAttribute("orders", page.getContent());
			m.addAttribute("srch", false);
			m.addAttribute("pageNo", page.getNumber());
			m.addAttribute("pageSize", pageSize);
			m.addAttribute("totalElements", page.getTotalElements());
			m.addAttribute("totalPages", page.getTotalPages());
			m.addAttribute("isFirst", page.isFirst());
			m.addAttribute("isLast", page.isLast());
		}

		return "/admin/orders";
	}


	/**
	 * Charge la page pour ajouter un nouvel administrateur.
	 *
	 * @return La vue `/admin/add_admin` pour afficher le formulaire d'ajout d'administrateur.
	 */
	@GetMapping("/add-admin")
	public String loadAdminAdd() {
		// Retourne la vue associée pour ajouter un nouvel administrateur.
		return "/admin/add_admin";
	}


	/**
	 * Sauvegarde un nouvel administrateur après soumission du formulaire.
	 *
	 * @param user Les informations de l'administrateur à sauvegarder.
	 * @param file Le fichier image représentant la photo de profil.
	 * @param session La session HTTP pour stocker les messages de feedback.
	 * @return Redirige vers la page d'ajout d'administrateur après le traitement.
	 * @throws IOException Si une erreur se produit lors de la manipulation des fichiers.
	 */
	@PostMapping("/save-admin")
	public String saveAdmin(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		// Détermine le nom de l'image, avec une valeur par défaut si aucune image n'est fournie.
		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		user.setProfileImage(imageName); // Définit l'image dans l'objet utilisateur.

		// Sauvegarde les informations de l'administrateur en base de données.
		UserDtls saveUser = userService.saveAdmin(user);

		// Vérifie si la sauvegarde a été effectuée avec succès.
		if (!ObjectUtils.isEmpty(saveUser)) {
			// Si une image a été fournie, la copie dans le répertoire prévu à cet effet.
			if (!file.isEmpty()) {
				// Récupère le répertoire des images dans le projet.
				File saveFile = new ClassPathResource("static/img").getFile();

				// Définit le chemin où l'image sera sauvegardée.
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
						+ file.getOriginalFilename());

				// Copie l'image téléchargée dans le répertoire cible.
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			// Ajoute un message de succès dans la session.
			session.setAttribute("succMsg", "L'administrateur a été enregistré avec succès !");
		} else {
			// Ajoute un message d'erreur si la sauvegarde échoue.
			session.setAttribute("errorMsg", "Erreur serveur : L'administrateur n'a pas pu être enregistré.");
		}

		// Redirige vers la page d'ajout d'administrateur.
		return "redirect:/admin/add-admin";
	}


	/**
	 * Charge la page de profil de l'administrateur.
	 *
	 * @return La vue `/admin/profile`.
	 */
	@GetMapping("/profile")
	public String profile() {
		// Retourne la vue associée au profil de l'administrateur.
		return "/admin/profile";
	}


	/**
	 * Met à jour le profil de l'administrateur.
	 *
	 * @param user Les nouvelles informations de l'utilisateur.
	 * @param img Le fichier image représentant la photo de profil (facultatif).
	 * @param session La session HTTP pour stocker les messages de feedback.
	 * @return Redirige vers la page de profil après la mise à jour.
	 */
	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute UserDtls user, @RequestParam MultipartFile img, HttpSession session) {
		// Appelle le service pour mettre à jour le profil avec les nouvelles informations.
		UserDtls updateUserProfile = userService.updateUserProfile(user, img);

		// Vérifie si la mise à jour a réussi.
		if (ObjectUtils.isEmpty(updateUserProfile)) {
			// Ajoute un message d'erreur si la mise à jour a échoué.
			session.setAttribute("errorMsg", "Profil non mis à jour.");
		} else {
			// Ajoute un message de succès si la mise à jour a réussi.
			session.setAttribute("succMsg", "Profil mis à jour avec succès !");
		}

		// Redirige vers la page du profil.
		return "redirect:/admin/profile";
	}


	/**
	 * Change le mot de passe de l'administrateur.
	 *
	 * @param newPassword Le nouveau mot de passe fourni par l'utilisateur.
	 * @param currentPassword Le mot de passe actuel pour vérification.
	 * @param p Le principal contenant les détails de l'utilisateur connecté.
	 * @param session La session HTTP pour stocker les messages de feedback.
	 * @return Redirige vers la page de profil après la mise à jour.
	 */
	@PostMapping("/change-password")
	public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
								 HttpSession session) {
		// Récupère les détails de l'utilisateur actuellement connecté.
		UserDtls loggedInUserDetails = commonUtil.getLoggedInUserDetails(p);

		// Vérifie si le mot de passe actuel correspond à celui en base de données.
		boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

		if (matches) {
			// Si le mot de passe correspond, encode le nouveau mot de passe.
			String encodePassword = passwordEncoder.encode(newPassword);
			loggedInUserDetails.setPassword(encodePassword);

			// Met à jour les informations de l'utilisateur avec le nouveau mot de passe.
			UserDtls updateUser = userService.updateUser(loggedInUserDetails);

			// Vérifie si la mise à jour a réussi.
			if (ObjectUtils.isEmpty(updateUser)) {
				// Ajoute un message d'erreur si la mise à jour a échoué.
				session.setAttribute("errorMsg", "Mot de passe non mis à jour ! Erreur serveur.");
			} else {
				// Ajoute un message de succès si le mot de passe a été changé avec succès.
				session.setAttribute("succMsg", "Mot de passe mis à jour avec succès !");
			}
		} else {
			// Ajoute un message d'erreur si le mot de passe actuel est incorrect.
			session.setAttribute("errorMsg", "Mot de passe actuel incorrect.");
		}

		// Redirige vers la page de profil.
		return "redirect:/admin/profile";
	}

}
