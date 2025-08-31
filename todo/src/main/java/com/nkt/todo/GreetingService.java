package com.nkt.todo;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    private final GreetingRepository repo;

    public GreetingService(GreetingRepository repo) {
        this.repo = repo;
    }

    public GreetingEntity addGreeting(String message) {
        return repo.saveAndFlush(new GreetingEntity(message));
    }

    public GreetingEntity getGreetingById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Greeting not found with id: " + id));
    }

    public List<GreetingEntity> getAllGreetings() {
        return repo.findAll();
    }

    public void deleteGreetingById(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Greeting not found with id: " + id);
        }
        repo.deleteById(id);
    }

    public GreetingEntity updateGreeting(Long id, String message) {
        GreetingEntity entity = getGreetingById(id); // Reuse getById to get the entity or throw
        entity.setMessage(message);
        return repo.save(entity);
    }
}
