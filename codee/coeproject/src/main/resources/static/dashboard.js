// ==================== API Configuration ====================

const API_BASE_URL = 'http://localhost:8083/api';

// ==================== Toast Notifications ====================

function showToast(message, type = 'info') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast show ${type}`;
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// ==================== Modal Management ====================

function openModal(modalId) {
    document.getElementById(modalId).classList.remove('hidden');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.add('hidden');
}

// ==================== Tab/Section Navigation ====================

function switchSection(sectionName) {
    // Hide all sections
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });

    // Remove active from all nav tabs
    document.querySelectorAll('.nav-tab').forEach(tab => {
        tab.classList.remove('active');
    });

    // Show selected section
    const section = document.getElementById(`${sectionName}-section`);
    if (section) {
        section.classList.add('active');
    }

    // Mark tab as active
    document.querySelectorAll('.nav-tab').forEach(tab => {
        if (tab.dataset.section === sectionName) {
            tab.classList.add('active');
        }
    });

    // Load data for section
    if (sectionName === 'users') {
        loadUsers();
    } else if (sectionName === 'products') {
        loadProducts();
    } else if (sectionName === 'orders') {
        loadOrders();
        loadOrderStats();
    }
}

// ==================== SERVER STATUS ====================

async function checkServerStatus() {
    try {
        const response = await fetch(`${API_BASE_URL}/users/health`);
        const data = await response.json();

        const statusBadge = document.getElementById('serverStatus');
        if (data.status === 'success') {
            statusBadge.classList.remove('status-loading');
            statusBadge.classList.add('status-connected');
            statusBadge.textContent = '● Server Connected';
        }
    } catch (error) {
        const statusBadge = document.getElementById('serverStatus');
        statusBadge.classList.remove('status-connected');
        statusBadge.textContent = '● Server Disconnected';
    }
}

// ==================== USER MANAGEMENT ====================

async function loadUsers() {
    try {
        const response = await fetch(`${API_BASE_URL}/users`);
        if (!response.ok) throw new Error('Failed to load users');

        const data = await response.json();
        const tbody = document.getElementById('usersBody');

        if (!data.users || data.users.length === 0) {
            tbody.innerHTML = '<tr class="loading-row"><td colspan="7" class="center">No users found</td></tr>';
            return;
        }

        tbody.innerHTML = data.users.map(user => `
            <tr>
                <td>#${user.id}</td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td>${user.phone || '-'}</td>
                <td><span class="status-${user.isActive ? 'active' : 'inactive'}">${user.isActive ? 'Active' : 'Inactive'}</span></td>
                <td>${new Date(user.createdAt).toLocaleDateString()}</td>
                <td>
                    <button class="btn btn-info" onclick="editUser(${user.id})">Edit</button>
                    <button class="btn btn-danger" onclick="deleteUser(${user.id})">Delete</button>
                </td>
            </tr>
        `).join('');

        showToast('Users loaded successfully', 'success');
    } catch (error) {
        console.error('Error loading users:', error);
        document.getElementById('usersBody').innerHTML = `
            <tr class="loading-row">
                <td colspan="7" class="center" style="color: red;">Error loading users. Please try again.</td>
            </tr>
        `;
        showToast('Error loading users', 'error');
    }
}

async function createUser(e) {
    e.preventDefault();

    const name = document.getElementById('userNameInput').value.trim();
    const email = document.getElementById('userEmailInput').value.trim();
    const phone = document.getElementById('userPhoneInput').value.trim();

    if (!name || !email) {
        showToast('Please fill all required fields', 'warning');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/users`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name, email, phone })
        });

        const data = await response.json();
        if (response.ok) {
            showToast('User created successfully', 'success');
            closeModal('userModal');
            document.getElementById('userForm').reset();
            loadUsers();
        } else {
            showToast(data.message || 'Failed to create user', 'error');
        }
    } catch (error) {
        showToast('Error creating user: ' + error.message, 'error');
    }
}

async function editUser(userId) {
    // Implementation for edit user
    showToast('Edit user feature coming soon', 'info');
}

