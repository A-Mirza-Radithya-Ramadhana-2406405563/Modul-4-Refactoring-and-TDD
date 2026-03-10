package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    public String paymentDetailPage() {
        return "paymentDetail";
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetailById(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "paymentDetail";
    }

    @GetMapping("/admin/list")
    public String paymentAdminListPage(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "paymentList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String paymentAdminDetailPage(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "paymentDetailAdmin";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(@PathVariable String paymentId, @RequestParam String status) {
        Payment payment = paymentService.getPayment(paymentId);
        if (payment != null) {
            paymentService.setStatus(payment, status);
        }
        return "redirect:/payment/admin/list";
    }
}