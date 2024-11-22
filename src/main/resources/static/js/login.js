document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault(); // Prevent default form submission

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        try {
            // Send the login request
            const response = await fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'Accept': 'application/json'  // Adding the Accept header for JSON response
                },
                body: new URLSearchParams({
                    username: username,
                    password: password
                })
            });

            if (response.ok) {
                // Successful login, redirect to home
                window.location.href = "/home"; // Redirect to the home page
            } else {
                // Handle 401 Unauthorized response
                if (response.status === 401) {
                    const errorData = await response.json(); // Parse the JSON error response
                    alert(errorData.message || "Invalid credentials"); // Show alert with error message
                    // Optionally, clear the password field
                    document.getElementById("password").value = ""; // Clear password field for retry
                } else {
                    // Handle other error statuses (optional)
                    alert("An unexpected error occurred. Please try again.");
                }
            }
        } catch (error) {
            console.error('Error:', error);
            alert("An unexpected error occurred. Please try again.");
        }
    });
});
