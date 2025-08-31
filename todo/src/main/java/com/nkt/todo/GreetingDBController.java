package com.nkt.todo;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class GreetingDBController {
    private final GreetingService gService;

    public GreetingDBController(GreetingService gService) {
        this.gService = gService;
    }

    @PostMapping("/greetings")
    @ResponseStatus(HttpStatus.CREATED)
    public GreetingEntity addGreeting(@RequestBody @Valid GreetingDTO dto) {
        return gService.addGreeting(dto.getMessage());
    }

    @GetMapping("/greetings")
    public List<GreetingEntity> getAllGreetings() {
        return gService.getAllGreetings();
    }
    
    @GetMapping("/greetings/{id}")
    public GreetingEntity getGreetingById(@PathVariable Long id) {
        return gService.getGreetingById(id);
    }

    @DeleteMapping("/greetings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGreetingById(@PathVariable Long id) {
        gService.deleteGreetingById(id);
    }

    @PutMapping("/greetings/{id}")
    public GreetingEntity updateGreeting(@PathVariable Long id, @RequestBody @Valid GreetingDTO dto) {
        return gService.updateGreeting(id, dto.getMessage());
    }

}
