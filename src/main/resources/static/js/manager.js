document.getElementById('userName').textContent = sessionStorage.getItem('username') || 'Guest';

async function loadStartedOrders() {
    try {
        const response = await fetch('/api/manager/started-orders', {
            headers: {'Accept': 'application/json'}
        });

        if (!response.ok) throw new Error('Failed to load orders');
        const data = await response.json();

        const ordersList = document.getElementById('ordersList');
        ordersList.innerHTML = data.orders.map(order => `
                    <tr onclick="goToOrderUpdate(${order.orderID})">
                        <td>${order.orderID}</td>
                        <td>${order.client}</td>
                        <td>${new Date(order.orderDate).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        })}</td>
                        <td>${order.supervisor}</td>
                        <td>${order.orderNotes || '-'}</td>
                    </tr>
                `).join('');
    } catch (error) {
        console.error('Error loading orders:', error);
        document.getElementById('ordersList').innerHTML =
            '<tr><td colspan="5">Error loading orders</td></tr>';
    }
}

function goToOrderUpdate(orderId) {
    window.location.href = `start-order.html?orderId=${orderId}`;
}

function backToDashboard() {
    window.location.href = 'dashboard';
}

loadStartedOrders();
