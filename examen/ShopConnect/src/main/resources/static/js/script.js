$(function(){

// Validation de l'enregistrement utilisateur

	var $userRegister=$("#userRegister");

	$userRegister.validate({

		rules:{
			name:{
				required:true,
				lettersonly:true
			}
			,
			email: {
				required: true,
				space: true,
				email: true
			},
			mobileNumber: {
				required: true,
				space: true,
				numericOnly: true,
				minlength: 10,
				maxlength: 12

			},
			password: {
				required: true,
				space: true

			},
			confirmpassword: {
				required: true,
				space: true,
				equalTo: '#pass'

			},
			address: {
				required: true,
				all: true

			},

			city: {
				required: true,
				space: true

			},
			state: {
				required: true,


			},
			pincode: {
				required: true,
				space: true,
				numericOnly: true

			}, img: {
				required: true,
			}

		},
		messages:{
			name:{
				required:'le nom est requis',
				lettersonly:'nom invalide'
			},
			email: {
				required: 'l\'email est requis',
				space: 'les espaces ne sont pas autorisés',
				email: 'email invalide'
			},
			mobileNumber: {
				required: 'le numéro de téléphone est requis',
				space: 'les espaces ne sont pas autorisés',
				numericOnly: 'numéro de téléphone invalide',
				minlength: 'minimum 10 chiffres',
				maxlength: 'maximum 12 chiffres'
			},

			password: {
				required: 'le mot de passe est requis',
				space: 'les espaces ne sont pas autorisés'

			},
			confirmpassword: {
				required: 'la confirmation du mot de passe est requise',
				space: 'les espaces ne sont pas autorisés',
				equalTo: 'les mots de passe ne correspondent pas'

			},
			address: {
				required: 'l\'adresse est requise',
				all: 'adresse invalide'

			},

			city: {
				required: 'la ville est requise',
				space: 'les espaces ne sont pas autorisés'

			},
			state: {
				required: 'l\'état est requis',
				space: 'les espaces ne sont pas autorisés'

			},
			pincode: {
				required: 'le code postal est requis',
				space: 'les espaces ne sont pas autorisés',
				numericOnly: 'code postal invalide'

			},
			img: {
				required: 'image requise',
			}
		}
	})


// Validation des commandes

var $orders=$("#orders");

$orders.validate({
		rules:{
			firstName:{
				required:true,
				lettersonly:true
			},
			lastName:{
				required:true,
				lettersonly:true
			}
			,
			email: {
				required: true,
				space: true,
				email: true
			},
			mobileNo: {
				required: true,
				space: true,
				numericOnly: true,
				minlength: 10,
				maxlength: 12

			},
			address: {
				required: true,
				all: true

			},

			city: {
				required: true,
				space: true

			},
			state: {
				required: true,


			},
			pincode: {
				required: true,
				space: true,
				numericOnly: true

			},
			paymentType:{
			required: true
			}
		},
		messages:{
			firstName:{
				required:'le prénom est requis',
				lettersonly:'prénom invalide'
			},
			lastName:{
				required:'le nom de famille est requis',
				lettersonly:'nom de famille invalide'
			},
			email: {
				required: 'l\'email est requis',
				space: 'les espaces ne sont pas autorisés',
				email: 'email invalide'
			},
			mobileNo: {
				required: 'le numéro de téléphone est requis',
				space: 'les espaces ne sont pas autorisés',
				numericOnly: 'numéro de téléphone invalide',
				minlength: 'minimum 10 chiffres',
				maxlength: 'maximum 12 chiffres'
			}
		   ,
			address: {
				required: 'l\'adresse est requise',
				all: 'adresse invalide'

			},

			city: {
				required: 'la ville est requise',
				space: 'les espaces ne sont pas autorisés'

			},
			state: {
				required: 'l\'état est requis',
				space: 'les espaces ne sont pas autorisés'

			},
			pincode: {
				required: 'le code postal est requis',
				space: 'les espaces ne sont pas autorisés',
				numericOnly: 'code postal invalide'

			},
			paymentType:{
			required: 'sélectionnez le type de paiement'
			}
		}
})

// Validation du mot de passe

var $resetPassword=$("#resetPassword");

$resetPassword.validate({

		rules:{
			password: {
				required: true,
				space: true

			},
			confirmPassword: {
				required: true,
				space: true,
				equalTo: '#pass'

			}
		},
		messages:{
		   password: {
				required: 'le mot de passe est requis',
				space: 'les espaces ne sont pas autorisés'

			},
			confirmpassword: {
				required: 'la confirmation du mot de passe est requise',
				space: 'les espaces ne sont pas autorisés',
				equalTo: 'les mots de passe ne correspondent pas'

			}
		}
})


})


jQuery.validator.addMethod('lettersonly', function(value, element) {
		return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
	});

		jQuery.validator.addMethod('space', function(value, element) {
		return /^[^-\s]+$/.test(value);
	});

	jQuery.validator.addMethod('all', function(value, element) {
		return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
	});


	jQuery.validator.addMethod('numericOnly', function(value, element) {
		return /^[0-9]+$/.test(value);
	});
