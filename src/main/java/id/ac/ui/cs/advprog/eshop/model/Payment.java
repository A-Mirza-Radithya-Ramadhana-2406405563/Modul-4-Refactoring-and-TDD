package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    private final String id;
    private final Order order;
    private final String method;
    private final Map<String, String> paymentData;
    private String status;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (method == null) {
            throw new IllegalArgumentException("Payment method cannot be null");
        }
        if (method.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method cannot be empty");
        }
        if (paymentData == null) {
            throw new IllegalArgumentException("Payment data cannot be null");
        }
        if (paymentData.isEmpty()) {
            throw new IllegalArgumentException("Payment data cannot be empty");
        }

        this.id = id;
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
        this.status = "PENDING";
    }

    public void cancel() {
        if ("CANCELLED".equals(this.status)) {
            throw new IllegalStateException("Cannot cancel a payment that is already cancelled");
        }
        if ("ACCEPTED".equals(this.status)) {
            throw new IllegalStateException("Cannot cancel a payment that has been accepted");
        }
        if ("REJECTED".equals(this.status)) {
            throw new IllegalStateException("Cannot cancel a payment that has been rejected");
        }
        this.status = "CANCELLED";
    }

    public void accept() {
        if ("CANCELLED".equals(this.status)) {
            throw new IllegalStateException("Cannot accept a payment that has been cancelled");
        }
        if ("ACCEPTED".equals(this.status)) {
            throw new IllegalStateException("Cannot accept a payment that is already accepted");
        }
        if ("REJECTED".equals(this.status)) {
            throw new IllegalStateException("Cannot accept a payment that has been rejected");
        }
        this.status = "ACCEPTED";
    }

    public void reject() {
        if ("CANCELLED".equals(this.status)) {
            throw new IllegalStateException("Cannot reject a payment that has been cancelled");
        }
        if ("ACCEPTED".equals(this.status)) {
            throw new IllegalStateException("Cannot reject a payment that is already accepted");
        }
        if ("REJECTED".equals(this.status)) {
            throw new IllegalStateException("Cannot reject a payment that has been rejected");
        }
        this.status = "REJECTED";
    }
}