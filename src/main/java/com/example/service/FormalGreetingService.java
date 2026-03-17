package com.example.service;

import com.example.GreetingException;
import com.example.model.GreetingModel;
import com.example.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;

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
     */
    @Override
    public GreetingModel findByName(String name) {
        return greetingRepository.findByName(name);
    }

    /**
     * Creates a new greeting.
     * @throws GreetingException if record already exists
     */
    @Override
    public GreetingModel create(GreetingModel greetingModel) {
        // First verify it does not exist
        GreetingModel findGreetingModel = greetingRepository.findByName(greetingModel.getName());
        if (findGreetingModel == null) {
        	return greetingRepository.save(greetingModel);
        } else {
        	throw new GreetingException("Record already exists with name: " + greetingModel.getName());
        }
    }

    /**
     * Updates an existing greeting.
     * @throws GreetingExistsException if record not found
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
        } else {
        	throw new GreetingException("Unable to find record: " + greetingModel.getName());
        }
    }

    /**
     * Deletes a greeting by ID.
     * @return rows affected (1 if successful, otherwise 0)
     */
    @Override
    public int delete(String name) {
        return greetingRepository.delete(name);
    }
}
