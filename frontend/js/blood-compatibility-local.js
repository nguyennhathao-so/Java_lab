// Blood Compatibility Data (Local JSON)
const BLOOD_COMPATIBILITY_DATA = {
  "bloodCompatibilityData": [
    {
      "receiverBloodType": "A+",
      "transfusionType": "Toàn Phần",
      "bloodComponent": "",
      "compatibleBloodTypes": "A+, A-, O+, O-"
    },
    {
      "receiverBloodType": "A+",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Hồng Cầu",
      "compatibleBloodTypes": "A+, A-, O+, O-"
    },
    {
      "receiverBloodType": "A+",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Tiểu Cầu",
      "compatibleBloodTypes": "A+, A-, B+, B-, AB+, AB-, O+, O- (Ưu Tiên A+)"
    },
    {
      "receiverBloodType": "A-",
      "transfusionType": "Toàn Phần",
      "bloodComponent": "",
      "compatibleBloodTypes": "A-, O-"
    },
    {
      "receiverBloodType": "A-",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Hồng Cầu",
      "compatibleBloodTypes": "A-, O-"
    },
    {
      "receiverBloodType": "A-",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Tiểu Cầu",
      "compatibleBloodTypes": "A-, B-, AB-, O- (Ưu Tiên A-)"
    },
    {
      "receiverBloodType": "B+",
      "transfusionType": "Toàn Phần",
      "bloodComponent": "",
      "compatibleBloodTypes": "B+, B-, O+, O-"
    },
    {
      "receiverBloodType": "B+",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Hồng Cầu",
      "compatibleBloodTypes": "B+, B-, O+, O-"
    },
    {
      "receiverBloodType": "B+",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Tiểu Cầu",
      "compatibleBloodTypes": "A+, A-, B+, B-, AB+, AB-, O+, O- (Ưu Tiên B+)"
    },
    {
      "receiverBloodType": "B-",
      "transfusionType": "Toàn Phần",
      "bloodComponent": "",
      "compatibleBloodTypes": "B-, O-"
    },
    {
      "receiverBloodType": "B-",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Hồng Cầu",
      "compatibleBloodTypes": "B-, O-"
    },
    {
      "receiverBloodType": "B-",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Tiểu Cầu",
      "compatibleBloodTypes": "A-, B-, AB-, O- (Ưu Tiên B-)"
    },
    {
      "receiverBloodType": "AB+",
      "transfusionType": "Toàn Phần",
      "bloodComponent": "",
      "compatibleBloodTypes": "A+, A-, B+, B-, AB+, AB-, O+, O-"
    },
    {
      "receiverBloodType": "AB+",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Hồng Cầu",
      "compatibleBloodTypes": "A+, A-, B+, B-, AB+, AB-, O+, O-"
    },
    {
      "receiverBloodType": "AB+",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Tiểu Cầu",
      "compatibleBloodTypes": "A+, A-, B+, B-, AB+, AB-, O+, O- (Ưu Tiên AB+)"
    },
    {
      "receiverBloodType": "AB-",
      "transfusionType": "Toàn Phần",
      "bloodComponent": "",
      "compatibleBloodTypes": "A-, B-, AB-, O-"
    },
    {
      "receiverBloodType": "AB-",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Hồng Cầu",
      "compatibleBloodTypes": "A-, B-, AB-, O-"
    },
    {
      "receiverBloodType": "AB-",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Tiểu Cầu",
      "compatibleBloodTypes": "A-, B-, AB-, O- (Ưu Tiên AB-)"
    },
    {
      "receiverBloodType": "O+",
      "transfusionType": "Toàn Phần",
      "bloodComponent": "",
      "compatibleBloodTypes": "O+, O-"
    },
    {
      "receiverBloodType": "O+",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Hồng Cầu",
      "compatibleBloodTypes": "O+, O-"
    },
    {
      "receiverBloodType": "O+",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Tiểu Cầu",
      "compatibleBloodTypes": "A+, A-, B+, B-, AB+, AB-, O+, O- (Ưu Tiên O+)"
    },
    {
      "receiverBloodType": "O-",
      "transfusionType": "Toàn Phần",
      "bloodComponent": "",
      "compatibleBloodTypes": "O-"
    },
    {
      "receiverBloodType": "O-",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Hồng Cầu",
      "compatibleBloodTypes": "O-"
    },
    {
      "receiverBloodType": "O-",
      "transfusionType": "Theo Thành Phần Máu",
      "bloodComponent": "Tiểu Cầu",
      "compatibleBloodTypes": "A-, B-, AB-, O- (Ưu Tiên O-)"
    }
  ]
};

