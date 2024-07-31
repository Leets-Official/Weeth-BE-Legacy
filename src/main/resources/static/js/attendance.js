const apiEndpoint = (window.location.hostname === 'localhost')
    ? 'http://localhost:8080'
    : 'https://api.weeth.site';

document.addEventListener('DOMContentLoaded', function () {
    loadAttendanceEvents();

    document.getElementById('createWeekButton').addEventListener('click', function () {
        const form = document.getElementById('createWeekForm');
        form.style.display = (form.style.display === 'block') ? 'none' : 'block';
    });

    document.getElementById('closeWeekFormButton').addEventListener('click', function () {
        document.getElementById('createWeekForm').style.display = 'none';
    });

    document.getElementById('submitEventButton').addEventListener('click', function () {
        if (!confirm('주차 정보를 저장하시겠습니까?')) {
            return;
        }
        const cardinal = document.getElementById('cardinal').value;
        const weekNumber = document.getElementById('weekNumber').value;
        const date = document.getElementById('date').value;

        const data = {
            cardinal: parseInt(cardinal),
            weekNumber: parseInt(weekNumber),
            date: date
        };

        apiRequest(`${apiEndpoint}/admin/attendances`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.text())
            .then(message => {
                showApiMessage(message, 'success');
            })
            .catch(error => {
                showApiMessage(error.message, 'danger');
            });
    });

    document.getElementById('submitEventButton').addEventListener('click', function () {
        if (!confirm('출석 일정을 저장하시겠습니까?')) {
            return;
        }
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;
        const location = document.getElementById('location').value;
        const requiredItems = document.getElementById('requiredItems').value;
        const memberNumber = document.getElementById('memberNumber').value;
        const startDateTime = document.getElementById('startDateTime').value;
        const endDateTime = document.getElementById('endDateTime').value;
        const cardinal = document.getElementById('cardinal').value; // 동일한 cardinal 사용

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

        apiRequest(`${apiEndpoint}/admin/attendance-event`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(eventData)
        })
            .then(response => response.text())
            .then(message => {
                showApiMessage(message, 'success');
                document.getElementById('createWeekForm').style.display = 'none';
            })
            .catch(error => {
                showApiMessage(error.message, 'danger');
            });
    });

    document.getElementById('checkAttendanceCodeButton').addEventListener('click', function () {
        const cardinal = document.getElementById('checkCardinal').value;

        apiRequest(`${apiEndpoint}/admin/attendances/${cardinal}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                showAttendanceCodes(data.data);
            })
            .catch(error => {
                showApiMessage(error.message, 'danger');
            });
    });

    document.getElementById('closeCheckFormButton').addEventListener('click', function () {
        document.getElementById('checkAttendanceCodeForm').style.display = 'none';
    });
});

function loadAttendanceEvents() {
    apiRequest(`${apiEndpoint}/admin/attendance-event`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            const attendanceEventList = document.getElementById('attendanceEventList');
            data.data.forEach(event => {
                const eventDiv = document.createElement('div');
                eventDiv.className = 'card mt-3';
                eventDiv.innerHTML = `
                    <div class="card-body">
                        <h5 class="card-title">${event.title}</h5>
                        <h6 class="card-subtitle mb-2 text-muted">${event.startDateTime} - ${event.endDateTime}</h6>
                        <p class="card-text">${event.content}</p>
                        <p class="card-text"><strong>장소:</strong> ${event.location}</p>
                        <p class="card-text"><strong>준비물:</strong> ${event.requiredItems}</p>
                        <p class="card-text"><strong>참여인원:</strong> ${event.memberNumber}</p>
                        <p class="card-text"><strong>출석 정보 기수:</strong> ${event.cardinal}</p>
                        <p class="card-text"><strong>출석 코드:</strong> ${event.weekCode ? event.weekCode.attendanceCode : 'N/A'}</p>
                    </div>
                `;
                attendanceEventList.appendChild(eventDiv);
            });
        })
        .catch(error => {
            showApiMessage(error.message, 'danger');
        });
}

function showAttendanceCodes(attendanceCodes) {
    const tableBody = document.getElementById('attendanceCodeTableBody');
    tableBody.innerHTML = '';
    attendanceCodes.forEach(code => {
        const row = document.createElement('tr');
        row.innerHTML = `<td>${code.weekNumber}</td><td>${code.attendanceCode || 'N/A'}</td>`;
        tableBody.appendChild(row);
    });
    document.getElementById('attendanceCodeResult').style.display = 'block';
}