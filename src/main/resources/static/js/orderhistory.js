// Retrieve username from sessionStorage
const username = sessionStorage.getItem('username') || 'Guest';

// Display the username in the nav
document.getElementById('username').textContent = username;
console.log(username);

// If a query parameter "username" exists in the URL, we replace it with a clean URL without query parameters
const currentUrl = window.location.href;
const urlWithoutParams = currentUrl.split('?')[0]; // Removes the query string

// Update the URL in the browser's address bar without reloading the page
if (currentUrl !== urlWithoutParams) {
    window.history.replaceState({}, '', urlWithoutParams);  // Removes the "username" query parameter
}