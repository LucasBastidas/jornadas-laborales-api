package com.neoris.turnosrotativos.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class JornadaRequestDTO {
    @NotNull(message = "idEmpleado es obligatorio")
    private Long idEmpleado;
    @NotNull(message = "idConcepto es obligatorio")
    private Long idConcepto;
    @NotNull(message = "fecha es obligatorio")
    private LocalDate fecha;

    private int horasTrabajadas;

    public Long getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Long idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Long getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(Long idConcepto) {
        this.idConcepto = idConcepto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }
}
