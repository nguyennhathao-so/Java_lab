class NotificationModal {
    constructor() {
        this.modal = null;
        this.currentAction = null;
        this.currentRequestId = null;
        this.currentUserId = null;
        this.allowedTypes = null;
        this.init();
    }

    init() {
        this.createModalHTML();
        this.bindEvents();
    }

    createModalHTML() {
        const modalHTML = `
            <div class="modal-overlay" id="notificationModal">
                <div class="notification-modal">
                    <div class="modal-header">
                        <h2 class="modal-title" id="modalTitle">Gửi thông báo</h2>
                        <button class="close-btn" id="closeModal">&times;</button>
                    </div>
                    <form class="notification-form" id="notificationForm">
                        <div class="form-row">
                            <div class="form-group">
                                <label for="messageType">Loại thông báo *</label>
                                <select id="messageType" required></select>
                            </div>
                            <div class="form-group">
                                <label for="notificationStatus">Trạng thái</label>
                                <select id="notificationStatus">
                                    <option value="unread">Chưa đọc</option>
                                    <option value="read">Đã đọc</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="notificationMessage">Nội dung thông báo cho người dùng *</label>
                            <textarea id="notificationMessage" placeholder="Nhập nội dung thông báo cho người dùng..." required></textarea>
                        </div>
                        <div class="form-group">
                            <label for="staffMessage">Ghi chú của admin/staff</label>
                            <textarea id="staffMessage" placeholder="Nhập ghi chú cho admin/staff..."></textarea>
                        </div>
                        <div class="modal-buttons">
                            <button type="button" class="btn btn-secondary" id="cancelBtn">Hủy</button>
                            <button type="submit" class="btn btn-primary" id="submitBtn">Gửi thông báo</button>
                        </div>
                    </form>
                </div>
            </div>
        `;
        document.body.insertAdjacentHTML('beforeend', modalHTML);
        this.modal = document.getElementById('notificationModal');
    }

    bindEvents() {
        document.getElementById('closeModal').addEventListener('click', () => this.hide());
        document.getElementById('cancelBtn').addEventListener('click', () => this.hide());
        this.modal.addEventListener('click', (e) => {
            if (e.target === this.modal) {
                this.hide();
            }
        });
        document.getElementById('notificationForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleSubmit();
        });
        document.getElementById('messageType').addEventListener('change', (e) => {
            this.autoFillMessage(e.target.value);
        });
    }

    show(action, requestId, userId, requestData = null, allowedTypes = null) {
        this.currentAction = action;
        this.currentRequestId = requestId;
        this.currentUserId = userId;
        this.allowedTypes = allowedTypes;
        // Cập nhật tiêu đề modal
        const titleMap = {
            'delete_donation': 'Thông báo từ chối yêu cầu hiến máu',
            'delete_blood_request': 'Thông báo từ chối yêu cầu cần máu',
            'complete_donation': 'Thông báo hoàn thành hiến máu',
            'approve_blood_request': 'Thông báo duyệt yêu cầu cần máu'
        };
        document.getElementById('modalTitle').textContent = titleMap[action] || 'Gửi thông báo';
        this.renderMessageTypeOptions();
        this.autoFillData(action, requestData);
        this.modal.classList.add('show');
        document.body.style.overflow = 'hidden';
    }

    renderMessageTypeOptions() {
        const select = document.getElementById('messageType');
        const allOptions = [
            { value: '', label: 'Chọn loại thông báo' },
            { value: 'approved', label: 'Đã duyệt' },
            { value: 'rejected', label: 'Từ chối' },
            { value: 'reminder', label: 'Nhắc nhở' }
        ];
        let options = allOptions;
        if (this.allowedTypes && Array.isArray(this.allowedTypes)) {
            options = allOptions.filter(opt => this.allowedTypes.includes(opt.value) || opt.value === '');
        }
        select.innerHTML = '';
        options.forEach(opt => {
            const option = document.createElement('option');
            option.value = opt.value;
            option.textContent = opt.label;
            select.appendChild(option);
        });
        // Nếu chỉ có 1 lựa chọn thực sự (không tính option rỗng), disable select
        const realOptions = options.filter(opt => opt.value !== '');
        if (realOptions.length === 1) {
            select.value = realOptions[0].value;
            select.disabled = true;
        } else {
            select.disabled = false;
            select.value = '';
        }
    }

    hide() {
        this.modal.classList.remove('show');
        document.body.style.overflow = '';
        this.resetForm();
    }

    resetForm() {
        document.getElementById('notificationForm').reset();
        this.currentAction = null;
        this.currentRequestId = null;
        this.currentUserId = null;
        this.allowedTypes = null;
        this.renderMessageTypeOptions();
    }

    autoFillData(action, requestData) {
        const messageType = document.getElementById('messageType');
        const message = document.getElementById('notificationMessage');
        const staffMessage = document.getElementById('staffMessage');
        message.value = '';
        staffMessage.value = '';
        // Auto-fill dựa trên action
        switch (action) {
            case 'delete_donation':
            case 'delete_blood_request':
                messageType.value = 'rejected';
                message.value = 'Rất tiếc, yêu cầu của bạn chưa được chấp nhận.';
                staffMessage.value = 'Yêu cầu bị từ chối bởi admin/staff.';
                break;
            case 'complete_donation':
            case 'approve_blood_request':
                messageType.value = 'approved';
                message.value = 'Yêu cầu của bạn đã được chấp nhận.';
                staffMessage.value = 'Yêu cầu đã được duyệt thành công.';
                break;
        }
    }

    autoFillMessage(messageType) {
        const message = document.getElementById('notificationMessage');
        const staffMessage = document.getElementById('staffMessage');
        const messageTemplates = {
            'approved': {
                donation: 'Yêu cầu hiến máu của bạn đã được chấp nhận. Vui lòng đến đúng giờ hẹn.',
                blood_request: 'Yêu cầu cần máu của bạn đã được chấp nhận. Chúng tôi sẽ liên hệ sớm nhất.',
                staff: 'Yêu cầu đã được duyệt thành công.'
            },
            'rejected': {
                donation: 'Rất tiếc, yêu cầu hiến máu của bạn chưa được chấp nhận.',
                blood_request: 'Rất tiếc, yêu cầu cần máu của bạn chưa được chấp nhận.',
                staff: 'Yêu cầu bị từ chối.'
            },
            'reminder': {
                donation: 'Đã đến thời gian có thể hiến máu lại. Vui lòng đăng ký nếu có nhu cầu.',
                blood_request: 'Nhắc nhở về yêu cầu cần máu của bạn.',
                staff: 'Thông báo nhắc nhở cho người dùng.'
            }
        };
        if (messageType && messageTemplates[messageType]) {
            const template = messageTemplates[messageType];
            const actionType = this.currentAction.includes('donation') ? 'donation' : 'blood_request';
            message.value = template[actionType] || template.donation;
            staffMessage.value = template.staff;
        }
    }

    async handleSubmit() {
        const formData = this.getFormData();
        if (!formData.messageType || !formData.message) {
            alert('Vui lòng điền đầy đủ thông tin bắt buộc.');
            return;
        }
        try {
            const notificationData = {
                userId: this.currentUserId,
                message: formData.message,
                messageType: formData.messageType,
                staffMessage: formData.staffMessage,
                status: formData.status
            };
            await this.createNotification(notificationData);
            await this.executeOriginalAction(formData.messageType);
            alert('Thông báo đã được gửi thành công!');
            this.hide();
            if (this.onActionComplete) {
                this.onActionComplete();
            }
        } catch (error) {
            console.error('Lỗi khi gửi thông báo:', error);
            alert('Có lỗi xảy ra khi gửi thông báo: ' + error.message);
        }
    }

    getFormData() {
        return {
            messageType: document.getElementById('messageType').value,
            message: document.getElementById('notificationMessage').value,
            staffMessage: document.getElementById('staffMessage').value,
            status: document.getElementById('notificationStatus').value
        };
    }

    async createNotification(notificationData) {
        const response = await fetch('http://localhost:8082/api/admin/notifications', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('authToken')
            },
            body: JSON.stringify(notificationData)
        });
        if (!response.ok) {
            throw new Error('Không thể tạo thông báo');
        }
        return await response.json();
    }

    async executeOriginalAction(messageType) {
        // Nếu chỉ có allowedTypes là rejected hoặc action là delete thì cập nhật status closed
        if ((this.allowedTypes && this.allowedTypes.length === 1 && this.allowedTypes[0] === 'rejected') || messageType === 'rejected' || this.currentAction.startsWith('delete')) {
            // Gọi API cập nhật status closed
            await fetch(`http://localhost:8082/api/admin/blood-requests/${this.currentRequestId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('authToken')
                }
            });
        } else if ((this.allowedTypes && this.allowedTypes.length === 1 && this.allowedTypes[0] === 'approved') || messageType === 'approved' || this.currentAction === 'complete_donation' || this.currentAction === 'approve_blood_request') {
            // Hoàn thành hoặc duyệt
            if (this.currentAction === 'complete_donation') {
                const illness = document.getElementById('illness')?.value || '';
                const amount = document.getElementById('amount')?.value || 350;
                const payload = {
                    notes: illness,
                    amount: parseInt(amount, 10)
                };
                await adminApiService.completeDonation(this.currentRequestId, payload);
                window.location.href = 'blood_donations_history.html';
            } else {
                // approve blood request (cần máu)
                await adminApiService.approveBloodRequest(this.currentRequestId);
            }
        }
    }
}

let notificationModal;
document.addEventListener('DOMContentLoaded', () => {
    notificationModal = new NotificationModal();
});
window.NotificationModal = NotificationModal; 