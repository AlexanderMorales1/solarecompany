package com.solare.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal totalCop;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 32)
    private PaymentMethod paymentMethod;

    /** Datos de envío / contacto capturados en checkout (snapshot al momento de la compra). */
    @Column(name = "customer_full_name", length = 200)
    private String customerFullName;

    @Column(name = "customer_email", length = 255)
    private String customerEmail;

    @Column(name = "customer_phone", length = 32)
    private String customerPhone;

    @Column(name = "ship_country", length = 100)
    private String country;

    @Column(name = "ship_department", length = 100)
    private String department;

    @Column(name = "ship_city", length = 100)
    private String city;

    @Column(name = "ship_neighborhood", length = 120)
    private String neighborhood;

    @Column(name = "ship_address", length = 500)
    private String address;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderDetailEntity> details = new ArrayList<>();

    public enum OrderStatus {
        PENDING,
        PAID,
        SHIPPED,
        CANCELLED
    }

    public enum PaymentMethod {
        SISTECREDITO,
        ADDI,
        BOLD
    }
}