async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}`, { method: 'DELETE' });

        if (response.ok) {
            showToast('User deleted successfully', 'success');
            loadUsers();
        } else {
            showToast('Failed to delete user', 'error');
        }
    } catch (error) {
        showToast('Error deleting user: ' + error.message, 'error');
    }
}

// ==================== PRODUCT MANAGEMENT ====================

async function loadProducts() {
    try {
        const response = await fetch(`${API_BASE_URL}/products`);
        if (!response.ok) throw new Error('Failed to load products');

        const data = await response.json();
        const tbody = document.getElementById('productsBody');

        if (!data.products || data.products.length === 0) {
            tbody.innerHTML = '<tr class="loading-row"><td colspan="8" class="center">No products found</td></tr>';
            return;
        }

        tbody.innerHTML = data.products.map(product => `
            <tr>
                <td>#${product.id}</td>
                <td>${product.name}</td>
                <td>${product.sku}</td>
                <td>$${parseFloat(product.price).toFixed(2)}</td>
                <td>${product.quantity}</td>
                <td>${product.category || 'N/A'}</td>
                <td><span class="status-${product.isActive ? 'active' : 'inactive'}">${product.isActive ? 'Active' : 'Inactive'}</span></td>
                <td>
                    <button class="btn btn-info" onclick="editProduct(${product.id})">Edit</button>
                    <button class="btn btn-danger" onclick="deleteProduct(${product.id})">Delete</button>
                </td>
            </tr>
        `).join('');

        showToast('Products loaded successfully', 'success');
    } catch (error) {
        console.error('Error loading products:', error);
        document.getElementById('productsBody').innerHTML = `
            <tr class="loading-row">
                <td colspan="8" class="center" style="color: red;">Error loading products. Please try again.</td>
            </tr>
        `;
        showToast('Error loading products', 'error');
    }
}

async function createProduct(e) {
    e.preventDefault();

    const name = document.getElementById('productNameInput').value.trim();
    const sku = document.getElementById('productSkuInput').value.trim();
    const price = parseFloat(document.getElementById('productPriceInput').value);
    const quantity = parseInt(document.getElementById('productQtyInput').value);
    const category = document.getElementById('productCategoryInput').value.trim();
    const description = document.getElementById('productDescInput').value.trim();

    if (!name || !sku || !price || !quantity) {
        showToast('Please fill all required fields', 'warning');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/products`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name,
                sku,
                price,
                quantity,
                category,
                description
            })
        });

        const data = await response.json();
        if (response.ok) {
            showToast('Product created successfully', 'success');
            closeModal('productModal');
            document.getElementById('productForm').reset();
            loadProducts();
        } else {
            showToast(data.message || 'Failed to create product', 'error');
        }
    } catch (error) {
        showToast('Error creating product: ' + error.message, 'error');
    }
}

async function editProduct(productId) {
    showToast('Edit product feature coming soon', 'info');
}

async function deleteProduct(productId) {
    if (!confirm('Are you sure you want to delete this product?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/products/${productId}`, { method: 'DELETE' });

        if (response.ok) {
            showToast('Product deleted successfully', 'success');
            loadProducts();
        } else {
            showToast('Failed to delete product', 'error');
        }
    } catch (error) {
        showToast('Error deleting product: ' + error.message, 'error');
    }
}

// ==================== ORDER MANAGEMENT ====================

async function loadOrders() {
    try {
        const response = await fetch(`${API_BASE_URL}/orders`);
        if (!response.ok) throw new Error('Failed to load orders');

        const data = await response.json();
        const tbody = document.getElementById('ordersBody');

        if (!data.orders || data.orders.length === 0) {
            tbody.innerHTML = '<tr class="loading-row"><td colspan="8" class="center">No orders found</td></tr>';
            return;
        }

        tbody.innerHTML = data.orders.map(order => `
            <tr>
                <td><strong>${order.orderNumber}</strong></td>
                <td>User #${order.userId}</td>
                <td>Product #${order.productId}</td>
                <td>${order.quantity}</td>
                <td>$${parseFloat(order.totalAmount).toFixed(2)}</td>
                <td><span class="status-${order.status.toLowerCase()}">${order.status}</span></td>
                <td>${new Date(order.createdAt).toLocaleDateString()}</td>
                <td>
                    <button class="btn btn-info" onclick="viewOrder(${order.id})">View</button>
                    <button class="btn btn-danger" onclick="deleteOrder(${order.id})">Delete</button>
                </td>
            </tr>
        `).join('');

        showToast('Orders loaded successfully', 'success');
    } catch (error) {
        console.error('Error loading orders:', error);
        document.getElementById('ordersBody').innerHTML = `
            <tr class="loading-row">
                <td colspan="8" class="center" style="color: red;">Error loading orders. Please try again.</td>
            </tr>
        `;
        showToast('Error loading orders', 'error');
    }
}

async function loadOrderStats() {
    try {
        const response = await fetch(`${API_BASE_URL}/orders`);
        if (!response.ok) return;

        const data = await response.json();
        if (!data.orders) return;

        const orders = data.orders;
        const total = orders.length;
        const pending = orders.filter(o => o.status === 'PENDING').length;
        const shipped = orders.filter(o => o.status === 'SHIPPED').length;
        const delivered = orders.filter(o => o.status === 'DELIVERED').length;

        document.getElementById('totalOrders').textContent = total;
        document.getElementById('pendingOrders').textContent = pending;
        document.getElementById('shippedOrders').textContent = shipped;
        document.getElementById('deliveredOrders').textContent = delivered;
    } catch (error) {
        console.error('Error loading order stats:', error);
    }
}

async function createOrder(e) {
    e.preventDefault();

    const userId = parseInt(document.getElementById('orderUserInput').value);
    const productId = parseInt(document.getElementById('orderProductInput').value);
    const quantity = parseInt(document.getElementById('orderQtyInput').value);
    const status = document.getElementById('orderStatusInput').value;
    const shippingAddress = document.getElementById('orderShippingInput').value.trim();

    if (!userId || !productId || !quantity) {
        showToast('Please fill all required fields', 'warning');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/orders`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                userId,
                productId,
                quantity,
                status,
                shippingAddress
            })
        });

        const data = await response.json();
        if (response.ok) {
            showToast('Order created successfully', 'success');
            closeModal('orderModal');
            document.getElementById('orderForm').reset();
            loadOrders();
            loadOrderStats();
        } else {
            showToast(data.message || 'Failed to create order', 'error');
        }
    } catch (error) {
        showToast('Error creating order: ' + error.message, 'error');
    }
}

async function viewOrder(orderId) {
    showToast('View order feature coming soon', 'info');
}

async function deleteOrder(orderId) {
    if (!confirm('Are you sure you want to delete this order?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/orders/${orderId}`, { method: 'DELETE' });

        if (response.ok) {
            showToast('Order deleted successfully', 'success');
            loadOrders();
            loadOrderStats();
        } else {
            showToast('Failed to delete order', 'error');
        }
    } catch (error) {
        showToast('Error deleting order: ' + error.message, 'error');
    }
}

