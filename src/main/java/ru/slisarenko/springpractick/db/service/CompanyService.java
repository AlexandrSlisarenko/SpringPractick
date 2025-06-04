package ru.slisarenko.springpractick.db.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.slisarenko.springpractick.db.repositary.CompanyRepository;
import ru.slisarenko.springpractick.dto.CompanyReadDto;
import ru.slisarenko.springpractick.listner.entity.AccessType;
import ru.slisarenko.springpractick.listner.entity.EntityEvent;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyService {

    private final ApplicationEventPublisher eventPublisher;
    private final CompanyRepository companyRepository;



    public Optional<CompanyReadDto> findById(Long id) {
        return companyRepository.findById(id)
                .map(entity -> {
                    var result = new CompanyReadDto(entity.getId());
                    eventPublisher.publishEvent(new EntityEvent(result, AccessType.READ));
                    return result;
                });
    }

    public List<CompanyReadDto> getCompanys(){
        return companyRepository.findAll().stream()
                .map(entity -> {
                    var result = new CompanyReadDto(entity.getId());
                    eventPublisher.publishEvent(new EntityEvent(result, AccessType.READ));
                    return result;
                }).toList();
    }
}
