package com.ecommerce.Domain.Infrastructure.DB.Spring.Entities;

import com.ecommerce.Domain.Domain.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Purchase")
@Table(name = "`purchase`")
public class PurchaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private PaymentMethod payment;


    private Long customerId;

    private String state;

    private Date createdAt;

    private Date updatedAt;

    @OneToMany(mappedBy = "purchase", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<PurchaseProductEntity> purchaseProducts;

    @PrePersist
    private void prePersist() {
        this.setState("pendiente");
        this.setCreatedAt(new Date());
        this.setUpdatedAt(getCreatedAt());
    }

    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedAt(new Date());
    }

}
