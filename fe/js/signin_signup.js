$(document).ready(function () {



  
  // Kiểm tra email đã tồn tại
  function checkEmail(email) {
    return $.ajax({
      url: `http://localhost:8082/api/auth/check-email`,
      method: 'GET',
      data: { email: email },
      dataType: 'json'
    }).catch(error => {
      console.error('Error checking email:', error);
      return false;
    });
  }

  // Kiểm tra số điện thoại đã tồn tại
  function checkPhoneNumber(phoneNumber) {
    return $.ajax({
      url: `http://localhost:8082/api/auth/check-phone`,
      method: 'GET',
      data: { phoneNumber: phoneNumber },
      dataType: 'json'
    }).catch(error => {
      console.error('Error checking phone number:', error);
      return false;
    });
  }

  // Tab switching
  $('.auth-tabs .tab').on('click', function () {
    $('.auth-tabs .tab').removeClass('active');
    $(this).addClass('active');

    $('.auth-form').removeClass('active');
    const target = $(this).data('target');
    $(target).addClass('active');
  });

  // Xử lý sự kiện khi email thay đổi
  $('#register-form input[type="email"]').on('blur', function() {
    const email = $(this).val();
    if (email) {
      checkEmail(email).then(exists => {
        if (exists) {
          alert('Email này đã được sử dụng. Vui lòng sử dụng email khác.');
          $(this).focus();
        }
      });
    }
  });

  // Xử lý sự kiện khi số điện thoại thay đổi
  $('#register-form input[type="tel"]').on('blur', function() {
    const phoneNumber = $(this).val();
    if (phoneNumber) {
      checkPhoneNumber(phoneNumber).then(exists => {
        if (exists) {
          alert('Số điện thoại này đã được sử dụng. Vui lòng sử dụng số điện thoại khác.');
          $(this).focus();
        }
      });
    }
  });

  // Kiểm tra mật khẩu khớp nhau
  $('#pass2').on('input', function() {
    const pass1 = $('#pass1').val();
    const pass2 = $(this).val();
    const nextFields = $('#register-form input[type="tel"], #register-form input[type="text"]:eq(1), #register-form select');
    
    if (pass1 === pass2) {
      // Mật khẩu trùng khớp
      $(this).css('border-color', 'green');
      $('#password-match-error').remove();
      nextFields.prop('disabled', false); // Mở khóa các trường tiếp theo
    } else {
      // Mật khẩu không trùng khớp
      $(this).css('border-color', 'red');
      if (!$('#password-match-error').length) {
        $(this).after('<div id="password-match-error" style="color: red; font-size: 12px;">Mật khẩu xác nhận không khớp!</div>');
      }
      nextFields.prop('disabled', true); // Khóa các trường tiếp theo
    }
  });

  // Khóa các trường sau mật khẩu khi trang được tải
  $(document).ready(function() {
    $('#register-form input[type="tel"], #register-form input[type="text"]:eq(1), #register-form select').prop('disabled', true);
  });

  // Xử lý submit form đăng ký
  $('#register-form').on('submit', function(event) {
    event.preventDefault();

    const email = $(this).find('input[type="email"]').val();
    const phoneNumber = $(this).find('input[type="tel"]').val();
    const pass1 = $('#pass1').val();
    const pass2 = $('#pass2').val();

    // Kiểm tra mật khẩu khớp nhau
    if (pass1 !== pass2) {
      alert('Mật khẩu xác nhận không khớp!');
      $('#pass2').focus();
      return;
    }

    // Kiểm tra lại email và số điện thoại trước khi submit
    $.when(checkEmail(email), checkPhoneNumber(phoneNumber))
      .then(function(emailExists, phoneExists) {
        if (emailExists[0]) {
          alert('Email này đã được sử dụng. Vui lòng sử dụng email khác.');
          $('#register-form input[type="email"]').focus();
          return;
        }

        if (phoneExists[0]) {
          alert('Số điện thoại này đã được sử dụng. Vui lòng sử dụng số điện thoại khác.');
          $('#register-form input[type="tel"]').focus();
          return;
        }

        // Nếu tất cả đều hợp lệ, tiến hành đăng ký
        const formData = {
          fullName: $(this).find('input[type="text"]').first().val(),
          email: email,
          password: pass1,
          phoneNumber: phoneNumber,
          address: $(this).find('input[type="text"]').eq(1).val(),
          gender: $(this).find('select').first().val(),
          bloodType: $(this).find('select').last().val()
        };

        $.ajax({
          url: 'http://localhost:8082/api/auth/register',
          method: 'POST',
          contentType: 'application/json',
          data: JSON.stringify(formData),
          success: function(response) {
            // Lưu thông tin đăng nhập từ response
            if (response.token) {
              localStorage.setItem('authToken', response.token);
              localStorage.setItem('userEmail', response.email);
              localStorage.setItem('userName', response.fullName);
              localStorage.setItem('role', response.role);
              
              // Chuyển đến trang chủ
              window.location.href = 'trangChu.html';
            }
          },
          error: function(xhr) {
            alert(xhr.responseText || 'Có lỗi xảy ra khi đăng ký. Vui lòng thử lại.');
          }
        });
      }.bind(this))
      .fail(function(error) {
        console.error('Error:', error);
        alert('Có lỗi xảy ra khi đăng ký. Vui lòng thử lại.');
      });
  });

  // Xử lý đăng nhập
  $('#login-form').on('submit', function(event) {
    event.preventDefault();
    
    const emailInput = $(this).find('input[type="email"]');
    const passwordInput = $('#login-pass');
    const submitButton = $(this).find('button[type="submit"]');
    
    // Validate inputs
    if (!emailInput.val().trim()) {
      alert('Vui lòng nhập email!');
      emailInput.focus();
      return;
    }
    
    if (!passwordInput.val().trim()) {
      alert('Vui lòng nhập mật khẩu!');
      passwordInput.focus();
      return;
    }

    const loginData = {
      email: emailInput.val().trim(),
      password: passwordInput.val().trim()
    };

    // Disable form during API call
    emailInput.prop('disabled', true);
    passwordInput.prop('disabled', true);
    submitButton.prop('disabled', true);
    submitButton.text('Đang đăng nhập...');

    $.ajax({
      url: 'http://localhost:8082/api/auth/login',
      method: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(loginData),
      success: function(response) {
        // Store authentication token and user info
        if (response.token) {
          localStorage.setItem('authToken', response.token);
          localStorage.setItem('userEmail', response.email);
          localStorage.setItem('userName', response.fullName);
          localStorage.setItem('role', response.role);
          
          // Redirect to home page after successful login
          window.location.href = 'trangChu.html';
        }
      },
      error: function(xhr) {
        let errorMessage = 'Email hoặc mật khẩu không đúng!';
        
        if (xhr.responseJSON && xhr.responseJSON.message) {
          errorMessage = xhr.responseJSON.message;
        } else if (xhr.status === 0) {
          errorMessage = 'Không thể kết nối đến máy chủ. Vui lòng thử lại sau!';
        }
        
        alert(errorMessage);
      },
      complete: function() {
        // Re-enable form
        emailInput.prop('disabled', false);
        passwordInput.prop('disabled', false);
        submitButton.prop('disabled', false);
        submitButton.text('Đăng Nhập');
      }
    });
  });
});
