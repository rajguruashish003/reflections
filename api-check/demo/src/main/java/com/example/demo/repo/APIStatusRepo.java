package com.example.demo.repo;


import com.example.demo.model.APIStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface APIStatusRepo extends CrudRepository<APIStatus, UUID>{
}
