package com.neoris.turnosrotativos.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "jornadas")
public class Jornada {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne//HACEMOS LA RELACION CON EMPLEADO
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    @ManyToOne//HACEMOS LA RELACION CON CONCEPTO
    @JoinColumn(name = "concepto_id")
    private Concepto concepto;

    private Integer hsTrabajadas;

    private LocalDate fecha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public Integer getHsTrabajadas() {
        return hsTrabajadas;
    }

    public void setHsTrabajadas(Integer hsTrabajadas) {
        this.hsTrabajadas = hsTrabajadas;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
