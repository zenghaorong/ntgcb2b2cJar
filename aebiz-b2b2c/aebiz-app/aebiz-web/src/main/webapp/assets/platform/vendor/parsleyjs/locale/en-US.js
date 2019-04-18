// ParsleyConfig definition if not already set
window.ParsleyConfig = window.ParsleyConfig || {};
window.ParsleyConfig.i18n = window.ParsleyConfig.i18n || {};
window.ParsleyConfig.validators = window.ParsleyConfig.validators || {};
// Define then the messages
window.ParsleyConfig.i18n.zh_cn = $.extend(window.ParsleyConfig.i18n.zh_cn
		|| {}, {
	defaultMessage : "Is not the correct value",
	type : {
		email : "Please enter a valid E-mail address",
		url : "Please enter a valid link",
		number : "Please enter the correct number",
		integer : "Please enter the correct integer",
		digits : "Please enter the correct number",
		alphanum : "Please enter the letters or Numbers",
		rate : "Please enter the correct rate"
	},
	notblank : "Please enter a value",
	required : "Mandatory",
	pattern : "The format is not correct",
	min : "The input value is greater than or equal to, please %s",
	max : "Please input value less than or equal to zero %s",
	range : "The input value should be in %s to %s between",
	minlength : "Please enter at least %s a character",
	maxlength : "Please enter up to %s a character",
	length : "Character length should be in %s to %s between",
	mincheck : "Please select at least %s an option",
	maxcheck : "Please select no more than %s an option",
	check : "Please select %s to %s an option",
	equalto : "The input value is different",
	dateiso : "Please enter the correct date format (YYYY-MM-DD)"
});
// If file is loaded after Parsley main file, auto-load locale
if ('undefined' !== typeof window.Parsley)
	window.Parsley.addCatalog('zh_cn', window.ParsleyConfig.i18n.zh_cn, true);
$(function() {
	window.Parsley.addValidator('price', {
		validateString : function(value) {
			var price = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
			return (price.test(value));
		},
		messages : {
			zh_cn : 'Please fill in the correct amount'
		}
	});
	window.Parsley.addValidator('phone', {
		validateString : function(value) {
			var phone = /^1[3|4|5|7|8][0-9]\d{8}$/;
			return (phone.test(value));
		},
		messages : {
			zh_cn : 'Please fill in the correct phone number'
		}
	});
	window.Parsley.addValidator('rate', {
		validateString : function(value) {
			var rate = /^([1-9]\d*|0)(\.\d{1,2})?$/;
			return (rate.test(value));
		},
		messages : {
			zh_cn : 'Please fill in the correct rate'
		}
	});

	window.Parsley
			.addValidator(
					'email',
					{
						validateString : function(value) {
							var email = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))){2,6}$/i;
							return (email.test(value));
						},
						messages : {
							zh_cn : 'Please fill in the correct email address'
						}
					});
});
