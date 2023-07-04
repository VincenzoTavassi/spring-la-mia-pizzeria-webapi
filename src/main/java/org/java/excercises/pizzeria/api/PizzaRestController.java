package org.java.excercises.pizzeria.api;

import jakarta.validation.Valid;
import org.java.excercises.pizzeria.exceptions.NotUniqueNameException;
import org.java.excercises.pizzeria.exceptions.PizzaNotFoundException;
import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/v1/pizza")
public class PizzaRestController {

@Autowired
    PizzaService pizzaService;

    @GetMapping
    public Page<Pizza> index(
            @RequestParam Optional<String> keyword,
            @RequestParam(defaultValue = "2") Integer size,
            @RequestParam(defaultValue = "0") Integer page
    ) {
    return pizzaService.getAll(keyword, size, page);
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

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        try {
            pizzaService.delete(id);
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
