$('.menu').hover(function () {
        $(this).addClass('active');   
        $('._ogran').addClass('menuActived');     
    }, function () {
        $(this).removeClass('active');
        $('._ogran').removeClass('menuActived');
    }
);