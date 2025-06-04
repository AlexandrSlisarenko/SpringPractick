package ru.slisarenko.springpractick.db.repositary;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.slisarenko.springpractick.db.entity.Company;

import java.util.List;
import java.util.Optional;


public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByName(String name);

    List<Company> findAllByNameContainingIgnoreCase(String name);
}
