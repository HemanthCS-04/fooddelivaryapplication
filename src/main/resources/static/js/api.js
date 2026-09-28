/**
 * HungerByte - Centralized Fetch API Client & Global Helpers
 * Pure JavaScript using Fetch API (No jQuery, No React, No Angular)
 */

const API_BASE = '/api';

// Format currency as Indian Rupees (₹)
function formatINR(amount) {
  if (amount === undefined || amount === null) return '₹0';
  const num = Number(amount);
  return '₹' + num.toLocaleString('en-IN', {
    maximumFractionDigits: 0
  });
}

// Fallback image helper
function handleImageError(img) {
  img.onerror = null;
  img.src = 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&auto=format&fit=crop&q=80';
}

// Current User Management via localStorage
const AuthManager = {
  getUser() {
    try {
      const data = localStorage.getItem('hb_user');
      return data ? JSON.parse(data) : null;
    } catch (e) {
      return null;
    }
  },

  setUser(user) {
    localStorage.setItem('hb_user', JSON.stringify(user));
    if (user && user.token) {
      localStorage.setItem('hb_token', user.token);
    }
  },

  isLoggedIn() {
    return this.getUser() !== null;
  },

  logout() {
    localStorage.removeItem('hb_user');
    localStorage.removeItem('hb_token');
    window.location.href = '/login.html';
  },

  getUserId() {
    const user = this.getUser();
    return user ? user.id : 3; // Default to sample customer if not logged in
  },

  getUserRole() {
    const user = this.getUser();
    return user ? user.role : 'CUSTOMER';
  }
};

// Generic Fetch API wrapper with JSON error parsing
async function apiRequest(endpoint, options = {}) {
  const url = endpoint.startsWith('http') ? endpoint : `${API_BASE}${endpoint}`;
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {})
  };

  const token = localStorage.getItem('hb_token');
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  try {
    const response = await fetch(url, {
      ...options,
      headers
    });

    const data = await response.json();

    if (!response.ok) {
      const errorMsg = data.message || `Request failed with status ${response.status}`;
      throw new Error(errorMsg);
    }

    return data;
  } catch (error) {
    console.error(`API Error on [${endpoint}]:`, error);
    throw error;
  }
}

// Simple Toast Notification helper
function showToast(message, type = 'success') {
  let container = document.getElementById('hb-toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'hb-toast-container';
    container.style.position = 'fixed';
    container.style.bottom = '24px';
    container.style.right = '24px';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  const bgColor = type === 'success' ? '#10b981' : type === 'error' ? '#ef4444' : '#ff5200';
  toast.style.background = bgColor;
  toast.style.color = '#ffffff';
  toast.style.padding = '12px 20px';
  toast.style.borderRadius = '10px';
  toast.style.marginTop = '10px';
  toast.style.boxShadow = '0 10px 25px rgba(0,0,0,0.15)';
  toast.style.fontWeight = '600';
  toast.style.fontSize = '0.95rem';
  toast.style.display = 'flex';
  toast.style.alignItems = 'center';
  toast.style.gap = '8px';
  toast.style.transition = 'all 0.3s ease';

  toast.innerHTML = `<span>${type === 'success' ? '✓' : type === 'error' ? '✕' : 'ℹ'}</span> <span>${message}</span>`;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateY(10px)';
    setTimeout(() => toast.remove(), 300);
  }, 3500);
}

// Update Cart Count in Navbar
async function updateNavCartBadge() {
  const badges = document.querySelectorAll('.hb-cart-count');
  if (!badges || badges.length === 0) return;

  try {
    const userId = AuthManager.getUserId();
    const res = await apiRequest(`/cart?userId=${userId}`);
    if (res && res.data && res.data.items) {
      const totalQty = res.data.items.reduce((sum, item) => sum + item.quantity, 0);
      badges.forEach(b => {
        b.textContent = totalQty;
        b.style.display = totalQty > 0 ? 'inline-block' : 'none';
      });
    }
  } catch (e) {
    console.warn('Could not load cart badge:', e);
  }
}

// Render dynamic navbar user actions
function initNavbarAuth() {
  const authContainer = document.getElementById('navbar-auth-section');
  if (!authContainer) return;

  const user = AuthManager.getUser();
  if (user) {
    let dashboardLink = '';
    if (user.role === 'ADMIN') {
      dashboardLink = `<li><a class="dropdown-item fw-bold text-danger" href="/admin-dashboard.html">🛡️ Admin Dashboard</a></li>`;
    } else if (user.role === 'RESTAURANT_OWNER') {
      dashboardLink = `<li><a class="dropdown-item fw-bold text-primary" href="/restaurant-owner-dashboard.html">🏪 Owner Portal</a></li>`;
    }

    authContainer.innerHTML = `
      <div class="dropdown">
        <button class="btn btn-outline-dark dropdown-toggle d-flex align-items-center gap-2 rounded-3 py-2 px-3" type="button" data-bs-toggle="dropdown">
          <span class="rounded-circle bg-warning text-dark fw-bold d-inline-flex align-items-center justify-content-center" style="width:28px;height:28px;font-size:0.85rem">
            ${user.name.charAt(0).toUpperCase()}
          </span>
          <span class="fw-semibold">${user.name}</span>
          <span class="badge bg-light text-dark border ms-1">${user.role}</span>
        </button>
        <ul class="dropdown-menu dropdown-menu-end shadow-sm border-0 mt-2 rounded-3">
          ${dashboardLink}
          <li><a class="dropdown-item" href="/profile.html">👤 My Profile</a></li>
          <li><a class="dropdown-item" href="/orders.html">📦 My Orders</a></li>
          <li><a class="dropdown-item" href="/favorites.html">❤️ Favorites</a></li>
          <li><hr class="dropdown-divider"></li>
          <li><button class="dropdown-item text-danger fw-semibold" onclick="AuthManager.logout()">🚪 Logout</button></li>
        </ul>
      </div>
    `;
  } else {
    authContainer.innerHTML = `
      <a href="/login.html" class="btn btn-link text-decoration-none text-dark fw-semibold me-2">Login</a>
      <a href="/register.html" class="btn btn-hb-primary">Sign Up</a>
    `;
  }
}

// Ensure dynamic favicon across all pages
function ensureFavicon() {
  let iconLink = document.querySelector("link[rel*='icon']");
  if (!iconLink) {
    iconLink = document.createElement('link');
    iconLink.type = 'image/svg+xml';
    iconLink.rel = 'icon';
    iconLink.href = '/favicon.svg';
    document.head.appendChild(iconLink);
  }

  // Update brand logo with SVG icon if needed
  const brandLogos = document.querySelectorAll('.navbar-brand-logo');
  brandLogos.forEach(brand => {
    const emoji = brand.querySelector('span.fs-3');
    if (emoji && !brand.querySelector('img')) {
      emoji.outerHTML = '<img src="/favicon.svg" alt="HungerByte Logo" width="32" height="32" class="me-2" style="border-radius: 8px; vertical-align: middle;">';
    }
  });
}

document.addEventListener('DOMContentLoaded', () => {
  ensureFavicon();
  initNavbarAuth();
  updateNavCartBadge();
});
