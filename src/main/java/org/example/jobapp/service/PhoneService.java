package org.example.jobapp.service;

import lombok.RequiredArgsConstructor;
import org.example.jobapp.model.Phone;
import org.example.jobapp.repository.PhoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneService {
    private final PhoneRepository phoneRepository;

    public Phone savePhone(Phone phone) {
        return phoneRepository.save(phone);
    }

    public List<Phone> listPhone() {
        return phoneRepository.findAll();
    }
}
