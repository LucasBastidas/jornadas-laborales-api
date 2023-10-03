package com.neoris.turnosrotativos.repository;

import com.neoris.turnosrotativos.entity.Jornada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JornadaRepository extends JpaRepository<Jornada, Long> {

}
