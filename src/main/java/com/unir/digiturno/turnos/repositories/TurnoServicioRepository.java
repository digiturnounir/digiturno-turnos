package com.unir.digiturno.turnos.repositories;

import com.unir.digiturno.turnos.models.entities.TurnoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TurnoServicioRepository extends JpaRepository<TurnoServicio, Long> {
    List<TurnoServicio> findByTurnoId(Long turnoId);
}
