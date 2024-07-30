const apiEndpoint = (window.location.hostname === 'localhost')
    ? 'http://localhost:8080'
    : 'https://api.weeth.site';

document.addEventListener('DOMContentLoaded', function () {
    if (getToken()) {
        loadAttendanceEvents();
    } else {
        showTokenErrorMessage('JWT token is missing. Please log in.');
    }

    document.getElementById('attendanceForm').addEventListener('submit', function (event) {
        event.preventDefault();
        if (!confirm('출석 일정을 저장하시겠습니까?')) {
            return;
        }
        saveAttendanceEvent();
    });
});

let allAttendances = [];

function loadAttendanceEvents() {
    apiRequest(`${apiEndpoint}/admin/attendance-event`)
        .then(response => response.json())
        .then(data => {
            if(data.code===200) {
                allAttendances = data.data;
                displayAttendances(allAttendances);
            }else {
                throw new Error(data.message);
            }
        })
        .catch(error => {
            showTokenErrorMessage(error.message);
            console.error('Error loading attendances:', error);
        });
}

function displayAttendances(attendanceArray) {
    const attendanceContainer = document.getElementById('attendanceContainer');
    if (attendanceContainer) {
        attendanceContainer.innerHTML = '';

        attendanceArray.forEach((event, index) => {
            const weekNumber = event.weekCode ? event.weekCode.weekNumber : 'N/A';
            const attendanceCode = event.weekCode ? event.weekCode.attendanceCode : 'N/A';
            const collapseId = `collapseCardExample${index}`;

            const card = document.createElement('div');
            card.className = 'card shadow mb-4';

            card.innerHTML = `
                <a href="#${collapseId}" class="d-block card-header py-3" data-toggle="collapse" role="button" aria-expanded="true" aria-controls="${collapseId}">
                    <h6 class="m-0 font-weight-bold text-gray-600">${event.title}</h6>
                </a>
                <div class="collapse" id="${collapseId}">
                    <div class="card-body">
                        <p><strong>Week Number:</strong> ${weekNumber}</p>
                        <p><strong>Time:</strong> ${formatTime(event.startDateTime)} - ${formatTime(event.endDateTime)}</p>
                        <p><strong>Content:</strong> ${event.content}</p>
                        <p><strong>Location:</strong> ${event.location}</p>
                        <p><strong>Required Items:</strong> ${event.requiredItems}</p>
                        <p><strong>Member Number:</strong> ${event.memberNumber}</p>
                        <p><strong>Cardinal:</strong> ${event.cardinal}</p>
                        <p><strong>Attendance Code:</strong> ${attendanceCode}</p>
                    </div>
                </div>
            `;
            attendanceContainer.appendChild(card);
        });
    }
}

function saveAttendanceEvent() {
    const weekNumber = document.getElementById('weekNumber').value;
    const date = document.getElementById('eventDate').value;
    const title = document.getElementById('title').value;
    const content = document.getElementById('eventContent').value;
    const location = document.getElementById('location').value;
    const requiredItems = document.getElementById('requiredItems').value;
    const memberNumber = document.getElementById('memberNumber').value;
    const startTime = document.getElementById('startTime').value;
    const endTime = document.getElementById('endTime').value;
    const cardinal = document.getElementById('cardinal').value;

    const startDateTime = `${date}T${startTime}`;
    const endDateTime = `${date}T${endTime}`;

    const eventData = {
        title: title,
        content: content,
        location: location,
        requiredItems: requiredItems,
        memberNumber: memberNumber,
        startDateTime: startDateTime,
        endDateTime: endDateTime,
        cardinal: parseInt(cardinal)
    };

    const weekData = {
        cardinal: parseInt(cardinal),
        weekNumber: parseInt(weekNumber),
        date: date
    };

    // 먼저 주차 정보를 저장하고 그 다음 출석 일정을 저장합니다.
    apiRequest(`${apiEndpoint}/admin/attendances`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(weekData)
    })
        .then(response => response.json())
        .then(message => {
            alert(`주차 정보 저장 성공: ${message.message}`);

            // 주차 정보가 성공적으로 저장된 후 출석 일정을 저장합니다.
            return apiRequest(`${apiEndpoint}/admin/attendance-event`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(eventData)
            });
        })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                alert(`출석 일정 저장 성공: ${data.message}`);
                loadAttendanceEvents();
            } else {
                throw new Error(data.message);
            }
        })
        .catch(error => {
            alert(`저장 실패: ${error.message}`);
        });
}

function formatTime(timeString) {
    const [date, time] = timeString.split('T');
    const [hours, minutes] = time.split(':');
    return `${date} ${hours}:${minutes}`;
}
