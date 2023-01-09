package com.dcc.jpa_stream_lab.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcc.jpa_stream_lab.repository.ProductsRepository;
import com.dcc.jpa_stream_lab.repository.RolesRepository;
import com.dcc.jpa_stream_lab.repository.ShoppingcartItemRepository;
import com.dcc.jpa_stream_lab.repository.UsersRepository;
import com.dcc.jpa_stream_lab.models.Product;
import com.dcc.jpa_stream_lab.models.Role;
import com.dcc.jpa_stream_lab.models.ShoppingcartItem;
import com.dcc.jpa_stream_lab.models.User;

@Transactional
@Service
public class StreamLabService {
	
	@Autowired
	private ProductsRepository products;
	@Autowired
	private RolesRepository roles;
	@Autowired
	private UsersRepository users;
	@Autowired
	private ShoppingcartItemRepository shoppingcartitems;


    // <><><><><><><><> R Actions (Read) <><><><><><><><><>

    public List<User> RDemoOne() {
    	// This query will return all the users from the User table.
    	return users.findAll().stream().toList();
    }

    public long RProblemOne()
    {
        // Return the COUNT of all the users from the User table.
        // You MUST use a .stream(), don't listen to the squiggle here!
        // Remember yellow squiggles are warnings and can be ignored.

        return users.count();
    }


    public List<Product> RDemoTwo()
    {
        // This query will get each product whose price is greater than $150.
    	return products.findAll().stream().filter(p -> p.getPrice() > 150).toList();
    }

    public List<Product> RProblemTwo()
    {
        // Write a query that gets each product whose price is less than or equal to $100.
        // Return the list


        return products.findAll().stream().filter(p -> p.getPrice() <= 100).toList();
    }

    public List<Product> RProblemThree()
    {
        // Write a query that gets each product that CONTAINS an "s" in the products name.
        // Return the list
        return products.findAll().stream().filter(p -> p.getName().contains("s")).toList();
    }

    public List<User> RProblemFour()
    {
        // Write a query that gets all the users who registered BEFORE 2016
        // Return the list
        // Research 'java create specific date' and 'java compare dates'
        // You may need to use the helper classes imported above!

        String date_string = "01-01-2016";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = formatter.parse(date_string);
            return users.findAll().stream().filter(u -> u.getRegistrationDate().before(date)).toList();

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }

