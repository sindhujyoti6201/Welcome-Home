//document.getElementById('userName').textContent = sessionStorage.getItem('username') || 'Guest';
const urlParams = new URLSearchParams(window.location.search);
const username = urlParams.get('username') || sessionStorage.getItem('username') || 'Guest';

// Display the username on the page
document.getElementById('userName').textContent = username;

function item() {
    window.location.href = '/profile';
}

function orders() {
    window.location.href = '/orderhistory?username=' + encodeURIComponent(username);
}

async function fetchCustomerData() {
    const username = sessionStorage.getItem('username');

    if (!username) {
        alert("User not logged in!");
        window.location.href = '/customer-login';
        return;
    }
    try {
        const response = await fetch(`/api/customer/${username}`);
        const data = await response.json();

        if (response.ok) {
            console.log('Customer Data:', data);
            // Handle data as needed
        } else {
            console.error('Failed to fetch customer data:', data);
            alert("Error fetching customer data.");
        }
    } catch (error) {
        console.error('Error during fetch:', error);
        alert("Unable to fetch customer data.");
    }
}

async function addToCart(username, itemId) {
    try {
        const parsedItemId = parseInt(itemId, 10);

        // Sending request to add the item to the order
        const response = await fetch('/customer/addToCart', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: new URLSearchParams({username, itemId: parsedItemId})
        });

        const message = await response.text();
        alert(message);

        if (response.ok) {
            // Remove the product item from the DOM after it's added to the order
            const itemElement = document.querySelector(`[data-item-id="${itemId}"]`).closest('.product-item');
            if (itemElement) {
                itemElement.remove();  // This will remove the product item from the page
            }
        }
    } catch (error) {
        console.error('Error adding to cart:', error);
        alert('Failed to add item to cart.');
    }
}


function logout() {
    sessionStorage.removeItem('username');
    window.location.href = '/customer-login';
}

// Add this at the end of your existing script tag or in a separate script tag
document.addEventListener('DOMContentLoaded', function () {
    // Load categories when the page loads
    loadCategories();

    // Add event listener to main category dropdown to load subcategories
    const mainCategorySelect = document.querySelector('select[name="mainCategory"]');
    mainCategorySelect.addEventListener('change', loadSubcategories);
});

async function loadCategories() {
    try {
        const response = await fetch('/api/categories');
        const categories = await response.json();

        const select = document.querySelector('select[name="mainCategory"]');
        select.innerHTML = '<option value="">Select Category</option>';

        categories.forEach(category => {
            const option = new Option(category, category);
            select.add(option);
        });
    } catch (error) {
        console.error('Failed to load categories:', error);
    }
}

async function loadSubcategories() {
    const mainCategory = document.querySelector('select[name="mainCategory"]').value;

    if (!mainCategory) {
        // Reset subcategories if no main category is selected
        const subcategorySelect = document.querySelector('select[name="subCategory"]');
        subcategorySelect.innerHTML = '<option value="">Select Sub Category</option>';
        return;
    }

    try {
        const response = await fetch(`/api/subcategories/${mainCategory}`);
        const subcategories = await response.json();

        const select = document.querySelector('select[name="subCategory"]');
        select.innerHTML = '<option value="">Select Sub Category</option>';

        subcategories.forEach(subcat => {
            const option = new Option(subcat, subcat);
            select.add(option);
        });
    } catch (error) {
        console.error('Failed to load subcategories:', error);
    }
}

fetchCustomerData();