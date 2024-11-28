const API_CONFIG = {
    BASE_URL: '/api',
    ENDPOINTS: {
        SEARCH_ORDER: '/orders/search',
        SEARCH_BY_USERNAME: '/orders/user',
        UPDATE_STATUS: '/orders/status',
        MARK_ITEM_FOUND: '/orders/items/found',
        GET_CURRENT_USER: '/user/current'
    }
};

class DeliveryManager {
    constructor() {
        this.currentOrderId = null;
        this.currentUserRole = null;
        this.init();
    }

    async init() {
        try {
            await this.loadUserData();
            this.setupEventListeners();
            this.clearOrderDisplay();
        } catch (error) {
            this.handleError('Failed to initialize page');
        }
    }

    setupEventListeners() {
        document.getElementById('searchForm').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleUnifiedSearch(e);
        });

        document.getElementById('backBtn').addEventListener('click', () => {
            this.handleBack();
        });
    }

    async loadUserData() {
        try {
            const response = await axios.get(`${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.GET_CURRENT_USER}`);
            const userData = response.data;
            const fullName = `${userData.fname} ${userData.lname}`;
            document.getElementById('userName').textContent = fullName;
            this.currentUserRole = userData.roleID;
        } catch (error) {
            this.handleError('Failed to load user data');
            console.error('User data error:', error);
        }
    }

    async handleUnifiedSearch(event) {
        const searchValue = document.getElementById('searchInput').value.trim();

        if (!searchValue) {
            this.handleError('Please enter an Order ID or Client Username');
            return;
        }

        const isOrderId = /^\d+$/.test(searchValue);

        try {
            if (isOrderId) {
                const response = await axios.get(
                    `${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.SEARCH_ORDER}/${searchValue}`
                );

                if (response.data) {
                    this.currentOrderId = searchValue;
                    this.displayOrderDetails(response.data);
                    await this.loadOrderItems(searchValue);
                } else {
                    this.handleError('Order not found');
                }
            } else {
                const response = await axios.get(
                    `${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.SEARCH_BY_USERNAME}/${searchValue}`
                );

                if (response.data.length > 0) {
                    this.displayUserOrders(response.data);
                } else {
                    this.handleError('No orders found for this client');
                }
            }
        } catch (error) {
            this.handleError('Search failed. Please try again.');
            console.error('Search error:', error);
        }
    }

    displayOrderDetails(order) {
        const orderDetailsSection = document.querySelector('.order-details');
        const statusUpdateForm = this.canUpdateStatus() ? this.getStatusUpdateForm(order.status) : '';

        orderDetailsSection.innerHTML = `
            <div class="card">
                <div class="order-header">
                    <div class="order-info">
                        <strong>Order #${order.orderID}</strong>
                        <p>Client: ${order.clientName}</p>
                    </div>
                    <div class="order-status">
                        <span class="status-badge">${order.status}</span>
                        <p>Date: ${this.formatDate(order.orderDate)}</p>
                    </div>
                </div>
                ${statusUpdateForm}
            </div>
        `;

        if (this.canUpdateStatus()) {
            document.getElementById('statusUpdateForm').addEventListener('submit', (e) => {
                e.preventDefault();
                this.handleStatusUpdate(e);
            });
        }
    }

    displayUserOrders(orders) {
        if (orders.length === 1) {
            this.currentOrderId = orders[0].orderID;
            this.displayOrderDetails(orders[0]);
            this.loadOrderItems(orders[0].orderID);
            return;
        }

        const orderDetailsSection = document.querySelector('.order-details');
        orderDetailsSection.innerHTML = `
            <div class="card">
                <h3>Client Orders</h3>
                <div class="orders-list">
                    ${orders.map(order => `
                        <div class="order-item" onclick="deliveryManager.handleOrderClick('${order.orderID}')">
                            <div class="order-item-header">
                                <strong>Order #${order.orderID}</strong>
                                <span class="status-badge">${order.status}</span>
                            </div>
                            <div class="order-item-details">
                                <p>Client: ${order.clientName}</p>
                                <p>Date: ${this.formatDate(order.orderDate)}</p>
                            </div>
                        </div>
                    `).join('')}
                </div>
            </div>
        `;
    }

    async loadOrderItems(orderId) {
        try {
            const response = await axios.get(
                `${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.SEARCH_ORDER}/${orderId}/items`
            );

            const tableBody = document.querySelector('#itemsTable tbody');
            tableBody.innerHTML = response.data.map(item => this.getItemRow(item)).join('');
        } catch (error) {
            this.handleError('Failed to load order items');
            console.error('Load items error:', error);
        }
    }

    getStatusUpdateForm(currentStatus) {
        return `
            <form id="statusUpdateForm" class="status-update-form">
                <select class="select-field" name="status" required>
                    <option value="">Select New Status</option>
                    <option value="IN_TRANSIT">In Transit</option>
                    <option value="DELIVERED">Delivered</option>
                    <option value="FAILED">Failed Delivery</option>
                </select>
                <button type="submit" class="btn-primary">Update Status</button>
            </form>
        `;
    }

    getItemRow(item) {
        return `
            <tr>
                <td>${item.ItemID}</td>
                <td>${item.iDescription}</td>
                <td>Room ${item.roomNum}, Shelf ${item.shelfNum}</td>
                <td><span class="status-badge">${item.found ? 'Found' : 'Not Found'}</span></td>
                <td>
                    ${!item.found ?
            `<button class="btn-primary" onclick="deliveryManager.handleMarkFound(${item.ItemID})">
                            Mark Found
                        </button>` :
            ''}
                </td>
            </tr>
        `;
    }

    async handleStatusUpdate(event) {
        const newStatus = event.target.status.value;
        if (!this.currentOrderId || !newStatus) return;

        try {
            const response = await axios.post(
                `${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.UPDATE_STATUS}`,
                {
                    orderId: this.currentOrderId,
                    status: newStatus
                }
            );

            if (response.data) {
                this.displayOrderDetails(response.data);
                this.handleSuccess('Status updated successfully');
            }
        } catch (error) {
            this.handleError('Failed to update status');
            console.error('Status update error:', error);
        }
    }

    async handleMarkFound(itemId) {
        if (!this.currentOrderId) return;

        try {
            const response = await axios.post(
                `${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.MARK_ITEM_FOUND}`,
                {
                    orderId: this.currentOrderId,
                    itemId: itemId
                }
            );

            if (response.status === 200) {
                await this.loadOrderItems(this.currentOrderId);
                this.handleSuccess('Item marked as found');
            }
        } catch (error) {
            this.handleError('Failed to mark item as found');
            console.error('Mark found error:', error);
        }
    }

    async handleOrderClick(orderId) {
        if (!orderId) return;

        try {
            const response = await axios.get(
                `${API_CONFIG.BASE_URL}${API_CONFIG.ENDPOINTS.SEARCH_ORDER}/${orderId}`
            );

            if (response.data) {
                this.currentOrderId = orderId;
                this.displayOrderDetails(response.data);
                await this.loadOrderItems(orderId);
            }
        } catch (error) {
            this.handleError('Failed to load order details');
            console.error('Order click error:', error);
        }
    }

    handleError(message) {
        const errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        errorDiv.textContent = message;
        document.querySelector('.container').prepend(errorDiv);
        setTimeout(() => errorDiv.remove(), 5000);
    }

    handleSuccess(message) {
        const successDiv = document.createElement('div');
        successDiv.className = 'success-message';
        successDiv.textContent = message;
        document.querySelector('.container').prepend(successDiv);
        setTimeout(() => successDiv.remove(), 5000);
    }

    clearOrderDisplay() {
        document.querySelector('.order-details').innerHTML = '';
        document.querySelector('#itemsTable tbody').innerHTML = '';
        this.currentOrderId = null;
    }

    canUpdateStatus() {
        return ['STAFF', 'VOLUNTEER'].includes(this.currentUserRole);
    }

    handleBack() {
        window.location.href = '/dashboard';  // Adjust this path to your dashboard path
    }

    formatDate(dateString) {
        return new Date(dateString).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }
}

// Initialize the delivery manager when the DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.deliveryManager = new DeliveryManager();
});