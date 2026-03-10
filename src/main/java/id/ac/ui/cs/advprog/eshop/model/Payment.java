package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

@Getter
public class Payment {
    private static final Set<String> FINAL_STATUSES = Set.of(
            PaymentStatus.ACCEPTED.getValue(),
            PaymentStatus.CANCELLED.getValue(),
            PaymentStatus.REJECTED.getValue()
    );

    private final String id;
    private final Order order;
    private final String method;
    private final Map<String, String> paymentData;
    private String status;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        validateConstructorParameters(order, method, paymentData);

        this.id = id;
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
        this.status = PaymentStatus.PENDING.getValue();
    }

    private void validateConstructorParameters(Order order, String method, Map<String, String> paymentData) {
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
    }

    public void cancel() {
        transitionToStatus(PaymentStatus.CANCELLED.getValue());
    }

    public void accept() {
        transitionToStatus(PaymentStatus.ACCEPTED.getValue());
    }

    public void reject() {
        transitionToStatus(PaymentStatus.REJECTED.getValue());
    }

    private void transitionToStatus(String targetStatus) {
        validateStatusTransition(targetStatus);
        this.status = targetStatus;
    }

    private void validateStatusTransition(String targetStatus) {
        if (status.equals(targetStatus)) {
            throw new IllegalStateException(
                    String.format("Payment is already %s", targetStatus.toLowerCase())
            );
        }

        if (FINAL_STATUSES.contains(status)) {
            throw new IllegalStateException(
                    String.format("Cannot %s a payment that is already %s",
                            targetStatus.toLowerCase(),
                            status.toLowerCase())
            );
        }
    }
}