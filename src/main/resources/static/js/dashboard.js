document.getElementById('userName').textContent = sessionStorage.getItem('username') || 'Guest';

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
                window.location.href = `${page}`;
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
    window.location.href = 'item';
}

function orders() {
    window.location.href = '/search-order';
}
