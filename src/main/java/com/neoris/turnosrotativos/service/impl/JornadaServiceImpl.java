package com.neoris.turnosrotativos.service.impl;

import com.neoris.turnosrotativos.dto.JornadaRequestDTO;
import com.neoris.turnosrotativos.dto.JornadaResponseDTO;
import com.neoris.turnosrotativos.entity.Concepto;
import com.neoris.turnosrotativos.entity.Empleado;
import com.neoris.turnosrotativos.entity.Jornada;
import com.neoris.turnosrotativos.exceptions.EmpleadoBusinessException;
import com.neoris.turnosrotativos.repository.JornadaRepository;
import com.neoris.turnosrotativos.service.ConceptoService;
import com.neoris.turnosrotativos.service.EmpleadoService;
import com.neoris.turnosrotativos.service.JornadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JornadaServiceImpl implements JornadaService {

    @Autowired
    JornadaRepository jornadaRepository;
    @Autowired
    EmpleadoService empleadoService;
    @Autowired
    ConceptoService conceptoService;



    @Override
    public JornadaResponseDTO createJornada(JornadaRequestDTO jornadaRequest) {
        Optional<Concepto> conceptoById = conceptoService.getConceptoById(jornadaRequest.getIdConcepto());
        Optional<Empleado> empleadoById = empleadoService.getEmpleadoById(jornadaRequest.getIdEmpleado());

        Empleado empleado = empleadoById.get();
        Concepto concepto = conceptoById.get();

        LocalDate fechaHoy = LocalDate.now();


        //COMPRUEBA SI EL CONCEPTO ES TURNO NORMAL O EXTRA
        if ((concepto.getNombre().equalsIgnoreCase("Turno Normal") || concepto.getNombre()
                .equalsIgnoreCase("Turno Extra"))){
            //Y LUEGO QUE SE INGRESEN LAS HORAS TRABAJADAS
            if ((jornadaRequest.getHorasTrabajadas() == null) || (jornadaRequest.getHorasTrabajadas() <= 0)){
                throw new EmpleadoBusinessException("’hsTrabajadas’ es obligatorio para el concepto ingresado",HttpStatus.BAD_REQUEST);
            }
            //SE COMPRUEBA QUE SE CUMPLA CON EL RANGO DE HORAS DEL CONCEPTO
            if (jornadaRequest.getHorasTrabajadas() > concepto.getHsMaximo() || jornadaRequest.getHorasTrabajadas() < concepto.getHsMinimo()){
                throw new EmpleadoBusinessException("El rango de horas que se puede cargar para este concepto es de "+ concepto.getHsMinimo()+
                        " - "+ concepto.getHsMaximo(),HttpStatus.BAD_REQUEST);
            }
        }

        //COMPRUEBA QUE SI EL CONCEPTO ES DIA LIBRE
        if ((concepto.getNombre().equalsIgnoreCase("Dia libre")) ){
            //Y LUEGO QUE NO SE INGRESEN LAS HORAS TRABAJADAS O NO SEAN MAYOR A CERO
            if(jornadaRequest.getHorasTrabajadas() != 0 && jornadaRequest.getHorasTrabajadas() != null){
                throw new EmpleadoBusinessException("El concepto ingresado no requiere el ingreso de ’hsTrabajadas’",HttpStatus.BAD_REQUEST);
            }
        }

        // Variable para llevar un seguimiento de las horas totales trabajadas en la misma semana
        int horasTotalesSemana = 0;

        // Variable para llevar un seguimiento de la cantidad de turnos extra en la misma semana
        int turnosExtraSemana = 0;

        // Variable para llevar un seguimiento de la cantidad de turnos normales en la misma semana
        int turnosNormalesSemana = 0;

        // Variable para llevar un seguimiento de la cantidad de días libres en la misma semana
        int diasLibresSemana = 0;


        // ITERA EN LAS JORNADAS DEL EMPLEADO
        for (Jornada jornada : empleado.getJornadas()) {


            //COMPRUEBA SI HAY UNA JORNADA CON LA MISMA FECHA
            if (jornada.getFecha().equals(jornadaRequest.getFecha())){
                //SI HAY UN TURNO NO SE LE PERMITE CARGAR UN DIA LIBRE
               if (concepto.getNombre().equalsIgnoreCase("Dia Libre")){
                   throw new EmpleadoBusinessException("El empleado no puede cargar Dia Libre si cargo un turno previamente para la fecha ingresada.",
                           HttpStatus.BAD_REQUEST);
               }
                //SI HAY UN DIA LIBRE NO SE PUEDE CARGAR NINGUNA JORNADA
                if (jornada.getConcepto().getNombre().equalsIgnoreCase("Dia Libre")) {
                    throw new EmpleadoBusinessException("El empleado ingresado cuenta con un día libre en esa fecha.",HttpStatus.BAD_REQUEST);
                }
                //SI YA HAY UNA JORNADA CON EL MISMO CONCEPTO NO SE PODRÁ CARGAR
                if (jornada.getConcepto().getNombre().equalsIgnoreCase(concepto.getNombre())){
                    throw new EmpleadoBusinessException("El empleado ya tiene registrado una jornada con este concepto en la fecha ingresada.",
                            HttpStatus.BAD_REQUEST);
                }
                //SI LA SUMA DE HORAS DEL REQUEST MAS LA DE LA JORNADA YA CARGADA SUPERAN A 12 NO SE PODRÁ CARGAR
                if (jornada.getHsTrabajadas() + jornadaRequest.getHorasTrabajadas() > 12){
                    throw new EmpleadoBusinessException("El empleado no puede cargar más de 12 horas trabajadas en un día.",
                            HttpStatus.BAD_REQUEST);
                }

            }

            // FORMA DE CALCULAR EL NUMERO DE LA SEMANA
            LocalDate fechaJornadaRequest = LocalDate.parse(jornadaRequest.getFecha().toString());
            LocalDate fechaJornada = LocalDate.parse(jornada.getFecha().toString());
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int numeroSemanaJornadaRequest = fechaJornadaRequest.get(weekFields.weekOfWeekBasedYear());
            int numeroSemanaJornada = fechaJornada.get(weekFields.weekOfWeekBasedYear());

            //COMPRUEBA SI LA JORNADA ES DE LA MISMA SEMANA
            if (numeroSemanaJornada == numeroSemanaJornadaRequest){
                //Se suman las horas

                horasTotalesSemana += jornada.getHsTrabajadas();

                //SE SUMAN LOS Turnos Extra
                if (jornada.getConcepto().getNombre().equalsIgnoreCase("Turno Extra")){
                    turnosExtraSemana++;
                }
                //SE SUMAN LOS TURNOS NORMALES
                if (jornada.getConcepto().getNombre().equalsIgnoreCase("Turno Normal")){
                    turnosNormalesSemana++;
                }
                //SE SUMAN LOS DIAS LIBRES
                if (jornada.getConcepto().getNombre().equalsIgnoreCase("Dia Libre")){
                    diasLibresSemana++;
                }
            }else {
                //SINO ES DE LA MISMA SEMANA SE REINICIAN LOS CONTADORES
                horasTotalesSemana = 0;
                turnosExtraSemana = 0;
                turnosNormalesSemana = 0;
                diasLibresSemana = 0;
            }

        }
        //SE SUMAN LAS HORAS DEL REQUEST
        horasTotalesSemana += jornadaRequest.getHorasTrabajadas();

        if (concepto.getNombre().equalsIgnoreCase("Turno Normal")){
            turnosNormalesSemana++;
        }
        if (concepto.getNombre().equalsIgnoreCase("Turno Extra")){
            turnosExtraSemana++;
        }
        if (concepto.getNombre().equalsIgnoreCase("Dia Libre")){
            diasLibresSemana++;
        }


        //SE COMPRUEBA QUE LOS TURNOS NORMALES NO SEAN MAYOR A 5
        if (turnosNormalesSemana > 5){
            throw new EmpleadoBusinessException("El empleado ingresado ya cuenta con 5 turnos normales esta semana.",
                    HttpStatus.BAD_REQUEST);
        }
        //SE COMPRUEBA QUE LOS TURNOS EXTRA NO SEAN MAYOR A 3
        if (turnosExtraSemana > 3){
            throw new EmpleadoBusinessException("El empleado ingresado ya cuenta con 3 turnos extra esta semana.",
                    HttpStatus.BAD_REQUEST);
        }
        //SE COMPRUEBA QUE LOS DIAS LIBRES NO SEAN MAYOR A 2
        if (diasLibresSemana > 2){
            throw new EmpleadoBusinessException("El empleado ingresado no cuenta con mas días libres esta semana",
                    HttpStatus.BAD_REQUEST);
        }


        //COMPRUEBA QUE LAS HORAS SEMANALES NO SEAN MAYOR A 48
        if (horasTotalesSemana > 48){
            throw new EmpleadoBusinessException("El empleado ingresado supera las 48 horas semanales.",HttpStatus.BAD_REQUEST);
        }


        //SE CREA LA NUEVA JORNADA
        Jornada jornada = new Jornada();
        jornada.setConcepto(concepto);
        jornada.setEmpleado(empleado);
        jornada.setFecha(jornadaRequest.getFecha());
        jornada.setHsTrabajadas(jornadaRequest.getHorasTrabajadas());


        //Y SE GUARDA EN LA DB
        jornadaRepository.save(jornada);


        //SE CREA Y RETORNA EL ResponseDTO

        return JornadaResponseDTO.mapJornadaToResponseDTO(jornada);


    }

    @Override //DEVUELVE TODA LAS JORNADAS COMO JORNADARESPONSEDTO
    public List<JornadaResponseDTO> getJornadas() {
        List<Jornada> jornadas = jornadaRepository.findAll();


        return JornadaResponseDTO.mapJornadasToResponseDTO(jornadas);
    }

    @Override//FILTRA LAS JORNADASRESPONSEDTO POR DNI
    public List<JornadaResponseDTO> getJornadasByNroDocumento(Long nroDocumento) {
        List<JornadaResponseDTO> jornadasDto = this.getJornadas();

        List<JornadaResponseDTO> jornadasByNroDocumento =  jornadasDto.stream()
                .filter(jornada -> jornada.getNroDocumento() != null && jornada.getNroDocumento().equals(nroDocumento))
                .collect(Collectors.toList());
        return jornadasByNroDocumento;
    }

    @Override//FILTRA LAS JORNADASRESPONSEDTO POR FECHA
    public List<JornadaResponseDTO> getJornadasByFecha(LocalDate fecha) {
        List<JornadaResponseDTO> jornadasDto = this.getJornadas();

        List<JornadaResponseDTO> jornadasByFecha =  jornadasDto.stream()
                .filter(jornada -> jornada.getFecha() != null && jornada.getFecha().equals(fecha))
                .collect(Collectors.toList());
        return jornadasByFecha;

    }

    @Override//FILTRA LAS JORNADASRESPONSEDTO POR FECHA Y DNI
    public List<JornadaResponseDTO> getJornadasByNroDocumentoAndFecha(Long nroDocumento, LocalDate fecha) {
        List<JornadaResponseDTO> jornadasDto = this.getJornadas();

        List<JornadaResponseDTO> jornadasByNroDocumentoAndFecha =  jornadasDto.stream()
                .filter((jornada -> (jornada.getFecha() != null && jornada.getFecha().equals(fecha) &&
                        jornada.getNroDocumento() != null && jornada.getNroDocumento().equals(nroDocumento))))
                .collect(Collectors.toList());
        return jornadasByNroDocumentoAndFecha;
    }

}
