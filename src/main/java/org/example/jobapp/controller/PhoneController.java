package org.example.jobapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.jobapp.model.Phone;
import org.example.jobapp.service.PhoneService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/phone")
public class PhoneController {
    private final PhoneService phoneService;

    @GetMapping
    // public api
    public List<Phone> getAll() {
        return phoneService.listPhone();
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public Phone postPhone(@RequestBody Phone phone) {
        return phoneService.savePhone(phone);
    }
}
