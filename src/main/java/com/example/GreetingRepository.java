package com.example;

import java.util.Optional;

public interface GreetingRepository {
	public Optional<GreetingModel> findByName(String name);
}
