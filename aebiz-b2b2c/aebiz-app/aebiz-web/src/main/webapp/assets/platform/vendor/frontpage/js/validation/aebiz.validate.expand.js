jQuery.validator.addMethod("isZipCode", function(value, element) {  
    var tel = /^[0-9]{6}$/;
    return this.optional(element) || (tel.test(value));
}, "请正确填写您的邮政编码");

jQuery.validator.addMethod("mobilezh", function(value, element) {  
    var mobile = /^1\d{10}$/;
    return this.optional(element) || (mobile.test(value));
}, "请正确填写您的手机号");

jQuery.validator.addMethod("telephone", function(value, element) {  
    var telephone = /^\d{3}-\d{8}|\d{4}-\d{7,8}$/;
    return this.optional(element) || (telephone.test(value));
}, "电话号码输入不正确，请重新输入");

jQuery.validator.addMethod("loginname", function(value, element) {  
    var loginname =/^[\w\.\_]{4,20}$/;    
    return this.optional(element) || (loginname.test(value));
}, "请输入4-20位字符(数字、字母、.、_)");
jQuery.validator.addMethod("positivenum", function(value, element) {  
    var positivenum = /^(0|[1-9]\d*)$/;
    return this.optional(element) || (positivenum.test(value));
}, "请输入非负整数");

jQuery.validator.addMethod("storeDomain", function(value, element) {  
	var storeDomain =/^[a-z0-9]{2,15}$/;    
	return this.optional(element) || (storeDomain.test(value));
}, "请输入2-15位字符(数字、字母)");

jQuery.validator.addMethod("float", function(value, element) {  
	var float =/^[0-9]+([.]{1}[0-9]+){0,1}$/;    
	return this.optional(element) || (float.test(value));
}, "请输入正数");

jQuery.validator.addMethod("phone", function(value, element) {  
	var phone =/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/; 
	return this.optional(element) || (phone.test(value));
}, "请输入正确的电话号");

jQuery.validator.addMethod("fax", function(value, element) {  
	var fax =/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/; 
	return this.optional(element) || (fax.test(value));
}, "请输入正确的传真号");

jQuery.validator.addMethod("isPrice", function(value, element) {  
	var storeDomain =/^[0-9]+([.]{1}[0-9]{0,2}){0,1}$/;    
	return this.optional(element) || (storeDomain.test(value));
}, "请输入正确的价格");

jQuery.validator.addMethod("nonnegativenum", function(value, element) {  
    var nonnegativenum = /^(([1-9]\d*)|0)(\.\d{0,2})?$/;
    return this.optional(element) || (nonnegativenum.test(value));
}, "请输入最多2位小数的非负数");
jQuery.validator.addMethod("integernum", function(value, element) {  
    var integernum = /^[1-9]\d*$/;
    return this.optional(element) || (integernum.test(value));
}, "请输入正整数");

jQuery.validator.addMethod("isEnglish", function(value, element) {  
	var storeDomain =/^[a-zA-Z]+$/;    
	return this.optional(element) || (storeDomain.test(value));
}, "请输入英文字母");

jQuery.validator.addMethod("productNo", function(value, element) {  
	var storeDomain =/^[a-zA-Z0-9]+$/;    
	return this.optional(element) || (storeDomain.test(value));
}, "请输入正确的商品编号");

jQuery.validator.addMethod("invoiceNo", function(value, element) {  
	var storeDomain =/^[a-zA-Z0-9]+$/;    
	return this.optional(element) || (storeDomain.test(value));
}, "请输入正确的发票编号");
jQuery.validator.addMethod("mobileOrPhone", function(value, element) {  
    var mobile = /^1\d{10}$/;
    var phone = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/; 
    return this.optional(element) || (mobile.test(value)) || (phone.test(value));
}, "请填写正确的手机号或电话号码");
