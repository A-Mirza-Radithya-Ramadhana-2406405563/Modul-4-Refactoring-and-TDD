package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "createOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam String productName,
                                  @RequestParam int quantity,
                                  @RequestParam String author,
                                  Model model) {
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName(productName);
        product.setProductQuantity(quantity);

        List<Product> products = new ArrayList<>();
        products.add(product);

        String orderId = UUID.randomUUID().toString();
        Long orderTime = System.currentTimeMillis();
        Order order = new Order(orderId, products, orderTime, author);

        orderService.createOrder(order);

        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String orderHistoryPage() {
        return "orderHistory";
    }

    @PostMapping("/history")
    public String orderHistoryPost(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        return "orderList";
    }
}