// Load year for footer
document.getElementById("year").textContent = new Date().getFullYear();

// API Base URL
const API_BASE_URL = "/api/analytics"; // Adjust this to your actual backend API URL

// Fetch and populate data
async function fetchAnalyticsData() {
    try {
        // Fetch all necessary data in parallel
        const [
            totalClients,
            totalItems,
            completionRate,
            volunteerContributions,
            avgProcessingTime,
            categoryData,
        ] = await Promise.all([
            fetch(`${API_BASE_URL}/clients-served`).then((res) => res.json()),
            fetch(`${API_BASE_URL}/items-donated`).then((res) => res.json()),
            fetch(`${API_BASE_URL}/order-completion-rate`).then((res) => res.json()),
            fetch(`${API_BASE_URL}/volunteer-contributions`).then((res) => res.json()),
            fetch(`${API_BASE_URL}/average-processing-time`).then((res) => res.json()),
            fetch(`${API_BASE_URL}/item-category-donations`).then((res) => res.json()),
        ]);

        console.log(categoryData);

        // Populate the DOM with data
        document.getElementById("totalClients").textContent = totalClients;
        document.getElementById("totalItems").textContent = totalItems;
        document.getElementById("completionRate").textContent = `${completionRate.toFixed(2)}%`;
        document.getElementById("volunteerContributions").textContent =
            `Volunteers: ${volunteerContributions.totalVolunteers}, Items: ${volunteerContributions.itemsContributed}`;
        document.getElementById("avgProcessingTime").textContent = `${avgProcessingTime.avgProcessingDays.toFixed(1)} days`;

        // Render chart
        renderCategoryChart(categoryData);
    } catch (error) {
        console.error("Error fetching analytics data:", error);
    }
}

// Render donations by category chart
function renderCategoryChart(data) {
    try {
        const ctx = document.getElementById("categoryChart").getContext("2d");
        const labels = data.map(
            (item) => `${item.mainCategory} - ${item.subCategory || "N/A"}`
        );
        const values = data.map((item) => item.itemCount);

        new Chart(ctx, {
            type: "bar",
            data: {
                labels: labels,
                datasets: [
                    {
                        label: "Donations",
                        data: values,
                        backgroundColor: "rgba(75, 192, 192, 0.6)",
                        borderColor: "rgba(75, 192, 192, 1)",
                        borderWidth: 1,
                    },
                ],
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                    },
                },
            },
        });
    } catch (error) {
        console.error("Error rendering category chart:", error);
    }
}


// Initialize analytics data fetching
fetchAnalyticsData();
