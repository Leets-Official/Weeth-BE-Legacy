const apiEndpoint = (window.location.hostname === 'localhost')
    ? 'http://localhost:8080'
    : 'https://api.weeth.site';

document.getElementById('receiptForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 기본 폼 제출 동작 방지
    confirmAction('영수증 제출', submitReceipt);
});

function submitTotalAccount() {

    const cardinal = document.getElementById('totalCardinal').value;
    const description = document.getElementById('totalDescription').value;
    const total = document.getElementById('totalAmount').value;

    const data = {
        description: description,
        total: parseInt(total),
        cardinal: parseInt(cardinal)
    };

    return apiRequest(`${apiEndpoint}/admin/account`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => response.json());
}


function checkAccount() {
    const cardinal = document.getElementById('checkCardinal').value;

    apiRequest(`${apiEndpoint}/account/${cardinal}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            displayAccountInfo(data.data);
        })
        .catch(error => {
            alert(`Error: ${error.message}`);
        });
}


function displayAccountInfo(account) {
    const accountInfoDiv = document.getElementById('accountInfo');
    accountInfoDiv.style.display = 'block';
    accountInfoDiv.innerHTML = `
        <div class="card mb-3">
            <div class="card-body">
                <h3 class="card-title">총 회비</h3>
                <p class="card-text"><strong>설명:</strong> ${account.description}</p>
                <p class="card-text"><strong>Total:</strong> ${account.total}</p>
                <p class="card-text"><strong>기수:</strong> ${account.cardinal}</p>
            </div>
        </div>
        <h4>영수증</h4>
        ${account.receipts.map(receipt => `
            <div class="card mb-2">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <strong>Receipt ID: ${receipt.id}</strong>
                        <button class="btn btn-danger btn-sm" onclick="deleteReceipt(${receipt.id})">삭제</button>
                    </div>
                    <p class="card-text"><strong>총 금액:</strong> ${receipt.amount}</p>
                    <p class="card-text"><strong>내역:</strong> ${receipt.description}</p>
                    <p class="card-text"><strong>Date:</strong> ${receipt.date}</p>
                    ${receipt.images.length > 0 ? `<p class="card-text"><strong>Images:</strong> ${receipt.images.map(image => `<a href="${image}" target="_blank">View Image</a>`).join(', ')}</p>` : ''}
                </div>
            </div>
        `).join('')}
    `;
}

function submitReceipt() {

    const cardinal = document.getElementById('cardinal').value;
    const amount = document.getElementById('amount').value;
    const description = document.getElementById('description').value;
    const date = document.getElementById('date').value;
    const fileInput = document.getElementById('file');
    const files = fileInput.files;

    const formData = new FormData();
    formData.append('dto', new Blob([JSON.stringify({
        amount: parseInt(amount),
        description: description,
        date: date
    })], { type: 'application/json' }));

    for (let i = 0; i < files.length; i++) {
        formData.append('files', files[i]);
    }

    apiRequest(`${apiEndpoint}/admin/account/${cardinal}`, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (data.code === 200) {
                alert(`성공: ${data.message}`);
            } else {
                throw new Error(data.message);
            }
        })
        .catch(error => {
            console.error('Request failed:', error);
            alert(`Error: ${error.message}`);
        });
}


function deleteReceipt(receiptId) {
    if (confirm('삭제하시겠습니까?')) {
        apiRequest(`${apiEndpoint}/admin/account/${receiptId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(message => {
                alert(`삭제 성공`);
                const cardinal = document.getElementById('checkCardinal').value;
                apiRequest(`${apiEndpoint}/account/${cardinal}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(response => response.json())
                    .then(data => {
                        if(data.code===200) {
                            displayAccountInfo(data.data);
                        }else {
                            throw new Error(data.message);
                        }
                    })
                    .catch(error => {
                        alert(`Error: ${error.message}`);
                    });
            })
            .catch(error => {
                alert(`Error: ${error.message}`);
            });
    }
}

function setModalContent(type) {
    let modalBodyContent = document.getElementById('modal-body-content');
    let modalSubmitButton = document.getElementById('modalSubmitButton');
    if (type === 'total') {
        modalBodyContent.innerHTML = `
                <form id="totalAccountForm">
                    <div class="form-group">
                        <label for="totalCardinal">기수</label>
                        <input type="number" class="form-control" id="totalCardinal" placeholder="기수 입력" required>
                    </div>
                    <div class="form-group">
                        <label for="totalDescription">설명</label>
                        <input type="text" class="form-control" id="totalDescription" placeholder="설명 입력" required>
                    </div>
                    <div class="form-group">
                        <label for="totalAmount">총 금액</label>
                        <input type="number" class="form-control" id="totalAmount" placeholder="총 금액 입력" required>
                    </div>
                </form>
            `;
        modalSubmitButton.onclick = function() {
            const form = document.getElementById('totalAccountForm');
            if (form.checkValidity()) {
                confirmAction('총 회비 등록', submitTotalAccount);
            } else {
                form.reportValidity();
            }
        };
    }
}

function confirmAction(actionName, actionFunction, ...args) {
    if (confirm(`${actionName} 하시겠습니까?`)) {
        actionFunction(...args)
            .then(response => {
                if (response.code === 200) {
                    alert(`${actionName} 성공: ${response.message}`);
                } else {
                    throw new Error(response.message);
                }
            })
            .catch(error => {
                alert(`${actionName} 실패: ${error.message}`);
            });
    }
}