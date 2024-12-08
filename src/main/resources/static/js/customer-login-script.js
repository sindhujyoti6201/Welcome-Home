// Event listener for the login form
document.getElementById("loginForm").addEventListener("submit", async function (event) {
    event.preventDefault(); // Prevent default form submission

    // Collect form data from login form
    const loginFormData = new FormData(document.getElementById("loginForm"));

    // Convert login form data to a JSON object
    const loginData = {};
    loginFormData.forEach((value, key) => {
        loginData[key] = value;
    });

    console.log("Login Data:", loginData);

    try {
        // Send the login request as JSON
        const loginResponse = await fetch('/customer/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',  // Set Content-Type to application/json
                'Accept': 'application/json'  // Expect JSON response
            },
            body: JSON.stringify(loginData)
        });

        const loginResponseData = await loginResponse.json();

        if (loginResponse.ok && loginResponseData.status === "success") {
            const successMessage = document.createElement("div");
            successMessage.textContent = "Login successful! Redirecting to Customer dashboard page...";
            successMessage.style.color = "green";
            successMessage.style.textAlign = "center";
            successMessage.style.marginTop = "20px";
            document.querySelector(".login-register").appendChild(successMessage);

            sessionStorage.setItem('username', loginData.username);
            window.location.href = '/customer';
        } else {
            // Handle login failure
            alert(loginResponseData.message || "Invalid credentials. Please try again.");
        }
    } catch (error) {
        console.error('Error during login:', error);
        alert("An unexpected error occurred during login. Please try again.");
    }
});