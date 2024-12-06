document.getElementById('userName').textContent = sessionStorage.getItem('username') || 'Guest';

// Map frontend pages to database ENUM values
const ROLE_MAPPING = {
    'delivering': 'DELIVERY AGENT',
    'donation': 'DONOR',
    'supervising': 'SUPERVISOR',
    'manager': 'MANAGER'
};

// Map pages to their URLs
const PAGE_URLS = {
    'delivering': '/delivery',
    'donation': '/donation',
    'supervising': '/supervising',
    'manager': '/manager'
};

async function checkAccess(page) {
    const username = sessionStorage.getItem('username');
    console.log('Checking access for page:', page);
    console.log('Username:', username);

    try {
        const response = await fetch(`/api/dashboard/${username}`);
        const data = await response.json();
        console.log('API Response:', data); // Log the full response

        if (response.ok) {
            const requiredRole = ROLE_MAPPING[page];
            console.log('Required Role:', requiredRole); // Log the required role

            // Log the roles returned by API
            console.log('User Roles from API:', data.roles);

            // Check if the user has the required role or is a manager
            const hasAccess = data.roles.some(role => {
                const normalizedRole = role.trim().toLowerCase();
                console.log(`Comparing: ${normalizedRole} with ${requiredRole.toLowerCase()}`);
                return (
                    normalizedRole === requiredRole.trim().toLowerCase() ||
                    normalizedRole === 'manager' // Manager has access to all pages
                );
            });

            console.log('Has access:', hasAccess); // If hasAccess is true, it should redirect

            if (hasAccess) {
                console.log('Access granted, redirecting to:', PAGE_URLS[page]);
                window.location.href = PAGE_URLS[page];
            } else {
                console.log('Access denied, showing popup');
                showPopup();
            }
        } else {
            console.error('Error in API response:', data);
            showPopup();
        }
    } catch (error) {
        console.error('Error fetching data:', error);
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
