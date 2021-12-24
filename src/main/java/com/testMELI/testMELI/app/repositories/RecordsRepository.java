/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testMELI.testMELI.app.repositories;

import com.testMELI.testMELI.app.entities.Records;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jandr
 */
@Repository
public class RecordsRepository {
    @Autowired
    private RecordsCrudRepository recordsCrudRepository;
    
    
    public List<Records> getAll() {
        return (List<Records>) recordsCrudRepository.findAll();
    }
    
    public Records save(Records records) {
        return recordsCrudRepository.save(records);
    }

    public Optional<Records> getRecords(int recordsId){
        return recordsCrudRepository.findById(recordsId);
    }
    
    public List<Records> RecordsSpecimen(String recordsSpecimen) {
        return recordsCrudRepository.findAllBySpecimen(recordsSpecimen);
    }

}
