import express from 'express';
import path from 'path';
import fs from 'fs';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = 3000;

app.use(express.json());

// In-Memory Database Store (mirrors MySQL and DataInitializer.java)
const users = [
  { id: 1, name: 'System Admin', email: 'admin@hungerbyte.com', role: 'ADMIN', mobile: '9876543210' },
  { id: 2, name: 'Rahul Sharma (Owner)', email: 'owner@hungerbyte.com', role: 'RESTAURANT_OWNER', mobile: '9876543211' },
  { id: 3, name: 'Priya Patel', email: 'customer@hungerbyte.com', role: 'CUSTOMER', mobile: '9876543212' }
];

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

// In-Memory User Carts: userId -> Cart
const carts = {
  3: {
    id: 3,
    items: [
      { id: 1, foodItem: foods[0], quantity: 1, price: 240, variant: null },
      { id: 2, foodItem: foods[13], quantity: 2, price: 50, variant: null }
    ]
  }
};

// In-Memory Orders
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

// ==================== REST APIS ====================

// Auth
app.post('/api/auth/register', (req, res) => {
  const { name, email, password, mobile, role, address } = req.body;
  const existing = users.find(u => u.email.toLowerCase() === (email || '').toLowerCase());
  if (existing) {
    return res.status(400).json({ success: false, message: 'Email already registered' });
  }
  const newUser = { id: users.length + 1, name, email, mobile, role: role || 'CUSTOMER' };
  users.push(newUser);
  carts[newUser.id] = { id: newUser.id, items: [] };
  if (address) {
    addresses.push({ id: addresses.length + 1, userId: newUser.id, street: address, city: 'Bangalore', state: 'Karnataka', pincode: '560001', addressType: 'HOME' });
  }
  return res.json({ success: true, message: 'Registration successful!', data: { ...newUser, token: 'HB-TOKEN-' + Date.now() } });
});

app.post('/api/auth/login', (req, res) => {
  const { email, password } = req.body;
  const user = users.find(u => u.email.toLowerCase() === (email || '').toLowerCase());
  if (!user) {
    return res.status(401).json({ success: false, message: 'Invalid email or password' });
  }
  return res.json({ success: true, message: 'Login successful!', data: { ...user, token: 'HB-TOKEN-' + Date.now() } });
});

app.post('/api/auth/logout', (_req, res) => {
  return res.json({ success: true, message: 'Logged out successfully' });
});

// Restaurants
app.get('/api/restaurants', (req, res) => {
  const search = (req.query.search || '').toLowerCase();
  let list = restaurants.filter(r => r.isActive);
  if (search) {
    list = list.filter(r => r.name.toLowerCase().includes(search) || r.cuisine.toLowerCase().includes(search) || r.address.toLowerCase().includes(search));
  }
  return res.json({ success: true, message: 'Restaurants retrieved', data: list });
});

app.get('/api/restaurants/:id', (req, res) => {
  const r = restaurants.find(x => x.id === parseInt(req.params.id));
  if (!r) return res.status(404).json({ success: false, message: 'Restaurant not found' });
  return res.json({ success: true, message: 'Restaurant details', data: r });
});

app.post('/api/restaurants', (req, res) => {
  const newR = {
    id: restaurants.length + 1,
    rating: 4.5,
    totalReviews: 1,
    isActive: true,
    ...req.body
  };
  restaurants.push(newR);
  return res.json({ success: true, message: 'Restaurant added', data: newR });
});

// Foods
app.get('/api/foods', (req, res) => {
  const restId = req.query.restaurantId ? parseInt(req.query.restaurantId) : null;
  const catId = req.query.categoryId ? parseInt(req.query.categoryId) : null;
  const search = (req.query.search || '').toLowerCase();

  let list = foods.filter(f => f.isAvailable);
  if (restId) list = list.filter(f => f.restaurant && f.restaurant.id === restId);
  if (catId) list = list.filter(f => f.category && f.category.id === catId);
  if (search) {
    list = list.filter(f => f.name.toLowerCase().includes(search) || f.description.toLowerCase().includes(search));
  }
  return res.json({ success: true, message: 'Foods retrieved', data: list });
});

