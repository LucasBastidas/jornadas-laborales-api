package com.neoris.turnosrotativos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity(name = "empleados")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull(message = "nro de documento es obligatorio")
    @Column(name = "nro_documento")
    private long nroDocumento;
    @NotNull (message = "nombre es obligatorio")
    @NotBlank(message = "nombre no puede estar vacío")
    @Column(length = 15,nullable = false)
    private String nombre;
    @NotNull (message = "apellido es obligatorio")
    @NotBlank(message = "apellido no puede estar vacío")
    @Column(length = 15,nullable = false)
    private String apellido;
    @NotNull (message = "email es obligatorio")
    @NotBlank(message = "email no puede estar vacío")
    @Column(length = 30,nullable = false)
    private String email;
    @NotNull(message = "Fecha de nacimiento es obligatorio")
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @JsonIgnore
    public List<Jornada> getJornadas() {
        return jornadas;
    }

    @NotNull(message = "Fecha de ingreso es obligatorio")
    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true) //RELACIONO EMPLEADO CON LAS JORNADAS Y CUANDO ELIMINO UN EMPLEADO, ELIMINO TODAS SUS JORNADAS
    private List<Jornada> jornadas;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(long nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
