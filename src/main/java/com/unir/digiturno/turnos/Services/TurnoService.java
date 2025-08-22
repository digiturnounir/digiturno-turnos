package com.unir.digiturno.turnos.Services;

import java.util.List;
import java.util.Optional;

import com.unir.digiturno.turnos.models.entities.Turno;



public interface TurnoService {
    Turno saveTurnoConServicios(Turno turno, java.util.List<Long> serviciosIds);

    List<Turno> findAll();
    
    Optional<Turno> findById(Long id);

    Turno save(Turno turno);

    Optional<Turno> update(Turno turno, Long id);

    void remove(Long id);
    
    // Métodos específicos para turnos
    List<Turno> findByNegocioId(Long negocioId);
    
    List<Turno> findByClienteId(Integer clienteId);
    
    List<Turno> findByFecha(java.time.LocalDate fecha);
    
    List<Turno> findByNegocioIdAndFecha(Long negocioId, java.time.LocalDate fecha);
    
    List<Turno> findByEstadoId(Integer estadoId);
    
        List<Turno> findTurnosByNegocioIdOrderByDateTime(Long negocioId);
    
        List<java.util.Map<String, Object>> getClientesPorNegocio(Long negocioId);
    
}