app.get('/api/foods/:id', (req, res) => {
  const f = foods.find(x => x.id === parseInt(req.params.id));
  if (!f) return res.status(404).json({ success: false, message: 'Food not found' });
  return res.json({ success: true, message: 'Food details', data: f });
});

app.post('/api/foods', (req, res) => {
  const rest = restaurants.find(r => r.id === req.body.restaurantId) || restaurants[0];
  const cat = categories.find(c => c.id === req.body.categoryId) || categories[0];
  const newF = {
    id: foods.length + 1,
    rating: 4.5,
    totalReviews: 1,
    variants: [],
    restaurant: rest,
    category: cat,
    ...req.body
  };
  foods.push(newF);
  return res.json({ success: true, message: 'Food item created', data: newF });
});

// Categories
app.get('/api/categories', (_req, res) => {
  return res.json({ success: true, message: 'Categories retrieved', data: categories });
});

// Coupons
app.get('/api/coupons', (_req, res) => {
  return res.json({ success: true, message: 'Coupons retrieved', data: coupons });
});

app.get('/api/coupons/validate/:code', (req, res) => {
  const code = (req.params.code || '').toUpperCase();
  const amount = parseFloat(req.query.amount || '0');
  const coupon = coupons.find(c => c.code === code && c.isActive);

  if (!coupon) {
    return res.status(404).json({ success: false, message: `Coupon code '${code}' is invalid or expired.` });
  }
  if (coupon.minOrderAmount && amount < coupon.minOrderAmount) {
    return res.status(400).json({ success: false, message: `Minimum order value for ${code} is ₹${coupon.minOrderAmount}.` });
  }
  return res.json({ success: true, message: 'Coupon is valid!', data: coupon });
});

// Cart
app.get('/api/cart', (req, res) => {
  const userId = parseInt(req.query.userId || '3');
  if (!carts[userId]) carts[userId] = { id: userId, items: [] };
  return res.json({ success: true, message: 'Cart retrieved', data: carts[userId] });
});

app.post('/api/cart/items', (req, res) => {
  const userId = parseInt(req.query.userId || '3');
  const { foodItemId, variantId, quantity } = req.body;
  if (!carts[userId]) carts[userId] = { id: userId, items: [] };

  const food = foods.find(f => f.id === foodItemId);
  if (!food) return res.status(404).json({ success: false, message: 'Food not found' });

  let price = food.price;
  let variantObj = null;
  if (variantId && food.variants) {
    variantObj = food.variants.find(v => v.id === variantId);
    if (variantObj) price = variantObj.price;
  }

  const existing = carts[userId].items.find(i => i.foodItem.id === foodItemId && (variantId ? i.variant?.id === variantId : true));
  if (existing) {
    existing.quantity += (quantity || 1);
  } else {
    carts[userId].items.push({
      id: Date.now(),
      foodItem: food,
      variant: variantObj,
      quantity: quantity || 1,
      price: price
    });
  }

  return res.json({ success: true, message: 'Item added to cart', data: carts[userId] });
});

app.put('/api/cart/items/:id', (req, res) => {
  const userId = parseInt(req.query.userId || '3');
  const itemId = parseInt(req.params.id);
  const qty = parseInt(req.query.quantity || '1');

  if (carts[userId]) {
    if (qty <= 0) {
      carts[userId].items = carts[userId].items.filter(i => i.id !== itemId);
    } else {
      const item = carts[userId].items.find(i => i.id === itemId);
      if (item) item.quantity = qty;
    }
  }
  return res.json({ success: true, message: 'Cart updated', data: carts[userId] });
});

