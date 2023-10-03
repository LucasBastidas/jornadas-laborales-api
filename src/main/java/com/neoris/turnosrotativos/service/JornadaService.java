package com.neoris.turnosrotativos.service;

import com.neoris.turnosrotativos.dto.JornadaRequestDTO;
import com.neoris.turnosrotativos.dto.JornadaResponseDTO;
import com.neoris.turnosrotativos.entity.Jornada;

import java.time.LocalDate;
import java.util.List;

public interface JornadaService {
    public JornadaResponseDTO createJornada(JornadaRequestDTO jornadaRequest);
    public List<JornadaResponseDTO>getJornadas();
    public List<JornadaResponseDTO>getJornadasByNroDocumento(Long nroDocumento);
    public List<JornadaResponseDTO>getJornadasByFecha(LocalDate fecha);
    public List<JornadaResponseDTO>getJornadasByNroDocumentoAndFecha(Long nroDocumento, LocalDate fecha);
}
