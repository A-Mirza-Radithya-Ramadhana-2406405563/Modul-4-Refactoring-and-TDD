package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String id = UUID.randomUUID().toString();
        Payment payment = new Payment(id, order, method, paymentData);

        if (method.equals("VOUCHER")) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null) {
                if (voucherCode.length() == 16) {
                    if (voucherCode.startsWith("ESHOP")) {
                        int count = 0;
                        for (int i = 0; i < voucherCode.length(); i++) {
                            char c = voucherCode.charAt(i);
                            if (c >= '0' && c <= '9') {
                                count++;
                            }
                        }
                        if (count == 8) {
                            payment.accept();
                        } else {
                            payment.reject();
                        }
                    } else {
                        payment.reject();
                    }
                } else {
                    payment.reject();
                }
            } else {
                payment.reject();
            }
        } else if (method.equals("COD")) {
            String address = paymentData.get("address");
            String deliveryFee = paymentData.get("deliveryFee");

            if (address == null || address.isEmpty()) {
                payment.reject();
            } else if (deliveryFee == null || deliveryFee.isEmpty()) {
                payment.reject();
            } else {
                payment.accept();
            }
        }

        Payment savedPayment = paymentRepository.save(payment);
        return savedPayment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        if (status != null) {
            if (status.equals("ACCEPTED")) {
                payment.accept();
                Order order = payment.getOrder();
                if (order != null) {
                    order.setStatus("SUCCESS");
                }
            } else if (status.equals("REJECTED")) {
                payment.reject();
                Order order = payment.getOrder();
                if (order != null) {
                    order.setStatus("FAILED");
                }
            } else if (status.equals("PENDING")) {
                // Do nothing, already pending
            }
        }
        Payment savedPayment = paymentRepository.save(payment);
        return savedPayment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        if (paymentId != null) {
            Payment payment = paymentRepository.findById(paymentId);
            if (payment != null) {
                return payment;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments;
    }
}