package com.neoris.turnosrotativos.service.impl;

import com.neoris.turnosrotativos.entity.Concepto;
import com.neoris.turnosrotativos.exceptions.EmpleadoBusinessException;
import com.neoris.turnosrotativos.repository.ConceptoRepository;
import com.neoris.turnosrotativos.service.ConceptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConceptoServiceImpl implements ConceptoService {

    @Autowired
    ConceptoRepository repository;

    @Override
    public Optional<Concepto> getConceptoById(Long id) {
        Optional<Concepto> conceptoById = repository.findById(id);

        if (conceptoById.isEmpty()){
            throw new EmpleadoBusinessException("No existe un concepto con el id: "+id, HttpStatus.NOT_FOUND);
        }else {
            return conceptoById;
        }

    }

    @Override
    public List<Concepto> getConceptos() {
        return repository.findAll();
    }
}
