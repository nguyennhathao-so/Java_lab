// Admin API service
const adminApiService = {
  // Donations
  async getDonationsByStatus(status) {
    return await apiService.getData(`/api/admin/donations/status/${status}`);
  },
  async getBloodDonations() {
    return await apiService.getData("/api/admin/blood-donations");
  },
  async getBloodDonationsHistory() {
    return await apiService.getData("/api/admin/blood-donations-history");
  },
  async getApprovedDonations() {
    return await apiService.getData("/api/admin/approved-donations");
  },
  async approveDonation(id) {
    return await apiService.postData(`/api/admin/donations/${id}/approve`, {});
  },
  async updateDonationStatus(id, newStatus) {
    return await apiService.putData(`/api/admin/donations/${id}/status`, { status: newStatus });
  },
  async completeDonation(id, data) {
    return await apiService.postData(`/api/admin/donations/${id}/complete`, data);
  },
  async deleteDonation(id) {
    return await apiService.deleteData(`/api/admin/donations/${id}`);
  },

  // Blood Requests
  async getBloodRequests() {
    return await apiService.getData("/api/admin/blood-requests");
  },
  async approveBloodRequest(id) {
    return await apiService.postData(`/api/admin/blood-requests/${id}/approve`, {});
  },
  async deleteBloodRequest(id) {
    return await apiService.deleteData(`/api/admin/blood-requests/${id}`);
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
  },

  // Blood Inventory
  async getBloodInventory() {
    return await apiService.getData("/api/admin/blood-inventory");
  },
  async updateBloodInventory(bloodType, quantity) {
    return await apiService.postData("/api/admin/blood-inventory/update", {
      bloodType: bloodType,
      quantity: quantity,
    });
  },
  async updateBloodInventoryWithCenter(bloodType, quantity, centerId) {
    return await apiService.postData('/api/admin/blood-inventory/update', {
      bloodType: bloodType,
      quantity: quantity,
      centerId: centerId
    });
  },

  // Health Centers
  async getHealthCenters() {
    return await apiService.getData('/api/admin/health-centers');
  },

  // Notifications
  async getNotifications() {
    return await apiService.getData("/api/admin/notifications");
  },
  async getNotificationsByUserId(userId) {
    return await apiService.getData(`/api/admin/notifications/user/${userId}`);
  },

  // Donation Registrations
  async getDonationRegistrationsByUserId(userId) {
    return await apiService.getData(`/api/admin/donation-registrations/${userId}`);
  },

  // Medical Record
  async createMedicalRecord(request) {
    return await apiService.postData('/api/admin/medical-records', request);
  },

  // History
  async getHistory() {
    return await apiService.getData('/api/admin/history');
  },

  // Users (nếu cần)
  async getAllUsers() {
    return await apiService.getData("/api/admin/users");
  },
  async updateUser(userId, userData) {
    return await apiService.putData(`/api/admin/users/${userId}`, userData);
  },
  async deleteUser(userId) {
    return await apiService.deleteData(`/api/admin/users/${userId}`);
  },

  // Donation Request (nếu cần cho frontend)
  async createDonationRequest(requestData) {
    return await apiService.postData('/api/donation-requests', requestData, true);
  }
};
