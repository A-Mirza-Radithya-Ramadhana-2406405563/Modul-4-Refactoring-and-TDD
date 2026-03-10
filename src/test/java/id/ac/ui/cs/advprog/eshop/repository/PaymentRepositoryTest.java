package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        products.add(product1);

        order = new Order("eb558e9f-1c39-460e-8860-71af6af63bd8",
                products, 1708560000L, "Safira Sudrajat");

        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testSavePayment() {
        Payment payment = new Payment("PAY-001", order, "VOUCHER", paymentData);
        Payment savedPayment = paymentRepository.save(payment);

        assertNotNull(savedPayment);
        assertEquals("PAY-001", savedPayment.getId());
    }

    @Test
    void testFindPaymentById() {
        Payment payment = new Payment("PAY-001", order, "VOUCHER", paymentData);
        paymentRepository.save(payment);

        Payment foundPayment = paymentRepository.findById("PAY-001");

        assertNotNull(foundPayment);
        assertEquals("PAY-001", foundPayment.getId());
        assertEquals("VOUCHER", foundPayment.getMethod());
    }

    @Test
    void testFindPaymentByIdNotFound() {
        Payment foundPayment = paymentRepository.findById("INVALID-ID");
        assertNull(foundPayment);
    }

    @Test
    void testFindAll() {
        Payment payment1 = new Payment("PAY-001", order, "VOUCHER", paymentData);
        Payment payment2 = new Payment("PAY-002", order, "COD", paymentData);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> allPayments = paymentRepository.findAll();

        assertEquals(2, allPayments.size());
    }

    @Test
    void testFindAllEmpty() {
        List<Payment> allPayments = paymentRepository.findAll();
        assertTrue(allPayments.isEmpty());
    }

    @Test
    void testSaveMultiplePayments() {
        Payment payment1 = new Payment("PAY-001", order, "VOUCHER", paymentData);
        Payment payment2 = new Payment("PAY-002", order, "COD", paymentData);
        Payment payment3 = new Payment("PAY-003", order, "VOUCHER", paymentData);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        assertEquals(3, paymentRepository.findAll().size());
        assertNotNull(paymentRepository.findById("PAY-001"));
        assertNotNull(paymentRepository.findById("PAY-002"));
        assertNotNull(paymentRepository.findById("PAY-003"));
    }

    @Test
    void testUpdatePayment() {
        Payment payment = new Payment("PAY-001", order, "VOUCHER", paymentData);
        paymentRepository.save(payment);

        payment.accept();
        Payment updatedPayment = paymentRepository.save(payment);

        assertEquals("ACCEPTED", updatedPayment.getStatus());
        assertEquals("ACCEPTED", paymentRepository.findById("PAY-001").getStatus());
    }

    @Test
    void testSaveSameIdOverwrites() {
        Payment payment1 = new Payment("PAY-001", order, "VOUCHER", paymentData);
        Payment payment2 = new Payment("PAY-001", order, "COD", paymentData);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> allPayments = paymentRepository.findAll();
        assertEquals(1, allPayments.size());
        assertEquals("COD", paymentRepository.findById("PAY-001").getMethod());
    }

    @Test
    void testFindAllReturnsCorrectOrder() {
        Payment payment1 = new Payment("PAY-001", order, "VOUCHER", paymentData);
        Payment payment2 = new Payment("PAY-002", order, "COD", paymentData);
        Payment payment3 = new Payment("PAY-003", order, "VOUCHER", paymentData);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        List<Payment> allPayments = paymentRepository.findAll();

        assertEquals("PAY-001", allPayments.get(0).getId());
        assertEquals("PAY-002", allPayments.get(1).getId());
        assertEquals("PAY-003", allPayments.get(2).getId());
    }
}