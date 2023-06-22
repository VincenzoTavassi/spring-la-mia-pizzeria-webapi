package org.java.excercises.pizzeria.repository;

import org.java.excercises.pizzeria.models.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
}
