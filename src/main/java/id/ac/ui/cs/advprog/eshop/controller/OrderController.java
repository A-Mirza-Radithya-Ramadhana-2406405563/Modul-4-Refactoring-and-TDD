package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
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

        orderService.createOrderFromRequest(productName, quantity, author);
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

    @GetMapping("/pay/{orderId}")
    public String paymentPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "paymentPage";
    }

    @PostMapping("/pay/{orderId}")
    public String processPayment(@PathVariable String orderId,
                                 @RequestParam String method,
                                 @RequestParam Map<String, String> allParams,
                                 Model model) {
        Order order = orderService.findById(orderId);

        Map<String, String> paymentData = new HashMap<>();
        allParams.forEach((key, value) -> {
            if (!key.equals("method")) {
                paymentData.put(key, value);
            }
        });

        Payment payment = paymentService.addPayment(order, method, paymentData);
        model.addAttribute("paymentId", payment.getId());
        return "paymentSuccess";
    }
}