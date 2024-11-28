class SuperviseManager {
    constructor() {
        this.API_BASE_URL = '/api';
        this.ENDPOINTS = {
            GET_CURRENT_USER: '/user/current',
            CREATE_ORDER: '/orders/create',
            GET_ORDER_STATS: '/orders/statistics',
            SET_SESSION: '/session/set',
            GET_SESSION: '/session/get'
        };
        this.currentUserRole = null;
        this.init();
    }

    async init() {
        try {
            await this.loadUserData();
            await this.checkExistingSession();
            this.setupEventListeners();
        } catch (error) {
            this.showMessage('Failed to initialize page', 'error');
            console.error('Init error:', error);
        }
    }

    setupEventListeners() {
        const newOrderForm = document.getElementById('newOrderForm');
        if (newOrderForm) {
            newOrderForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.handleNewOrder();
            });
        }

        // Add event listeners for enter key in input fields
        const clientUsername = document.getElementById('clientUsername');
        if (clientUsername) {
            clientUsername.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    document.getElementById('orderNotes').focus();
                }
            });
        }
    }

    async loadUserData() {
        try {
            const response = await axios.get(`${this.API_BASE_URL}${this.ENDPOINTS.GET_CURRENT_USER}`);
            const userData = response.data;

            document.getElementById('userName').textContent = `${userData.fname} ${userData.lname}`;
            this.currentUserRole = userData.roleID;

            // Hide new order button if user is not staff
            if (this.currentUserRole !== 'STAFF') {
                const newOrderBtn = document.querySelector('.btn-primary');
                if (newOrderBtn) {
                    newOrderBtn.style.display = 'none';
                }
            }
        } catch (error) {
            this.showMessage('Failed to load user data', 'error');
            throw error;
        }
    }

    async checkExistingSession() {
        try {
            const response = await axios.get(`${this.API_BASE_URL}${this.ENDPOINTS.GET_SESSION}`);
            if (response.data && response.data.orderId) {
                this.showMessage(`Resuming Order #${response.data.orderId}`, 'success');
            }
        } catch (error) {
            console.error('Session check error:', error);
        }
    }

    // UI Control Methods
    showNewOrderForm() {
        if (this.currentUserRole !== 'STAFF') {
            this.showMessage('Only staff members can create orders', 'error');
            return;
        }

        // Hide statistics if showing
        const statsSection = document.getElementById('statisticsSection');
        if (statsSection) {
            statsSection.classList.add('hidden');
        }

        // Show new order form
        const newOrderSection = document.getElementById('newOrderSection');
        if (newOrderSection) {
            newOrderSection.classList.remove('hidden');
            // Focus on first input
            document.getElementById('clientUsername').focus();
        }
    }

    hideNewOrderForm() {
        const form = document.getElementById('newOrderForm');
        if (form) {
            form.reset();
        }

        const section = document.getElementById('newOrderSection');
        if (section) {
            section.classList.add('hidden');
        }
    }

    showStatistics() {
        // Hide new order form if showing
        this.hideNewOrderForm();

        // Show and load statistics
        const statsSection = document.getElementById('statisticsSection');
        if (statsSection) {
            statsSection.classList.remove('hidden');
        }
        this.loadStatistics();
    }

    // Feature #5: Start an order
    async handleNewOrder() {
        if (this.currentUserRole !== 'STAFF') {
            this.showMessage('Only staff members can create orders', 'error');
            return;
        }

        const clientUsername = document.getElementById('clientUsername').value.trim();
        const orderNotes = document.getElementById('orderNotes').value.trim();

        if (!clientUsername) {
            this.showMessage('Client username is required', 'error');
            return;
        }

        const submitButton = document.querySelector('#newOrderForm button[type="submit"]');
        if (submitButton) {
            submitButton.disabled = true;
            submitButton.textContent = 'Creating Order...';
        }

        try {
            const response = await axios.post(`${this.API_BASE_URL}${this.ENDPOINTS.CREATE_ORDER}`, {
                clientUsername,
                orderNotes,
                supervisor: document.getElementById('userName').textContent
            });

            if (response.data && response.data.orderID) {
                // Store order ID in session
                await this.setSessionData({
                    orderId: response.data.orderID,
                    timestamp: new Date().toISOString()
                });

                this.showMessage(`Order #${response.data.orderID} created successfully`, 'success');
                this.hideNewOrderForm();
            }
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Failed to create order';
            this.showMessage(errorMessage, 'error');
            console.error('Create order error:', error);
        } finally {
            if (submitButton) {
                submitButton.disabled = false;
                submitButton.textContent = 'Create Order';
            }
        }
    }

    // Feature #11: Statistics
    async loadStatistics() {
        const statsContainer = document.getElementById('statisticsSection');
        if (!statsContainer) return;

        try {
            statsContainer.classList.add('loading');
            const response = await axios.get(`${this.API_BASE_URL}${this.ENDPOINTS.GET_ORDER_STATS}`);
            this.displayStatistics(response.data);
        } catch (error) {
            this.showMessage('Failed to load statistics', 'error');
            console.error('Statistics error:', error);
        } finally {
            statsContainer.classList.remove('loading');
        }
    }

    displayStatistics(stats) {
        // Year Summary
        const yearStats = document.getElementById('yearStats');
        if (yearStats) {
            yearStats.innerHTML = `
                <p><strong>Total Orders:</strong> ${stats.totalOrders}</p>
                <p><strong>Total Items:</strong> ${stats.totalItems}</p>
                <p><strong>Clients Served:</strong> ${stats.clientsServed}</p>
                <p><strong>Average Items per Order:</strong> ${Number(stats.avgItemsPerOrder).toFixed(2)}</p>
                <p><strong>Total Value:</strong> $${Number(stats.totalValue).toFixed(2)}</p>
            `;
        }

        // Category Summary
        const categoryStats = document.getElementById('categoryStats');
        if (categoryStats && stats.categoryBreakdown) {
            categoryStats.innerHTML = stats.categoryBreakdown
                .sort((a, b) => b.itemCount - a.itemCount)
                .map(cat => `
                    <p>
                        <strong>${cat.category}:</strong> 
                        ${cat.itemCount} items 
                        (${((cat.itemCount / stats.totalItems) * 100).toFixed(1)}%)
                    </p>
                `).join('');
        }
    }

    // Session Management
    async setSessionData(data) {
        try {
            await axios.post(`${this.API_BASE_URL}${this.ENDPOINTS.SET_SESSION}`, data);
        } catch (error) {
            console.error('Session storage error:', error);
        }
    }

    // Message Display
    showMessage(message, type = 'info') {
        const messageContainer = document.getElementById('messageContainer');
        if (!messageContainer) return;

        const messageElement = document.createElement('div');
        messageElement.className = `message ${type}-message`;

        // Create message content
        const messageContent = document.createElement('span');
        messageContent.textContent = message;
        messageElement.appendChild(messageContent);

        // Create close button
        const closeButton = document.createElement('button');
        closeButton.innerHTML = '×';
        closeButton.className = 'message-close';
        closeButton.onclick = () => messageElement.remove();
        messageElement.appendChild(closeButton);

        // Add to container
        messageContainer.appendChild(messageElement);

        // Auto remove after 5 seconds
        setTimeout(() => {
            if (messageElement.parentElement) {
                messageElement.remove();
            }
        }, 5000);
    }

    // Utility Methods
    formatCurrency(amount) {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(amount);
    }

    formatDate(dateString) {
        return new Date(dateString).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }
}

// Initialize the supervise manager when the DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.superviseManager = new SuperviseManager();
});