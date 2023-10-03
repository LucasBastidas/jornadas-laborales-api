package com.neoris.turnosrotativos.controllers;

import com.neoris.turnosrotativos.entity.Empleado;
import com.neoris.turnosrotativos.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class EmpleadoController {
    @Autowired
    EmpleadoService empleadoService;

    @GetMapping("/empleado")//DEVUELVE TODOS LOS EMPLEADOS
    @CrossOrigin(origins = "*") // Especifica el origen permitido (URL) para este endpoint
    public ResponseEntity<List<Empleado>> getEmpleados(){
        return ResponseEntity.ok(this.empleadoService.getEmpleados());
    }
    @GetMapping("/empleado/{id}")//DEVUELVE UN EMPLEADO POR ID SI ES QUE EXISTE
    @CrossOrigin(origins = "*") // Especifica el origen permitido (URL) para este endpoint
    public ResponseEntity<Optional<Empleado>> getEmpleado(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.empleadoService.getEmpleadoById(id));
    }
    @PostMapping("/empleado")//AGREGA UN EMPLEADO SIEMPRE Y CUANDO LOS DATOS SEAN VALIDOS
    @CrossOrigin(origins = "*") // Especifica el origen permitido (URL) para este endpoint
    public ResponseEntity<Empleado> addEmpleado(@Valid @RequestBody Empleado nuevoEmpleado){
        Empleado empleadoAdded =this.empleadoService.addEmpleado(nuevoEmpleado);
        return ResponseEntity.created(URI
                        .create(String.format("/empleados/%d",empleadoAdded.getId())))
                        .body(empleadoAdded);
    }
    @PutMapping("/empleado/{id}")//ACTUALIZA UN EMPLEADO POR ID SI ES QUE EXISTE
    @CrossOrigin(origins = "*") // Especifica el origen permitido (URL) para este endpoint
    public ResponseEntity<Optional<Empleado>> updateEmpleado(@Valid @PathVariable("id") Long id, @Valid @RequestBody Empleado updateEmpeleado){
        this.empleadoService.updateEmpleado(id,updateEmpeleado);
        return ResponseEntity.ok(this.empleadoService.getEmpleadoById(id));
    }

    @PatchMapping("/empleado/{id}")//ACTUALIZA UN EMPLEADO POR ID SI ES QUE EXISTE
    @CrossOrigin(origins = "*") // Especifica el origen permitido (URL) para este endpoint
    public ResponseEntity<Optional<Empleado>> updateEmpleadoDos(@Valid @PathVariable("id") Long id, @Valid @RequestBody Empleado updateEmpeleado){
        this.empleadoService.updateEmpleado(id,updateEmpeleado);
        return ResponseEntity.ok(this.empleadoService.getEmpleadoById(id));
    }

    @DeleteMapping("/empleado/{id}")//ELIMINA UN EMPLEADO POR ID SI ES QUE EXISTE
    @CrossOrigin(origins = "*") // Especifica el origen permitido (URL) para este endpoint
    public ResponseEntity<Object>deleteEmpleado(@Valid @PathVariable("id") Long id){
        this.empleadoService.deleteEmpleado(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
