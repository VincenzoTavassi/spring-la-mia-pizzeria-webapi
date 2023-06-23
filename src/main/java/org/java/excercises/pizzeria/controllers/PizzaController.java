package org.java.excercises.pizzeria.controllers;

import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class PizzaController {

    @Autowired
    private PizzaRepository pizzaRepository;

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

    @GetMapping("/pizza/{id}")
    public String show(Model model, @PathVariable Integer id) {
        if (id > pizzaRepository.count() || id <= 0) return "redirect:/";
        Pizza pizza = pizzaRepository.getReferenceById(id);
        model.addAttribute("pizza", pizza);
        return "pizza/show";
    }
}
