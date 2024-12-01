document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault(); // Prevent default form submission

        // const username = document.getElementById("username").value;
        // const password = document.getElementById("password").value;
        // console.log(username);
        // console.log(password);

        const formData = new FormData(loginForm);

        // Convert form data to a JSON object
        const data = {};
        formData.forEach((value, key) => {
            data[key] = value;
        });

        console.log("The data passed by the user is: ", data);

        try {
            // Send the login request as JSON
            const response = await fetch('/volunteer/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',  // Set Content-Type to application/json
                    'Accept': 'application/json'  // Expect JSON response
                },
                body: JSON.stringify(data)
            });

            const responseData = await response.json();
            console.log(responseData);
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
});