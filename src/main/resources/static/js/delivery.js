
document.getElementById('userName').textContent = sessionStorage.getItem('username') || 'Guest';

async function loadOrders() {
    const username = sessionStorage.getItem('username');
    try {
        const response = await fetch(`/api/delivery/orders/${username}`);
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Failed to load orders');
        }

        const data = await response.json();
        displayOrders(data.orders || []);
    } catch (error) {
        console.error('Error loading orders:', error);
        document.getElementById('ordersList').innerHTML =
            `<div class="order-card">Error loading orders: ${error.message}</div>`;
    }
}

function displayOrders(orders) {
    const ordersList = document.getElementById('ordersList');

    if (orders.length === 0) {
        ordersList.innerHTML = '<div class="order-card">No delivery orders found</div>';
        return;
    }

    ordersList.innerHTML = orders.map(order => {
        const formattedDate = new Date(order.date).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });

        return `
<div class="order-card">
    <div class="order-header">
    <h3>Order #${order.orderID}</h3>
<div class="action-buttons">
    <button class="deliver-btn" onclick="updateOrderStatus(${order.orderID}, 'deliver')">
        Mark as Delivered
    </button>
    <button class="cancel-btn" onclick="updateOrderStatus(${order.orderID}, 'cancel')">
        Cancel Order
    </button>
</div>
</div>
<div class="order-details">
    <div class="detail-item">
        <span class="detail-label">Date:</span>
        <span>${formattedDate}</span>
    </div>
    <div class="detail-item">
        <span class="detail-label">Client:</span>
        <span>${order.clientName}</span>
    </div>
    <div class="detail-item">
        <span class="detail-label">Supervisor:</span>
        <span>${order.supervisorName}</span>
    </div>
    <div class="detail-item">
        <span class="detail-label">Status:</span>
        <span>${order.deliveredStatus}</span>
    </div>
    ${order.orderNotes ? `
                        <div class="detail-item">
                            <span class="detail-label">Notes:</span>
                            <span>${order.orderNotes}</span>
                        </div>
                    ` : ''}
</div>
</div>
`;
    }).join('');
}

async function updateOrderStatus(orderId, action) {
    const username = sessionStorage.getItem('username');
    try {
        const response = await fetch('/api/delivery/update-status', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userName: username,
                orderID: orderId,
                action: action
            })
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.error || 'Failed to update order status');
        }

        await loadOrders();
        alert(action === 'deliver' ? 'Order marked as delivered!' : 'Order cancelled successfully!');

    } catch (error) {
        console.error('Error:', error);
        alert(error.message);
    }
}

function backToDashboard() {
    window.location.href = '/dashboard';
}

// Load orders when page loads
loadOrders();
