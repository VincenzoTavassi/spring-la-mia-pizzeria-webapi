package org.java.excercises.pizzeria.controllers;

import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

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
    public String show(Model model, @PathVariable String id) {
        Pizza pizza;
        if(isNumeric(id)) {
            Integer pizzaId = Integer.valueOf(id);
            if (pizzaId > pizzaRepository.count() || pizzaId <= 0) return "redirect:/";
            pizza = pizzaRepository.getReferenceById(pizzaId);
        } else {
            try {
            pizza = pizzaRepository.findByNameIgnoreCase(id);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
            }
        if (pizza == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        model.addAttribute("pizza", pizza);
            return "pizza/show";
        }

    private boolean isNumeric(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
