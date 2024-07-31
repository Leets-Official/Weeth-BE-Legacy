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

function login(email, password) {
    fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    })
        .then(response => {
            if (response.ok) {
                const token = response.headers.get('Authorization');
                setToken(token);
                window.location.href = '/admin';
            } else {
                return response.text().then(message => { throw new Error(message); });
            }
        })
        .catch(error => {
            console.error("Login failed:", error.message);
            alert(error.message);
        });
}

function loadPage(url) {
    authFetch(url).then(response => {
        if (response.ok) {
            window.location.href = url;
        } else {
            console.error("Failed to load page:", url);
        }
    }).catch(error => {
        console.error("Error loading page:", error);
    });
}

function logout() {
    removeToken();
    window.location.href = '/login-html';
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