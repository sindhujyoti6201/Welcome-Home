document.addEventListener('DOMContentLoaded', () => {
    const username = sessionStorage.getItem('username') || 'Guest';
    console.log('Username:', username);  // Debugging line to check the value
    document.getElementById('userName').textContent = username;
});

async function checkAccess(page) {
    const username = sessionStorage.getItem('username');

    try {
        const response = await fetch(`/api/dashboard/${username}`);
        const data = await response.json();

        if (response.ok) {
            let hasAccess = false;

            switch (page) {
                case 'delivery':
                    hasAccess = data.hasDeliveryAccess;
                    break;
                case 'donations':
                    hasAccess = data.hasDonationAccess;
                    break;
                case 'supervise':
                    hasAccess = data.hasSupervisorAccess;
                    break;
                case 'manager':
                    hasAccess = data.hasManagerAccess;
                    break;
                default:
                    hasAccess = false;
            }

            if (hasAccess) {
                window.location.href = `${page}?username=${username}`;
            } else {
                showPopup();
            }
        } else {
            showPopup();
        }
    } catch (error) {
        console.error('Error checking access:', error);
        showPopup();
    }
}

function showPopup() {
    document.getElementById('overlay').style.display = 'block';
    document.getElementById('registrationPopup').style.display = 'block';
}

function closePopup() {
    document.getElementById('overlay').style.display = 'none';
    document.getElementById('registrationPopup').style.display = 'none';
}

function logout() {
    sessionStorage.removeItem('username');
    window.location.href = '/volunteer-login';
}

function item() {
    window.location.href = '/itemsearch';
}

function orders() {
    window.location.href = '/search-order';
}
