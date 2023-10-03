package com.neoris.turnosrotativos.service.impl;

import com.neoris.turnosrotativos.entity.Empleado;
import com.neoris.turnosrotativos.exceptions.EmpleadoBusinessException;
import com.neoris.turnosrotativos.repository.EmpleadoRepository;
import com.neoris.turnosrotativos.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {
    @Autowired
    EmpleadoRepository repository;


    @Override
    public List<Empleado> getEmpleados() {
        return repository.findAll();
    }

    @Override
    public Optional<Empleado> getEmpleadoById(Long id) {

        Optional<Empleado> empleado = repository.findById(id);
        //CHEQUEA SI EXISTE UN EMPLEADO CON ESE ID
        if (empleado.isEmpty()){
            //SI NO EXISTE DEVUELVE ESTA EXCEPTION
            throw new EmpleadoBusinessException("No se encontró el empleado con Id: "+id, HttpStatus.NOT_FOUND);
        }else {
            //SI EXISTE LO DEVUELVE
            return empleado;
        }
    }

    @Override
    public Empleado addEmpleado(Empleado empleado) {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaDeNacimiento = empleado.getFechaNacimiento();
        Period edad = Period.between(fechaDeNacimiento,hoy);

        Optional<Empleado> empleadoByDni = repository.findByNroDocumento(empleado.getNroDocumento());

        Optional<Empleado> empleadoByEmail = repository.findByEmail(empleado.getEmail());

        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        String regexNombreYApellido = "^[a-zA-Z]+$";

        //SE COMPRUEBA QUE EL EMPLEADO SEA MAYOR DE 18
        if (!(edad.getYears() >= 18 || (edad.getYears() == 17 && hoy.getMonthValue() > fechaDeNacimiento.getMonthValue()) ||
                (edad.getYears() == 17 && hoy.getMonthValue() == fechaDeNacimiento.getMonthValue() &&
                        hoy.getDayOfMonth() >= fechaDeNacimiento.getDayOfMonth()))) {
            throw new EmpleadoBusinessException("La edad del empleado no puede ser menor a 18 años.", HttpStatus.BAD_REQUEST);
        }
        //SE COMPRUEBA QUE NO EXISTA UN EMPLEADO CON EL MISMO DNI
        if (empleadoByDni.isPresent()){
            throw new EmpleadoBusinessException("Ya existe un empleado con el documento ingresado.", HttpStatus.CONFLICT);
        }
        //SE COMPRUEBA QUE NO EXISTA UN EMPLEADO CON EL MISMO EMAIL
        if (empleadoByEmail.isPresent()){
            throw new EmpleadoBusinessException("Ya existe un empleado con el email ingresado.", HttpStatus.CONFLICT);
        }
        //SE COMPRUEBA QUE LA FECHA DE INGRESO NO SEA POSTERIOR AL DIA DE LA FECHA(HOY)
        if (empleado.getFechaIngreso().isAfter(hoy)){
            throw new EmpleadoBusinessException("La fecha de ingreso no puede ser posterior al día de la fecha.", HttpStatus.BAD_REQUEST);
        }
        //SE COMPRUEBA QUE LA FECHA DE NACIMIENTO NO SEA POSTERIOR AL DIA DE LA FECHA(HOY)
        if (fechaDeNacimiento.isAfter(hoy)){
            throw new EmpleadoBusinessException("La fecha de nacimiento no puede ser posterior al día de la fecha", HttpStatus.BAD_REQUEST);
        }
        //SE COMPRUEBA QUE EL EMAIL TENGA EL FORMATO CORRECTO (regexEmail)
        if (!empleado.getEmail().matches(regexEmail)){
            throw new EmpleadoBusinessException("El email ingresado no es correcto.", HttpStatus.BAD_REQUEST);
        }
        //SE COMPRUEBA QUE EL NOMBRE SOLO CONTENGA LETRAS
        if (!empleado.getNombre().matches(regexNombreYApellido)){
            throw new EmpleadoBusinessException("Solo se permiten letras en el campo nombre.", HttpStatus.BAD_REQUEST);
        }
        //SE COMPRUEBA QUE EL APELLIDO SOLO CONTENGA LETRAS
        if (!empleado.getApellido().matches(regexNombreYApellido)){
            throw new EmpleadoBusinessException("Solo se permiten letras en el campo apellido.", HttpStatus.BAD_REQUEST);
        }
        //SE LE AGREGA LA FECHA DE CREACIÓN (hoy)
        empleado.setFechaCreacion(hoy);
        return repository.save(empleado);
    }

    @Override
    public Optional<Empleado> updateEmpleado(Long id, Empleado dataEmpleado) {
        Optional<Empleado> empleadoById = repository.findById(id);

        if(empleadoById.isEmpty()){
            throw new EmpleadoBusinessException("No existe un empleado con el id: "+ id,HttpStatus.NOT_FOUND);
        }

        //CHEQUEA SI EXISTE UN EMPLEADO CON ESE ID

            Empleado empleadoAUpdatear = empleadoById.get();

            Optional<Empleado> empleadoByEmail = repository.findByEmail(dataEmpleado.getEmail());
            Optional<Empleado> empleadoByDni = repository.findByNroDocumento(dataEmpleado.getNroDocumento());

            //CHEQUEA SI EXISTE UN EMPLEADO CON ESTE EMAIL
            if (empleadoByEmail.isPresent() && (empleadoByEmail.get().getId() != id)){
                throw new EmpleadoBusinessException("Ya existe un empleado con el email ingresado.", HttpStatus.CONFLICT);
            }

            if (empleadoByDni.isPresent() && (empleadoByDni.get().getId() != id)){
                throw new EmpleadoBusinessException("Ya existe un empleado con el número de documento ingresado.", HttpStatus.CONFLICT);
            }

            LocalDate hoy = LocalDate.now();
            LocalDate nuevaFechaDeNacimiento = dataEmpleado.getFechaNacimiento();
            Period edad = Period.between(nuevaFechaDeNacimiento,hoy);

            if (!(edad.getYears() >= 18 || (edad.getYears() == 17 && hoy.getMonthValue() > nuevaFechaDeNacimiento.getMonthValue()) ||
                    (edad.getYears() == 17 && hoy.getMonthValue() == nuevaFechaDeNacimiento.getMonthValue() &&
                            hoy.getDayOfMonth() >= nuevaFechaDeNacimiento.getDayOfMonth()))){
                throw new EmpleadoBusinessException("La edad del empleado no puede ser menor a 18 años.", HttpStatus.BAD_REQUEST);
            }

            //SE COMPRUEBA QUE LA FECHA DE INGRESO NO SEA POSTERIOR AL DIA DE LA FECHA(HOY)
            if (dataEmpleado.getFechaIngreso().isAfter(hoy)){
                throw new EmpleadoBusinessException("La fecha de ingreso no puede ser posterior al día de la fecha.", HttpStatus.BAD_REQUEST);
            }

            //SE COMPRUEBA QUE LA FECHA DE NACIMIENTO NO SEA POSTERIOR AL DIA DE LA FECHA(HOY)
            if (dataEmpleado.getFechaNacimiento().isAfter(hoy)){
                throw new EmpleadoBusinessException("La fecha de nacimiento no puede ser posterior al día de la fecha", HttpStatus.BAD_REQUEST);
            }

            String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
            String regexNombreYApellido = "^[a-zA-Z]+$";

            //SE COMPRUEBA QUE EL EMAIL TENGA EL FORMATO CORRECTO (regexEmail)
            if (!dataEmpleado.getEmail().matches(regexEmail)){
                throw new EmpleadoBusinessException("El email ingresado no es correcto.", HttpStatus.BAD_REQUEST);
            }
            //SE COMPRUEBA QUE EL NOMBRE SOLO CONTENGA LETRAS
            if (!dataEmpleado.getNombre().matches(regexNombreYApellido)){
                throw new EmpleadoBusinessException("Solo se permiten letras en el campo nombre.", HttpStatus.BAD_REQUEST);
            }
            //SE COMPRUEBA QUE EL APELLIDO SOLO CONTENGA LETRAS
            if (!dataEmpleado.getApellido().matches(regexNombreYApellido)){
                throw new EmpleadoBusinessException("Solo se permiten letras en el campo apellido.", HttpStatus.BAD_REQUEST);
            }

            //SE ACTUALIZA EL EMPLEADO
            empleadoAUpdatear.setNombre(dataEmpleado.getNombre());
            empleadoAUpdatear.setApellido(dataEmpleado.getApellido());
            empleadoAUpdatear.setEmail(dataEmpleado.getEmail());
            empleadoAUpdatear.setNroDocumento(dataEmpleado.getNroDocumento());
            empleadoAUpdatear.setFechaIngreso(dataEmpleado.getFechaIngreso());
            empleadoAUpdatear.setFechaNacimiento(dataEmpleado.getFechaNacimiento());

            //SE GUARDA EN LA DB
            repository.save(empleadoAUpdatear);


            return Optional.of(empleadoAUpdatear);

    }

    @Override
    public void deleteEmpleado(Long id) {
        Optional<Empleado> empleadoById = repository.findById(id);

        if(empleadoById.isEmpty()){
            throw new EmpleadoBusinessException("No existe un empleado con el id: "+ id,HttpStatus.NOT_FOUND);
        }else {
            repository.deleteById(id);
        }


    }
}
