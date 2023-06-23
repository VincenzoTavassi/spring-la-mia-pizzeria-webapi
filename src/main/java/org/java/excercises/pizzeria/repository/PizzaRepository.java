package org.java.excercises.pizzeria.repository;

import org.java.excercises.pizzeria.models.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {

    List<Pizza> findByNameContainingIgnoreCase(String searchTerm);
    Optional<Pizza> findByNameIgnoreCase(String searchTerm);

}
