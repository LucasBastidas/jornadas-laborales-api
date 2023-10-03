package com.neoris.turnosrotativos.service;

import com.neoris.turnosrotativos.entity.Empleado;

import java.util.List;
import java.util.Optional;


public interface EmpleadoService {
    public List<Empleado> getEmpleados();
    public Optional<Empleado> getEmpleadoById(Long id);
    public Empleado addEmpleado(Empleado empleado);
    public Optional<Empleado> updateEmpleado(Long id,Empleado dataEmpleado);
    public void deleteEmpleado(Long id);
}
