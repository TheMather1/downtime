$(document).click(function(event) {
    if ($(event.target).parents('.formModal').length === 0 && !$(event.target).hasClass('formOpen')) {
        $('.formModal').hide();
    }
});

$(document).on('keydown', function(event) {
    if (event.key === 'Escape') {
        $('.formModal').hide();
    }
});