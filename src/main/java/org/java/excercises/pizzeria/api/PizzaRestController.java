package org.java.excercises.pizzeria.api;

import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
