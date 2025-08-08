package com.unir.digiturno.turnos.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.digiturno.turnos.models.entities.Turno;
import com.unir.digiturno.turnos.repositories.TurnoRepository;

@Service
public class TurnoServiceImpl implements TurnoService {

    @Autowired
    private TurnoRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Turno> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Turno> update(Turno turno, Long id) {
        Optional<Turno> o = this.findById(id);
        if(o.isPresent()){
            Turno turnoDb = o.orElseThrow();
            turnoDb.setNegocioId(turno.getNegocioId());
            turnoDb.setClienteId(turno.getClienteId());
            turnoDb.setEstadoId(turno.getEstadoId());
            turnoDb.setFecha(turno.getFecha());
            turnoDb.setHora(turno.getHora());
            turnoDb.setTipoServicio(turno.getTipoServicio());
            turnoDb.setNotas(turno.getNotas());
            return Optional.of(this.save(turnoDb));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Turno save(Turno turno) {
        return repository.save(turno);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }

    // Implementación de métodos específicos
    @Override
    @Transactional(readOnly = true)
    public List<Turno> findByNegocioId(Integer negocioId) {
        return repository.findByNegocioId(negocioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findByClienteId(Integer clienteId) {
        return repository.findByClienteId(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findByFecha(java.time.LocalDate fecha) {
        return repository.findByFecha(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findByNegocioIdAndFecha(Integer negocioId, java.time.LocalDate fecha) {
        return repository.findByNegocioIdAndFecha(negocioId, fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findByEstadoId(Integer estadoId) {
        return repository.findByEstadoId(estadoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findTurnosByNegocioIdOrderByDateTime(Integer negocioId) {
        return repository.findTurnosByNegocioIdOrderByDateTime(negocioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findByTipoServicio(String tipoServicio) {
        return repository.findByTipoServicio(tipoServicio);
    }
}
