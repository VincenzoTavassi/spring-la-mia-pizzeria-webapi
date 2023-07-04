package org.java.excercises.pizzeria.api;

import jakarta.validation.Valid;
import org.java.excercises.pizzeria.exceptions.NotUniqueNameException;
import org.java.excercises.pizzeria.exceptions.PizzaNotFoundException;
import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/v1/pizza")
public class PizzaRestController {

@Autowired
    PizzaService pizzaService;

    @GetMapping
    public List<Pizza> index(@RequestParam Optional<String> keyword) {
        return pizzaService.getAll(keyword);
    }

    @GetMapping("/{id}")
    public Pizza show(@PathVariable Integer id) {
        try {
            return pizzaService.getById(id);
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
    }

    @PostMapping("/create")
    public Pizza create(@Valid @RequestBody Pizza pizza) {
        try {
            return pizzaService.create(pizza);
        } catch (NotUniqueNameException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public Pizza update(@Valid @RequestBody Pizza pizza, @PathVariable Integer id) {
        try {
            return pizzaService.update(id, pizza);
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