// ==================== DROPDOWN POPULATION ====================

async function populateUserDropdown() {
    try {
        const response = await fetch(`${API_BASE_URL}/users`);
        const data = await response.json();

        if (data.users) {
            const select = document.getElementById('orderUserInput');
            select.innerHTML = data.users.map(u =>
                `<option value="${u.id}">User #${u.id} - ${u.name}</option>`
            ).join('');
        }
    } catch (error) {
        console.error('Error populating user dropdown:', error);
    }
}

async function populateProductDropdown() {
    try {
        const response = await fetch(`${API_BASE_URL}/products`);
        const data = await response.json();

        if (data.products) {
            const select = document.getElementById('orderProductInput');
            select.innerHTML = data.products.map(p =>
                `<option value="${p.id}">Product #${p.id} - ${p.name}</option>`
            ).join('');
        }
    } catch (error) {
        console.error('Error populating product dropdown:', error);
    }
}

// ==================== SEARCH & FILTER ====================

document.addEventListener('DOMContentLoaded', () => {
    // Search users
    const userSearch = document.getElementById('userSearch');
    if (userSearch) {
        userSearch.addEventListener('input', (e) => {
            const filter = e.target.value.toLowerCase();
            const rows = document.querySelectorAll('#usersTable tbody tr');
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(filter) ? '' : 'none';
            });
        });
    }

    // Search products
    const productSearch = document.getElementById('productSearch');
    if (productSearch) {
        productSearch.addEventListener('input', (e) => {
            const filter = e.target.value.toLowerCase();
            const rows = document.querySelectorAll('#productsTable tbody tr');
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(filter) ? '' : 'none';
            });
        });
    }

    // Filter orders by status
    const statusFilter = document.getElementById('orderStatusFilter');
    if (statusFilter) {
        statusFilter.addEventListener('change', (e) => {
            const filter = e.target.value.toUpperCase();
            const rows = document.querySelectorAll('#ordersTable tbody tr');
            rows.forEach(row => {
                if (!filter) {
                    row.style.display = '';
                } else {
                    const statusCell = row.cells[5].textContent;
                    row.style.display = statusCell.includes(filter) ? '' : 'none';
                }
            });
        });
    }
});

// ==================== EVENT LISTENERS ====================

document.addEventListener('DOMContentLoaded', () => {
    // Initialize
    checkServerStatus();
    loadUsers();
    populateUserDropdown();
    populateProductDropdown();

    // Navigation
    document.querySelectorAll('.nav-tab').forEach(tab => {
        tab.addEventListener('click', (e) => {
            const section = e.target.closest('.nav-tab').dataset.section;
            switchSection(section);
        });
    });

    // Add buttons
    document.getElementById('addUserBtn').onclick = () => openModal('userModal');
    document.getElementById('addProductBtn').onclick = () => openModal('productModal');
    document.getElementById('addOrderBtn').onclick = () => openModal('orderModal');

    // Forms
    document.getElementById('userForm').onsubmit = createUser;
    document.getElementById('productForm').onsubmit = createProduct;
    document.getElementById('orderForm').onsubmit = createOrder;

    // Refresh button
    document.getElementById('refreshBtn').onclick = () => {
        const activeSection = document.querySelector('.content-section.active');
        if (activeSection.id === 'users-section') loadUsers();
        else if (activeSection.id === 'products-section') loadProducts();
        else if (activeSection.id === 'orders-section') {
            loadOrders();
            loadOrderStats();
        }
        showToast('Data refreshed', 'success');
    };

    // Auto-refresh every 30 seconds
    setInterval(() => {
        const activeSection = document.querySelector('.content-section.active');
        if (activeSection.id === 'users-section') loadUsers();
        else if (activeSection.id === 'products-section') loadProducts();
        else if (activeSection.id === 'orders-section') {
            loadOrders();
            loadOrderStats();
        }
    }, 30000);

    // Server status check every 10 seconds
    setInterval(checkServerStatus, 10000);

    // Close modals on Escape key
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            document.querySelectorAll('.modal').forEach(modal => {
                modal.classList.add('hidden');
            });
        }
    });
});
