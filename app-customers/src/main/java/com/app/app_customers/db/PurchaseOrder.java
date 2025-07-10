package com.app.app_customers.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"customer", "lineItems"})
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    public enum Status {
        NINGUNA,    // ---> 0
        ENTREGADO,  // ---> 1
        PENDIENTE,  // ---> 2
        NINGUNA_ALT // ---> 3
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private Integer total;

    @Column
    //@Enumerated(EnumType.STRING)
    @Enumerated(EnumType.ORDINAL)
    private Status status ;

    @Column(name = "placed_on", nullable = false)
    private LocalDateTime placedOn;

    @Column(name = "delivered_on")
    private LocalDateTime deliveredOn;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineItem> lineItems;
}
