$(document).ready(function () {

  // Tab switching
  $('.auth-tabs .tab').on('click', function () {
    $('.auth-tabs .tab').removeClass('active');
    $(this).addClass('active');

    $('.auth-form').removeClass('active');
    const target = $(this).data('target');
    $(target).addClass('active');
  });
});
