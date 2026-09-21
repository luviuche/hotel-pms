package io.github.luviuche.hotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    private Payment payment;

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal taxes;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    private String description;

    @JsonProperty("paymentId")
    public Long getPaymentId() {
        return payment != null ? payment.getId() : null;
    }

    public void setPaymentId(Long paymentId) {
        if (paymentId == null) {
            this.payment = null;
        } else {
            Payment reference = new Payment();
            reference.setId(paymentId);
            this.payment = reference;
        }
    }
}
