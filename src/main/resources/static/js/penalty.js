document.addEventListener('DOMContentLoaded', function() {
    if (getToken()) {
        loadPenalties();
    } else {
        alert('JWT token is missing. Please log in.');
    }
});

function loadPenalties() {
    const penaltyList = document.getElementById('penaltyList');
    apiRequest('/admin/penalty/all')
        .then(response => response.json())
        .then(data => {
            penaltyList.innerHTML = '';
            const penalties = data.data;
            penalties.forEach(penalty => {
                const row = document.createElement('tr');
                row.innerHTML = `
                        <td>${penalty.userId}</td>
                        <td>${penalty.userName}</td>
                        <td>${penalty.penaltyId}</td>
                        <td>${penalty.penaltyDescription}</td>
                        <td>${penalty.time}</td>
                        <td>${penalty.penaltyCount}</td>
                        <td>
                            <button class="btn btn-danger" onclick="confirmDeletePenalty(${penalty.penaltyId})">삭제</button>
                        </td>
                    `;
                penaltyList.appendChild(row);
            });
        })
        .catch(error => {
            alert(error.message);
        });
}

function confirmDeletePenalty(penaltyId) {
    if (confirm("삭제 하시겠습니까?")) {
        deletePenalty(penaltyId)
            .then(message => {
                alert(`패널티 삭제 성공: ${message}`);
            })
            .catch(error => {
                alert(`패널티 삭제 실패: ${error.message}`);
            });
    }
}

function deletePenalty(penaltyId) {
    return apiRequest(`/admin/penalty?penaltyId=${penaltyId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.text());
}