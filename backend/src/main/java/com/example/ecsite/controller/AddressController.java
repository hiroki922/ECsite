package com.example.ecsite.controller;

import com.example.ecsite.dto.AddressResponse;
import com.example.ecsite.dto.AddressRequest;
import com.example.ecsite.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService service;

    @GetMapping
    public List<AddressResponse> list() {
        return service.findByUser();
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(@RequestBody AddressRequest request) {
        AddressResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public AddressResponse update(@PathVariable Long id, @RequestBody AddressRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/{id}/default")
    public AddressResponse setDefault(@PathVariable Long id) {
        return service.setDefault(id);
    }
}
