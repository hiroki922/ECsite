package com.example.ecsite.service;

import com.example.ecsite.dto.AddressRequest;
import com.example.ecsite.dto.AddressResponse;
import com.example.ecsite.model.Address;
import com.example.ecsite.model.User;
import com.example.ecsite.repository.AddressRepository;
import com.example.ecsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AddressResponse> findByUser() {
        User user = getCurrentUser();
        return addressRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse create(AddressRequest request) {
        User user = getCurrentUser();

        // デフォルト設定がtrueの場合、既存のデフォルトを解除
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            clearDefault(user.getId());
        }

        Address address = toEntity(request);
        address.setUser(user);
        Address saved = addressRepository.save(address);
        return toResponse(saved);
    }

    @Transactional
    public AddressResponse update(Long id, AddressRequest request) {
        User user = getCurrentUser();
        Address address = addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("住所が見つかりません: " + id));

        address.setName(request.getName());
        address.setPostalCode(request.getPostalCode());
        address.setPrefecture(request.getPrefecture());
        address.setCity(request.getCity());
        address.setAddressLine(request.getAddressLine());
        address.setPhone(request.getPhone());

        Address saved = addressRepository.save(address);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        User user = getCurrentUser();
        Address address = addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("住所が見つかりません: " + id));
        addressRepository.delete(address);
    }

    @Transactional
    public AddressResponse setDefault(Long id) {
        User user = getCurrentUser();

        // 既存のデフォルトを解除
        clearDefault(user.getId());

        // 対象をデフォルトに設定
        Address target = addressRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("住所が見つかりません: " + id));
        target.setIsDefault(true);
        return toResponse(addressRepository.save(target));
    }

    private void clearDefault(Long userId) {
        addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .ifPresent(addr -> {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                });
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("ログインユーザーが見つかりません"));
    }

    private Address toEntity(AddressRequest req) {
        Address a = new Address();
        a.setName(req.getName());
        a.setPostalCode(req.getPostalCode());
        a.setPrefecture(req.getPrefecture());
        a.setCity(req.getCity());
        a.setAddressLine(req.getAddressLine());
        a.setPhone(req.getPhone());
        a.setIsDefault(Boolean.TRUE.equals(req.getIsDefault()));
        return a;
    }

    private AddressResponse toResponse(Address a) {
        AddressResponse r = new AddressResponse();
        r.setId(a.getId());
        r.setName(a.getName());
        r.setPostalCode(a.getPostalCode());
        r.setPrefecture(a.getPrefecture());
        r.setCity(a.getCity());
        r.setAddressLine(a.getAddressLine());
        r.setPhone(a.getPhone());
        r.setIsDefault(a.getIsDefault());
        return r;
    }
}