    public List<User> RProblemFive()
    {
        // Write a query that gets all of the users who registered AFTER 2016 and BEFORE 2018
        // Return the list

        String date_string = "01-01-2016";
        String date_string1 = "01-01-2018";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = formatter.parse(date_string);
            Date date1 = formatter.parse(date_string1);

            return users.findAll().stream().filter(u -> u.getRegistrationDate().after(date) && u.getRegistrationDate().before(date1)).toList();

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    // <><><><><><><><> R Actions (Read) with Foreign Keys <><><><><><><><><>

    public List<User> RDemoThree()
    {
        // Write a query that retrieves all of the users who are assigned to the role of Customer.
    	Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
    	List<User> customers = users.findAll().stream().filter(u -> u.getRoles().contains(customerRole)).toList();

    	return customers;
    }

    public List<Product> RProblemSix()
    {
        // Write a query that retrieves all of the products in the shopping cart of the user who has the email "afton@gmail.com".
        // Return the list

        User myCustWithEmail = users.findAll().stream().filter(c ->
                c.getEmail().equals("afton@gmail.com")).findFirst().orElse(null);
//
//        List<ShoppingcartItem> productsInCart = shoppingcartitems.findAll().stream().filter(i -> {
//            return i.getUser(customerWithEmail).getShoppingcartItems();
//        }).toList();

//        Integer custWithEmailId = users.findAll().stream().filter(c -> c.getEmail().equals("afton@gmail.com")).findFirst().orElse(null).getId();

        return shoppingcartitems.findAll().stream().filter(item ->
                item.getUser().equals(myCustWithEmail)).map(s -> s.getProduct()).toList();
    }

    public long RProblemSeven()
    {
        // Write a query that retrieves all of the products in the shopping cart of the user who has the email "oda@gmail.com" and returns the sum of all of the products prices.
    	// Remember to break the problem down and take it one step at a time!
        User myCustWithEmail = users.findAll().stream().filter(c ->
                c.getEmail().equals("oda@gmail.com")).findFirst().orElse(null);
        List<Product> allItems = shoppingcartitems.findAll().stream().filter(item ->
                item.getUser().equals(myCustWithEmail)).map(s -> s.getProduct()).toList();

        List<Integer> allPrices = allItems.stream().map(p -> p.getPrice()).toList();

        Long length = allPrices.stream().count();

        Integer sum = 0;

        for (int i = 0; i < length; i++) {
            sum += allPrices.get(i);
        }

        return sum;

    }

    public List<Product> RProblemEight()
    {
        // Write a query that retrieves all of the products in the shopping cart of users who have the role of "Employee".
    	// Return the list

        //TODO: identify all employees as objects, get all employees' items, translate to products, return list.

        //list of names of employees:
        Role employeeRole = roles.findAll().stream().filter(r -> r.getName().equals("Employee"))
                .findFirst().orElse(null);
        List<User> employees = users.findAll().stream().filter(u -> u.getRoles().contains(employeeRole)).toList();
        List<Product> employeeProducts = employees.stream().map(e -> shoppingcartitems.findAll().stream().filter(s->s.getUser().equals(e)).map(p->p.getProduct()).toList()).toList();
        //returns id's of all employees
//        List <Integer> employeeIds = employees.stream().map(e -> e.getId()).toList();

//        List <Product> employeeItems;
//        employeeItems = employees.stream().map(e -> {
//            List<Product> list = new ArrayList<>();
//            for (ShoppingcartItem item : shoppingcartitems.findAll()) {
//                if (item.getUser().equals(e)) {
//                    Product product = item.getProduct();
//                    list.add(product);
//                }
//            }
//            return list;
//        });
        return employeeProducts;

    }

    //    Long lengthEmployees = employees.stream().count();
//        List<Product> employeeItems;
//        List<Product> sum;
//        for (int i = 0; i < lengthEmployees; i++) {
//            employeeItems = shoppingcartitems.findAll().stream().filter(item ->
//                    item.getUser().equals((employees.get(i))).map(s -> s.getProduct()).toList();
//            sum += employeeItems;
//        }
//    	return sum;


    // <><><><><><><><> CUD (Create, Update, Delete) Actions <><><><><><><><><>

//     <><> C Actions (Create) <><>

    public User CDemoOne()
    {
        // Create a new User object and add that user to the Users table.
        User newUser = new User();
        newUser.setEmail("david@gmail.com");
        newUser.setPassword("DavidsPass123");
        users.save(newUser);
        return newUser;
    }

    public Product CProblemOne()
    {
        // Create a new Product object and add that product to the Products table.
        // Return the product

        Product newProduct = new Product();
        newProduct.setName("iPad 6th generation");
        newProduct.setPrice(450);
        newProduct.setDescription("Refurbished iPad, very good condition. Minor superficial damage.");
        products.save(newProduct);
        return newProduct;


    }

    public List<Role> CDemoTwo()
    {
        // Add the role of "Customer" to the user we just created in the UserRoles junction table.
    	Role customerRole = roles.findAll().stream().filter(r -> r.getName().equals("Customer")).findFirst().orElse(null);
    	User david = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
    	david.addRole(customerRole);
    	return david.getRoles();
    }

    public ShoppingcartItem CProblemTwo()
    {
    	// Create a new ShoppingCartItem to represent the new product you created being added to the new User you created's shopping cart.
        // Add the product you created to the user we created in the ShoppingCart junction table.
        // Return the ShoppingcartItem

    	return null;
    	
    }

    // <><> U Actions (Update) <><>

    public User UDemoOne()
    {
         //Update the email of the user we created in problem 11 to "mike@gmail.com"
          User user = users.findAll().stream().filter(u -> u.getEmail().equals("david@gmail.com")).findFirst().orElse(null);
          user.setEmail("mike@gmail.com");
          return user;
    }

    public Product UProblemOne()
    {
        // Update the price of the product you created to a different value.
        // Return the updated product
    	return null;
    }

    public User UProblemTwo()
    {
        // Change the role of the user we created to "Employee"
        // HINT: You need to delete the existing role relationship and then create a new UserRole object and add it to the UserRoles table

    	return null;
    }

    //BONUS:
    // <><> D Actions (Delete) <><>

    // For these bonus problems, you will also need to create their associated routes in the Controller file!
    
    // DProblemOne
    // Delete the role relationship from the user who has the email "oda@gmail.com".

    // DProblemTwo
    // Delete all the product relationships to the user with the email "oda@gmail.com" in the ShoppingCart table.

    // DProblemThree
    // Delete the user with the email "oda@gmail.com" from the Users table.

}
