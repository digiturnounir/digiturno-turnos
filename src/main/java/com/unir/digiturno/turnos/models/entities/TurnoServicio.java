package com.unir.digiturno.turnos.models.entities;

import org.hibernate.annotations.Generated;

import jakarta.persistence.*;

@Entity
@Table(name = "TURNO_SERVICIO")
public class TurnoServicio {
    @Id
    @Generated()
    @Column(name = "ID_TURNO_SERVICIO", updatable = false, insertable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TURNO_ID", referencedColumnName = "ID_TURNO", nullable = false)
    private Turno turno;

    @Column(name = "SERVICIO_ID", nullable = false)
    private Long servicioId;

    @Column(name = "CANTIDAD")
    private Integer cantidad;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }

    public Long getServicioId() { return servicioId; }
    public void setServicioId(Long servicioId) { this.servicioId = servicioId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
