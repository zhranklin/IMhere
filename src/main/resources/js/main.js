function Filter(el){
	var filter = this;
	this.el = el;
	var option_type = this.el.data('option');
	this.showOptions = function(){
		$.ajax(option_type, {
			context: filter,
			success: function(response){
				var result = $(this.el).closest('.filter').find('.filter-result');
				result.html(response);
				result.slideDown('400');
			}
		})
	}
	this.el.on('click', 'button', this.showOptions);
}
function changeForm(el){
	var form = this;
	this.el = el;
	var form_type = this.el.data('form');
	this.showForms = function(){
		$.ajax(form_type, {
			context: form,
			success: function(response){
				$(this.el).closest('.container').find('.main-view').html(response);
			}
		})
	}
	this.el.on('click', 'i, img', this.showForms);
}
function showCover(el){
	var trigger = this;
	this.el = el;
	var cover = this.el.data('cover');
	this.subShowCover = function(){
		$.ajax(cover, {
			context: trigger,
			success: function(response){
				var form = $(this.el).closest('body').find('.create-form');
				form.html(response);
				form.hide();
				form.fadeIn('400');
			}
		})
	}
	this.el.on('click', 'i', this.subShowCover);
}
function goToUrl(url) {
	window.location.href = url;
}
jQuery(document).ready(function($) {
	var position = new Filter($('.filter').find('.position'));
	var price = new Filter($('.filter').find('.price'));
	var time = new Filter($('.filter').find('.time'));
	var my_missions = new changeForm($('.bottom-nav').find('.my-missions'));
	var info = new changeForm($('.bottom-nav').find('.info'))
	var missions = new changeForm($('.bottom-nav').find('.missions'));
	var cover = new showCover($('.top-nav').find('.btn-add'));
	var head_info = new changeForm($('.top-nav').find('.head'));

	$('.create-form').find('.cancel').on('click', 'button', function(){
		$(this).closest('.create-bg').hide('400');
	});
	$('.filter-result').find('.cancel').on('click', 'button', function(){
		$(this).closest('.filter-result').hide('slow');
	});
});