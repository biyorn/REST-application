package com.epam.esm.entity.impl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"})
})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity userEntity;
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "time")
    private LocalDateTime time;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH},
            mappedBy = "order", fetch = FetchType.EAGER)
    private List<BoughtCertificate> boughtCertificates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public List<BoughtCertificate> getBoughtCertificates() {
        return boughtCertificates;
    }

    public void setBoughtCertificates(List<BoughtCertificate> boughtCertificates) {
        this.boughtCertificates = boughtCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(userEntity, order.userEntity) &&
                Objects.equals(cost, order.cost) &&
                Objects.equals(time, order.time) &&
                Objects.equals(boughtCertificates, order.boughtCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userEntity, cost, time, boughtCertificates);
    }
}