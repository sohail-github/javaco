// API Configuration
const API_BASE_URL = 'http://localhost:8083/api';

// Toast Notification
function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast show ${type}`;
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Tab Navigation
function switchTab(tabName) {
    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });

    // Remove active class from buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });

    // Show selected tab
    const selectedTab = document.getElementById(tabName + '-tab');
    if (selectedTab) {
        selectedTab.classList.add('active');
    }

    // Mark button as active
    event.target.classList.add('active');

    // Load data for the selected tab
    if (tabName === 'users') {
        loadUsers();
    } else if (tabName === 'products') {
        loadProducts();
    } else if (tabName === 'orders') {
        loadOrders();
        loadOrderStatistics();
    }
}

// Toggle Add Form
function toggleAddForm(section) {
    const form = document.getElementById(`add-${section}-form`);
    if (form) {
        form.classList.toggle('hidden');
        if (!form.classList.contains('hidden')) {
            // Clear form fields
            form.querySelectorAll('input, textarea, select').forEach(field => {
                field.value = '';
            });
            form.querySelector('h3').textContent = `Add New ${section.charAt(0).toUpperCase() + section.slice(1).slice(0, -1)}`;
        }
    }
}

// ==================== USER MANAGEMENT ====================

// Load Users
async function loadUsers() {
    try {
        showToast('Loading users...', 'info');
        const response = await fetch(`${API_BASE_URL}/users`);
        const data = await response.json();

        if (data.status === 'success' && Array.isArray(data.data)) {
            const tbody = document.querySelector('#users-table tbody');
            tbody.innerHTML = '';

            if (data.data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="6" class="loading">No users found</td></tr>';
                return;
            }

            data.data.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>#${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.phone || '-'}</td>
                    <td><span class="status-badge ${user.isActive ? 'status-active' : 'status-inactive'}">${user.isActive ? 'Active' : 'Inactive'}</span></td>
                    <td>
                        <div class="actions">
                            <button class="btn btn-info" onclick="editUser(${user.id})">Edit</button>
                            <button class="btn btn-danger" onclick="deleteUser(${user.id})">Delete</button>
                        </div>
                    </td>
                `;
                tbody.appendChild(row);
            });
            showToast('Users loaded successfully', 'success');
        } else {
            showToast(data.message || 'Failed to load users', 'error');
        }
    } catch (error) {
        console.error('Error loading users:', error);
        showToast('Error loading users: ' + error.message, 'error');
    }
}

// Create User
async function createUser(e) {
    e.preventDefault();
    const name = document.getElementById('user-name').value;
    const email = document.getElementById('user-email').value;
    const phone = document.getElementById('user-phone').value;

    if (!name || !email) {
        showToast('Please fill required fields', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/users`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, phone })
        });

        const data = await response.json();
        if (data.status === 'success') {
            showToast('User created successfully', 'success');
            toggleAddForm('users');
            loadUsers();
        } else {
            showToast(data.message || 'Failed to create user', 'error');
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

// Update User
async function editUser(userId) {
    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}`);
        const data = await response.json();

        if (data.status === 'success') {
            const user = data.data;
            document.getElementById('user-name').value = user.name;
            document.getElementById('user-email').value = user.email;
            document.getElementById('user-phone').value = user.phone || '';

            const form = document.getElementById('add-users-form');
            form.classList.remove('hidden');
            const heading = form.querySelector('h3');
            heading.textContent = `Edit User`;

            // Replace submit handler
            const submitBtn = form.querySelector('button[type="submit"]');
            submitBtn.onclick = (e) => updateUserSubmit(e, userId);
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

async function updateUserSubmit(e, userId) {
    e.preventDefault();
    const name = document.getElementById('user-name').value;
    const email = document.getElementById('user-email').value;
    const phone = document.getElementById('user-phone').value;

    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, phone })
        });

        const data = await response.json();
        if (data.status === 'success') {
            showToast('User updated successfully', 'success');
            toggleAddForm('users');
            loadUsers();
        } else {
            showToast(data.message || 'Failed to update user', 'error');
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

// Delete User
async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}`, {
            method: 'DELETE'
        });

        const data = await response.json();
        if (data.status === 'success') {
            showToast('User deleted successfully', 'success');
            loadUsers();
        } else {
            showToast(data.message || 'Failed to delete user', 'error');
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

// ==================== PRODUCT MANAGEMENT ====================

// Load Products
async function loadProducts() {
    try {
        showToast('Loading products...', 'info');
        const response = await fetch(`${API_BASE_URL}/products`);
        const data = await response.json();

        if (data.status === 'success' && Array.isArray(data.data)) {
            const tbody = document.querySelector('#products-table tbody');
            tbody.innerHTML = '';

            if (data.data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="8" class="loading">No products found</td></tr>';
                return;
            }

            data.data.forEach(product => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>#${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.sku}</td>
                    <td>$${parseFloat(product.price).toFixed(2)}</td>
                    <td>${product.quantity}</td>
                    <td>${product.category}</td>
                    <td><span class="status-badge ${product.isActive ? 'status-active' : 'status-inactive'}">${product.isActive ? 'Active' : 'Inactive'}</span></td>
                    <td>
                        <div class="actions">
                            <button class="btn btn-info" onclick="editProduct(${product.id})">Edit</button>
                            <button class="btn btn-danger" onclick="deleteProduct(${product.id})">Delete</button>
                        </div>
                    </td>
                `;
                tbody.appendChild(row);
            });
            showToast('Products loaded successfully', 'success');
        } else {
            showToast(data.message || 'Failed to load products', 'error');
        }
    } catch (error) {
        console.error('Error loading products:', error);
        showToast('Error loading products: ' + error.message, 'error');
    }
}

