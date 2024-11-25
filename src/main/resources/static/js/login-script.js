document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault(); // Prevent default form submission

        // const username = document.getElementById("username").value;
        // const password = document.getElementById("password").value;
        // console.log(username);
        // console.log(password);

        // Collect form data
        const formData = new FormData(loginForm);

        // Convert form data to a JSON object
        const data = {};
        formData.forEach((value, key) => {
            data[key] = value;
        });

        console.log(data);
        try {
            // Send the login request as JSON
            const response = await fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',  // Set Content-Type to application/json
                    'Accept': 'application/json'  // Expect JSON response
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            const responseData = await response.json();

            if (response.ok) {
                if (responseData.status === "success") {
                    // Successful login, redirect to home
                    window.location.href = "/home";
                } else {
                    // Handle failure response
                    alert(responseData.message || "Invalid credentials");
                }
            } else {
                // Handle failure
                alert(responseData.message || "Invalid credentials");
            }
        } catch (error) {
            console.error('Error:', error);
            alert("An unexpected error occurred. Please try again.");
        }
    });

    // Handle register link
    const registerLink = document.querySelector(".login-register a");
    registerLink.addEventListener("click", function (event) {
        event.preventDefault();
        window.location.href = "/register"; // Redirect to the register page
    });
});