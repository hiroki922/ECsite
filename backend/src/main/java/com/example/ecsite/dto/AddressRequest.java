package com.example.ecsite.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String name;
    private String postalCode;
    private String prefecture;
    private String city;
    private String addressLine;
    private String phone;
    private Boolean isDefault;
}
