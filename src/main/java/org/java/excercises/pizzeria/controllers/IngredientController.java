package org.java.excercises.pizzeria.controllers;

import org.java.excercises.pizzeria.models.Ingredient;
import org.java.excercises.pizzeria.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
public class IngredientController {

    @Autowired
    IngredientRepository ingredientRepository;

    @GetMapping("/ingredients")
    public String index(Model model) {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        model.addAttribute("ingredients", ingredients);
        return "ingredients/index";
    }

    @PostMapping("/ingredients/delete")
    public String delete(@RequestParam("ingredientId") Integer id, Model model) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if (ingredient.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        ingredientRepository.deleteById(id);
        return "redirect:/ingredients";
    }

}
