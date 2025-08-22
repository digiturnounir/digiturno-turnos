package com.unir.digiturno.turnos.models.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TurnoRequestDTO {
    public Long idNegocio;
    public Integer clienteId;
    public Integer estadoId;
    public LocalDate fecha;
    public LocalDateTime hora;
    public String notas;
    public List<Long> servicios; // IDs de servicios
}
