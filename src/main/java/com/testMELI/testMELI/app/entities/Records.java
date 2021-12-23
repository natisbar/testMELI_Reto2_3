/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testMELI.testMELI.app.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author jandr
 */
@Data  //trae getters y setters
@AllArgsConstructor  //Constructor con todos los argumentos
@NoArgsConstructor   //Constructor vacio
@Entity
@Table(name="registros")
public class Records implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)  //genera automáticamente el ID
    private Integer id;
    private String[] dna;
    private String individualType;
}
