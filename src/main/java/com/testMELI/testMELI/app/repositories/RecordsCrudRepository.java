/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testMELI.testMELI.app.repositories;

import com.testMELI.testMELI.app.entities.Records;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author jandr
 */
public interface RecordsCrudRepository extends CrudRepository<Records, Integer> {
    
    public List<Records> findAllBySpecimen(String status);
}
