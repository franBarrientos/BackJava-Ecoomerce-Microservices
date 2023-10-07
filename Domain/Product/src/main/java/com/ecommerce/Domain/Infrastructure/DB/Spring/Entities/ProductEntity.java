package com.ecommerce.Domain.Infrastructure.DB.Spring.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Product")
@Table(name = "`product`")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    @ManyToOne()
    @JoinColumn(name = "categoryId")
    private CategoryEntity category;

    private String img;

    private Long stock;

    private Boolean hasStock;

    private Boolean fav;

    private Date createdAt;

    private Date updatedAt;

    @PrePersist
    private void prePersist() {
        if (this.getStock() == null) {
            this.setStock(1l);
        }
        if (this.getFav() == null) {
            this.setFav(false);
        }
        this.setHasStock(true);
        this.setCreatedAt(new Date());
        this.setUpdatedAt(this.getCreatedAt());
    }

    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedAt(new Date());
    }


}
