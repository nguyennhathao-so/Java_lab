// Admin API service
const adminApiService = {
    // API chung để lấy donations theo status
    async getDonationsByStatus(status) {
        return await apiService.getData(`/api/admin/donations/status/${status}`);
    },

    // API chung để cập nhật status của donation
    async updateDonationStatus(id, newStatus) {
        return await apiService.postData(`/api/admin/donations/${id}/update-status`, { status: newStatus });
    },

    async completeDonation(id, data) {
        return await apiService.postData(`/api/admin/donations/${id}/complete`, data);
    },

    async deleteDonation(id) {
        return await apiService.deleteData(`/api/admin/donations/${id}`);
    },

    // Blood Requests (Need Blood)
    async getBloodRequests() {
        return await apiService.getData('/api/admin/blood-requests');
    },

    async approveBloodRequest(id) {
        return await apiService.postData(`/api/admin/blood-requests/${id}/approve`, {});
    },

    async deleteBloodRequest(id) {
        return await apiService.deleteData(`/api/admin/blood-requests/${id}`);
    },

    // Blood Inventory
    async getBloodInventory() {
        return await apiService.getData('/api/admin/blood-inventory');
    },

    async createDonationRequest(requestData) {
        return await apiService.postData('/api/donation-requests', requestData, true);
    },

    async updateBloodInventory(bloodType, quantity) {
        const token = localStorage.getItem('authToken');
        return fetch('http://localhost:8082/api/admin/blood-inventory/update', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                ...(token ? { 'Authorization': `Bearer ${token}` } : {})
            },
            body: JSON.stringify({ bloodType, quantity }),
            credentials: 'include'
        }).then(res => {
            if (!res.ok) throw new Error('Lỗi server');
            return res.json();
        });
    },

    async getHealthCenters() {
        return await apiService.getData('/api/admin/health-centers');
    },

    async updateBloodInventoryWithCenter(bloodType, quantity, centerId) {
        return await apiService.postData('/api/admin/blood-inventory/update', {
            bloodType: bloodType,
            quantity: quantity,
            centerId: centerId
        });
    },

    async createMedicalRecord(request) {
        return await apiService.postData('/api/admin/medical-records', request);
    },

    async getHistory() {
        return await apiService.getData('/api/admin/history');
    },

    getDonationRequestsByStatus: async function (status) {
        const response = await fetch(`http://localhost:8082/api/admin/blood-requests?status=${encodeURIComponent(status)}`, {
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authToken')
            },
            credentials: 'include'
        });
        if (!response.ok) throw new Error('Lỗi khi lấy danh sách yêu cầu hiến máu');
        return await response.json();
    }
}; 