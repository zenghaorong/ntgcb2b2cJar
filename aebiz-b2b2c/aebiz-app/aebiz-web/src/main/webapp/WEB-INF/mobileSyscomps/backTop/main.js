/**
 * Created by wenyu on 2017/5/10.
 */
Comps.backTop = function(){

};

Comps.backTop.prototype.init = function(){
    $(window).scroll(function() {
        if ($(window).scrollTop() > 1000) {
            $(".m-backtop").addClass("ReturnTop");
        } else {
            $(".m-backtop").removeClass("ReturnTop");
        } //2000像素时返回顶部标志出现
    });

    $(".m-backtop").click(function() {
        $('html,body').animate({ 'scrollTop': 0 }, 800);
    });
};