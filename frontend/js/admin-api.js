// Admin API service
const adminApiService = {
  // Blood Donations
  async getBloodDonations() {
    return await apiService.getData("/api/admin/blood-donations");
  },

  async approveDonation(id) {
    return await apiService.postData(`/api/admin/donations/${id}/approve`, {});
  },

  async deleteDonation(id) {
    return await apiService.deleteData(`/api/admin/donations/${id}`);
  },

  // Blood Donations History
  async getBloodDonationsHistory() {
    return await apiService.getData("/api/admin/blood-donations-history");
  },

  // Approved Donations
  async getApprovedDonations() {
    return await apiService.getData("/api/admin/approved-donations");
  },

  // Blood Requests (Need Blood)
  async getBloodRequests() {
    return await apiService.getData("/api/admin/blood-requests");
  },

  async approveBloodRequest(id) {
    return await apiService.postData(
      `/api/admin/blood-requests/${id}/approve`,
      {}
    );
  },

  async deleteBloodRequest(id) {
    return await apiService.deleteData(`/api/admin/blood-requests/${id}`);
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

  // Notifications
  async getNotifications() {
    return await apiService.getData("/api/admin/notifications");
  },

  // Lấy lịch sử đăng ký hiến/cần máu của user
  async getDonationRegistrationsByUserId(userId) {
    return await apiService.getData(
      `/api/admin/donation-registrations/${userId}`
    );
  },

  async getNotificationsByUserId(userId) {
    return await apiService.getData(`/api/admin/notifications/user/${userId}`);
  },

  // Lấy danh sách người dùng
  async getAllUsers() {
    return await apiService.getData("/api/admin/users");
  },

  // Cập nhật thông tin người dùng
  async updateUser(userId, userData) {
    return await apiService.putData(`/api/admin/users/${userId}`, userData);
  },

  deleteUser: async function(userId) {
    return await apiService.deleteData(`/api/admin/users/${userId}`);
  },
};
