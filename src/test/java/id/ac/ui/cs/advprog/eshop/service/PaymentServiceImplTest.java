package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        products.add(product1);

        order = new Order("eb558e9f-1c39-460e-8860-71af6af63bd8",
                products, 1708560000L, "Safira Sudrajat");

        paymentData = new HashMap<>();
    }

    @Test
    void testAddPaymentVoucher() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);

        assertNotNull(payment);
        assertEquals(order, payment.getOrder());
        assertEquals("VOUCHER", payment.getMethod());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCOD() {
        paymentData.put("address", "Jl. Sudirman No. 1");
        paymentData.put("deliveryFee", "10000");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "COD", paymentData);

        assertNotNull(payment);
        assertEquals(order, payment.getOrder());
        assertEquals("COD", payment.getMethod());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentValidVoucherAutoSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);

        assertEquals(PaymentStatus.ACCEPTED.getValue(), payment.getStatus());
    }

    @Test
    void testAddPaymentInvalidVoucherAutoRejected() {
        paymentData.put("voucherCode", "INVALID123");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testAddPaymentVoucherTooShort() {
        paymentData.put("voucherCode", "ESHOP123");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testAddPaymentVoucherNotStartWithESHOP() {
        paymentData.put("voucherCode", "WRONG1234ABC5678");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testAddPaymentVoucherNotEnoughNumbers() {
        paymentData.put("voucherCode", "ESHOP12ABCDEFGH");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "VOUCHER", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testAddPaymentCODValidAutoSuccess() {
        paymentData.put("address", "Jl. Sudirman No. 1");
        paymentData.put("deliveryFee", "10000");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "COD", paymentData);

        assertEquals(PaymentStatus.ACCEPTED.getValue(), payment.getStatus());
    }

    @Test
    void testAddPaymentCODEmptyAddress() {
        paymentData.put("address", "");
        paymentData.put("deliveryFee", "10000");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "COD", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testAddPaymentCODNullAddress() {
        paymentData.put("address", null);
        paymentData.put("deliveryFee", "10000");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "COD", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testAddPaymentCODEmptyDeliveryFee() {
        paymentData.put("address", "Jl. Sudirman No. 1");
        paymentData.put("deliveryFee", "");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "COD", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testAddPaymentCODNullDeliveryFee() {
        paymentData.put("address", "Jl. Sudirman No. 1");
        paymentData.put("deliveryFee", null);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.addPayment(order, "COD", paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusToSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("PAY-001", order, "VOUCHER", paymentData);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.setStatus(payment, "ACCEPTED");

        assertEquals(PaymentStatus.ACCEPTED.getValue(), result.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusToRejected() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("PAY-001", order, "VOUCHER", paymentData);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testSetStatusToPending() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("PAY-001", order, "VOUCHER", paymentData);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.setStatus(payment, "PENDING");

        assertEquals(PaymentStatus.PENDING.getValue(), result.getStatus());
    }

    @Test
    void testGetPayment() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("PAY-001", order, "VOUCHER", paymentData);
        when(paymentRepository.findById("PAY-001")).thenReturn(payment);

        Payment result = paymentService.getPayment("PAY-001");

        assertNotNull(result);
        assertEquals("PAY-001", result.getId());
        verify(paymentRepository, times(1)).findById("PAY-001");
    }

    @Test
    void testGetPaymentNotFound() {
        when(paymentRepository.findById("INVALID-ID")).thenReturn(null);

        Payment result = paymentService.getPayment("INVALID-ID");

        assertNull(result);
    }

    @Test
    void testGetAllPayments() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment("PAY-001", order, "VOUCHER", paymentData);
        Payment payment2 = new Payment("PAY-002", order, "COD", paymentData);
        List<Payment> payments = Arrays.asList(payment1, payment2);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllPaymentsEmpty() {
        when(paymentRepository.findAll()).thenReturn(new ArrayList<>());

        List<Payment> result = paymentService.getAllPayments();

        assertTrue(result.isEmpty());
    }
}