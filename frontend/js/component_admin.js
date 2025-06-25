$(document).ready(function () {
    // Get the current path and adjust the relative path accordingly
    var path = window.location.pathname;
    var componentPath = path.includes('/admin/') ? '../components/slibar_admin.html' : 'components/slibar_admin.html';
    
    // Load the sidebar
    $("#slide-bar").load(componentPath, function(response, status, xhr) {
        if (status == "error") {
            // If first attempt fails, try the alternative path
            var altPath = path.includes('/admin/') ? 'components/slibar_admin.html' : '../components/slibar_admin.html';
            $("#slide-bar").load(altPath);
        }
    });

    // Display user name instead of "Admin"
    displayUserName();
    
    // Add logout functionality
    setupLogout();
});

// Function to display user name from localStorage
function displayUserName() {
    const userName = localStorage.getItem('userName');
    const userEmail = localStorage.getItem('userEmail');
    
    // Find all admin info spans and replace "Admin" with actual user name
    const adminSpans = document.querySelectorAll('.admin-info span');
    
    adminSpans.forEach(span => {
        if (span.textContent.trim() === 'Admin') {
            if (userName) {
                span.textContent = userName;
            } else if (userEmail) {
                // If no name, use email
                span.textContent = userEmail;
            } else {
                // Fallback to "Admin" if no user info
                span.textContent = 'Admin';
            }
        }
    });
}

// Function to setup logout functionality
function setupLogout() {
    const logoutImages = document.querySelectorAll('.admin-info img[alt="logout"]');
    
    logoutImages.forEach(img => {
        img.style.cursor = 'pointer';
        img.addEventListener('click', function() {
            // Clear localStorage
            localStorage.removeItem('authToken');
            localStorage.removeItem('userEmail');
            localStorage.removeItem('userName');
            localStorage.removeItem('role');
            localStorage.removeItem('userData');
            
            // Redirect to login page
            window.location.href = '../signin_signup.html';
        });
    });
}