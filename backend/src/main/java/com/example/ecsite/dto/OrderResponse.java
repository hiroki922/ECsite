package com.example.ecsite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String addressName;
    private String addressPostalCode;
    private String addressPrefecture;
    private String addressCity;
    private String addressLine;
    private String addressPhone;
    private Double totalAmount;
    private String status;
    private String createdAt;
    private List<OrderItemResponse> items;
}
