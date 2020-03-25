package com.epam.esm.dto.deserialize;

import com.epam.esm.dto.BoughtCertificateDTO;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDTODeserializer extends StdDeserializer<OrderDTO> {

    public OrderDTODeserializer() {
        this(null);
    }

    public OrderDTODeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public OrderDTO deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

        List<Long> list = new ArrayList<>();
        Iterator<JsonNode> iterator = node.get("certificates").elements();
        while (iterator.hasNext()) {
            Long id = iterator.next().longValue();
            list.add(id);
        }

        List<BoughtCertificateDTO> certificates = list.stream()
                .map(value -> {
                    CertificateDTO certificate = new CertificateDTO();
                    certificate.setId(value);
                    BoughtCertificateDTO boughtCertificateDTO = new BoughtCertificateDTO();
                    boughtCertificateDTO.setCertificate(certificate);
                    return boughtCertificateDTO;
                }).collect(Collectors.toList());
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBoughtCertificates(certificates);
        return orderDTO;
    }
}
