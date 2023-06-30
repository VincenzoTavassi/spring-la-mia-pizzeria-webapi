package org.java.excercises.pizzeria.repository;

import org.java.excercises.pizzeria.models.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
}
