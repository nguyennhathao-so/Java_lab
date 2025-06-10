// Example of using the API service
async function loadUserData() {
    try {
        // Get user data from backend
        const userData = await apiService.getData('/api/users/current');
        console.log('User data:', userData);
        
        // Update UI with user data
        document.getElementById('userName').textContent = userData.name;
    } catch (error) {
        console.error('Failed to load user data:', error);
    }
}

async function createNewPost(postData) {
    try {
        // Send new post data to backend
        const response = await apiService.postData('/api/posts', postData);
        console.log('Post created:', response);
        
        // Show success message
        alert('Post created successfully!');
    } catch (error) {
        console.error('Failed to create post:', error);
        alert('Failed to create post. Please try again.');
    }
}

// Example form submission
document.getElementById('postForm')?.addEventListener('submit', async (event) => {
    event.preventDefault();
    
    const postData = {
        title: document.getElementById('postTitle').value,
        content: document.getElementById('postContent').value
    };
    
    await createNewPost(postData);
}); 