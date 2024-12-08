
    document.getElementById('userName').textContent = sessionStorage.getItem('username') || 'Guest';

    let selectedOrderId = null;
    let selectedAction = null;
    let allOrders = [];

    async function loadOrders() {
    const username = sessionStorage.getItem('username');
    try {
    const response = await fetch(`/api/manager/orders/${username}`);
    if (!response.ok) {
    throw new Error('Failed to load orders');
}

    const data = await response.json();
    allOrders = data.orders || [];
    filterOrders('INITIATED');
} catch (error) {
    console.error('Error:', error);
    document.getElementById('ordersList').innerHTML =
    `<div class="order-card">Error loading orders: ${error.message}</div>`;
}
}

    function filterOrders(status) {
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.toggle('active', btn.textContent.includes(status.replace('_', ' ')));
    });

    const filteredOrders = allOrders.filter(order => order.orderStatus === status);
    displayOrders(filteredOrders, status);
}

    function displayOrders(orders, currentStatus) {
    const ordersList = document.getElementById('ordersList');

    if (orders.length === 0) {
    ordersList.innerHTML = `<div class="order-card">No ${currentStatus.toLowerCase()} orders found</div>`;
    return;
}

    ordersList.innerHTML = orders.map(order => {
    const formattedDate = new Date(order.orderDate).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
});

    return `
                    <div class="order-card">
                        <div class="order-header">
                            <div>
                                <h3>Order #${order.orderID}</h3>
                                <span class="status-badge status-${order.orderStatus}">
                                    ${order.orderStatus}
                                </span>
                            </div>
                            <div class="order-actions">
                                ${order.orderStatus === 'INITIATED' ? `
                                    <button class="start-btn" onclick="showStartConfirmation(${order.orderID})">
                                        APPROVE
                                    </button>
                                ` : ''}
                                <button class="cancel-btn" onclick="showCancelConfirmation(${order.orderID})">
                                    CANCEL
                                </button>
                            </div>
                        </div>
                        <div class="order-details">
                            <div class="detail-item">
                                <span class="detail-label">Order Date:</span>
                                <span>${formattedDate}</span>
                            </div>
                            <div class="detail-item">
                                <span class="detail-label">Client:</span>
                                <span>${order.clientName}</span>
                            </div>
                            ${order.supervisor ? `
                                <div class="detail-item">
                                    <span class="detail-label">Supervisor:</span>
                                    <span>${order.supervisorName}</span>
                                </div>
                            ` : ''}
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

    function showStartConfirmation(orderId) {
    selectedOrderId = orderId;
    selectedAction = 'start';
    document.getElementById('modalTitle').textContent = 'Start Order';
    document.getElementById('modalMessage').textContent =
    'This will assign a supervisor and delivery agent to the order. Continue?';
    showModal();
}

    function showCancelConfirmation(orderId) {
    selectedOrderId = orderId;
    selectedAction = 'cancel';
    document.getElementById('modalTitle').textContent = 'Cancel Order';
    document.getElementById('modalMessage').textContent = 'Are you sure you want to cancel this order?';
    showModal();
}

    function showModal() {
    document.getElementById('modalOverlay').style.display = 'block';
    document.getElementById('confirmationModal').style.display = 'block';
}

    function closeModal() {
    document.getElementById('modalOverlay').style.display = 'none';
    document.getElementById('confirmationModal').style.display = 'none';
    selectedOrderId = null;
    selectedAction = null;
}

    document.getElementById('confirmButton').addEventListener('click', async () => {
    if (selectedOrderId && selectedAction) {
    await updateOrderStatus(selectedOrderId, selectedAction);
    closeModal();
}
});

    async function updateOrderStatus(orderId, action) {
    try {
    const response = await fetch('/api/manager/update-order', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify({
    orderID: orderId,
    action: action
})
});

    const data = await response.json();

    if (!response.ok) {
    throw new Error(data.error || 'Failed to update order');
}

    await loadOrders();

        if (action === 'start') {
            alert(`Order started successfully!\nAssigned Supervisor: ${data.supervisor}\nDelivery Agent: ${data.deliveryAgent}`);
        } else {
            alert('Order cancelled successfully!');
        }

} catch (error) {
    console.error('Error:', error);
    alert(error.message);
}
}

    function backToDashboard() {
    window.location.href = '/dashboard';
}

    // Initial load
    loadOrders();
