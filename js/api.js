/**
 * HungerByte - Centralized Fetch API Client & Standalone Client-Side Store
 * Works seamlessly with Spring Boot / Node.js backend AND standalone on GitHub Pages!
 */

const API_BASE = '/api';

// Check if running in a static hosting environment without active backend (e.g. GitHub Pages)
const isStaticHost = window.location.hostname.includes('github.io') ||
                     window.location.protocol === 'file:' ||
                     window.location.hostname.includes('localhost') === false && window.location.port === '';

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
    window.location.href = 'login.html';
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

// =========================================================================
// Client-Side Mock Database (Enables 100% full functionality on GitHub Pages)
// =========================================================================
const MockDatabase = {
  getInitialData() {
    const categories = [
      { id: 1, name: 'Biryani', description: 'Aromatic royal rice delicacies cooked with rich spices and herbs', imageUrl: 'https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=500&auto=format&fit=crop&q=80', isActive: true },
      { id: 2, name: 'Pizza', description: 'Authentic crusts topped with artisanal cheese and fresh toppings', imageUrl: 'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&auto=format&fit=crop&q=80', isActive: true },
      { id: 3, name: 'Burger', description: 'Juicy gourmet patties layered with melted cheese and fresh crisp greens', imageUrl: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&auto=format&fit=crop&q=80', isActive: true },
      { id: 4, name: 'Chinese', description: 'Wok-tossed noodles, aromatic fried rice, and savory Asian gravies', imageUrl: 'https://images.unsplash.com/photo-1585032226651-759b368d7246?w=500&auto=format&fit=crop&q=80', isActive: true },
      { id: 5, name: 'South Indian', description: 'Crispy dosas, fluffy idlis, and traditional coastal spiced curries', imageUrl: 'https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?w=500&auto=format&fit=crop&q=80', isActive: true },
      { id: 6, name: 'North Indian', description: 'Rich creamy paneer, buttery tandoori curries, and warm breads', imageUrl: 'https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=500&auto=format&fit=crop&q=80', isActive: true },
      { id: 7, name: 'Desserts', description: 'Sweet delights, warm gulab jamun, and artisanal ice creams', imageUrl: 'https://images.unsplash.com/photo-1551024709-8f23befc6f87?w=500&auto=format&fit=crop&q=80', isActive: true },
      { id: 8, name: 'Beverages', description: 'Refreshing mocktails, fresh lime sodas, and chilled beverages', imageUrl: 'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=500&auto=format&fit=crop&q=80', isActive: true }
    ];

    const restaurants = [
      {
        id: 1,
        name: 'Spice Hub',
        description: 'Authentic North Indian curries, aromatic tandoor specialties and creamy butter gravies.',
        cuisine: 'North Indian, Mughlai',
        address: '12 Church Street, MG Road, Bangalore',
        phone: '080-25581234',
        imageUrl: 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&auto=format&fit=crop&q=80',
        rating: 4.6,
        totalReviews: 142,
        deliveryTimeMins: 35,
        costForTwo: 450,
        isActive: true
      },
      {
        id: 2,
        name: 'Bangalore Biryani House',
        description: 'Legendary dum biryanis slow-cooked with fragrant seeraga samba & long grain basmati.',
        cuisine: 'Biryani, South Indian',
        address: '88 Koramangala 5th Block, Bangalore',
        phone: '080-41235678',
        imageUrl: 'https://images.unsplash.com/photo-1552611052-33e04de081de?w=800&auto=format&fit=crop&q=80',
        rating: 4.8,
        totalReviews: 380,
        deliveryTimeMins: 25,
        costForTwo: 400,
        isActive: true
      },
      {
        id: 3,
        name: 'Pizza Palace',
        description: 'Hand-tossed wood-fired pizzas, cheesy garlic breads, and gourmet Italian pastas.',
        cuisine: 'Pizza, Italian, Fast Food',
        address: '204 100ft Road, Indiranagar, Bangalore',
        phone: '080-49876543',
        imageUrl: 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&auto=format&fit=crop&q=80',
        rating: 4.5,
        totalReviews: 215,
        deliveryTimeMins: 30,
        costForTwo: 500,
        isActive: true
      },
      {
        id: 4,
        name: 'South Indian Kitchen',
        description: 'Traditional breakfast, golden ghee roast dosas, steamed idlis, and filter coffee.',
        cuisine: 'South Indian, Pure Veg',
        address: '14 Malleshwaram 8th Cross, Bangalore',
        phone: '080-23349988',
        imageUrl: 'https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=800&auto=format&fit=crop&q=80',
        rating: 4.7,
        totalReviews: 490,
        deliveryTimeMins: 20,
        costForTwo: 200,
        isActive: true
      },
      {
        id: 5,
        name: 'Burger Town',
        description: 'Stacked gourmet burgers with artisan buns, house secret sauces, and crispy golden fries.',
        cuisine: 'Burger, American, Fast Food',
        address: '55 Brigade Road, Bangalore',
        phone: '080-43217890',
        imageUrl: 'https://images.unsplash.com/photo-1550547660-d9450f859349?w=800&auto=format&fit=crop&q=80',
        rating: 4.4,
        totalReviews: 180,
        deliveryTimeMins: 25,
        costForTwo: 350,
        isActive: true
      },
      {
        id: 6,
        name: 'Chinese Wok',
        description: 'Sizzling wok bowls, Hakka noodles, crispy Manchurian, and spicy Schezwan specialties.',
        cuisine: 'Chinese, Asian',
        address: '77 HSR Layout Sector 2, Bangalore',
        phone: '080-67891234',
        imageUrl: 'https://images.unsplash.com/photo-1540420773420-3366772f4999?w=800&auto=format&fit=crop&q=80',
        rating: 4.3,
        totalReviews: 160,
        deliveryTimeMins: 30,
        costForTwo: 380,
        isActive: true
      }
    ];

    const foods = [
      {
        id: 1,
        name: 'Chicken Biryani',
        description: 'Fragrant basmati rice dum-cooked with tender chicken pieces, saffron, and aromatic whole spices. Served with creamy raita and spicy salan.',
        price: 240,
        imageUrl: 'https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=600&auto=format&fit=crop&q=80',
        isVeg: false,
        isAvailable: true,
        rating: 4.8,
        totalReviews: 320,
        restaurant: restaurants[1],
        category: categories[0],
        variants: [
          { id: 101, name: 'Regular (Serves 1)', price: 240 },
          { id: 102, name: 'Large (Serves 2)', price: 420 }
        ]
      },
      {
        id: 2,
        name: 'Veg Biryani',
        description: 'Layers of seasoned basmati rice and garden-fresh vegetables slow-cooked in a sealed clay handi with rich spices.',
        price: 180,
        imageUrl: 'https://images.unsplash.com/photo-1642821373181-696a54913e9a?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.5,
        totalReviews: 140,
        restaurant: restaurants[1],
        category: categories[0],
        variants: []
      },
      {
        id: 3,
        name: 'Paneer Butter Masala',
        description: 'Soft cottage cheese cubes simmered in a rich, buttery, velvety tomato and cashew nut gravy infused with kasuri methi.',
        price: 210,
        imageUrl: 'https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.7,
        totalReviews: 195,
        restaurant: restaurants[0],
        category: categories[5],
        variants: []
      },
      {
        id: 4,
        name: 'Masala Dosa',
        description: 'Golden crispy rice crepe smeared with pure spiced butter, stuffed with fragrant tempered potato mash. Served with 3 chutneys and hot sambar.',
        price: 80,
        imageUrl: 'https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.9,
        totalReviews: 450,
        restaurant: restaurants[3],
        category: categories[4],
        variants: []
      },
      {
        id: 5,
        name: 'Idli Vada',
        description: 'Two pillowy steamed idlis paired with a crispy medu vada, served with traditional coconut chutney and lentil sambar.',
        price: 60,
        imageUrl: 'https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.6,
        totalReviews: 210,
        restaurant: restaurants[3],
        category: categories[4],
        variants: []
      },
      {
        id: 6,
        name: 'Veg Pizza',
        description: 'Thin crust base layered with zesty herb tomato sauce, melted mozzarella, bell peppers, sweet corn, olives and fresh mushrooms.',
        price: 299,
        imageUrl: 'https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.6,
        totalReviews: 180,
        restaurant: restaurants[2],
        category: categories[1],
        variants: [
          { id: 201, name: 'Medium (10 inch)', price: 299 },
          { id: 202, name: 'Large (12 inch)', price: 499 }
        ]
      },
      {
        id: 7,
        name: 'Chicken Pizza',
        description: 'Loaded with BBQ grilled chicken chunks, smoky paprika, red onions, jalapeños, and generous stringy mozzarella cheese.',
        price: 399,
        imageUrl: 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=600&auto=format&fit=crop&q=80',
        isVeg: false,
        isAvailable: true,
        rating: 4.7,
        totalReviews: 230,
        restaurant: restaurants[2],
        category: categories[1],
        variants: [
          { id: 301, name: 'Medium (10 inch)', price: 399 },
          { id: 302, name: 'Large (12 inch)', price: 599 }
        ]
      },
      {
        id: 8,
        name: 'Veg Burger',
        description: 'Crispy spiced vegetable patty topped with melted cheddar, fresh tomato slices, crisp iceberg lettuce, and herb mayonnaise.',
        price: 149,
        imageUrl: 'https://images.unsplash.com/photo-1550547660-d9450f859349?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.4,
        totalReviews: 135,
        restaurant: restaurants[4],
        category: categories[2],
        variants: []
      },
      {
        id: 9,
        name: 'Chicken Burger',
        description: 'Tender grilled chicken patty seasoned with garlic herbs, chipotle mayonnaise, pickled gherkins, and cheddar in a brioche bun.',
        price: 199,
        imageUrl: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&auto=format&fit=crop&q=80',
        isVeg: false,
        isAvailable: true,
        rating: 4.6,
        totalReviews: 260,
        restaurant: restaurants[4],
        category: categories[2],
        variants: []
      },
      {
        id: 10,
        name: 'Fried Rice',
        description: 'Classic wok-tossed long grain rice stir-fried with diced bell peppers, carrots, spring onions, and light soy sauce.',
        price: 170,
        imageUrl: 'https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.4,
        totalReviews: 110,
        restaurant: restaurants[5],
        category: categories[3],
        variants: []
      },
      {
        id: 11,
        name: 'Noodles',
        description: 'Hakka style thin wheat noodles tossed on high flame with shredded crunchy vegetables, garlic, and savory Asian sauces.',
        price: 160,
        imageUrl: 'https://images.unsplash.com/photo-1585032226651-759b368d7246?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.5,
        totalReviews: 145,
        restaurant: restaurants[5],
        category: categories[3],
        variants: []
      },
      {
        id: 12,
        name: 'Gulab Jamun',
        description: 'Warm, melt-in-mouth milk solids dumplings soaked in fragrant cardamom and saffron infused sugar syrup. 2 pieces.',
        price: 70,
        imageUrl: 'https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.9,
        totalReviews: 310,
        restaurant: restaurants[0],
        category: categories[6],
        variants: []
      },
      {
        id: 13,
        name: 'Ice Cream',
        description: 'Two scoops of rich artisanal Madagascar vanilla ice cream topped with dark chocolate fudge sauce and crunchy roasted almonds.',
        price: 90,
        imageUrl: 'https://images.unsplash.com/photo-1501443762994-82bd5dace89a?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.7,
        totalReviews: 220,
        restaurant: restaurants[4],
        category: categories[6],
        variants: []
      },
      {
        id: 14,
        name: 'Fresh Lime Soda',
        description: 'Refreshing freshly squeezed lime juice blended with chilled sparkling soda, mint leaves, and rock salt.',
        price: 50,
        imageUrl: 'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=600&auto=format&fit=crop&q=80',
        isVeg: true,
        isAvailable: true,
        rating: 4.6,
        totalReviews: 180,
        restaurant: restaurants[1],
        category: categories[7],
        variants: []
      }
    ];

    const coupons = [
      { id: 1, code: 'HUNGER50', description: 'Get ₹50 flat off on all orders above ₹299', discountType: 'FLAT', discountValue: 50, minOrderAmount: 299, isActive: true },
      { id: 2, code: 'FEAST100', description: 'Get ₹100 flat off on grand feast orders above ₹499', discountType: 'FLAT', discountValue: 100, minOrderAmount: 499, isActive: true },
      { id: 3, code: 'WELCOME20', description: '20% discount up to ₹150 for your first delicious meal', discountType: 'PERCENTAGE', discountValue: 20, minOrderAmount: 199, maxDiscountAmount: 150, isActive: true }
    ];

    const users = [
      { id: 1, name: 'System Admin', email: 'admin@hungerbyte.com', role: 'ADMIN', mobile: '9876543210' },
      { id: 2, name: 'Rahul Sharma (Owner)', email: 'owner@hungerbyte.com', role: 'RESTAURANT_OWNER', mobile: '9876543211' },
      { id: 3, name: 'Priya Patel', email: 'customer@hungerbyte.com', role: 'CUSTOMER', mobile: '9876543212' }
    ];

    const carts = {
      3: {
        id: 3,
        items: [
          { id: 1, foodItem: foods[0], quantity: 1, price: 240, variant: null },
          { id: 2, foodItem: foods[13], quantity: 2, price: 50, variant: null }
        ]
      }
    };

    const orders = [
      {
        id: 1,
        orderNumber: 'HB1727481234',
        user: users[2],
        restaurant: restaurants[1],
        orderStatus: 'DELIVERED',
        subtotal: 340,
        deliveryFee: 40,
        tax: 17,
        discount: 50,
        totalAmount: 347,
        deliveryAddress: '42, 4th Cross, Indiranagar, Bangalore, Karnataka - 560038',
        createdAt: new Date(Date.now() - 3600000 * 24).toISOString(),
        orderItems: [
          { id: 1, foodName: 'Chicken Biryani', quantity: 1, price: 240 },
          { id: 2, foodName: 'Fresh Lime Soda', quantity: 2, price: 50 }
        ],
        payment: {
          paymentMethod: 'UPI',
          paymentStatus: 'COMPLETED',
          transactionId: 'TXN-7A9B3C'
        }
      }
    ];

    const reviews = [
      { id: 1, user: users[2], restaurantId: 2, foodItemId: 1, rating: 5, comment: 'Absolutely authentic seeraga samba dum biryani! So succulent.', createdAt: new Date().toISOString() }
    ];

    const favorites = [
      { id: 1, userId: 3, restaurant: restaurants[1] },
      { id: 2, userId: 3, foodItem: foods[0] }
    ];

    const addresses = [
      { id: 1, userId: 3, street: '42, 4th Cross, Indiranagar', city: 'Bangalore', state: 'Karnataka', pincode: '560038', addressType: 'HOME', isDefault: true }
    ];

    return { categories, restaurants, foods, coupons, users, carts, orders, reviews, favorites, addresses };
  },

  getData() {
    try {
      const stored = localStorage.getItem('hb_mock_db');
      if (stored) return JSON.parse(stored);
    } catch (e) {}
    const initial = this.getInitialData();
    this.saveData(initial);
    return initial;
  },

  saveData(db) {
    try {
      localStorage.setItem('hb_mock_db', JSON.stringify(db));
    } catch (e) {}
  },

  handleRequest(endpoint, options = {}) {
    const db = this.getData();
    const method = (options.method || 'GET').toUpperCase();
    const [pathPart, queryPart] = endpoint.split('?');
    const params = new URLSearchParams(queryPart || '');
    let body = {};
    if (options.body) {
      try { body = typeof options.body === 'string' ? JSON.parse(options.body) : options.body; } catch (e) {}
    }

    // Categories
    if (pathPart === '/categories') {
      return { success: true, data: db.categories };
    }

    // Restaurants
    if (pathPart === '/restaurants') {
      if (method === 'POST') {
        const newR = {
          id: db.restaurants.length + 1,
          rating: 4.5,
          totalReviews: 1,
          isActive: true,
          ...body
        };
        db.restaurants.push(newR);
        this.saveData(db);
        return { success: true, message: 'Restaurant added', data: newR };
      }
      const search = (params.get('search') || '').toLowerCase();
      let list = db.restaurants.filter(r => r.isActive);
      if (search) {
        list = list.filter(r => r.name.toLowerCase().includes(search) || r.cuisine.toLowerCase().includes(search) || r.address.toLowerCase().includes(search));
      }
      return { success: true, data: list };
    }

    if (pathPart.startsWith('/restaurants/')) {
      const id = parseInt(pathPart.split('/')[2]);
      const r = db.restaurants.find(x => x.id === id);
      if (!r) throw new Error('Restaurant not found');
      return { success: true, data: r };
    }

    // Foods
    if (pathPart === '/foods') {
      if (method === 'POST') {
        const rest = db.restaurants.find(r => r.id === parseInt(body.restaurantId)) || db.restaurants[0];
        const cat = db.categories.find(c => c.id === parseInt(body.categoryId)) || db.categories[0];
        const newF = {
          id: db.foods.length + 1,
          name: body.name,
          description: body.description,
          price: parseFloat(body.price),
          imageUrl: body.imageUrl || 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&auto=format&fit=crop&q=80',
          isVeg: !!body.isVeg,
          isAvailable: true,
          rating: 4.5,
          totalReviews: 1,
          restaurant: rest,
          category: cat,
          variants: []
        };
        db.foods.push(newF);
        this.saveData(db);
        return { success: true, message: 'Food item added', data: newF };
      }
      const restId = params.get('restaurantId') ? parseInt(params.get('restaurantId')) : null;
      const catId = params.get('categoryId') ? parseInt(params.get('categoryId')) : null;
      const search = (params.get('search') || '').toLowerCase();

      let list = db.foods.filter(f => f.isAvailable);
      if (restId) list = list.filter(f => f.restaurant && f.restaurant.id === restId);
      if (catId) list = list.filter(f => f.category && f.category.id === catId);
      if (search) {
        list = list.filter(f => f.name.toLowerCase().includes(search) || f.description.toLowerCase().includes(search));
      }
      return { success: true, data: list };
    }

    if (pathPart.startsWith('/foods/')) {
      const id = parseInt(pathPart.split('/')[2]);
      if (method === 'DELETE') {
        const idx = db.foods.findIndex(f => f.id === id);
        if (idx !== -1) db.foods.splice(idx, 1);
        this.saveData(db);
        return { success: true, message: 'Food item deleted' };
      }
      const f = db.foods.find(x => x.id === id);
      if (!f) throw new Error('Food item not found');
      return { success: true, data: f };
    }

    // Coupons
    if (pathPart === '/coupons') {
      return { success: true, data: db.coupons };
    }

    if (pathPart.startsWith('/coupons/validate/')) {
      const code = pathPart.split('/')[3].toUpperCase();
      const amount = parseFloat(params.get('amount') || '0');
      const cp = db.coupons.find(c => c.code === code && c.isActive);
      if (!cp) throw new Error(`Coupon '${code}' is invalid or expired.`);
      if (cp.minOrderAmount && amount < cp.minOrderAmount) {
        throw new Error(`Minimum order value for ${code} is ₹${cp.minOrderAmount}.`);
      }
      return { success: true, data: cp };
    }

    // Cart
    if (pathPart === '/cart') {
      const userId = parseInt(params.get('userId') || '3');
      if (!db.carts[userId]) db.carts[userId] = { id: userId, items: [] };
      return { success: true, data: db.carts[userId] };
    }

    if (pathPart === '/cart/items') {
      const userId = parseInt(params.get('userId') || '3');
      const { foodItemId, variantId, quantity } = body;
      if (!db.carts[userId]) db.carts[userId] = { id: userId, items: [] };

      const food = db.foods.find(f => f.id === foodItemId);
      if (!food) throw new Error('Food not found');

      let price = food.price;
      let variantObj = null;
      if (variantId && food.variants) {
        variantObj = food.variants.find(v => v.id === variantId);
        if (variantObj) price = variantObj.price;
      }

      const existing = db.carts[userId].items.find(i => i.foodItem.id === foodItemId && (variantId ? i.variant?.id === variantId : true));
      if (existing) {
        existing.quantity += (quantity || 1);
      } else {
        db.carts[userId].items.push({
          id: Date.now(),
          foodItem: food,
          variant: variantObj,
          quantity: quantity || 1,
          price: price
        });
      }
      this.saveData(db);
      return { success: true, message: 'Item added to cart', data: db.carts[userId] };
    }

    if (pathPart.startsWith('/cart/items/')) {
      const userId = parseInt(params.get('userId') || '3');
      const itemId = parseInt(pathPart.split('/')[3]);
      if (method === 'DELETE') {
        if (db.carts[userId]) {
          db.carts[userId].items = db.carts[userId].items.filter(i => i.id !== itemId);
          this.saveData(db);
        }
        return { success: true, message: 'Item removed', data: db.carts[userId] };
      }
      if (method === 'PUT') {
        const qty = parseInt(params.get('quantity') || '1');
        if (db.carts[userId]) {
          if (qty <= 0) {
            db.carts[userId].items = db.carts[userId].items.filter(i => i.id !== itemId);
          } else {
            const item = db.carts[userId].items.find(i => i.id === itemId);
            if (item) item.quantity = qty;
          }
          this.saveData(db);
        }
        return { success: true, message: 'Cart updated', data: db.carts[userId] };
      }
    }

    if (pathPart === '/cart/clear') {
      const userId = parseInt(params.get('userId') || '3');
      if (db.carts[userId]) db.carts[userId].items = [];
      this.saveData(db);
      return { success: true, message: 'Cart cleared' };
    }

    // Orders
    if (pathPart === '/orders') {
      if (method === 'POST') {
        const userId = parseInt(params.get('userId') || '3');
        const { restaurantId, deliveryAddress, paymentMethod, couponCode } = body;
        const user = db.users.find(u => u.id === userId) || db.users[2];
        const userCart = db.carts[userId] || { items: [] };

        if (userCart.items.length === 0) throw new Error('Cart is empty');

        const rest = db.restaurants.find(r => r.id === restaurantId) || (userCart.items[0]?.foodItem?.restaurant) || db.restaurants[0];
        const subtotal = userCart.items.reduce((s, i) => s + (i.price * i.quantity), 0);
        const deliveryFee = 40.0;
        const tax = Math.round((subtotal * 0.05) * 100) / 100;
        let discount = 0;

        if (couponCode) {
          const cp = db.coupons.find(c => c.code === couponCode.toUpperCase());
          if (cp) {
            discount = cp.discountType === 'PERCENTAGE' ? (subtotal * cp.discountValue) / 100 : cp.discountValue;
            discount = Math.min(discount, subtotal);
          }
        }

        const totalAmount = Math.max(0, Math.round((subtotal + deliveryFee + tax - discount) * 100) / 100);

        const newOrder = {
          id: db.orders.length + 1,
          orderNumber: 'HB' + Date.now(),
          user,
          restaurant: rest,
          orderStatus: 'PLACED',
          subtotal,
          deliveryFee,
          tax,
          discount,
          totalAmount,
          deliveryAddress,
          couponCode,
          createdAt: new Date().toISOString(),
          orderItems: userCart.items.map((ci, idx) => ({
            id: idx + 1,
            foodName: ci.foodItem.name,
            variantName: ci.variant?.name || null,
            quantity: ci.quantity,
            price: ci.price
          })),
          payment: {
            paymentMethod: paymentMethod || 'UPI',
            paymentStatus: 'COMPLETED',
            transactionId: 'TXN-' + Math.random().toString(36).substring(2, 9).toUpperCase()
          }
        };

        db.orders.unshift(newOrder);
        db.carts[userId].items = [];
        this.saveData(db);
        return { success: true, message: 'Order placed successfully!', data: newOrder };
      }

      const userId = params.get('userId') ? parseInt(params.get('userId')) : null;
      const restId = params.get('restaurantId') ? parseInt(params.get('restaurantId')) : null;

      let list = db.orders;
      if (userId) list = list.filter(o => o.user && o.user.id === userId);
      if (restId) list = list.filter(o => o.restaurant && o.restaurant.id === restId);
      return { success: true, data: list };
    }

    if (pathPart.startsWith('/orders/')) {
      const parts = pathPart.split('/');
      const id = parseInt(parts[2]);
      const o = db.orders.find(x => x.id === id);
      if (!o) throw new Error('Order not found');

      if (parts[3] === 'status' && method === 'PUT') {
        o.orderStatus = body.status;
        this.saveData(db);
        return { success: true, message: 'Order status updated', data: o };
      }
      return { success: true, data: o };
    }

    // Reviews
    if (pathPart.startsWith('/reviews/food/')) {
      const fId = parseInt(pathPart.split('/')[3]);
      return { success: true, data: db.reviews.filter(r => r.foodItemId === fId) };
    }

    if (pathPart.startsWith('/reviews/restaurant/')) {
      const rId = parseInt(pathPart.split('/')[3]);
      return { success: true, data: db.reviews.filter(r => r.restaurantId === rId) };
    }

    if (pathPart === '/reviews' && method === 'POST') {
      const userId = parseInt(params.get('userId') || '3');
      const user = db.users.find(u => u.id === userId) || db.users[2];
      const newRev = {
        id: db.reviews.length + 1,
        user,
        createdAt: new Date().toISOString(),
        ...body
      };
      db.reviews.unshift(newRev);
      this.saveData(db);
      return { success: true, message: 'Review added', data: newRev };
    }

    // Favorites
    if (pathPart === '/favorites') {
      const userId = parseInt(params.get('userId') || '3');
      if (method === 'POST') {
        const restId = params.get('restaurantId') ? parseInt(params.get('restaurantId')) : null;
        const foodId = params.get('foodItemId') ? parseInt(params.get('foodItemId')) : null;
        const newFav = { id: db.favorites.length + 1, userId };
        if (restId) newFav.restaurant = db.restaurants.find(r => r.id === restId);
        if (foodId) newFav.foodItem = db.foods.find(f => f.id === foodId);
        db.favorites.push(newFav);
        this.saveData(db);
        return { success: true, message: 'Added to favorites', data: newFav };
      }
      return { success: true, data: db.favorites.filter(f => f.userId === userId) };
    }

    if (pathPart.startsWith('/favorites/')) {
      const id = parseInt(pathPart.split('/')[2]);
      const idx = db.favorites.findIndex(f => f.id === id);
      if (idx !== -1) db.favorites.splice(idx, 1);
      this.saveData(db);
      return { success: true, message: 'Removed from favorites' };
    }

    // Auth
    if (pathPart === '/auth/login' && method === 'POST') {
      const { email } = body;
      const user = db.users.find(u => u.email.toLowerCase() === (email || '').toLowerCase());
      if (!user) throw new Error('Invalid email or password');
      return { success: true, message: 'Login successful!', data: { ...user, token: 'HB-TOKEN-' + Date.now() } };
    }

    if (pathPart === '/auth/register' && method === 'POST') {
      const { name, email, mobile, role, address } = body;
      const existing = db.users.find(u => u.email.toLowerCase() === (email || '').toLowerCase());
      if (existing) throw new Error('Email already registered');
      const newUser = { id: db.users.length + 1, name, email, mobile, role: role || 'CUSTOMER' };
      db.users.push(newUser);
      db.carts[newUser.id] = { id: newUser.id, items: [] };
      if (address) {
        db.addresses.push({ id: db.addresses.length + 1, userId: newUser.id, street: address, city: 'Bangalore', state: 'Karnataka', pincode: '560001', addressType: 'HOME' });
      }
      this.saveData(db);
      return { success: true, message: 'Registration successful!', data: { ...newUser, token: 'HB-TOKEN-' + Date.now() } };
    }

    // Admin
    if (pathPart === '/admin/dashboard') {
      const totalRevenue = db.orders.filter(o => o.orderStatus !== 'CANCELLED').reduce((s, o) => s + o.totalAmount, 0);
      const activeOrders = db.orders.filter(o => o.orderStatus !== 'DELIVERED' && o.orderStatus !== 'CANCELLED').length;
      return {
        success: true,
        data: {
          totalUsers: db.users.length,
          totalCustomers: db.users.filter(u => u.role === 'CUSTOMER').length,
          totalRestaurants: db.restaurants.length,
          totalFoodItems: db.foods.length,
          totalOrders: db.orders.length,
          totalRevenue: Math.round(totalRevenue * 100) / 100,
          activeOrders
        }
      };
    }

    if (pathPart === '/admin/users') return { success: true, data: db.users };
    if (pathPart === '/admin/orders') return { success: true, data: db.orders };
    if (pathPart === '/admin/restaurants') return { success: true, data: db.restaurants };

    // Users & Addresses
    if (pathPart.startsWith('/users/')) {
      const parts = pathPart.split('/');
      const id = parseInt(parts[2]);
      const u = db.users.find(x => x.id === id);

      if (parts[3] === 'addresses') {
        if (method === 'POST') {
          const newAddr = { id: db.addresses.length + 1, userId: id, ...body };
          db.addresses.push(newAddr);
          this.saveData(db);
          return { success: true, data: newAddr };
        }
        return { success: true, data: db.addresses.filter(a => a.userId === id) };
      }

      if (!u) throw new Error('User not found');
      if (method === 'PUT') {
        if (body.name) u.name = body.name;
        if (body.mobile) u.mobile = body.mobile;
        this.saveData(db);
        return { success: true, message: 'User updated', data: u };
      }
      return { success: true, data: u };
    }

    return { success: true, data: null };
  }
};

// Generic Fetch API wrapper with JSON error parsing & automatic GitHub Pages fallback
async function apiRequest(endpoint, options = {}) {
  // If hosted on GitHub Pages or static host, route directly through the client-side store
  if (isStaticHost) {
    try {
      return MockDatabase.handleRequest(endpoint, options);
    } catch (err) {
      console.error(`Client store error on [${endpoint}]:`, err);
      throw err;
    }
  }

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

    // Check if the server returned HTML (like a 404 page) instead of JSON
    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
      // Fallback to client-side store if server returns non-JSON (e.g. 404 HTML)
      console.warn(`Non-JSON response from ${url}, falling back to mock database.`);
      return MockDatabase.handleRequest(endpoint, options);
    }

    const data = await response.json();

    if (!response.ok) {
      const errorMsg = data.message || `Request failed with status ${response.status}`;
      throw new Error(errorMsg);
    }

    return data;
  } catch (error) {
    // If network error or server down, fallback to client-side store seamlessly
    console.warn(`Backend unreachable on [${endpoint}], falling back to mock store:`, error);
    try {
      return MockDatabase.handleRequest(endpoint, options);
    } catch (mockError) {
      throw mockError;
    }
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
      dashboardLink = `<li><a class="dropdown-item fw-bold text-danger" href="admin-dashboard.html">🛡️ Admin Dashboard</a></li>`;
    } else if (user.role === 'RESTAURANT_OWNER') {
      dashboardLink = `<li><a class="dropdown-item fw-bold text-primary" href="restaurant-owner-dashboard.html">🏪 Owner Portal</a></li>`;
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
          <li><a class="dropdown-item" href="profile.html">👤 My Profile</a></li>
          <li><a class="dropdown-item" href="orders.html">📦 My Orders</a></li>
          <li><a class="dropdown-item" href="favorites.html">❤️ Favorites</a></li>
          <li><hr class="dropdown-divider"></li>
          <li><button class="dropdown-item text-danger fw-semibold" onclick="AuthManager.logout()">🚪 Logout</button></li>
        </ul>
      </div>
    `;
  } else {
    authContainer.innerHTML = `
      <a href="login.html" class="btn btn-link text-decoration-none text-dark fw-semibold me-2">Login</a>
      <a href="register.html" class="btn btn-hb-primary">Sign Up</a>
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
    iconLink.href = 'favicon.svg';
    document.head.appendChild(iconLink);
  }

  // Update brand logo with SVG icon if needed
  const brandLogos = document.querySelectorAll('.navbar-brand-logo');
  brandLogos.forEach(brand => {
    const emoji = brand.querySelector('span.fs-3');
    if (emoji && !brand.querySelector('img')) {
      emoji.outerHTML = '<img src="favicon.svg" alt="HungerByte Logo" width="32" height="32" class="me-2" style="border-radius: 8px; vertical-align: middle;">';
    }
  });
}

document.addEventListener('DOMContentLoaded', () => {
  ensureFavicon();
  initNavbarAuth();
  updateNavCartBadge();
});
