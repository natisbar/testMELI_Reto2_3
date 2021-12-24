/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testMELI.testMELI.app.services;

import com.testMELI.testMELI.app.Reports.specimenRecords;
import com.testMELI.testMELI.app.entities.Records;
import com.testMELI.testMELI.app.repositories.RecordsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author jandr
 */
@Service
public class RecordsService {
    
    @Autowired
    private RecordsRepository repository;
    static final int SEQUENCES_NUMBER_LETTERS = 3;
    static final int CONSECUTIVE_NUMBER = 2;
    
    /**
     * GET
     *
     * @return
     */
    public List<Records> getAll() {
        return repository.getAll();
    }
    
    /**
     * POST
     *
     * @param records
     * @return
     */
    public boolean save(Records records) {
        
        String[] dna = records.getDna();
        String nitrogenBase = dna[0];
        boolean validateSequence = false;
        boolean validateNitrogenBase = false;
        String specimen;
        
        //Size validation of all bases. It is necessary for a matrix to exist
        for (String dna1 : dna) {
            if (nitrogenBase.length() != dna1.length()) {
                validateSequence = true;
            }
        //Validation of the letters of each base: A, C, G o T
            for (int j = 0; j<nitrogenBase.length(); j++) {
                if (!(dna1.charAt(j) == 'A' || dna1.charAt(j) == 'C' || dna1.charAt(j) == 'G' || dna1.charAt(j) == 'T')) {
                    validateNitrogenBase = true;
                }
            }
        }
        
        if (records.getId() == null && !validateSequence && !validateNitrogenBase) {
            if(isMutant(dna, nitrogenBase)){
                specimen = "mutant";
            }
            else specimen = "human";
            
            records.setSpecimen(specimen);
            repository.save(records);
            return true;
        } else if(records.getId() != null && !validateSequence && !validateNitrogenBase){
            Optional<Records> resultado = repository.getRecords(records.getId());
            if (resultado.isPresent()) {
                return false;
            } else {
                    if(isMutant(dna, nitrogenBase)){
                        specimen = "mutant";
                    }
                    else specimen = "human";

                    records.setSpecimen(specimen);
                    repository.save(records);
                    return true;              
            }
        }
        else {
            return false;
        }
    }
    
    public specimenRecords getSpecimen() {
        double ratio;
        List<Records> mutant = repository.RecordsSpecimen("mutant");
        List<Records> human = repository.RecordsSpecimen("human");
        if(human.isEmpty()){
            ratio = 0;
        }
        else ratio = mutant.size()/human.size();
        return new specimenRecords(mutant.size(), human.size(), ratio);
    }
    
    //Method to validate if the DNA sequence is from a mutant or a human.
    public static boolean isMutant(String[] dna, String nitrogenBase){
        int consecutiveSum = 0;
        /*from this function, the functions horizontal, vertical and diagonal(1 and 2) are called. 
        These functions return the number of consecutive found.
        If in any of the first methods 2 or more nitrogen bases are found, then the other 
        remaining methods are not called*/
        consecutiveSum = consecutiveSum + horizontal(dna);
        if(consecutiveSum>=CONSECUTIVE_NUMBER){
            return true;
        }
        else{
            consecutiveSum = consecutiveSum + vertical(dna, nitrogenBase);
            if(consecutiveSum>=CONSECUTIVE_NUMBER){
                return true;
            }
            else {
                consecutiveSum = consecutiveSum + diagonal1(dna, nitrogenBase);
                if(consecutiveSum>=CONSECUTIVE_NUMBER){
                    return true;
                }
                else{
                    consecutiveSum = consecutiveSum + diagonal2(dna, nitrogenBase);
                    return consecutiveSum>=CONSECUTIVE_NUMBER;
                }
            } 
        }
    }
    
