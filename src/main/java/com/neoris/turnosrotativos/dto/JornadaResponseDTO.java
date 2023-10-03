package com.neoris.turnosrotativos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.neoris.turnosrotativos.entity.Jornada;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JornadaResponseDTO {
    private Long id;
    private Long nroDocumento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(Long nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public int getHsTrabajadas() {
        return hsTrabajadas;
    }

    public void setHsTrabajadas(Integer hsTrabajadas) {
        this.hsTrabajadas = hsTrabajadas;
    }

    private String nombreCompleto;
    private LocalDate fecha;
    private String concepto;
    private Integer hsTrabajadas = null;

    //TRANSFORMA UNA JORNADA EN JORNADARESPONSEDTO
    public static JornadaResponseDTO mapJornadaToResponseDTO(Jornada jornada) {
        JornadaResponseDTO jornadaResponseDTO = new JornadaResponseDTO();

        jornadaResponseDTO.setId(jornada.getId());
        jornadaResponseDTO.setNroDocumento(jornada.getEmpleado().getNroDocumento());
        jornadaResponseDTO.setNombreCompleto(jornada.getEmpleado().getNombre()+ " " + jornada.getEmpleado().getApellido());
        jornadaResponseDTO.setFecha(jornada.getFecha());
        jornadaResponseDTO.setConcepto(jornada.getConcepto().getNombre());
        jornadaResponseDTO.setHsTrabajadas(jornada.getHsTrabajadas());

        return jornadaResponseDTO;
    }

    //HAGO UNA LISTA DE JORNADASRESPONSEDTO
    public static List<JornadaResponseDTO> mapJornadasToResponseDTO(List<Jornada> jornadas){
        List<JornadaResponseDTO> jornadaResponseDTOs = new ArrayList<>();

        for (Jornada jornada : jornadas) {
            JornadaResponseDTO jornadaResponseDTO = mapJornadaToResponseDTO(jornada);
            jornadaResponseDTOs.add(jornadaResponseDTO);
        }

        return jornadaResponseDTOs;

    }


}
