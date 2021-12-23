/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testMELI.testMELI.app.services;

import com.testMELI.testMELI.app.Reports.IndividualTypeRecords;
import com.testMELI.testMELI.app.entities.Records;
import com.testMELI.testMELI.app.repositories.RecordsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author jandr
 */
@Service
public class RecordsService {
    
    @Autowired
    private RecordsRepository repository;
    
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
    public ResponseEntity save(Records records) {
        
        String[] dna = records.getDna();
        String baseNitrogenada = dna[0];
        boolean validarSecuencia = false;
        boolean validarBaseIndividual = false;
        String individualType;
        
        //Validación tamaño de todas las bases, que sean iguales para que se cumpla la matriz.
        for (String dna1 : dna) {
            if (baseNitrogenada.length() != dna1.length()) {
                validarSecuencia = true;
            }
        //Validación de que cada base tenga unicamente las letras: A, C, G o T
            for (int j = 0; j<baseNitrogenada.length(); j++) {
                if (!(dna1.charAt(j) == 'A' || dna1.charAt(j) == 'C' || dna1.charAt(j) == 'G' || dna1.charAt(j) == 'T')) {
                    validarBaseIndividual = true;
                }
            }
        }
        
        if (records.getId() == null && !validarSecuencia && !validarBaseIndividual) {
            if(isMutant(dna, baseNitrogenada)){
                individualType = "mutant";
            }
            else individualType = "human";
            
            records.setIndividualType(individualType);
            repository.save(records);
            return (ResponseEntity) ResponseEntity.status(HttpStatus.OK).body(null);
        } else if(records.getId() != null && !validarSecuencia && !validarBaseIndividual){
            Optional<Records> resultado = repository.getRecords(records.getId());
            if (resultado.isPresent()) {
                return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            } else {
                    if(isMutant(dna, baseNitrogenada)){
                        individualType = "mutant";
                    }
                    else individualType = "human";

                    records.setIndividualType(individualType);
                    repository.save(records);
                    return (ResponseEntity) ResponseEntity.status(HttpStatus.OK).body(null);              
            }
        }
        else {
            return (ResponseEntity) ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
    
    public IndividualTypeRecords getIndividualTypeRecords() {
        List<Records> mutant = repository.RecordsIndividualType("mutant");
        List<Records> human = repository.RecordsIndividualType("human");
        return new IndividualTypeRecords(mutant.size(), human.size(),mutant.size()/human.size());
    }
    
    //Función para validar si la secuencia de dna es de un mutante o un humano.
    public static boolean isMutant(String[] dna, String baseNitrogenada){
        boolean mutant;
        /*desde aquí se llaman las funciones horizontal, vertical y diagonal positiva y negativa, las
        cuales retornan el número de consecutivos.*/
        int horizontal = horizontal(dna);
        int vertical = vertical(dna, baseNitrogenada);
        int diagonal1 = diagonal1(dna, baseNitrogenada);
        int diagonal2 = diagonal2(dna, baseNitrogenada);
        int sumaConsecutivos = horizontal + vertical + diagonal1 + diagonal2;
        
        mutant = sumaConsecutivos>=2;
        
        return mutant;
    }
    
    //Función para validar bases horizontalmente
    public static int horizontal(String [] dna){
        int conteo = 0;
        char letra = ' ';
        int numHorizontal = 0;
        for (String baseNitrogenada : dna) {
            /*Solo se validan las bases que tengan mas de 3 letras, teniendo en cuenta que a está
            función llega el arreglo de diagonales.*/
            if (baseNitrogenada.length()>3) {
                for (int j = 0; j<baseNitrogenada.length(); j++) {
                    if (letra == baseNitrogenada.charAt(j)) {
                        conteo++;
                        if(conteo == 3){
                            numHorizontal++;
//                            System.out.println(dna[i]);
                        }
                    } else {
                        conteo = 0;
                        letra = baseNitrogenada.charAt(j);
                    }
                }
            }
        }
        return numHorizontal;
    }

    //Función para validar bases verticalmente
    public static int vertical(String [] dna, String baseNitrogenada){
        int conteo = 0;
        char letra = ' ';
        int numVertical = 0;
        for(int j=0; j<baseNitrogenada.length(); j++){
            for (String dna1 : dna) {
                if (letra == dna1.charAt(j)) {
                    conteo++;
                    if(conteo == 3){
                        numVertical++;
                    }
                } else {
                    conteo = 0;
                    letra = dna1.charAt(j);
                }
            }
        }
        return numVertical;
    }

    //Función para validar bases diagonales negativas
    public static int diagonal1(String[] dna, String baseNitrogenada){
        //se crea arrayList con el numero de bases posibles en diagonal
        ArrayList<String> diagonal1 = new ArrayList<>();
        for(int i=0; i<(dna.length+baseNitrogenada.length()-1); i++){
            diagonal1.add("");
        }
        String dataPosition;
        //se empiezan a organizar las bases diagonales teniendo en cuenta relación de i con j (relación con resta)
        for(int j=0; j<baseNitrogenada.length(); j++){
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

        /*se convierte el arrayList en un array regular y se envía a la función horizontal, la 
        cual retorna el número de consecutivos encontrados*/
        String[] diagonal = diagonal1.toArray(new String[0]);
        return horizontal(diagonal);
    }
    
    //Función para validar bases diagonales positivas
    public static int diagonal2(String[] dna, String baseNitrogenada){
        //se crea arrayList con el numero de bases posibles en diagonal
        ArrayList<String> diagonal1 = new ArrayList<>();
        for(int i=0; i<(dna.length+baseNitrogenada.length()-1); i++){
            diagonal1.add("");
        }
        String dataPosition;
        //se empiezan a organizar las bases diagonales teniendo en cuenta relación de i con j (relación con suma)
        for(int j=baseNitrogenada.length()-1; j>=0; j--){
            for(int i=0; i<dna.length; i++){
                if(i+j == baseNitrogenada.length()-1){
                    dataPosition = diagonal1.get(i+j);
                    diagonal1.set(i+j, dataPosition+dna[i].charAt(j));
                }
                else if(i+j > baseNitrogenada.length()-1){
                    dataPosition = diagonal1.get(i+j);
                    diagonal1.set(i+j, dataPosition+dna[i].charAt(j));
                }
                else if(i+j < baseNitrogenada.length()-1){
                    dataPosition = diagonal1.get(i+j);
                    diagonal1.set(i+j, dataPosition+dna[i].charAt(j));
                }
            }
        }
        
        /*se convierte el arrayList en un array regular y se envía a la función horizontal, la 
        cual retorna el número de consecutivos encontrados*/
        String[] diagonal = diagonal1.toArray(new String[0]);
        return horizontal(diagonal);
    }

    
    
}
