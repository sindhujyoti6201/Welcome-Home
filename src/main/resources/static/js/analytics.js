document.addEventListener('DOMContentLoaded', function () {
    // Ensure categoryData is available from Thymeleaf
    if (typeof categoryData !== 'undefined' && categoryData.length > 0) {
        const ctx = document.getElementById('categoryChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: categoryData.map(item => item.mainCategory + ' - ' + item.subCategory),
                datasets: [{
                    label: 'Number of Items',
                    data: categoryData.map(item => item.itemCount),
                    backgroundColor: 'rgba(197, 181, 164, 0.6)'
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
});