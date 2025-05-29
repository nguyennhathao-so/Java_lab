$(function () {
  const images = [
    '/fe/assets/images/banner/banner1.jpeg',
    '/fe/assets/images/banner/banner2.jpeg',
    '/fe/assets/images/banner/banner3.jpeg',
    '/fe/assets/images/banner/banner4.jpeg',
  ];

  let current = 0;
  const $bg1 = $('.bg1');
  const $bg2 = $('.bg2');

  // Gán ảnh đầu tiên cho bg1
  $bg1.css('background-image', `url('${images[0]}')`).css('opacity', 1);

  setInterval(() => {
    current = (current + 1) % images.length;

    // Cập nhật ảnh mới vào lớp trên (bg2)
    $bg2
      .css('background-image', `url('${images[current]}')`)
      .stop(true, true)
      .css('opacity', 0)
      .animate({ opacity: 1 }, 1000, function () {
        // Sau khi mờ dần xong → đổi lại ảnh nền lớp bg1 và reset bg2
        $bg1.css('background-image', $bg2.css('background-image'));
        $bg2.css('opacity', 0);
      });
  }, 5000);
});
$(function () {
  $('.doctor-carousel').slick({
    slidesToShow: 3,             // Hiển thị 3 bác sĩ cùng lúc
    slidesToScroll: 1,
    autoplay: true,              // Bật tự động chuyển slide
    autoplaySpeed: 1000,         // Mỗi 3 giây
    arrows: false,               // Không hiển thị mũi tên trái/phải
    dots: true,                  // Hiển thị chấm tròn điều hướng
    pauseOnHover: false,         // KHÔNG dừng khi hover chuột
    responsive: [
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 2
        }
      },
      {
        breakpoint: 768,
        settings: {
          slidesToShow: 1
        }
      }
    ]
  });
});

