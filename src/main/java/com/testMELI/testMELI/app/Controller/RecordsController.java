/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testMELI.testMELI.app.Controller;

import com.testMELI.testMELI.app.Reports.IndividualTypeRecords;
import com.testMELI.testMELI.app.entities.Records;
import com.testMELI.testMELI.app.services.RecordsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jandr
 */
@RestController
//@RequestMapping("mutant")
@CrossOrigin(origins = "*")
public class RecordsController {
    
    @Autowired
    private RecordsService recordsService;
    
    /**
     * GET
     * @return 
     */
    @GetMapping("/all")
    public List<Records> getRecords(){
        return recordsService.getAll();
    }
    
    /**
     * POST
     * @param records
     * @return 
     */
    @PostMapping("/mutant")
    @ResponseBody
    public ResponseEntity save(@RequestBody Records records){
        if(recordsService.save(records)){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }
    
    @GetMapping("/stats")
    public IndividualTypeRecords getSpecimen(){
        return recordsService.getSpecimen();
    }
    
}
