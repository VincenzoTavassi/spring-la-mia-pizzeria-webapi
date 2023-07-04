package org.java.excercises.pizzeria.service;

import jakarta.validation.Valid;
import org.java.excercises.pizzeria.exceptions.NotUniqueNameException;
import org.java.excercises.pizzeria.exceptions.PizzaNotFoundException;
import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
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

    // Ottieni la pizza in base all'id
    public Pizza getById(Integer id) throws PizzaNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);
        if (foundPizza.isPresent()) return foundPizza.get();
        else throw new PizzaNotFoundException("Pizza not found");
    }

    public Pizza create(Pizza pizza) throws NotUniqueNameException {
        if(!isUniqueName(pizza)) throw new NotUniqueNameException("Pizza name must be unique");
        Pizza pizzaToSave = new Pizza();
        pizzaToSave.setCreatedAt(LocalDateTime.now());
        pizzaToSave.setName(pizza.getName());
        pizzaToSave.setDescription(pizza.getDescription());
        pizzaToSave.setPrice(pizza.getPrice());
        pizzaToSave.setIngredients(pizza.getIngredients());
        pizzaToSave.setOffers(pizza.getOffers());
        pizzaToSave.setPictureUrl(pizza.getPictureUrl());
        return pizzaRepository.save(pizzaToSave);
    }

    public Pizza update(Integer id, Pizza pizza) throws PizzaNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);
        if (foundPizza.isEmpty()) throw new PizzaNotFoundException("Pizza not found");
        Pizza pizzaToSave = new Pizza();
        pizzaToSave.setId(id);
        pizzaToSave.setName(pizza.getName());
        pizzaToSave.setDescription(pizza.getDescription());
        pizzaToSave.setPrice(pizza.getPrice());
        pizzaToSave.setIngredients(pizza.getIngredients());
        pizzaToSave.setOffers(pizza.getOffers());
        pizzaToSave.setPictureUrl(pizza.getPictureUrl());
        return pizzaRepository.save(pizzaToSave);
    }

    public void delete(Integer id) throws PizzaNotFoundException {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);
        if (foundPizza.isEmpty()) throw new PizzaNotFoundException("Pizza not found");
        pizzaRepository.delete(foundPizza.get());
    }

    private boolean isUniqueName(Pizza pizza) {
        Optional<Pizza> foundPizza = pizzaRepository.findByNameIgnoreCase(pizza.getName());
        return foundPizza.isEmpty();
    }
}
