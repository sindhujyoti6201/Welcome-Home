// Ensure the script runs after the DOM is fully loaded
document.addEventListener("DOMContentLoaded", () => {
    // Select the form element
    const registerForm = document.getElementById("volunteer-register-form");

    // Check if the form exists
    if (registerForm) {
        registerForm.addEventListener("submit", async (event) => {
            event.preventDefault(); // Prevent the default form submission behavior

            // Collect form data
            const formData = new FormData(registerForm);

            // Convert form data to a JSON object
            const data = {
                username: formData.get("username"),
                password: formData.get("password"),
                firstName: formData.get("first-name"),
                lastName: formData.get("last-name"),
                email: formData.get("email"),
                roleEnrolled: Array.from(formData.getAll("activities")) // Collect all checked activities
            };

            console.log("Form Data:", data);

            try {
                // Send the data to the server
                const response = await fetch("/volunteer/register", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json", // Specify JSON format
                        "Accept": "application/json"
                    },
                    body: JSON.stringify(data) // Convert the data object to a JSON string
                });

                // Parse the server response
                const responseData = await response.json();
                console.log(responseData);
                if (response.ok && responseData.status === "success") {
                    // Show a success message
                    const successMessage = document.createElement("div");
                    successMessage.textContent = "Registration successful! Redirecting to Volunteer login page...";
                    successMessage.style.color = "green";
                    successMessage.style.textAlign = "center";
                    successMessage.style.marginTop = "20px";
                    document.querySelector(".login-register").appendChild(successMessage);

                    // Redirect to the login page after 3 seconds
                    setTimeout(() => {
                        window.location.href = "/volunteer-login";
                    }, 3000);
                } else {
                    // Handle server-side validation errors or failures
                    alert(responseData.message || "Registration failed. Please try again.");
                }
            } catch (error) {
                console.error("Error:", error);
                alert("An unexpected error occurred. Please try again.");
            }
        });
    } else {
        console.error("Form with ID 'volunteer-register-form' not found.");
    }
});