package com.unir.digiturno.turnos.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.digiturno.turnos.models.entities.Turno;
import com.unir.digiturno.turnos.repositories.TurnoRepository;
import com.unir.digiturno.turnos.repositories.TurnoServicioRepository;
import com.unir.digiturno.turnos.models.entities.TurnoServicio;

@Service
public class TurnoServiceImpl implements TurnoService {
    private final org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
    private final String USERS_SERVICE_URL = "http://localhost:8083/users/";

    @Autowired
    private TurnoServicioRepository turnoServicioRepository;
    /**
     * Guarda el turno y sus servicios en la misma transacción
     */
    // Eliminado entityManager no usado

    @Transactional
    public Turno saveTurnoConServicios(Turno turno, java.util.List<Long> serviciosIds) {
        // Guardar el turno y obtener el ID real generado por la BD
        Turno savedTurno = repository.saveAndFlush(turno);
        System.out.println("Turno guardado, ID: " + savedTurno.getId());

        if (savedTurno.getId() == null) {
            throw new IllegalStateException("El turno no tiene ID asignado después de guardar");
        }

        // Usar el objeto Turno persistido para asociar los servicios
        if (serviciosIds != null && !serviciosIds.isEmpty()) {
            for (Long idServicio : serviciosIds) {
                TurnoServicio ts = new TurnoServicio();
                ts.setTurno(savedTurno);
                ts.setServicioId(idServicio);
                ts.setCantidad(1);
                System.out.println("[LOG] Insertando en TURNO_SERVICIO: turno_id=" + savedTurno.getId() + ", servicio_id=" + idServicio + ", cantidad=1");
                turnoServicioRepository.save(ts);
            }
        }
        return savedTurno;
    }
    @Override
    @Transactional
    public Turno save(Turno turno) {
        return repository.save(turno);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findTurnosByNegocioIdOrderByDateTime(Long negocioId) {
        return repository.findTurnosByNegocioIdOrderByDateTime(negocioId);
    }

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
        @Transactional(readOnly = true)
        public List<Turno> findByNegocioId(Long negocioId) {
            return repository.findByNegocioId(negocioId);
        }

        @Override
        @Transactional(readOnly = true)
        public List<java.util.Map<String, Object>> getClientesPorNegocio(Long negocioId) {
            List<Turno> turnos = repository.findByNegocioId(negocioId);
            java.util.Map<Long, java.util.Map<String, Object>> clientesMap = new java.util.HashMap<>();
            for (Turno turno : turnos) {
                Long clienteId = turno.getClienteId().longValue();
                if (!clientesMap.containsKey(clienteId)) {
                    // Llamada REST al microservicio de usuarios
                    java.util.Map<String, Object> userData = null;
                    try {
                        java.util.Map<String, Object> apiResponse = restTemplate.getForObject(USERS_SERVICE_URL + clienteId, java.util.Map.class);
                        // El objeto ApiResponse tiene la info en la clave "data"
                        if (apiResponse != null && apiResponse.get("data") != null) {
                            userData = (java.util.Map<String, Object>) apiResponse.get("data");
                        } else {
                            userData = new java.util.HashMap<>();
                        }
                    } catch (Exception ex) {
                        userData = new java.util.HashMap<>();
                    }
                    java.util.Map<String, Object> info = new java.util.HashMap<>();
                    info.put("id", clienteId);
                    info.put("nombre", userData.getOrDefault("nombre", "Desconocido"));
                    info.put("email", userData.getOrDefault("correo", ""));
                    info.put("telefono", userData.getOrDefault("telefono", ""));
                    info.put("ultimoTurno", turno.getFecha());
                    info.put("totalTurnos", 1);
                    clientesMap.put(clienteId, info);
                } else {
                    java.util.Map<String, Object> info = clientesMap.get(clienteId);
                    info.put("totalTurnos", (int) info.get("totalTurnos") + 1);
                    if (turno.getFecha().compareTo((java.time.LocalDate) info.get("ultimoTurno")) > 0) {
                        info.put("ultimoTurno", turno.getFecha());
                    }
                }
            }
            return new java.util.ArrayList<>(clientesMap.values());
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
                turnoDb.setNotas(turno.getNotas());
                return Optional.of(this.save(turnoDb));
            }
            return Optional.empty();
        }

        @Override
        @Transactional(readOnly = true)
        public List<Turno> findByNegocioIdAndFecha(Long negocioId, java.time.LocalDate fecha) {
            return repository.findByNegocioIdAndFecha(negocioId, fecha);
        }

    @Override
    @Transactional
    public void remove(Long id) {
        // Eliminar primero los TurnoServicio relacionados
        List<TurnoServicio> servicios = turnoServicioRepository.findByTurnoId(id);
        if (servicios != null && !servicios.isEmpty()) {
            for (TurnoServicio ts : servicios) {
                turnoServicioRepository.delete(ts);
            }
        }
        repository.deleteById(id);
    }

    // Eliminado método duplicado y llave sobrante

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

    // Eliminado método obsoleto findByNegocioIdAndFecha(Integer, LocalDate)

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findByEstadoId(Integer estadoId) {
        return repository.findByEstadoId(estadoId);
    }

    // Eliminado método duplicado

    // ...existing code...
}
