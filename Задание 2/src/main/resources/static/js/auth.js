const page = document.body;
const tabs = document.querySelectorAll('[data-auth-tab]');
const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');

function setMode(mode) {
    const register = mode === 'register';
    page.dataset.mode = mode;
    loginForm.hidden = register;
    registerForm.hidden = !register;
    tabs.forEach(tab => {
        const active = tab.dataset.authTab === mode;
        tab.classList.toggle('active', active);
        tab.setAttribute('aria-selected', String(active));
    });
}

tabs.forEach(tab => tab.addEventListener('click', () => setMode(tab.dataset.authTab)));
setMode(page.dataset.mode || 'login');
