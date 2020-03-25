package com.epam.esm.dto;

import com.epam.esm.dto.deserialize.OrderDTODeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(using = OrderDTODeserializer.class)
public class OrderDTO {

    private Long id;
    private BigDecimal cost;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime time;
    private List<BoughtCertificateDTO> boughtCertificates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<BoughtCertificateDTO> getBoughtCertificates() {
        return boughtCertificates;
    }

    public void setBoughtCertificates(List<BoughtCertificateDTO> boughtCertificates) {
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
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(id, orderDTO.id) &&
                Objects.equals(cost, orderDTO.cost) &&
                Objects.equals(time, orderDTO.time) &&
                Objects.equals(boughtCertificates, orderDTO.boughtCertificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, time, boughtCertificates);
    }
}