// Create Product
async function createProduct(e) {
    e.preventDefault();
    const name = document.getElementById('product-name').value;
    const sku = document.getElementById('product-sku').value;
    const price = document.getElementById('product-price').value;
    const quantity = document.getElementById('product-quantity').value;
    const category = document.getElementById('product-category').value;

    if (!name || !sku || !price || !quantity) {
        showToast('Please fill all required fields', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/products`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name,
                sku,
                price: parseFloat(price),
                quantity: parseInt(quantity),
                category,
                isActive: true
            })
        });

        const data = await response.json();
        if (data.status === 'success') {
            showToast('Product created successfully', 'success');
            toggleAddForm('products');
            loadProducts();
        } else {
            showToast(data.message || 'Failed to create product', 'error');
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

// Edit Product
async function editProduct(productId) {
    try {
        const response = await fetch(`${API_BASE_URL}/products/${productId}`);
        const data = await response.json();

        if (data.status === 'success') {
            const product = data.data;
            document.getElementById('product-name').value = product.name;
            document.getElementById('product-sku').value = product.sku;
            document.getElementById('product-price').value = product.price;
            document.getElementById('product-quantity').value = product.quantity;
            document.getElementById('product-category').value = product.category;

            const form = document.getElementById('add-products-form');
            form.classList.remove('hidden');
            const heading = form.querySelector('h3');
            heading.textContent = `Edit Product`;

            const submitBtn = form.querySelector('button[type="submit"]');
            submitBtn.onclick = (e) => updateProductSubmit(e, productId);
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

async function updateProductSubmit(e, productId) {
    e.preventDefault();
    const name = document.getElementById('product-name').value;
    const sku = document.getElementById('product-sku').value;
    const price = document.getElementById('product-price').value;
    const quantity = document.getElementById('product-quantity').value;
    const category = document.getElementById('product-category').value;

    try {
        const response = await fetch(`${API_BASE_URL}/products/${productId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name,
                sku,
                price: parseFloat(price),
                quantity: parseInt(quantity),
                category
            })
        });

        const data = await response.json();
        if (data.status === 'success') {
            showToast('Product updated successfully', 'success');
            toggleAddForm('products');
            loadProducts();
        } else {
            showToast(data.message || 'Failed to update product', 'error');
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

// Delete Product
async function deleteProduct(productId) {
    if (!confirm('Are you sure you want to delete this product?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/products/${productId}`, {
            method: 'DELETE'
        });

        const data = await response.json();
        if (data.status === 'success') {
            showToast('Product deleted successfully', 'success');
            loadProducts();
        } else {
            showToast(data.message || 'Failed to delete product', 'error');
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

// ==================== ORDER MANAGEMENT ====================

// Load Orders
async function loadOrders() {
    try {
        showToast('Loading orders...', 'info');
        const response = await fetch(`${API_BASE_URL}/orders`);
        const data = await response.json();

        if (data.status === 'success' && Array.isArray(data.data)) {
            const tbody = document.querySelector('#orders-table tbody');
            tbody.innerHTML = '';

            if (data.data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="8" class="loading">No orders found</td></tr>';
                return;
            }

            data.data.forEach(order => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${order.orderNumber}</td>
                    <td>User #${order.userId}</td>
                    <td>Product #${order.productId}</td>
                    <td>${order.quantity}</td>
                    <td>$${parseFloat(order.totalAmount).toFixed(2)}</td>
                    <td><span class="status-badge ${order.status}">${order.status}</span></td>
                    <td>${new Date(order.createdAt).toLocaleDateString()}</td>
                    <td>
                        <div class="actions">
                            <button class="btn btn-edit" onclick="viewOrder(${order.id})">View</button>
                            ${order.status !== 'DELIVERED' && order.status !== 'CANCELLED' ? `
                                <select class="btn btn-secondary" onchange="updateOrderStatus(${order.id}, this.value)">
                                    <option value="">Change Status</option>
                                    ${getValidStatusTransitions(order.status).map(status => `<option value="${status}">${status}</option>`).join('')}
                                </select>
                            ` : ''}
                            ${order.status === 'PENDING' || order.status === 'CONFIRMED' ? `
                                <button class="btn btn-danger" onclick="cancelOrder(${order.id})">Cancel</button>
                            ` : ''}
                        </div>
                    </td>
                `;
                tbody.appendChild(row);
            });
            showToast('Orders loaded successfully', 'success');
        } else {
            showToast(data.message || 'Failed to load orders', 'error');
        }
    } catch (error) {
        console.error('Error loading orders:', error);
        showToast('Error loading orders: ' + error.message, 'error');
    }
}

// Valid Status Transitions
function getValidStatusTransitions(currentStatus) {
    const transitions = {
        'PENDING': ['CONFIRMED', 'CANCELLED'],
        'CONFIRMED': ['SHIPPED', 'CANCELLED'],
        'SHIPPED': ['DELIVERED'],
        'DELIVERED': [],
        'CANCELLED': []
    };
    return transitions[currentStatus] || [];
}

// Create Order
async function createOrder(e) {
    e.preventDefault();
    const userId = document.getElementById('order-user').value;
    const productId = document.getElementById('order-product').value;
    const quantity = document.getElementById('order-quantity').value;

    if (!userId || !productId || !quantity) {
        showToast('Please fill all required fields', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/orders`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                userId: parseInt(userId),
                productId: parseInt(productId),
                quantity: parseInt(quantity)
            })
        });

        const data = await response.json();
        if (data.status === 'success') {
            showToast('Order created successfully', 'success');
            toggleAddForm('orders');
            loadOrders();
            loadOrderStatistics();
        } else {
            showToast(data.message || 'Failed to create order', 'error');
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

// Update Order Status
async function updateOrderStatus(orderId, newStatus) {
    if (!newStatus) return;

    try {
        const response = await fetch(`${API_BASE_URL}/orders/${orderId}/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: newStatus })
        });

        const data = await response.json();
        if (data.status === 'success') {
            showToast('Order status updated successfully', 'success');
            loadOrders();
            loadOrderStatistics();
        } else {
            showToast(data.message || 'Failed to update order', 'error');
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

// Cancel Order
async function cancelOrder(orderId) {
    if (!confirm('Are you sure you want to cancel this order?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/orders/${orderId}/cancel`, {
            method: 'PUT'
        });

        const data = await response.json();
        if (data.status === 'success') {
            showToast('Order cancelled successfully', 'success');
            loadOrders();
            loadOrderStatistics();
        } else {
            showToast(data.message || 'Failed to cancel order', 'error');
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

// View Order Details
async function viewOrder(orderId) {
    try {
        const response = await fetch(`${API_BASE_URL}/orders/${orderId}`);
        const data = await response.json();

        if (data.status === 'success') {
            const order = data.data;
            const modal = document.getElementById('detailsModal');
            const modalBody = document.getElementById('modalBody');

            modalBody.innerHTML = `
                <h3>Order Details</h3>
                <p><strong>Order Number:</strong> ${order.orderNumber}</p>
                <p><strong>User ID:</strong> ${order.userId}</p>
                <p><strong>Product ID:</strong> ${order.productId}</p>
                <p><strong>Quantity:</strong> ${order.quantity}</p>
                <p><strong>Unit Price:</strong> $${parseFloat(order.unitPrice).toFixed(2)}</p>
                <p><strong>Total Amount:</strong> $${parseFloat(order.totalAmount).toFixed(2)}</p>
                <p><strong>Status:</strong> <span class="status-badge ${order.status}">${order.status}</span></p>
                <p><strong>Shipping Address:</strong> ${order.shippingAddress || 'Not specified'}</p>
                <p><strong>Notes:</strong> ${order.notes || 'No notes'}</p>
                <p><strong>Created:</strong> ${new Date(order.createdAt).toLocaleString()}</p>
                <p><strong>Updated:</strong> ${new Date(order.updatedAt).toLocaleString()}</p>
            `;

            modal.classList.remove('hidden');
        }
    } catch (error) {
        showToast('Error: ' + error.message, 'error');
    }
}

// Load Order Statistics
async function loadOrderStatistics() {
    try {
        const response = await fetch(`${API_BASE_URL}/orders/statistics/all`);
        const data = await response.json();

        if (data.status === 'success') {
            const stats = data.data;
            const statsContainer = document.querySelector('.stats-container');

            statsContainer.innerHTML = `
                <div class="stat-card">
                    <h4>Total Orders</h4>
                    <div class="stat-value">${stats.totalOrders || 0}</div>
                </div>
                <div class="stat-card">
                    <h4>Pending</h4>
                    <div class="stat-value">${stats.PENDING || 0}</div>
                </div>
                <div class="stat-card">
                    <h4>Confirmed</h4>
                    <div class="stat-value">${stats.CONFIRMED || 0}</div>
                </div>
                <div class="stat-card">
                    <h4>Shipped</h4>
                    <div class="stat-value">${stats.SHIPPED || 0}</div>
                </div>
                <div class="stat-card">
                    <h4>Delivered</h4>
                    <div class="stat-value">${stats.DELIVERED || 0}</div>
                </div>
                <div class="stat-card">
                    <h4>Cancelled</h4>
                    <div class="stat-value">${stats.CANCELLED || 0}</div>
                </div>
            `;
        }
    } catch (error) {
        console.error('Error loading statistics:', error);
    }
}

// ==================== INITIALIZATION ====================

// Check Server Status
async function checkServerStatus() {
    try {
        const response = await fetch(`${API_BASE_URL}/users/health`);
        const data = await response.json();

        const indicator = document.querySelector('.status-indicator');
        if (data.status === 'success') {
            indicator.classList.add('connected');
            indicator.textContent = '• Server Connected';
        } else {
            indicator.classList.remove('connected');
            indicator.textContent = '• Server Disconnected';
        }
    } catch (error) {
        const indicator = document.querySelector('.status-indicator');
        indicator.classList.remove('connected');
        indicator.textContent = '• Server Disconnected';
    }
}

// Modal Close
function closeModal() {
    document.getElementById('detailsModal').classList.add('hidden');
}

// Initialize on Page Load
document.addEventListener('DOMContentLoaded', () => {
    checkServerStatus();

    // Set up form submission handlers
    const userForm = document.getElementById('add-users-form');
    if (userForm) {
        userForm.querySelector('button[type="submit"]').onclick = createUser;
    }

    const productForm = document.getElementById('add-products-form');
    if (productForm) {
        productForm.querySelector('button[type="submit"]').onclick = createProduct;
    }

    const orderForm = document.getElementById('add-orders-form');
    if (orderForm) {
        orderForm.querySelector('button[type="submit"]').onclick = createOrder;
    }

    // Close modal on background click
    document.getElementById('detailsModal').addEventListener('click', (e) => {
        if (e.target.id === 'detailsModal') {
            closeModal();
        }
    });

    // Load initial data
    switchTab('users');
});

// Refresh data every 30 seconds (optional)
setInterval(() => {
    const activeTab = document.querySelector('.tab-content.active');
    if (activeTab && activeTab.id === 'users-tab') {
        loadUsers();
    } else if (activeTab && activeTab.id === 'products-tab') {
        loadProducts();
    } else if (activeTab && activeTab.id === 'orders-tab') {
        loadOrders();
        loadOrderStatistics();
    }
}, 30000);
