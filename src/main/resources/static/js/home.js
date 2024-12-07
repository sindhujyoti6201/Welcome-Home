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
    if (heroText) {
        heroText.style.opacity = "0";
        heroText.style.transform = "translateY(30px)";

        setTimeout(() => {
            heroText.style.transition = "all 1.5s ease";
            heroText.style.opacity = "1";
            heroText.style.transform = "translateY(0)";
        }, 500);
    }
});

// Auto Update Footer Year
const footerYear = document.querySelector("#year");
footerYear.textContent = new Date().getFullYear();

// Add hover effect on buttons (for better control)
const buttons = document.querySelectorAll('.btn');
buttons.forEach((button) => {
    button.addEventListener('mouseenter', () => {
        button.style.backgroundColor = '#a3917d'; // Hover effect color
    });

    button.addEventListener('mouseleave', () => {
        button.style.backgroundColor = '#b3a291'; // Default color
    });
});