    //Method to validate nitrogen bases horizontally
    private static int horizontal(String [] dna){
        int count = 0;
        char letter = ' ';
        int numHorizontal = 0;
        for (String nitrogenBase : dna) {
            /*Only nitrogenous bases that have more than 3 letters are validated. This method receive
            the array formed by the diagonals*/
            if (nitrogenBase.length()>SEQUENCES_NUMBER_LETTERS) {
                for (int j = 0; j<nitrogenBase.length(); j++) {
                    if (letter == nitrogenBase.charAt(j)) {
                        count++;
                        if(count == SEQUENCES_NUMBER_LETTERS){
                            numHorizontal++;
                        }
                    } else {
                        count = 0;
                        letter = nitrogenBase.charAt(j);
                    }
                }
            }
        }
        return numHorizontal;
    }

    //Function to validate nitrogenous bases vertically
    private static int vertical(String [] dna, String nitrogenBase){
        int count = 0;
        char letter = ' ';
        int numVertical = 0;
        for(int j=0; j<nitrogenBase.length(); j++){
            for (String dna1 : dna) {
                if (letter == dna1.charAt(j)) {
                    count++;
                    if(count == SEQUENCES_NUMBER_LETTERS){
                        numVertical++;
                    }
                } else {
                    count = 0;
                    letter = dna1.charAt(j);
                }
            }
        }
        return numVertical;
    }

    //Method to validate nitrogenous bases of negative diagonals
    private static int diagonal1(String[] dna, String nitrogenBase){
        //an arraylist is created with the number of possible nitrogenous bases diagonally.
        ArrayList<String> diagonal1 = new ArrayList<>();
        for(int i=0; i<(dna.length+nitrogenBase.length()-1); i++){
            diagonal1.add("");
        }
        String dataPosition;
        //Diagonal bases are organized according to the relation of i with j (relation with subtraction)
        for(int j=0; j<nitrogenBase.length(); j++){
            for(int i=0; i<dna.length; i++){
                if(i==j){
                    dataPosition = diagonal1.get(0);
                    diagonal1.set(0, dataPosition+dna[i].charAt(j));
                }
                else if(i>j){
                    dataPosition = diagonal1.get(i-j);
                    diagonal1.set(i-j, dataPosition+dna[i].charAt(j));
                }
                else if(j>i){
                    dataPosition = diagonal1.get((dna.length-1)+(j-i));
                    diagonal1.set((dna.length-1)+(j-i), dataPosition+dna[i].charAt(j));
                }
            }
        }

        /*convert the arraylist to array, and send it to the horizontal method*/
        String[] diagonal = diagonal1.toArray(new String[0]);
        return horizontal(diagonal);
    }
    
    //Method to validate nitrogenous bases of positive diagonals
    private static int diagonal2(String[] dna, String nitrogenBase){
        //an arraylist is created with the number of possible nitrogenous bases diagonally.
        ArrayList<String> diagonal1 = new ArrayList<>();
        for(int i=0; i<(dna.length+nitrogenBase.length()-1); i++){
            diagonal1.add("");
        }
        String dataPosition;
        //Diagonal bases are organized according to the relation of i with j (relation with sum)
        for(int j=nitrogenBase.length()-1; j>=0; j--){
            for(int i=0; i<dna.length; i++){
                if(i+j == nitrogenBase.length()-1){
                    dataPosition = diagonal1.get(i+j);
                    diagonal1.set(i+j, dataPosition+dna[i].charAt(j));
                }
                else if(i+j > nitrogenBase.length()-1){
                    dataPosition = diagonal1.get(i+j);
                    diagonal1.set(i+j, dataPosition+dna[i].charAt(j));
                }
                else if(i+j < nitrogenBase.length()-1){
                    dataPosition = diagonal1.get(i+j);
                    diagonal1.set(i+j, dataPosition+dna[i].charAt(j));
                }
            }
        }
        
        /*convert the arraylist to array, and send it to the horizontal method*/
        String[] diagonal = diagonal1.toArray(new String[0]);
        return horizontal(diagonal);
    }

    
    
}