// Local Blood Compatibility API functions
const BLOOD_COMPATIBILITY_LOCAL_API = {
    // Tìm kiếm tương thích máu từ dữ liệu local
    searchCompatibility: function(bloodType, transfusionType, bloodComponent) {
        return new Promise((resolve) => {
            const results = BLOOD_COMPATIBILITY_DATA.bloodCompatibilityData.filter(item => {
                // Lọc theo nhóm máu
                if (item.receiverBloodType !== bloodType) {
                    return false;
                }
                
                // Lọc theo loại truyền máu
                if (item.transfusionType !== transfusionType) {
                    return false;
                }
                
                // Nếu là "Toàn Phần", không cần kiểm tra blood component
                if (transfusionType === "Toàn Phần") {
                    return true;
                }
                
                // Nếu là "Theo Thành Phần Máu", kiểm tra blood component
                if (transfusionType === "Theo Thành Phần Máu") {
                    return item.bloodComponent === bloodComponent;
                }
                
                return false;
            });
            
            resolve(results);
        });
    }
};

// DOM elements
let bloodTypeSelect, transfusionTypeRadios, bloodComponentSelect, resultsTable;

// Initialize blood compatibility form
function initBloodCompatibilityForm() {
    bloodTypeSelect = document.getElementById('blood-type-select');
    transfusionTypeRadios = document.querySelectorAll('input[name="transfusion-type"]');
    bloodComponentSelect = document.getElementById('blood-component-select');
    resultsTable = document.getElementById('blood-compatibility-results');
    
    // Add event listeners
    if (bloodTypeSelect) {
        bloodTypeSelect.addEventListener('change', updateResults);
    }
    
    transfusionTypeRadios.forEach(radio => {
        radio.addEventListener('change', function() {
            toggleBloodComponentSelect();
            updateResults();
        });
    });
    
    if (bloodComponentSelect) {
        bloodComponentSelect.addEventListener('change', updateResults);
    }
}

// Toggle blood component select visibility
function toggleBloodComponentSelect() {
    const selectedType = document.querySelector('input[name="transfusion-type"]:checked')?.value;
    const bloodComponentSelect = document.getElementById('blood-component-select');
    
    if (bloodComponentSelect) {
        if (selectedType === 'Theo Thành Phần Máu') {
            bloodComponentSelect.classList.add('show');
            bloodComponentSelect.required = true;
        } else {
            bloodComponentSelect.classList.remove('show');
            bloodComponentSelect.required = false;
            bloodComponentSelect.value = '';
        }
    }
}

// Update results based on form selections
async function updateResults() {
    const bloodType = bloodTypeSelect ? bloodTypeSelect.value : '';
    const transfusionType = document.querySelector('input[name="transfusion-type"]:checked')?.value || '';
    let bloodComponent = bloodComponentSelect ? bloodComponentSelect.value : '';
    
    if (!bloodType || !transfusionType) {
        clearResults();
        return;
    }
    
    // Nếu chọn "Toàn Phần", không cần blood component
    if (transfusionType === 'Toàn Phần') {
        bloodComponent = '';
    }
    
    try {
        const results = await BLOOD_COMPATIBILITY_LOCAL_API.searchCompatibility(bloodType, transfusionType, bloodComponent);
        displayResults(results);
    } catch (error) {
        console.error('Error updating results:', error);
        displayError('Có lỗi xảy ra khi tìm kiếm. Vui lòng thử lại.');
    }
}

// Display results in table
function displayResults(results) {
    if (!resultsTable) return;
    
    if (results.length === 0) {
        resultsTable.innerHTML = '<tr><td colspan="4" style="text-align: center; color: #666;">Không tìm thấy kết quả</td></tr>';
        return;
    }
    
    let tableHTML = '';
    results.forEach(result => {
        tableHTML += `
            <tr>
                <td>${result.receiverBloodType}</td>
                <td>${result.transfusionType}</td>
                <td>${result.bloodComponent || '-'}</td>
                <td>${result.compatibleBloodTypes}</td>
            </tr>
        `;
    });
    
    resultsTable.innerHTML = tableHTML;
}

// Clear results
function clearResults() {
    if (resultsTable) {
        resultsTable.innerHTML = '<tr><td colspan="4" style="text-align: center; color: #666;">Vui lòng chọn nhóm máu và loại truyền máu</td></tr>';
    }
}

// Display error message
function displayError(message) {
    if (resultsTable) {
        resultsTable.innerHTML = `<tr><td colspan="4" style="text-align: center; color: #d32f2f;">${message}</td></tr>`;
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initBloodCompatibilityForm();
    // Khởi tạo trạng thái ban đầu
    toggleBloodComponentSelect();
}); 