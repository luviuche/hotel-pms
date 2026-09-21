package io.github.luviuche.hotel.repository;

import io.github.luviuche.hotel.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // Underscore: forces the payment.id traversal (avoids clashing with getPaymentId()).
    Optional<Invoice> findByPayment_Id(Long paymentId);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceNumber(String invoiceNumber);

    boolean existsByPayment_Id(Long paymentId);
}
