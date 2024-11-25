// Write JavaScript here
// Sticky Navigation Bar
window.addEventListener("scroll", () => {
    const nav = document.querySelector("nav");
    if (window.scrollY > 50) {
        nav.classList.add("sticky");
    } else {
        nav.classList.remove("sticky");
    }
});

// Hero Section Text Animation
document.addEventListener("DOMContentLoaded", () => {
    const heroText = document.querySelector(".hero h1");
    heroText.style.opacity = "0";
    heroText.style.transform = "translateY(30px)";

    setTimeout(() => {
        heroText.style.transition = "all 1.5s ease";
        heroText.style.opacity = "1";
        heroText.style.transform = "translateY(0)";
    }, 500);
});

// Smooth Scroll to Section
document.querySelectorAll("nav a").forEach((link) => {
    link.addEventListener("click", (e) => {
        e.preventDefault(); // Prevent default anchor behavior
        const targetId = e.target.getAttribute("href").slice(1); // Get target ID (e.g., #services)
        const targetSection = document.getElementById(targetId);

        if (targetSection) {
            window.scrollTo({
                top: targetSection.offsetTop - 50, // Offset for sticky nav
                behavior: "smooth",
            });
        }
    });
});

// Button Click Effect
document.querySelectorAll("nav a").forEach((link) => {
    link.addEventListener("click", () => {
        link.classList.add("clicked");
        setTimeout(() => link.classList.remove("clicked"), 300);
    });
});

// Auto Update Footer Year
const footerYear = document.querySelector("footer p");
const currentYear = new Date().getFullYear();
footerYear.innerHTML = `© ${currentYear} The Giving Nest. All Rights Reserved.`;

// Add event listeners to the elements
document.addEventListener("DOMContentLoaded", function() {
    // Select elements by their text content
    const volunteerButton = [...document.querySelectorAll("button, a")].find(
        el => el.textContent.trim() === "Want to Volunteer" || el.textContent.trim() === "Donate Now"
    );
    const borrowButton = [...document.querySelectorAll("button, a")].find(
        el => el.textContent.trim() === "Borrow" || el.textContent.trim() === "Join Now"
    );

    if (volunteerButton) {
        volunteerButton.addEventListener("click", function() {
            window.location.href = "login";
        });
    }

    if (borrowButton) {
        borrowButton.addEventListener("click", function() {
            window.location.href = "login";
        });
    }
});