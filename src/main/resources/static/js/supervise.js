document.getElementById('userName').textContent = sessionStorage.getItem('username') || 'Guest';

async function loadOrders() {
    const username = sessionStorage.getItem('username');
    try {
        console.log('Fetching orders for:', username);
        const response = await fetch(`/api/supervise/orders/${username}`);

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Error details:', errorData);
            throw new Error(`Failed to load orders: ${errorData.error || 'Unknown error'}`);
        }

        const data = await response.json();
        console.log('Received orders:', data);

        if (!data.orders) {
            console.error('No orders data received');
            throw new Error('Invalid response format');
        }

        displayOrders(data.orders);
    } catch (error) {
        console.error('Error loading orders:', error);
        document.getElementById('ordersList').innerHTML =
            `<p>Error loading orders: ${error.message}</p>`;
    }
}

function displayOrders(orders) {
    const ordersList = document.getElementById('ordersList');
    ordersList.innerHTML = orders.map(order => {
        const formattedDate = new Date(order.orderDate).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
        });

        return `
            <div class="order-card">
                <div class="order-header">
                    <h3>Order #${order.orderID}</h3>
                    <div class="status-group">
                        <select class="status-select" id="status-${order.orderID}">
                            <option value="pending" ${order.currentStatus === 'pending' ? 'selected' : ''}>Pending</option>
                            <option value="in_progress" ${order.currentStatus === 'in_progress' ? 'selected' : ''}>In Progress</option>
                            <option value="delivered" ${order.currentStatus === 'delivered' ? 'selected' : ''}>Delivered</option>
                        </select>
                        <button class="update-btn" onclick="updateOrderStatus(${order.orderID})">
                            Update Status
                        </button>
                    </div>
                </div>
                <div class="order-details">
                    <div class="detail-item">Order Date: ${formattedDate}</div>
                    <div class="detail-item">Client: ${order.client}</div>
                    <div class="detail-item">Supervisor: ${order.supervisor}</div>
                    <div class="detail-item">Notes: ${order.orderNotes || 'No notes'}</div>
                </div>
            </div>
        `;
    }).join('');
}

async function updateOrderStatus(orderId) {
    const status = document.getElementById(`status-${orderId}`).value;
    const username = sessionStorage.getItem('username');
    const currentDate = new Date().toISOString().split('T')[0];

    const requestBody = {
        userName: username,
        orderID: orderId,
        status: status,
        date: currentDate
    };

    console.log('Sending update request:', requestBody);

    try {
        const response = await fetch('/api/supervise/update-status', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        console.log('Response status:', response.status);

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Error details:', errorData);
            throw new Error(`Update failed: ${errorData.error || 'Unknown error'}`);
        }

        console.log('Update successful');
        // Wait a moment before reloading orders to ensure backend has processed
        setTimeout(() => loadOrders(), 500);

    } catch (error) {
        console.error('Error updating status:', error);
        alert(`Failed to update order status: ${error.message}`);
    }
}

function backToDashboard() {
    window.location.href = 'dashboard';
}

loadOrders();
