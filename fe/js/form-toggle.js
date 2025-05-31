$(document).ready(function () {
  $('.noti-content').each(function () {
  const lineHeight = parseFloat($(this).css('line-height')) || 22;
  const maxLines = 3;
  const maxHeight = lineHeight * maxLines;

  console.log('→ nội dung:', this.innerText);
  console.log('→ scrollHeight:', this.scrollHeight, 'vs maxHeight:', maxHeight);

  if (this.scrollHeight > maxHeight + 5) {
    $(this).siblings('.noti-readmore').show();
  }
});


  $('.noti-readmore').click(function (e) {
    e.preventDefault();
    const $content = $(this).siblings('.noti-content');
    $content.toggleClass('expanded');
    $(this).text($content.hasClass('expanded') ? 'Thu Gọn' : 'Đọc Thêm');
  });
});
