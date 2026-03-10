package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentTest {
    private Map<String, String> paymentData;
    private Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        products.add(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9906");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        products.add(product2);

        this.order = new Order("eb558e9f-1c39-460e-8860-71af6af63bd8",
                products, 1708560000L, "Safira Sudrajat");

        this.paymentData = new HashMap<>();
        paymentData.put("voucherNumber", "123456789");
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        assertEquals("ob558e9f-1c39-460e-8860-71af6af63bd8", payment.getId());
        assertEquals(this.order, payment.getOrder());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals(this.paymentData, payment.getPaymentData());
        assertEquals(PaymentStatus.PENDING.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentIfOrderIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", null,
                "VOUCHER", this.paymentData));
    }

    @Test
    void testCreatePaymentIfMethodIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                null, this.paymentData));
    }

    @Test
    void testCreatePaymentIfMethodIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "", this.paymentData));
    }

    @Test
    void testCreatePaymentIfPaymentDataIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", null));
    }

    @Test
    void testCreatePaymentIfPaymentDataIsEmpty() {
        Map<String, String> emptyData = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", emptyData));
    }

    @Test
    void testCancelPaymentIfStatusIsPending() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.cancel();
        assertEquals(PaymentStatus.CANCELLED.getValue(), payment.getStatus());
    }

    @Test
    void testCancelPaymentIfStatusIsCancelled() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.cancel();
        assertThrows(IllegalStateException.class, payment::cancel);
    }

    @Test
    void testCancelPaymentIfStatusIsAccepted() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.accept();
        assertThrows(IllegalStateException.class, payment::cancel);
    }

    @Test
    void testCancelPaymentIfStatusIsRejected() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.reject();
        assertThrows(IllegalStateException.class, payment::accept);
    }

    @Test
    void testAcceptPaymentIfStatusIsPending() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.accept();
        assertEquals(PaymentStatus.ACCEPTED.getValue(), payment.getStatus());
    }

    @Test
    void testAcceptPaymentIfStatusIsCancelled() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.cancel();
        assertThrows(IllegalStateException.class, payment::accept);
    }

    @Test
    void testAcceptPaymentIfStatusIsAccepted() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.accept();
        assertThrows(IllegalStateException.class, payment::accept);
    }

    @Test
    void testAcceptPaymentIfStatusIsRejected() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.reject();
        assertThrows(IllegalStateException.class, payment::accept);
    }

    @Test
    void testRejectPaymentIfStatusIsPending() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.reject();
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testRejectPaymentIfStatusIsCancelled() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.cancel();
        assertThrows(IllegalStateException.class, payment::reject);
    }

    @Test
    void testRejectPaymentIfStatusIsAccepted() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.accept();
        assertThrows(IllegalStateException.class, payment::reject);
    }

    @Test
    void testRejectPaymentIfStatusIsRejected() {
        Payment payment = new Payment("ob558e9f-1c39-460e-8860-71af6af63bd8", this.order,
                "VOUCHER", this.paymentData);
        payment.reject();
        assertThrows(IllegalStateException.class, payment::reject);
    }
}
