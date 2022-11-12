package dev.jpcasas.repositories;

import org.springframework.data.repository.CrudRepository;

import dev.jpcasas.model.Configuration;

public interface ConfigurationRepo extends CrudRepository<Configuration, String>{
    
    
}
