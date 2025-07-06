// Blood Compatibility API functions
const BLOOD_COMPATIBILITY_API = {
    baseUrl: 'http://localhost:8080/api/blood-compatibility',
    
    // Tìm kiếm tương thích máu
    searchCompatibility: async function(bloodType, transfusionType, bloodComponent) {
        try {
            let url = `${this.baseUrl}/search?bloodType=${encodeURIComponent(bloodType)}&transfusionType=${encodeURIComponent(transfusionType)}`;
            
            if (bloodComponent) {
                url += `&bloodComponent=${encodeURIComponent(bloodComponent)}`;
            }
            
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                }
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('Error searching blood compatibility:', error);
            throw error;
        }
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
        bloodComponent = null;
    }
    
    try {
        const results = await BLOOD_COMPATIBILITY_API.searchCompatibility(bloodType, transfusionType, bloodComponent);
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