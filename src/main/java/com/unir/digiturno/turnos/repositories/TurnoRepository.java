package com.unir.digiturno.turnos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unir.digiturno.turnos.models.entities.Turno;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    // Buscar turnos por negocio
    List<Turno> findByNegocioId(Integer negocioId);
    
    // Buscar turnos por cliente
    List<Turno> findByClienteId(Integer clienteId);
    
    // Buscar turnos por fecha
    List<Turno> findByFecha(java.time.LocalDate fecha);
    
    // Buscar turnos por negocio y fecha
    List<Turno> findByNegocioIdAndFecha(Integer negocioId, java.time.LocalDate fecha);
    
    // Buscar turnos por estado
    List<Turno> findByEstadoId(Integer estadoId);
    
    // Buscar turnos por negocio y estado
    List<Turno> findByNegocioIdAndEstadoId(Integer negocioId, Integer estadoId);
    
    // Consulta personalizada para turnos de un rango de fechas
    @Query("SELECT t FROM Turno t WHERE t.negocioId = :negocioId ORDER BY t.fecha, t.hora")
    List<Turno> findTurnosByNegocioIdOrderByDateTime(@Param("negocioId") Integer negocioId);
    
    // Buscar turnos por servicio
    List<Turno> findByTipoServicio(String tipoServicio);
}


