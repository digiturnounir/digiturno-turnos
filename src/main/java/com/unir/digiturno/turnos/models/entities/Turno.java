
package com.unir.digiturno.turnos.models.entities;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Generated;


@Entity
@Table(name = "turnos")
public class Turno {

    @Id
    @Generated()
    @Column(name = "ID_TURNO", updatable = false, insertable = false)
    private Long id;

    @Column(name = "NEGOCIO_ID", nullable = false)
    private Long negocioId;
    
    @Column(name = "CLIENTE_ID", nullable = false)
    private Integer clienteId;
    
    @Column(name = "ESTADO_ID", nullable = false)
    private Integer estadoId;
    
    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;
    
    @Column(name = "HORA", nullable = false)
    private LocalDateTime hora;
    
    
    @Column(name = "NOTAS", length = 1000)
    private String notas;
    
    @Column(name = "CREADO_EN")
    private LocalDateTime creadoEn;

    @PrePersist
    public void prePersist() {
        if (creadoEn == null) {
            creadoEn = LocalDateTime.now();
        }
        
        // Asignar valores por defecto para las claves foraneas SOLO si no vienen del request
        if (estadoId == null) {
            estadoId = 33; // Estado PENDIENTE por defecto
        }
        
        // Asignar fecha/hora actual si no vienen especificadas
        if (fecha == null) {
            fecha = LocalDate.now();
        }
        
        if (hora == null) {
            hora = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getNegocioId() {
        return negocioId;
    }

    public void setNegocioId(Long negocioId) {
        this.negocioId = negocioId;
    }
    
    public Integer getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
    
    public Integer getEstadoId() {
        return estadoId;
    }
    
    public void setEstadoId(Integer estadoId) {
        this.estadoId = estadoId;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public LocalDateTime getHora() {
        return hora;
    }
    
    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }
    

    
    public String getNotas() {
        return notas;
    }
    
    public void setNotas(String notas) {
        this.notas = notas;
    }
    
    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }
    
    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}