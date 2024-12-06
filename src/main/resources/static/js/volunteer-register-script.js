document.addEventListener("DOMContentLoaded", () => {
    const registerForm = document.getElementById("volunteer-register-form");

    if (registerForm) {
        registerForm.addEventListener("submit", async (event) => {
            event.preventDefault();

            // Get all checked checkboxes
            const checkedActivities = Array.from(
                document.querySelectorAll('input[name="activities"]:checked')
            ).map(checkbox => checkbox.value);

            if (checkedActivities.length === 0) {
                alert("Please select at least one role");
                return;
            }

            const data = {
                username: document.getElementById("username").value,
                password: document.getElementById("password").value,
                firstName: document.getElementById("first-name").value,
                lastName: document.getElementById("last-name").value,
                email: document.getElementById("email").value,
                roleEnrolled: checkedActivities
            };

            console.log("Form Data:", data);

            try {
                const response = await fetch("/volunteer/register", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    },
                    body: JSON.stringify(data)
                });

                const responseData = await response.json();
                console.log("Server Response:", responseData);

                if (response.ok && responseData.status === "success") {
                    const successMessage = document.createElement("div");
                    successMessage.textContent = "Registration successful! Redirecting to login page...";
                    successMessage.style.color = "green";
                    successMessage.style.textAlign = "center";
                    successMessage.style.marginTop = "20px";
                    document.querySelector(".login-register").appendChild(successMessage);

                    setTimeout(() => {
                        window.location.href = "/volunteer-login";
                    }, 3000);
                } else {
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