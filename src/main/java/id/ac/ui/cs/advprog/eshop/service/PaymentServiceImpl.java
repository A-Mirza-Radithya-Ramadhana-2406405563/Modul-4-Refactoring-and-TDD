package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final int VALID_VOUCHER_LENGTH = 16;
    private static final String VOUCHER_PREFIX = "ESHOP";
    private static final int REQUIRED_DIGIT_COUNT = 8;

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String id = UUID.randomUUID().toString();
        Payment payment = new Payment(id, order, method, paymentData);

        if ("VOUCHER".equals(method)) {
            processVoucherPayment(payment, paymentData);
        } else if ("COD".equals(method)) {
            processCODPayment(payment, paymentData);
        }

        return paymentRepository.save(payment);
    }

    private void processVoucherPayment(Payment payment, Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");

        if (isValidVoucherCode(voucherCode)) {
            payment.accept();
        } else {
            payment.reject();
        }
    }

    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null) {
            return false;
        }

        if (voucherCode.length() != VALID_VOUCHER_LENGTH) {
            return false;
        }

        if (!voucherCode.startsWith(VOUCHER_PREFIX)) {
            return false;
        }

        return countDigits(voucherCode) == REQUIRED_DIGIT_COUNT;
    }

    private int countDigits(String str) {
        return (int) str.chars()
                .filter(Character::isDigit)
                .count();
    }

    private void processCODPayment(Payment payment, Map<String, String> paymentData) {
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");

        if (!isValidCODData(address, deliveryFee)) {
            payment.reject();
        } else {
            payment.accept();
        }
    }

    private boolean isValidCODData(String address, String deliveryFee) {
        return !isNullOrEmpty(address) && !isNullOrEmpty(deliveryFee);
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        if (PaymentStatus.ACCEPTED.getValue().equals(status)) {
            payment.accept();
            updateOrderStatus(payment.getOrder(), OrderStatus.SUCCESS.getValue());
        } else if (PaymentStatus.REJECTED.getValue().equals(status)) {
            payment.reject();
            updateOrderStatus(payment.getOrder(), OrderStatus.FAILED.getValue());
        }

        return paymentRepository.save(payment);
    }

    private void updateOrderStatus(Order order, String status) {
        if (order != null) {
            order.setStatus(status);
        }
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}