package io.github.luviuche.hotel.service;

import io.github.luviuche.hotel.entity.Invoice;
import io.github.luviuche.hotel.entity.Payment;
import io.github.luviuche.hotel.exception.BusinessRuleException;
import io.github.luviuche.hotel.exception.ResourceNotFoundException;
import io.github.luviuche.hotel.repository.InvoiceRepository;
import io.github.luviuche.hotel.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository) {
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Invoice findById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", id));
    }

    public Invoice create(Invoice invoice) {
        Payment payment = resolvePayment(invoice.getPaymentId());
        if (invoiceRepository.existsByPayment_Id(payment.getId())) {
            throw new BusinessRuleException(
                    "Payment " + payment.getId() + " already has an invoice attached.");
        }
        if (invoiceRepository.existsByInvoiceNumber(invoice.getInvoiceNumber())) {
            throw new BusinessRuleException(
                    "An invoice numbered '" + invoice.getInvoiceNumber() + "' already exists.");
        }
        invoice.setPayment(payment);
        prepareAmounts(invoice);
        invoice.setId(null);
        return invoiceRepository.save(invoice);
    }

    public Invoice update(Long id, Invoice data) {
        Invoice existing = findById(id);
        Payment payment = resolvePayment(data.getPaymentId());
        if (!existing.getPayment().getId().equals(payment.getId())
                && invoiceRepository.existsByPayment_Id(payment.getId())) {
            throw new BusinessRuleException(
                    "Payment " + payment.getId() + " already has an invoice attached.");
        }
        if (!existing.getInvoiceNumber().equals(data.getInvoiceNumber())
                && invoiceRepository.existsByInvoiceNumber(data.getInvoiceNumber())) {
            throw new BusinessRuleException(
                    "An invoice numbered '" + data.getInvoiceNumber() + "' already exists.");
        }
        existing.setPayment(payment);
        existing.setInvoiceNumber(data.getInvoiceNumber());
        existing.setSubtotal(data.getSubtotal());
        existing.setTaxes(data.getTaxes());
        existing.setTotal(data.getTotal());
        existing.setDescription(data.getDescription());
        if (data.getIssuedAt() != null) {
            existing.setIssuedAt(data.getIssuedAt());
        }
        prepareAmounts(existing);
        return invoiceRepository.save(existing);
    }

    public void delete(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice", id);
        }
        invoiceRepository.deleteById(id);
    }

    /** Works out the total from subtotal + taxes when it is missing, and stamps the issue date. */
    private void prepareAmounts(Invoice invoice) {
        BigDecimal subtotal = invoice.getSubtotal() != null ? invoice.getSubtotal() : BigDecimal.ZERO;
        BigDecimal taxes = invoice.getTaxes() != null ? invoice.getTaxes() : BigDecimal.ZERO;
        invoice.setSubtotal(subtotal);
        invoice.setTaxes(taxes);
        if (invoice.getTotal() == null) {
            invoice.setTotal(subtotal.add(taxes));
        }
        if (invoice.getIssuedAt() == null) {
            invoice.setIssuedAt(LocalDateTime.now());
        }
    }

    private Payment resolvePayment(Long paymentId) {
        if (paymentId == null) {
            throw new BusinessRuleException("An invoice requires a paymentId.");
        }
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));
    }
}
