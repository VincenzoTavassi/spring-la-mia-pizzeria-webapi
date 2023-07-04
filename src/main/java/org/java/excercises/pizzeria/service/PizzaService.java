package org.java.excercises.pizzeria.service;

import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {

    @Autowired
    PizzaRepository pizzaRepository;

    // Metodo di get per tutte le pizze che può avere o meno un search term
    public List<Pizza> getAll(@RequestParam Optional<String> keyword) {
        if (keyword.isPresent()) return pizzaRepository.findByNameContainingIgnoreCase(keyword.get());
        else return pizzaRepository.findAll();
    }
}
