const apiEndpoint = (window.location.hostname === 'localhost')
    ? 'http://localhost:8080'
    : 'https://api.weeth.site';

document.addEventListener('DOMContentLoaded', function() {
    if (getToken()) {
        loadPenalties();
    } else {
        alert('JWT token is missing. Please log in.');
        window.location.href = "/adminpage/login";
    }

    const topbarSearchInput = document.getElementById('topbarSearchInput');
    if (topbarSearchInput) {
        topbarSearchInput.addEventListener('input', filterPenalties);
    }
});

let allPenalties = [];

function loadPenalties() {
    const penaltyList = document.getElementById('penaltyList');
    apiRequest(`${apiEndpoint}/admin/penalty/all`)
        .then(response => response.json())
        .then(data => {
            penaltyList.innerHTML = '';
            allPenalties = data.data;

            if (allPenalties.length === 0) {
                penaltyList.innerHTML = `
                    <tr>
                        <td colspan="7" style="text-align: center;">패널티가 없습니다</td>
                    </tr>
                `;
                return;
            }

            displayPenalties(allPenalties);
        })
        .catch(error => {
            alert(error.message);
        });
}

function filterPenalties() {
    const query = document.getElementById('topbarSearchInput').value.toLowerCase();
    const filteredPenalties = allPenalties.filter(penalty =>
        penalty.userId.toString().includes(query) ||
        penalty.userName.toLowerCase().includes(query) ||
        penalty.penaltyId.toString().includes(query) ||
        penalty.penaltyDescription.toLowerCase().includes(query) ||
        penalty.time.toLowerCase().includes(query) ||
        penalty.penaltyCount.toString().includes(query)
    );
    displayPenalties(filteredPenalties);
}

function displayPenalties(penalties) {
    const penaltyList = document.getElementById('penaltyList');
    penaltyList.innerHTML = '';

    penalties.forEach(penalty => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td class="border-left-danger">${penalty.userId}</td>
            <td>${penalty.userName}</td>
            <td>${penalty.penaltyId}</td>
            <td>${penalty.penaltyDescription}</td>
            <td>${formatTime(penalty.time)}</td>
            <td>${penalty.penaltyCount}</td>
            <td>
                <button class="btn btn-danger btn-circle btn-sm" onclick="confirmDeletePenalty(${penalty.penaltyId})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
        penaltyList.appendChild(row);
    });
}

function confirmDeletePenalty(penaltyId) {
    if (confirm("삭제 하시겠습니까?")) {
        deletePenalty(penaltyId)
            .then(response => {
                if(response.code===200) {
                    alert(`패널티 삭제 성공`);
                    loadPenalties();
                }else {
                    throw new Error(response.message);
                }
            })
            .catch(error => {
                alert(`패널티 삭제 실패: ${error.message}`);
            });
    }
}

function deletePenalty(penaltyId) {
    return apiRequest(`${apiEndpoint}/admin/penalty?penaltyId=${penaltyId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json());
}

function formatTime(timeString) {
    const [date, time] = timeString.split('T');
    const [hours, minutes] = time.split(':');
    return `${date} ${hours}:${minutes}`;
}