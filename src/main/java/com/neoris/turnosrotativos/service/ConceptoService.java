package com.neoris.turnosrotativos.service;

import com.neoris.turnosrotativos.entity.Concepto;

import java.util.List;
import java.util.Optional;

public interface ConceptoService {
    public Optional<Concepto> getConceptoById(Long id);

    public List<Concepto> getConceptos();
}
