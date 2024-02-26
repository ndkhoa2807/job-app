package org.example.jobapp.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.jobapp.model.Phone;
import org.example.jobapp.service.PhoneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/phone")
@SecurityRequirement(name = "Keycloak")
public class PhoneController {
    private final PhoneService phoneService;

    @GetMapping("public/list")
    // public api
    public List<Phone> getAll() {
        return phoneService.listPhone();
    }

    @PostMapping("/create-phone")
//    @PreAuthorize("hasRole('admin')")
    public Phone postPhone(@RequestBody Phone phone) {
        return phoneService.savePhone(phone);
    }
}
