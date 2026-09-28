package com.hungerbyte.config;

import com.hungerbyte.entity.*;
import com.hungerbyte.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;
    private final FoodItemVariantRepository variantRepository;
    private final CouponRepository couponRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           AddressRepository addressRepository,
                           CartRepository cartRepository,
                           CategoryRepository categoryRepository,
                           RestaurantRepository restaurantRepository,
                           FoodItemRepository foodItemRepository,
                           FoodItemVariantRepository variantRepository,
                           CouponRepository couponRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.cartRepository = cartRepository;
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodItemRepository = foodItemRepository;
        this.variantRepository = variantRepository;
        this.couponRepository = couponRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            System.out.println("DataInitializer: Database already seeded.");
            return;
        }

        System.out.println("DataInitializer: Seeding initial HungerByte sample data...");

        // 1. Users
        User admin = new User("System Admin", "admin@hungerbyte.com", passwordEncoder.encode("admin123"), "9876543210", Role.ADMIN);
        User owner = new User("Rahul Sharma (Owner)", "owner@hungerbyte.com", passwordEncoder.encode("owner123"), "9876543211", Role.RESTAURANT_OWNER);
        User customer = new User("Priya Patel", "customer@hungerbyte.com", passwordEncoder.encode("customer123"), "9876543212", Role.CUSTOMER);

        admin = userRepository.save(admin);
        owner = userRepository.save(owner);
        customer = userRepository.save(customer);

        cartRepository.save(new Cart(customer));
        cartRepository.save(new Cart(owner));
        cartRepository.save(new Cart(admin));

        // Default Address for customer
        Address defaultAddr = new Address("42, 4th Cross, Indiranagar", "Bangalore", "Karnataka", "560038", "HOME", true, customer);
        addressRepository.save(defaultAddr);

        // 2. Categories
        Category catBiryani = categoryRepository.save(new Category("Biryani", "Aromatic royal rice delicacies cooked with rich spices and herbs", "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=500&auto=format&fit=crop&q=80"));
        Category catPizza = categoryRepository.save(new Category("Pizza", "Authentic crusts topped with artisanal cheese, savory sauces and fresh toppings", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500&auto=format&fit=crop&q=80"));
        Category catBurger = categoryRepository.save(new Category("Burger", "Juicy gourmet patties layered with melted cheese and fresh crisp greens", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500&auto=format&fit=crop&q=80"));
        Category catChinese = categoryRepository.save(new Category("Chinese", "Wok-tossed noodles, aromatic fried rice, and savory Asian gravies", "https://images.unsplash.com/photo-1585032226651-759b368d7246?w=500&auto=format&fit=crop&q=80"));
        Category catSouth = categoryRepository.save(new Category("South Indian", "Crispy dosas, fluffy idlis, and traditional coastal spiced curries", "https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?w=500&auto=format&fit=crop&q=80"));
        Category catNorth = categoryRepository.save(new Category("North Indian", "Rich creamy paneer, buttery tandoori curries, and warm breads", "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=500&auto=format&fit=crop&q=80"));
        Category catDesserts = categoryRepository.save(new Category("Desserts", "Sweet delights, warm gulab jamun, and artisanal ice creams", "https://images.unsplash.com/photo-1551024709-8f23befc6f87?w=500&auto=format&fit=crop&q=80"));
        Category catBeverages = categoryRepository.save(new Category("Beverages", "Refreshing mocktails, fresh lime sodas, and chilled beverages", "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=500&auto=format&fit=crop&q=80"));

        // 3. Restaurants
        Restaurant r1 = restaurantRepository.save(new Restaurant(
                "Spice Hub",
                "Authentic North Indian curries, aromatic tandoor specialties and creamy butter gravies.",
                "North Indian, Mughlai",
                "12 Church Street, MG Road, Bangalore",
                "080-25581234",
                "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&auto=format&fit=crop&q=80",
                4.6, 142, 35, 450.0, owner
        ));

        Restaurant r2 = restaurantRepository.save(new Restaurant(
                "Bangalore Biryani House",
                "Legendary dum biryanis slow-cooked with fragrant seeraga samba & long grain basmati.",
                "Biryani, South Indian",
                "88 Koramangala 5th Block, Bangalore",
                "080-41235678",
                "https://images.unsplash.com/photo-1552611052-33e04de081de?w=800&auto=format&fit=crop&q=80",
                4.8, 380, 25, 400.0, owner
        ));

        Restaurant r3 = restaurantRepository.save(new Restaurant(
                "Pizza Palace",
                "Hand-tossed wood-fired pizzas, cheesy garlic breads, and gourmet Italian pastas.",
                "Pizza, Italian, Fast Food",
                "204 100ft Road, Indiranagar, Bangalore",
                "080-49876543",
                "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&auto=format&fit=crop&q=80",
                4.5, 215, 30, 500.0, owner
        ));

        Restaurant r4 = restaurantRepository.save(new Restaurant(
                "South Indian Kitchen",
                "Traditional breakfast, golden ghee roast dosas, steamed idlis, and filter coffee.",
                "South Indian, Pure Veg",
                "14 Malleshwaram 8th Cross, Bangalore",
                "080-23349988",
                "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=800&auto=format&fit=crop&q=80",
                4.7, 490, 20, 200.0, owner
        ));

        Restaurant r5 = restaurantRepository.save(new Restaurant(
                "Burger Town",
                "Stacked gourmet burgers with artisan buns, house secret sauces, and crispy golden fries.",
                "Burger, American, Fast Food",
                "55 Brigade Road, Bangalore",
                "080-43217890",
                "https://images.unsplash.com/photo-1550547660-d9450f859349?w=800&auto=format&fit=crop&q=80",
                4.4, 180, 25, 350.0, owner
        ));

        Restaurant r6 = restaurantRepository.save(new Restaurant(
                "Chinese Wok",
                "Sizzling wok bowls, Hakka noodles, crispy Manchurian, and spicy Schezwan specialties.",
                "Chinese, Asian",
                "77 HSR Layout Sector 2, Bangalore",
                "080-67891234",
                "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=800&auto=format&fit=crop&q=80",
                4.3, 160, 30, 380.0, owner
        ));

        // 4. Food Items
        // Bangalore Biryani House
        FoodItem f1 = foodItemRepository.save(new FoodItem(
                "Chicken Biryani",
                "Fragrant basmati rice dum-cooked with tender chicken pieces, saffron, and aromatic whole spices. Served with creamy raita and spicy salan.",
                240.0,
                "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=600&auto=format&fit=crop&q=80",
                false, true, 4.8, r2, catBiryani
        ));
        variantRepository.save(new FoodItemVariant("Regular (Serves 1)", 240.0, f1));
        variantRepository.save(new FoodItemVariant("Large (Serves 2)", 420.0, f1));

        FoodItem f2 = foodItemRepository.save(new FoodItem(
                "Veg Biryani",
                "Layers of seasoned basmati rice and garden-fresh vegetables slow-cooked in a sealed clay handi with rich spices.",
                180.0,
                "https://images.unsplash.com/photo-1642821373181-696a54913e9a?w=600&auto=format&fit=crop&q=80",
                true, true, 4.5, r2, catBiryani
        ));

        // Spice Hub
        FoodItem f3 = foodItemRepository.save(new FoodItem(
                "Paneer Butter Masala",
                "Soft cottage cheese cubes simmered in a rich, buttery, velvety tomato and cashew nut gravy infused with kasuri methi.",
                210.0,
                "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=600&auto=format&fit=crop&q=80",
                true, true, 4.7, r1, catNorth
        ));

        // South Indian Kitchen
        FoodItem f4 = foodItemRepository.save(new FoodItem(
                "Masala Dosa",
                "Golden crispy rice crepe smeared with pure spiced butter, stuffed with fragrant tempered potato mash. Served with 3 chutneys and piping hot sambar.",
                80.0,
                "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=600&auto=format&fit=crop&q=80",
                true, true, 4.9, r4, catSouth
        ));

        FoodItem f5 = foodItemRepository.save(new FoodItem(
                "Idli Vada",
                "Two pillowy steamed idlis paired with a crispy medu vada, served with traditional coconut chutney and lentil sambar.",
                60.0,
                "https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?w=600&auto=format&fit=crop&q=80",
                true, true, 4.6, r4, catSouth
        ));

        // Pizza Palace
        FoodItem f6 = foodItemRepository.save(new FoodItem(
                "Veg Pizza",
                "Thin crust base layered with zesty herb tomato sauce, melted mozzarella, bell peppers, sweet corn, olives and fresh mushrooms.",
                299.0,
                "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600&auto=format&fit=crop&q=80",
                true, true, 4.6, r3, catPizza
        ));
        variantRepository.save(new FoodItemVariant("Medium (10 inch)", 299.0, f6));
        variantRepository.save(new FoodItemVariant("Large (12 inch)", 499.0, f6));

        FoodItem f7 = foodItemRepository.save(new FoodItem(
                "Chicken Pizza",
                "Loaded with BBQ grilled chicken chunks, smoky paprika, red onions, jalapeños, and generous stringy mozzarella cheese.",
                399.0,
                "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=600&auto=format&fit=crop&q=80",
                false, true, 4.7, r3, catPizza
        ));
        variantRepository.save(new FoodItemVariant("Medium (10 inch)", 399.0, f7));
        variantRepository.save(new FoodItemVariant("Large (12 inch)", 599.0, f7));

        // Burger Town
        FoodItem f8 = foodItemRepository.save(new FoodItem(
                "Veg Burger",
                "Crispy spiced vegetable patty topped with melted cheddar, fresh tomato slices, crisp iceberg lettuce, and herb mayonnaise.",
                149.0,
                "https://images.unsplash.com/photo-1550547660-d9450f859349?w=600&auto=format&fit=crop&q=80",
                true, true, 4.4, r5, catBurger
        ));

        FoodItem f9 = foodItemRepository.save(new FoodItem(
                "Chicken Burger",
                "Tender grilled chicken patty seasoned with garlic herbs, chipotle mayonnaise, pickled gherkins, and cheddar in a brioche bun.",
                199.0,
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&auto=format&fit=crop&q=80",
                false, true, 4.6, r5, catBurger
        ));

        // Chinese Wok
        FoodItem f10 = foodItemRepository.save(new FoodItem(
                "Fried Rice",
                "Classic wok-tossed long grain rice stir-fried with diced bell peppers, carrots, spring onions, and light soy sauce.",
                170.0,
                "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=600&auto=format&fit=crop&q=80",
                true, true, 4.4, r6, catChinese
        ));

        FoodItem f11 = foodItemRepository.save(new FoodItem(
                "Noodles",
                "Hakka style thin wheat noodles tossed on high flame with shredded crunchy vegetables, garlic, and savory Asian sauces.",
                160.0,
                "https://images.unsplash.com/photo-1585032226651-759b368d7246?w=600&auto=format&fit=crop&q=80",
                true, true, 4.5, r6, catChinese
        ));

        // Desserts & Beverages
        FoodItem f12 = foodItemRepository.save(new FoodItem(
                "Gulab Jamun",
                "Warm, melt-in-mouth milk solids dumplings soaked in fragrant cardamom and saffron infused sugar syrup. 2 pieces.",
                70.0,
                "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=600&auto=format&fit=crop&q=80",
                true, true, 4.9, r1, catDesserts
        ));

        FoodItem f13 = foodItemRepository.save(new FoodItem(
                "Ice Cream",
                "Two scoops of rich artisanal Madagascar vanilla ice cream topped with dark chocolate fudge sauce and crunchy roasted almonds.",
                90.0,
                "https://images.unsplash.com/photo-1501443762994-82bd5dace89a?w=600&auto=format&fit=crop&q=80",
                true, true, 4.7, r5, catDesserts
        ));

        FoodItem f14 = foodItemRepository.save(new FoodItem(
                "Fresh Lime Soda",
                "Refreshing freshly squeezed lime juice blended with chilled sparkling soda, mint leaves, and rock salt.",
                50.0,
                "https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=600&auto=format&fit=crop&q=80",
                true, true, 4.6, r2, catBeverages
        ));

        // 5. Coupons
        couponRepository.save(new Coupon("HUNGER50", "Get ₹50 flat off on all orders above ₹299", "FLAT", 50.0, 299.0, 50.0, LocalDate.now().plusMonths(6)));
        couponRepository.save(new Coupon("FEAST100", "Get ₹100 flat off on grand feast orders above ₹499", "FLAT", 100.0, 499.0, 100.0, LocalDate.now().plusMonths(6)));
        couponRepository.save(new Coupon("WELCOME20", "20% discount up to ₹150 for your first delicious meal", "PERCENTAGE", 20.0, 199.0, 150.0, LocalDate.now().plusMonths(6)));

        System.out.println("DataInitializer: Successfully seeded 3 users, 8 categories, 6 restaurants, 14 food items, and 3 coupons.");
    }
}
