package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PaymentRepository {
    private Map<String, Payment> paymentData = new LinkedHashMap<>();

    public Payment save(Payment payment) {
        if (payment != null) {
            if (payment.getId() != null) {
                String id = payment.getId();
                paymentData.put(id, payment);
                return payment;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Payment findById(String id) {
        if (id != null) {
            if (paymentData.containsKey(id)) {
                Payment payment = paymentData.get(id);
                return payment;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<Payment> findAll() {
        List<Payment> result = new ArrayList<>();
        Iterator<Map.Entry<String, Payment>> iterator = paymentData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Payment> entry = iterator.next();
            Payment payment = entry.getValue();
            result.add(payment);
        }
        return result;
    }
}