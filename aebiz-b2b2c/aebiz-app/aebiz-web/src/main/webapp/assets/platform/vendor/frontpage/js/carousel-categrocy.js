/* Home Page Onsale - Latest-items carousel */
var  recommondItems = $('.recommond-items-slider.owl-carousel');
if (self.checkSupport(recommondItems, $.fn.owlCarousel)) {
    recommondItems.owlCarousel({
        items: 4,
        itemsDesktop : [1199,4],
        itemsDesktopSmall: [979,3],
        itemsTablet: [768,2],
        itemsMobile : [479,1],
        slideSpeed: 400,
        autoPlay: 8000,
        stopOnHover: true,
        navigation: false,
        pagination: false,
        responsive: true,
        mouseDrag: false,
        autoHeight : true
    }).data('navigationBtns', ['#recommond-items-slider-prev', '#recommond-items-slider-next']);
}