package com.neoris.turnosrotativos.controllers;

import com.neoris.turnosrotativos.dto.JornadaRequestDTO;
import com.neoris.turnosrotativos.dto.JornadaResponseDTO;
import com.neoris.turnosrotativos.entity.Jornada;
import com.neoris.turnosrotativos.service.JornadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/")
public class JornadaController  {
    @Autowired
    JornadaService jornadaService;

    @PostMapping("/jornada")//CREA UNA NUEVA JORNADA SIEMPRE Y CUANDO LOS DATOS SEAN VALIDOS
    @CrossOrigin(origins = "*") // Especifica el origen permitido (URL) para este endpoint
    public ResponseEntity<JornadaResponseDTO>createJornada(@Valid @RequestBody JornadaRequestDTO jornadaRequest){
        JornadaResponseDTO jornadaAdded = jornadaService.createJornada(jornadaRequest);

        return ResponseEntity.created(URI
                        .create(String.format("/jornada/%d",jornadaAdded.getId())))
                .body(jornadaAdded);
    }

    @GetMapping("/jornada")//DEVUELVE JORNADAS
    @CrossOrigin(origins = "*") // Especifica el origen permitido (URL) para este endpoint
    public ResponseEntity<List<JornadaResponseDTO>>getJornadas(
            @RequestParam(name = "nroDocumento",required = false)Long nroDocumento,
            @RequestParam(name = "fecha",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha)
    {
        //SI SE INGRESA UN DNI Y UNA FECHA SE FILTRA POR DNI Y FECHA
        if(nroDocumento != null && fecha != null){
            return ResponseEntity.ok(jornadaService.getJornadasByNroDocumentoAndFecha(nroDocumento,fecha));
        }
        //SI SE INGRESA SOLO DNI SE FILTRA SOLO POR DNI
        if (nroDocumento != null && fecha == null){
            return ResponseEntity.ok(jornadaService.getJornadasByNroDocumento(nroDocumento));
        }
        //SI SE INGRESA SOLO FECHA SE FILTRA SOLO POR FECHA
        if (nroDocumento == null && fecha != null){
            return ResponseEntity.ok(jornadaService.getJornadasByFecha(fecha));
        }
        //SI NO SE INGRESA NINGUN FILTRO, SE DEVUELVEN TODAS LAS JORNADAS
        return ResponseEntity.ok(jornadaService.getJornadas());
    }

}
