const apiEndpoint = (window.location.hostname === 'localhost')
    ? 'http://localhost:8080'
    : 'https://api.weeth.site';

document.addEventListener('DOMContentLoaded', function () {
    if (getToken()) {
        loadMembers();
    } else {
        showTokenErrorMessage('JWT token is missing. Please log in.');
    }
});

function loadMembers() {
    apiRequest(`${apiEndpoint}/admin/users/all`)
        .then(response => response.json())
        .then(data => {
            const membersList = document.getElementById('membersList');
            membersList.innerHTML = '';  // Clear existing members before adding new ones

            const membersArray = data.data; // data.data가 배열이라고 가정

            membersArray.forEach(member => {
                const row = document.createElement('tr');
                row.innerHTML = `
                        <td>${member.id}</td>
                        <td>
                            <div class="member-info">
                                <div class="info-section"><strong>이름:</strong> ${member.name}</div>
                                <div class="info-section"><strong>이메일:</strong> ${member.email}</div>
                                <div class="info-section"><strong>학번:</strong> ${member.studentId}</div>
                                <div class="info-section"><strong>Tel:</strong> ${member.tel}</div>
                                <div class="info-section"><strong>학과:</strong> ${member.department}</div>
                                <div class="info-section"><strong>기수:</strong> ${member.cardinals.join(', ')}</div>
                                <div class="info-section"><strong>Position:</strong> ${member.position}</div>
                                <div class="info-section"><strong>가입상태:</strong> ${member.status}</div>
                                <div class="info-section"><strong>Role:</strong> ${member.role}</div>
                                <div class="info-section"><strong>출석 횟수:</strong> ${member.attendanceCount}</div>
                                <div class="info-section"><strong>결석 횟수:</strong> ${member.absenceCount}</div>
                                <div class="info-section"><strong>출석률:</strong> ${member.attendanceRate ?? 'N/A'}</div>
                                <div class="info-section"><strong>Created At:</strong> ${member.createdAt}</div>
                                <div class="info-section"><strong>Modified At:</strong> ${member.modifiedAt}</div>
                            </div>
                        </td>
                        <td>
                            <div class="actions-row">
                                <button class="btn btn-primary btn-sm" onclick="confirmAction('가입 승인', approveUser, ${member.id})" ${member.status === 'ACTIVE' ? 'disabled' : ''}>가입 승인</button>
                                ${member.role === 'USER' ?
                    `<button class="btn btn-primary btn-sm" onclick="confirmAction('관리자로 변경', changeUserRole, ${member.id}, 'ADMIN')">관리자로 변경</button>` :
                    `<button class="btn btn-danger btn-sm" onclick="confirmAction('사용자로 변경', changeUserRole, ${member.id}, 'USER')">사용자로 변경</button>`
                }
                                <button class="btn btn-danger btn-sm" onclick="confirmAction('유저 추방', deleteUser, ${member.id})">유저 추방</button>
                                <button class="btn btn-danger btn-sm" onclick="showPenaltyForm(${member.id})">패널티 부여</button>
                                <button class="btn btn-secondary btn-sm" onclick="confirmAction('비밀번호 초기화', resetPassword, ${member.id})">비밀번호 초기화</button>
                                <button class="btn btn-info btn-sm" onclick="showNextCardinalForm(${member.id})">다음 기수 진행</button>
                                <div id="penaltyForm-${member.id}" class="penalty-form" style="display: none;">
                                    <div class="form-group">
                                        <label for="penaltyDescription-${member.id}">Penalty Description</label>
                                        <input type="text" class="form-control" id="penaltyDescription-${member.id}" placeholder="Enter penalty description" required>
                                    </div>
                                    <button class="btn btn-success btn-sm mt-2" onclick="confirmAction('패널티 제출', submitPenalty, ${member.id})">Submit</button>
                                    <button class="btn btn-secondary btn-sm mt-2" onclick="hidePenaltyForm(${member.id})">Close</button>
                                </div>
                                <div id="nextCardinalForm-${member.id}" class="penalty-form" style="display: none;">
                                    <div class="form-group">
                                        <label for="nextCardinal-${member.id}">기수 입력</label>
                                        <input type="number" class="form-control" id="nextCardinal-${member.id}" placeholder="Enter next cardinal" required>
                                    </div>
                                    <button class="btn btn-success btn-sm mt-2" onclick="confirmAction('다음 기수 진행', submitNextCardinal, ${member.id})">Submit</button>
                                    <button class="btn btn-secondary btn-sm mt-2" onclick="hideNextCardinalForm(${member.id})">Close</button>
                                </div>
                            </div>
                        </td>
                    `;
                membersList.appendChild(row);
            });
        })
        .catch(error => {
            showTokenErrorMessage(error.message);
            console.error('Error loading members:', error);
        });
}

function confirmAction(actionName, actionFunction, ...args) {
    if (confirm(`${actionName} 하시겠습니까? ${actionName === '비밀번호 초기화' ? '초기화시 비밀번호가 학번으로 초기화됩니다.' : ''}`)) {
        actionFunction(...args)
            .then(message => {
                alert(`${actionName} 성공: ${message}`);
            })
            .catch(error => {
                alert(`${actionName} 실패: ${error.message}`);
            });
    }
}

function approveUser(userId) {
    return apiRequest(`${apiEndpoint}/admin/users?userId=${userId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.text());
}

function changeUserRole(userId, role) {
    return apiRequest(`${apiEndpoint}/admin/users/${role}?userId=${userId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.text());
}

function deleteUser(userId) {
    return apiRequest(`${apiEndpoint}/admin/users?userId=${userId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({userId: userId})
    }).then(response => response.text());
}

function resetPassword(userId) {
    return apiRequest(`${apiEndpoint}/admin/users/reset?userId=${userId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.text());
}

function showPenaltyForm(userId) {
    const form = document.getElementById(`penaltyForm-${userId}`);
    form.style.display = 'inline-block';
}

function hidePenaltyForm(userId) {
    const form = document.getElementById(`penaltyForm-${userId}`);
    form.style.display = 'none';
}

function submitPenalty(userId) {
    const penaltyDescription = document.getElementById(`penaltyDescription-${userId}`).value;

    const data = {
        userId: userId,
        penaltyDescription: penaltyDescription
    };

    return apiRequest(`${apiEndpoint}/admin/penalty`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => response.text());
}

function showNextCardinalForm(userId) {
    const form = document.getElementById(`nextCardinalForm-${userId}`);
    form.style.display = 'inline-block';
}

function hideNextCardinalForm(userId) {
    const form = document.getElementById(`nextCardinalForm-${userId}`);
    form.style.display = 'none';
}

function submitNextCardinal(userId) {
    const cardinalInput = document.getElementById(`nextCardinal-${userId}`);
    const cardinal = cardinalInput.value;

    return apiRequest(`${apiEndpoint}/admin/users/apply/${cardinal}?userId=${userId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.text());

}

function showTokenErrorMessage(message) {
    const tokenErrorMessageDiv = document.getElementById('tokenErrorMessage');
    tokenErrorMessageDiv.style.display = 'block';
    tokenErrorMessageDiv.innerText = message;
}