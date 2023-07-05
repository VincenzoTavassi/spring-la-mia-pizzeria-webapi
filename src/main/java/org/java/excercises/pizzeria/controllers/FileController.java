package org.java.excercises.pizzeria.controllers;

import org.java.excercises.pizzeria.exceptions.PizzaNotFoundException;
import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/files")
public class FileController {

    @Autowired
    PizzaService pizzaService;

    @GetMapping("/{id}")

    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        try {
            Pizza foundPizza = pizzaService.getById(id);
            MediaType mediaType = MediaType.IMAGE_JPEG;
            return ResponseEntity.ok().contentType(mediaType).body(foundPizza.getDBimage());
        } catch (PizzaNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
