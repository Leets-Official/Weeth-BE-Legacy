const apiEndpoint = (window.location.hostname === 'localhost')
    ? 'http://localhost:8080'
    : 'https://api.weeth.site';

document.addEventListener('DOMContentLoaded', function () {
    if (getToken()) {
        loadMembers();
    } else {
        showTokenErrorMessage('JWT token is missing. Please log in.');
    }

    document.getElementById('topbarSearchInput').addEventListener('input', filterMembers);

});

let allMembers = [];

// 모든 사용자 조회
function loadMembers() {
    apiRequest(`${apiEndpoint}/admin/users/all`)
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                allMembers = data.data;
                displayMembers(allMembers);
            } else {
                throw new Error(data.message);
            }
        })
        .catch(error => {
            showTokenErrorMessage(error.message);
            console.error('Error loading members:', error);
        });
}

// 검색을 위한 필터
function filterMembers() {
    const query = document.getElementById('topbarSearchInput').value.toLowerCase();
    const filteredMembers = allMembers.filter(member =>
        member.name.toLowerCase().includes(query) ||
        member.email.toLowerCase().includes(query) ||
        member.studentId.toLowerCase().includes(query) ||
        member.tel.toLowerCase().includes(query) ||
        member.department.toLowerCase().includes(query) ||
        member.position.toLowerCase().includes(query) ||
        member.status.toLowerCase().includes(query) ||
        member.role.toLowerCase().includes(query)
    );
    displayMembers(filteredMembers);
}

// 조회된 사용자들을 반복하며 화면에 표시
function displayMembers(membersArray) {
    const membersList = document.getElementById('membersList');
    if (membersList) {
        membersList.innerHTML = '';

        membersArray.forEach(member => {
            const row = document.createElement('tr');
            const rowClass = member.status === 'WAITING' ? 'border-left-secondary' : 'border-left-success';
            row.innerHTML = `
                <td class="${rowClass}">${member.id}</td>
                <td>${member.name}</td>
                <td>${member.email}</td>
                <td>${member.studentId}</td>
                <td>${member.tel}</td>
                <td>${member.department}</td>
                <td>${member.cardinals.join(', ')}</td>
                <td>${member.position}</td>
                <td>${member.status}</td>
                <td>${member.role}</td>
                <td>${member.attendanceCount}</td>
                <td>${member.absenceCount}</td>
                <td>${member.attendanceRate ?? 'N/A'}</td>
                <td>${formatDate(member.createdAt)}</td>
                <td>${formatDate(member.modifiedAt)}</td>
                <td>
                    <a class="button" href="#" data-toggle="modal" data-target="#managemember" onclick="setModalContent(${member.id})">
                        <i class="fas fa-fw fa-cog"></i>
                    </a>
                </td>
            `;
            membersList.appendChild(row);
        });
    }
}

// 관리버튼 클릭시 모달창으로 관리 기능들 표시
function setModalContent(userId) {
    const member = allMembers.find(m => m.id === userId);
    const modalBodyContent = document.getElementById('modal-body-content');
    if (modalBodyContent) {
        modalBodyContent.innerHTML = `
            <button class="btn btn-primary btn-sm" onclick="confirmAction('가입 승인', approveUser, ${member.id})" ${member.status === 'ACTIVE' ? 'disabled' : ''}>가입 승인</button>
            ${member.role === 'USER' ?
            `<button class="btn btn-primary btn-sm" onclick="confirmAction('관리자로 변경', changeUserRole, ${member.id}, 'ADMIN')">관리자로 변경</button>` :
            `<button class="btn btn-danger btn-sm" onclick="confirmAction('사용자로 변경', changeUserRole, ${member.id}, 'USER')">사용자로 변경</button>`
        }
            <button class="btn btn-danger btn-sm" onclick="confirmAction('유저 추방', deleteUser, ${member.id})">유저 추방</button>
            <button class="btn btn-secondary btn-sm" onclick="confirmAction('비밀번호 초기화', resetPassword, ${member.id})">비밀번호 초기화</button>
            
            <div class="form-group mt-3">
                <label for="nextCardinal-${member.id}">다음 기수 입력</label>
                <input type="number" class="form-control" id="nextCardinal-${member.id}" placeholder="기수 입력">
                <button class="btn btn-info btn-sm mt-2" onclick="confirmAction('다음 기수 진행', submitNextCardinal, ${member.id})">제출</button>
            </div>
            
            <div class="form-group mt-3">
                <label for="penaltyDescription-${member.id}">패널티 설명</label>
                <input type="text" class="form-control" id="penaltyDescription-${member.id}" placeholder="설명 입력">
                <button class="btn btn-warning btn-sm mt-2" onclick="confirmAction('패널티 부여', submitPenalty, ${member.id})">제출</button>
            </div>
        `;
    }
}

/*
사용자 관리 기능 메서드
*/

// 가입승인
function approveUser(userId) {
    return apiRequest(`${apiEndpoint}/admin/users?userId=${userId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json());
}

// 사용자 role 변경
function changeUserRole(userId, role) {
    return apiRequest(`${apiEndpoint}/admin/users/${role}?userId=${userId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json());
}

// 사용자 밴
function deleteUser(userId) {
    return apiRequest(`${apiEndpoint}/admin/users?userId=${userId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({userId: userId})
    }).then(response => response.json());
}

// 비밀번호 초기화
function resetPassword(userId) {
    return apiRequest(`${apiEndpoint}/admin/users/reset?userId=${userId}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json());
}

// 매 요청시 확인 메시지를 표시하고, 완료하면 다시 멤버를 조회
function confirmAction(actionName, actionFunction, ...args) {
    if (confirm(`${actionName} 하시겠습니까? ${actionName === '비밀번호 초기화' ? '초기화시 비밀번호가 학번으로 초기화됩니다.' : ''}`)) {
        actionFunction(...args)
            .then(response => {
                if(response.code===200) {
                    alert(`${actionName} 성공`);
                    loadMembers();
                }
                else {
                        throw new Error(response.message);
                    }
                })
            .catch(error => {
                alert(`${actionName} 실패: ${error.message}`);
            });
    }
}

function submitNextCardinal(userId) {
    const cardinalInput = document.getElementById(`nextCardinal-${userId}`);
    const cardinal = cardinalInput.value;

    return apiRequest(`${apiEndpoint}/admin/users/apply/${cardinal}?userId=${userId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json());
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
    }).then(response => response.json());
}


function showTokenErrorMessage(message) {
    const tokenErrorMessageDiv = document.getElementById('tokenErrorMessage');
    if (tokenErrorMessageDiv) {
        tokenErrorMessageDiv.style.display = 'block';
        tokenErrorMessageDiv.innerText = message;
    }
}

function formatDate(dateString) {
    const options = {year: 'numeric', month: '2-digit', day: '2-digit'};
    return new Date(dateString).toLocaleDateString('ko-KR', options).replace(/\. /g, '-').replace(/\.$/, '');
}