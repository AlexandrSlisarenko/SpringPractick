package ru.slisarenko.springpractick.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;



@NamedQuery(
        name = "Company.findByName",
        query = "select c from Company c where lower(c.name) = lower(:name)"
)
@Entity
@Table(name = "company", schema = "data_jpa")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = "locales")
public class Company implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "company_locales", schema = "data_jpa",
            joinColumns = @JoinColumn(name = "company_id"))
    @MapKeyColumn(name = "lang")
    @Column(name = "description")
    private Map<String, String> locales = new HashMap<>();
}
