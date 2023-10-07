package com.ecommerce.Domain.Infrastructure.DB.Spring.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Builder
@Entity(name = "Customer")
@Table(name = "`customer`")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer dni;

    private String addres;

    private String phone;

    @OneToOne
    @JoinColumn(name = "userId")
    private UserEntity user;


    @Column(columnDefinition = "TIMESTAMP", name = "createdAt")
    private Date createdAt;
    @Column(columnDefinition = "TIMESTAMP", name = "updatedAt")
    private Date updatedAt;

    @PrePersist
    private void prePersist() {
        setCreatedAt(new Date());
        setUpdatedAt(getCreatedAt());
    }

    @PreUpdate
    protected void onUpdate() {
        setUpdatedAt(new Date());
    }


}
