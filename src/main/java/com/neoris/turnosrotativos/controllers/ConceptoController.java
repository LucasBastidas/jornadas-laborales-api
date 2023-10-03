package com.neoris.turnosrotativos.controllers;

import com.neoris.turnosrotativos.entity.Concepto;
import com.neoris.turnosrotativos.service.ConceptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class ConceptoController  {

    @Autowired
    ConceptoService conceptoService;

    @GetMapping("/concepto")//DEVUELVE TODOS LOS CONCEPTOS
    public ResponseEntity<List<Concepto>> getConceptos(){
        return ResponseEntity.ok(this.conceptoService.getConceptos());
    }



}
