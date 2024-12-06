document.getElementById('userName').textContent = sessionStorage.getItem('username') || 'Guest';

async function loadOrders() {
    const username = sessionStorage.getItem('username');
    console.log('Loading orders for:', username);  // Debug log
    try {
        const response = await fetch(`/api/supervise/orders/${username}`);
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || 'Failed to load orders');
        }

        const data = await response.json();
        console.log('Received orders:', data);  // Debug log

        if (!data.orders) {
            throw new Error('No orders data received');
        }

        displayOrders(data.orders);
    } catch (error) {
        console.error('Error loading orders:', error);
        document.getElementById('ordersList').innerHTML =
            `<div class="detail-item">Error loading orders: ${error.message}</div>`;
    }
}

function displayOrders(orders) {
    const ordersList = document.getElementById('ordersList');

    if (orders.length === 0) {
        ordersList.innerHTML = '<div class="detail-item">No orders requiring supervision at this time.</div>';
        return;
    }

    ordersList.innerHTML = orders.map(order => {
        const formattedDate = new Date(order.orderDate).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });

        console.log('Order status:', order.orderStatus);  // Debug log
        console.log('Current delivery status:', order.currentStatus);  // Debug log

        return ` 
            <div class="order-card">
                <div class="order-header">
                    <h3>Order #${order.orderID}</h3>
                    <div class="status-group">
                        <button class="update-btn" onclick="updateOrderStatus(${order.orderID}, '${order.client}')">
                            Mark as In Transit
                        </button>
                    </div>
                </div>
                <div class="order-details">
                    <div class="detail-item">
                        <strong>Order Date:</strong> ${formattedDate}
                    </div>
                    <div class="detail-item">
                        <strong>Client:</strong> ${order.client}
                    </div>
                    <div class="detail-item">
                        <strong>Order Status:</strong> ${order.orderStatus}
                    </div>
                    <div class="detail-item">
                        <strong>Delivery Status:</strong> ${order.currentStatus || 'NOT YET DELIVERED'}
                    </div>
                    <div class="detail-item">
                        <strong>Notes:</strong> ${order.orderNotes || 'No notes'}
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

async function updateOrderStatus(orderId, client) {
    const username = sessionStorage.getItem('username');
    const currentDate = new Date().toISOString().split('T')[0];

    const requestBody = {
        userName: client,  // Using client instead of supervisor username
        orderID: orderId,
        deliveredStatus: 'IN_TRANSIT',
        date: currentDate
    };

    console.log('Sending update request:', requestBody);  // Debug log

    try {
        const response = await fetch('/api/supervise/update-status', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        const responseData = await response.json();
        console.log('Response:', responseData);  // Debug log

        if (!response.ok) {
            throw new Error(responseData.error || 'Failed to update status');
        }

        await loadOrders();
        alert('Order status updated successfully!');

    } catch (error) {
        console.error('Error updating status:', error);
        alert(`Failed to update status: ${error.message}`);
    }
}

function backToDashboard() {
    window.location.href = '/dashboard';
}

// Load orders when page loads
loadOrders();
