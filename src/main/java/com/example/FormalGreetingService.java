package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class FormalGreetingService implements GreetingService {
    private final GreetingRepository greetingRepository;

    @Autowired
    public FormalGreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    /**
     * Returns all greetings.
     */
    @Override
    public List<GreetingModel> findAll() {
        return greetingRepository.findAll();
    }

    /**
     * Finds a greeting by name.
     * @throws GreetingNotFoundException if not found
     */
    @Override
    public GreetingModel findByName(String name) {
        return greetingRepository.findByName(name);
    }

    /**
     * Creates a new greeting.
     */
    @Override
    public GreetingModel create(GreetingModel greetingModel) {
        return greetingRepository.save(greetingModel);
    }

    /**
     * Updates an existing greeting.
     * @throws GreetingNotFoundException if not found
     */
    @Override
    public GreetingModel update(GreetingModel greetingModel) {
        // First verify it exists
        GreetingModel findGreetingModel = greetingRepository.findByName(greetingModel.getName());
        if (findGreetingModel != null) {
            // Set the ID from the parameter (don't trust the request body)
            greetingModel.setId(findGreetingModel.getId());
            greetingRepository.update(greetingModel);
            // Return the updated greeting
            return greetingRepository.findByName(greetingModel.getName());
        }
        return null;
    }

    /**
     * Deletes a greeting by ID.
     * @throws GreetingNotFoundException if not found
     * @return rows affected (1 if successful, otherwise 0)
     */
    @Override
    public int delete(String name) {
        return greetingRepository.delete(name);
    }
}
