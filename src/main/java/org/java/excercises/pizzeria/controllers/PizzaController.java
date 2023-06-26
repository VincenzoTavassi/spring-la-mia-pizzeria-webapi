package org.java.excercises.pizzeria.controllers;

import jakarta.validation.Valid;
import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class PizzaController {

    @Autowired
    private PizzaRepository pizzaRepository;


    // INDEX
    @GetMapping
    public String index(@RequestParam(name="search", required = false) String searchTerm, Model model) {
        List<Pizza> pizze;
        if (searchTerm == null || searchTerm.isBlank()) {
            pizze = pizzaRepository.findAll();
        } else {
            pizze = pizzaRepository.findByNameContainingIgnoreCase(searchTerm);
        }
        model.addAttribute("pizze", pizze);
        model.addAttribute("searchTerm", searchTerm == null ? "" : searchTerm);
        return "pizza/index";

    }

    // SHOW
    @GetMapping("/pizza/{id}")
    public String show(Model model, @PathVariable String id) {
        Pizza pizza = null;
        if(isNumeric(id)) { // Se è numerico, trovo la pizza in base all'ID
            Integer pizzaId = Integer.valueOf(id);
            if (pizzaId > pizzaRepository.count() || pizzaId <= 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            pizza = pizzaRepository.getReferenceById(pizzaId);
        } else { // Altrimenti cerco la pizza in base al nome
            Optional<Pizza> foundPizza = pizzaRepository.findByNameIgnoreCase(id);
            if (foundPizza.isPresent()) pizza = foundPizza.get();
        }
        if (pizza == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        model.addAttribute("pizza", pizza);
        return "pizza/show";
        }

        // CREATE / STORE
    @GetMapping("/pizza/create")
    public String create(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "pizza/create";
    }

    @PostMapping("/pizza/create")
    public String create(@Valid @ModelAttribute("pizza") Pizza pizza, BindingResult bindingResult) {
        // Se il nome della pizza non è unico, aggiungo un errore
        if(!isUniqueName(pizza)) bindingResult.addError(new FieldError("pizza", "name", pizza.getName(), false, null, null, "Il nome della pizza deve essere unico"));
        if(bindingResult.hasErrors()) return "pizza/create";
        else {
            pizza.setCreatedAt(LocalDateTime.now());
        pizzaRepository.save(pizza);
        return "redirect:/";
        }
    }

        // CUSTOM METHODS
    private boolean isNumeric(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isUniqueName(Pizza pizza) {
        Optional<Pizza> foundPizza = pizzaRepository.findByNameIgnoreCase(pizza.getName());
        return foundPizza.isEmpty();
    }

}
