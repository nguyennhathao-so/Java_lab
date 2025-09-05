// API base URL - change this to match your backend URL
const API_BASE_URL = 'http://localhost:8082';

function getAuthHeaders() {
    const token = localStorage.getItem('authToken');
    const headers = {
        'Content-Type': 'application/json',
    };
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    return headers;
}

// Example API service
const apiService = {
    // GET request example
    async getData(endpoint) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: 'GET',
                headers: getAuthHeaders(),
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error('Error fetching data:', error);
            throw error;
        }
    },

    // POST request example
    async postData(endpoint, data) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: 'POST',
                headers: getAuthHeaders(),
                credentials: 'include',
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const contentType = response.headers.get("Content-Type");
            if (contentType && contentType.includes("application/json")) {
                return await response.json();
            } else {
                return await response.text();
            }
        } catch (error) {
            console.error('Error posting data:', error);
            throw error;
        }
    },

    // PUT request example
    async putData(endpoint, data) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: 'PUT',
                headers: getAuthHeaders(),
                credentials: 'include',
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const contentType = response.headers.get("Content-Type");
            if (contentType && contentType.includes("application/json")) {
                return await response.json();
            } else {
                return await response.text();
            }
        } catch (error) {
            console.error('Error updating data:', error);
            throw error;
        }
    },

    // DELETE request example
    async deleteData(endpoint) {
        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method: "DELETE",
                headers: getAuthHeaders(),
                credentials: "include"
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(errorText || "Lỗi server");
            }

            // Xử lý response body một cách an toàn và nhất quán
            const contentType = response.headers.get("Content-Type");
            const text = await response.text();
            
            // Nếu có content và là JSON thì parse, không thì trả về text hoặc true
            if (text && contentType && contentType.includes("application/json")) {
                return JSON.parse(text);
            } else if (text) {
                return text;
            } else {
                return true; // Trả về true khi DELETE thành công nhưng không có response body
            }
        } catch (error) {
            console.error("Error deleting data:", error);
            throw error;
        }
    }


}; 