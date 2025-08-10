package com.unir.digiturno.turnos.models.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "turnos")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "turno_seq")
    @SequenceGenerator(name = "turno_seq", sequenceName = "seq_turnos", allocationSize = 1)
    @Column(name = "ID_TURNO")
    private Long id;

    @Column(name = "NEGOCIO_ID", nullable = false)
    private Integer negocioId;
    
    @Column(name = "CLIENTE_ID", nullable = false)
    private Integer clienteId;
    
    @Column(name = "ESTADO_ID", nullable = false)
    private Integer estadoId;
    
    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;
    
    @Column(name = "HORA", nullable = false)
    private LocalDateTime hora;
    
    @Column(name = "TIPO_SERVICIO", length = 255)
    private String tipoServicio;
    
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
    
    public Integer getNegocioId() {
        return negocioId;
    }
    
    public void setNegocioId(Integer negocioId) {
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
    
    public String getTipoServicio() {
        return tipoServicio;
    }
    
    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
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