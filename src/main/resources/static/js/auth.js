// /static/js/auth.js

function getToken() {
    return localStorage.getItem('token');
}

function setToken(token) {
    localStorage.setItem('token', token);
}

function removeToken() {
    localStorage.removeItem('token');
}

function authFetch(url, options = {}) {
    const token = getToken();
    if (!options.headers) {
        options.headers = {};
    }
    if (token) {
        options.headers['Authorization'] = 'Bearer ' + token;
    }
    return fetch(url, options);
}


function logout() {
    removeToken();
}

function apiRequest(url, options = {}) {
    const token = getToken();
    if (!token) {
        return Promise.reject(new Error('Token not found. Please log in again.'));
    }

    const headers = options.headers || {};
    headers['Authorization'] = `Bearer ${token}`;
    options.headers = headers;
    return fetch(url, options).then(response => {
        if (response.status === 401) {
            return response.text().then(message => { throw new Error('Unauthorized: ' + message); });
        }
        return response;
    });
}

function showTokenErrorMessage(message) {
    const tokenErrorMessageDiv = document.getElementById('tokenErrorMessage');
    tokenErrorMessageDiv.style.display = 'block';
    tokenErrorMessageDiv.innerText = message;
    setTimeout(() => {
        tokenErrorMessageDiv.style.display = 'none';
    }, 5000);
}

function showApiMessage(message, type) {
    const apiMessageDiv = document.getElementById('apiMessage');
    apiMessageDiv.className = `alert alert-${type}`;
    apiMessageDiv.style.display = 'block';
    apiMessageDiv.innerText = message;
    setTimeout(() => {
        apiMessageDiv.style.display = 'none';
    }, 5000);
}