app.delete('/api/cart/items/:id', (req, res) => {
  const userId = parseInt(req.query.userId || '3');
  const itemId = parseInt(req.params.id);
  if (carts[userId]) {
    carts[userId].items = carts[userId].items.filter(i => i.id !== itemId);
  }
  return res.json({ success: true, message: 'Item removed', data: carts[userId] });
});

app.delete('/api/cart/clear', (req, res) => {
  const userId = parseInt(req.query.userId || '3');
  if (carts[userId]) carts[userId].items = [];
  return res.json({ success: true, message: 'Cart cleared' });
});

// Orders
app.post('/api/orders', (req, res) => {
  const userId = parseInt(req.query.userId || '3');
  const { restaurantId, deliveryAddress, paymentMethod, couponCode } = req.body;
  const user = users.find(u => u.id === userId) || users[2];
  const userCart = carts[userId] || { items: [] };

  if (userCart.items.length === 0) {
    return res.status(400).json({ success: false, message: 'Cart is empty' });
  }

  const rest = restaurants.find(r => r.id === restaurantId) || (userCart.items[0]?.foodItem?.restaurant) || restaurants[0];

  const subtotal = userCart.items.reduce((s, i) => s + (i.price * i.quantity), 0);
  const deliveryFee = 40.0;
  const tax = Math.round((subtotal * 0.05) * 100) / 100;
  let discount = 0;

  if (couponCode) {
    const cp = coupons.find(c => c.code === couponCode.toUpperCase());
    if (cp) {
      discount = cp.discountType === 'PERCENTAGE' ? (subtotal * cp.discountValue) / 100 : cp.discountValue;
      discount = Math.min(discount, subtotal);
    }
  }

  const totalAmount = Math.max(0, Math.round((subtotal + deliveryFee + tax - discount) * 100) / 100);

  const newOrder = {
    id: orders.length + 1,
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

  orders.unshift(newOrder);
  // Clear cart
  carts[userId].items = [];

  return res.json({ success: true, message: 'Order placed successfully!', data: newOrder });
});

app.get('/api/orders', (req, res) => {
  const userId = req.query.userId ? parseInt(req.query.userId) : null;
  const restId = req.query.restaurantId ? parseInt(req.query.restaurantId) : null;

  let list = orders;
  if (userId) list = list.filter(o => o.user && o.user.id === userId);
  if (restId) list = list.filter(o => o.restaurant && o.restaurant.id === restId);
  return res.json({ success: true, message: 'Orders retrieved', data: list });
});

app.get('/api/orders/:id', (req, res) => {
  const o = orders.find(x => x.id === parseInt(req.params.id));
  if (!o) return res.status(404).json({ success: false, message: 'Order not found' });
  return res.json({ success: true, message: 'Order details', data: o });
});

app.put('/api/orders/:id/status', (req, res) => {
  const o = orders.find(x => x.id === parseInt(req.params.id));
  if (!o) return res.status(404).json({ success: false, message: 'Order not found' });
  o.orderStatus = req.body.status;
  return res.json({ success: true, message: 'Order status updated', data: o });
});

// Reviews
app.get('/api/reviews/food/:foodId', (req, res) => {
  const fId = parseInt(req.params.foodId);
  const list = reviews.filter(r => r.foodItemId === fId);
  return res.json({ success: true, message: 'Reviews', data: list });
});

app.get('/api/reviews/restaurant/:restId', (req, res) => {
  const rId = parseInt(req.params.restId);
  const list = reviews.filter(r => r.restaurantId === rId);
  return res.json({ success: true, message: 'Reviews', data: list });
});

app.post('/api/reviews', (req, res) => {
  const userId = parseInt(req.query.userId || '3');
  const user = users.find(u => u.id === userId) || users[2];
  const newRev = {
    id: reviews.length + 1,
    user,
    createdAt: new Date().toISOString(),
    ...req.body
  };
  reviews.unshift(newRev);
  return res.json({ success: true, message: 'Review added', data: newRev });
});

// Favorites
app.get('/api/favorites', (req, res) => {
  const userId = parseInt(req.query.userId || '3');
  const list = favorites.filter(f => f.userId === userId);
  return res.json({ success: true, message: 'Favorites retrieved', data: list });
});

app.post('/api/favorites', (req, res) => {
  const userId = parseInt(req.query.userId || '3');
  const restId = req.query.restaurantId ? parseInt(req.query.restaurantId) : null;
  const foodId = req.query.foodItemId ? parseInt(req.query.foodItemId) : null;

  const newFav = { id: favorites.length + 1, userId };
  if (restId) newFav.restaurant = restaurants.find(r => r.id === restId);
  if (foodId) newFav.foodItem = foods.find(f => f.id === foodId);
  favorites.push(newFav);
  return res.json({ success: true, message: 'Added to favorites', data: newFav });
});

app.delete('/api/favorites/:id', (req, res) => {
  const id = parseInt(req.params.id);
  const idx = favorites.findIndex(f => f.id === id);
  if (idx !== -1) favorites.splice(idx, 1);
  return res.json({ success: true, message: 'Removed from favorites' });
});

// Admin
app.get('/api/admin/dashboard', (_req, res) => {
  const totalRevenue = orders.filter(o => o.orderStatus !== 'CANCELLED').reduce((s, o) => s + o.totalAmount, 0);
  const activeOrders = orders.filter(o => o.orderStatus !== 'DELIVERED' && o.orderStatus !== 'CANCELLED').length;
  return res.json({
    success: true,
    data: {
      totalUsers: users.length,
      totalCustomers: users.filter(u => u.role === 'CUSTOMER').length,
      totalRestaurants: restaurants.length,
      totalFoodItems: foods.length,
      totalOrders: orders.length,
      totalRevenue: Math.round(totalRevenue * 100) / 100,
      activeOrders
    }
  });
});

app.get('/api/admin/users', (_req, res) => res.json({ success: true, data: users }));
app.get('/api/admin/orders', (_req, res) => res.json({ success: true, data: orders }));
app.get('/api/admin/restaurants', (_req, res) => res.json({ success: true, data: restaurants }));

// User Profile & Addresses
app.get('/api/users/:id', (req, res) => {
  const u = users.find(x => x.id === parseInt(req.params.id));
  if (!u) return res.status(404).json({ success: false, message: 'User not found' });
  return res.json({ success: true, data: u });
});

app.put('/api/users/:id', (req, res) => {
  const u = users.find(x => x.id === parseInt(req.params.id));
  if (!u) return res.status(404).json({ success: false, message: 'User not found' });
  if (req.body.name) u.name = req.body.name;
  if (req.body.mobile) u.mobile = req.body.mobile;
  return res.json({ success: true, message: 'User updated', data: u });
});

app.get('/api/users/:id/addresses', (req, res) => {
  const list = addresses.filter(a => a.userId === parseInt(req.params.id));
  return res.json({ success: true, data: list });
});

app.post('/api/users/:id/addresses', (req, res) => {
  const newAddr = { id: addresses.length + 1, userId: parseInt(req.params.id), ...req.body };
  addresses.push(newAddr);
  return res.json({ success: true, data: newAddr });
});

app.delete('/api/users/:id/addresses/:addrId', (req, res) => {
  const idx = addresses.findIndex(a => a.id === parseInt(req.params.addrId));
  if (idx !== -1) addresses.splice(idx, 1);
  return res.json({ success: true, message: 'Address deleted' });
});

// Serve static frontend files
const staticDir = path.join(__dirname, 'src/main/resources/static');
app.use(express.static(staticDir));
app.use(express.static(__dirname));

// Fallback for root
app.get('/', (_req, res) => {
  res.sendFile(path.join(__dirname, 'index.html'));
});

// Start Express server
app.listen(PORT, '0.0.0.0', () => {
  console.log(`HungerByte server is running at http://0.0.0.0:${PORT}`);
  console.log(`Frontend served from: ${staticDir}`);
});
