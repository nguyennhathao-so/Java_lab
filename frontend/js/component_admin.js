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
});