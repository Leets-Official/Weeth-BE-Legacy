document.getElementById('totalAccountButton').addEventListener('click', function() {
    const form = document.getElementById('totalAccountForm');
    form.style.display = (form.style.display === 'block') ? 'none' : 'block';
});

document.getElementById('closeTotalAccountFormButton').addEventListener('click', function() {
    document.getElementById('totalAccountForm').style.display = 'none';
});

document.getElementById('submitTotalAccountButton').addEventListener('click', function() {
    if (!confirm('총 회비 정보를 제출하시겠습니까?')) {
        return;
    }

    const cardinal = document.getElementById('totalCardinal').value;
    const description = document.getElementById('totalDescription').value;
    const total = document.getElementById('totalAmount').value;

    const data = {
        description: description,
        total: parseInt(total),
        cardinal: parseInt(cardinal)
    };

    apiRequest('/admin/account', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.text())
        .then(message => {
            alert(`Success: ${message}`);
            document.getElementById('totalAccountForm').style.display = 'none';
        })
        .catch(error => {
            alert(`Error: ${error.message}`);
        });
});

document.getElementById('manageAccountButton').addEventListener('click', function() {
    const form = document.getElementById('manageAccountForm');
    form.style.display = (form.style.display === 'block') ? 'none' : 'block';
});

document.getElementById('closeAccountFormButton').addEventListener('click', function() {
    document.getElementById('manageAccountForm').style.display = 'none';
});

document.getElementById('submitAccountButton').addEventListener('click', function() {
    if (!confirm('회비 정보를 제출하시겠습니까?')) {
        return;
    }

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

    apiRequest(`/admin/account/${cardinal}`, {
        method: 'POST',
        body: formData
    })
        .then(response => response.text())
        .then(message => {
            alert(`Success: ${message}`);
            document.getElementById('manageAccountForm').style.display = 'none';
        })
        .catch(error => {
            alert(`Error: ${error.message}`);
        });
});

document.getElementById('checkAccountButton').addEventListener('click', function() {
    const cardinal = document.getElementById('checkCardinal').value;

    apiRequest(`/account/${cardinal}`, {
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
});

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

function deleteReceipt(receiptId) {
    if (confirm('삭제하시겠습니까?')) {
        apiRequest(`/admin/account/${receiptId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.text())
            .then(message => {
                alert(`Success: ${message}`);
                const cardinal = document.getElementById('checkCardinal').value;
                apiRequest(`/admin/account/${cardinal}`, {
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
            })
            .catch(error => {
                alert(`Error: ${error.message}`);
            });
    }
}