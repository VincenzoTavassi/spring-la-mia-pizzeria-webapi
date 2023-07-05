package org.java.excercises.pizzeria.controllers;

import jakarta.validation.Valid;
import org.java.excercises.pizzeria.dto.PizzaForm;
import org.java.excercises.pizzeria.exceptions.NotUniqueNameException;
import org.java.excercises.pizzeria.exceptions.PizzaNotFoundException;
import org.java.excercises.pizzeria.messages.Message;
import org.java.excercises.pizzeria.messages.MessageType;
import org.java.excercises.pizzeria.models.Pizza;
import org.java.excercises.pizzeria.repository.IngredientRepository;
import org.java.excercises.pizzeria.repository.PizzaRepository;
import org.java.excercises.pizzeria.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class PizzaController {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    PizzaService pizzaService;

    @Autowired
    private IngredientRepository ingredientRepository;


    // INDEX
    @GetMapping("/pizza")
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
        model.addAttribute("pizza", new PizzaForm());
        model.addAttribute("ingredients", ingredientRepository.findAll());
        return "pizza/edit";
    }

    @PostMapping("/pizza/create")
    public String create(
            @Valid @ModelAttribute("pizza") PizzaForm pizza,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        if(!bindingResult.hasErrors()) {
            try {
                pizzaService.create(pizza);
            } catch (NotUniqueNameException e) {
                bindingResult.addError(new FieldError("pizza", "name", pizza.getName(), false, null, null,
                        "Il nome deve essere unico"));
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("ingredients", ingredientRepository.findAll());
            return "pizza/edit";}

            redirectAttributes.addFlashAttribute("message", new Message(MessageType.SUCCESS, "Pizza creata con successo."));
            return "redirect:/pizza";
        }

    @GetMapping("/pizza/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Pizza foundPizza = pizzaService.getById(id);
        PizzaForm pizzaForm = pizzaService.fromPizzaToPizzaForm(foundPizza);
        model.addAttribute("pizza", pizzaForm);
        model.addAttribute("ingredients", ingredientRepository.findAll());
        return "pizza/edit";
    }

    @PostMapping("/pizza/edit/{id}")
    public String update(
            @PathVariable("id") Integer id,
            @Valid @ModelAttribute("pizza") PizzaForm formPizza,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        Pizza foundPizza = pizzaService.getById(id);
        if(bindingResult.hasErrors()) return "pizza/edit";
        else {
            try {
            pizzaService.update(formPizza);
            } catch (PizzaNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            redirectAttributes.addFlashAttribute("message", new Message(MessageType.SUCCESS, "Pizza modificata con successo."));
            return "redirect:/pizza";
        }
    }

    @PostMapping("/pizza/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Optional<Pizza> foundPizza = pizzaRepository.findById(id);
        if(foundPizza.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        pizzaRepository.delete(foundPizza.get());
        redirectAttributes.addFlashAttribute("message", new Message(MessageType.SUCCESS, "Pizza eliminata con successo."));
        return "redirect:/";
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
