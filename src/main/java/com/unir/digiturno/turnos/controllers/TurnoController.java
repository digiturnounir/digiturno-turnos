package com.unir.digiturno.turnos.controllers;

import java.util.Map;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unir.digiturno.turnos.Services.TurnoService;
import com.unir.digiturno.turnos.models.entities.Turno;
import com.unir.digiturno.turnos.response.ApiResponse;

@RestController
@RequestMapping("/turnos")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class TurnoController {
    @PostMapping("/negocio/{negocioId}/clientes")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> postClientesPorNegocio(@PathVariable Long negocioId, @RequestBody(required = false) Map<String, Object> payload) {
        // Si el gateway envía targetMethod: "GET", ejecuta la lógica de GET
        return getClientesPorNegocio(negocioId);
    }

    @GetMapping("/negocio/{negocioId}/clientes")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getClientesPorNegocio(@PathVariable Long negocioId) {
        try {
            List<Map<String, Object>> clientes = service.getClientesPorNegocio(negocioId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Clientes del negocio obtenidos exitosamente", clientes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener clientes del negocio: " + e.getMessage(), null));
        }
    }
    @jakarta.persistence.PersistenceContext
    private jakarta.persistence.EntityManager entityManager;
    @Autowired
    private com.unir.digiturno.turnos.repositories.TurnoServicioRepository turnoServicioRepository;

    @Autowired
    private TurnoService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<java.util.Map<String, Object>>>> list(){
        try {
            List<Turno> turnos = service.findAll();
            List<java.util.Map<String, Object>> turnosConServicios = new java.util.ArrayList<>();
            for (Turno turno : turnos) {
                java.util.Map<String, Object> turnoMap = new java.util.HashMap<>();
                turnoMap.put("id", turno.getId());
                turnoMap.put("negocioId", turno.getNegocioId());
                turnoMap.put("fecha", turno.getFecha());
                turnoMap.put("hora", turno.getHora());
                turnoMap.put("notas", turno.getNotas());
                turnoMap.put("estadoId", turno.getEstadoId());
                turnoMap.put("clienteId", turno.getClienteId());
                turnoMap.put("creadoEn", turno.getCreadoEn());
                // Obtener servicios seleccionados
                java.util.List<Long> serviciosIds = turnoServicioRepository.findByTurnoId(turno.getId())
                    .stream()
                    .map(com.unir.digiturno.turnos.models.entities.TurnoServicio::getServicioId)
                    .toList();
                turnoMap.put("servicios", serviciosIds);
                turnosConServicios.add(turnoMap);
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos obtenidos exitosamente", turnosConServicios));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Turno>> show(@PathVariable Long id){
        try {
            Optional<Turno> turnoOptional = service.findById(id);
            if(turnoOptional.isPresent()){
                return ResponseEntity.ok(new ApiResponse<>(true, "Turno encontrado", turnoOptional.get()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Turno no encontrado", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al buscar turno: " + e.getMessage(), null));
        }
    }
    
    @PostMapping
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<ApiResponse<Turno>> create(@RequestBody com.unir.digiturno.turnos.models.dto.TurnoRequestDTO dto){
        try {
            // Crear el turno principal y sus servicios en la misma transacción
            Turno turno = new Turno();
            turno.setNegocioId(dto.idNegocio);
            turno.setClienteId(dto.clienteId);
            turno.setEstadoId(dto.estadoId);
            turno.setFecha(dto.fecha);
            turno.setHora(dto.hora);
            turno.setNotas(dto.notas);
            Turno savedTurno = service.saveTurnoConServicios(turno, dto.servicios);

            if (savedTurno == null || savedTurno.getId() == null) {
                throw new IllegalStateException("No se pudo generar el turno correctamente");
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Turno creado exitosamente", savedTurno));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al crear turno: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Turno>> update(@RequestBody com.unir.digiturno.turnos.models.dto.TurnoRequestDTO dto, @PathVariable Long id){
        try {
            Optional<Turno> o = service.findById(id);
            if (o.isPresent()) {
                Turno turnoDb = o.get();
                turnoDb.setNegocioId(dto.idNegocio);
                turnoDb.setClienteId(dto.clienteId);
                turnoDb.setEstadoId(dto.estadoId);
                turnoDb.setFecha(dto.fecha);
                turnoDb.setHora(dto.hora);
                turnoDb.setNotas(dto.notas);
                // Actualizar servicios asociados
                turnoServicioRepository.deleteAll(turnoServicioRepository.findByTurnoId(id));
                if (dto.servicios != null && !dto.servicios.isEmpty()) {
                    for (Long idServicio : dto.servicios) {
                        com.unir.digiturno.turnos.models.entities.TurnoServicio ts = new com.unir.digiturno.turnos.models.entities.TurnoServicio();
                        ts.setTurno(turnoDb);
                        ts.setServicioId(idServicio);
                        ts.setCantidad(1);
                        turnoServicioRepository.save(ts);
                    }
                }
                Turno updated = service.save(turnoDb);
                return ResponseEntity.ok(new ApiResponse<>(true, "Turno actualizado exitosamente (incluye servicios)", updated));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Turno no encontrado para actualizar", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al actualizar turno: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> remove(@PathVariable Long id){
        try {
            Optional<Turno> turno = service.findById(id);
            if(turno.isPresent()){
                service.remove(id);
                return ResponseEntity.ok(new ApiResponse<>(true, "Turno eliminado exitosamente", null));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Turno no encontrado para eliminar", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al eliminar turno: " + e.getMessage(), null));
        }
    }

    // Endpoints específicos para turnos
    
    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<ApiResponse<List<java.util.Map<String, Object>>>> getByNegocio(@PathVariable Long negocioId){
        try {
            List<Turno> turnos = service.findByNegocioId(negocioId);
            List<java.util.Map<String, Object>> turnosConServicios = new java.util.ArrayList<>();
            for (Turno turno : turnos) {
                java.util.Map<String, Object> turnoMap = new java.util.HashMap<>();
                turnoMap.put("id", turno.getId());
                turnoMap.put("negocioId", turno.getNegocioId());
                turnoMap.put("fecha", turno.getFecha());
                turnoMap.put("hora", turno.getHora());
                turnoMap.put("notas", turno.getNotas());
                turnoMap.put("estadoId", turno.getEstadoId());
                turnoMap.put("clienteId", turno.getClienteId());
                turnoMap.put("creadoEn", turno.getCreadoEn());
                // Obtener servicios seleccionados
                java.util.List<Long> serviciosIds = turnoServicioRepository.findByTurnoId(turno.getId())
                    .stream()
                    .map(com.unir.digiturno.turnos.models.entities.TurnoServicio::getServicioId)
                    .toList();
                turnoMap.put("servicios", serviciosIds);
                turnosConServicios.add(turnoMap);
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos del negocio obtenidos exitosamente", turnosConServicios));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos del negocio: " + e.getMessage(), null));
        }
    }
    
    // Eliminado endpoint no usado /business/{businessId}/ordered

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<Turno>>> getByCliente(@PathVariable Integer clienteId){
        try {
            List<Turno> turnos = service.findByClienteId(clienteId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos del cliente obtenidos exitosamente", turnos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos del cliente: " + e.getMessage(), null));
        }
    }
    
    // Eliminado endpoint duplicado /cliente/{clienteId} POST

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<ApiResponse<List<Turno>>> getByFecha(@PathVariable String fecha){
        try {
            LocalDate localDate = LocalDate.parse(fecha);
            List<Turno> turnos = service.findByFecha(localDate);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos por fecha obtenidos exitosamente", turnos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos por fecha: " + e.getMessage(), null));
        }
    }

    @GetMapping("/estado/{estadoId}")
    public ResponseEntity<ApiResponse<List<Turno>>> getByEstado(@PathVariable Integer estadoId){
        try {
            List<Turno> turnos = service.findByEstadoId(estadoId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos por estado obtenidos exitosamente", turnos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos por estado: " + e.getMessage(), null));
        }
    }
    

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Turno>>> searchTurnos(
            @RequestParam(required = false) Integer negocioId,
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) Integer estadoId,
            @RequestParam(required = false) Integer clienteId) {
        try {
            List<Turno> turnos;
            
            if (negocioId != null && fecha != null) {
                LocalDate localDate = LocalDate.parse(fecha);
                turnos = service.findByNegocioIdAndFecha(negocioId.longValue(), localDate);
            } else if (negocioId != null) {
                turnos = service.findByNegocioId(negocioId.longValue());
            } else if (clienteId != null) {
                turnos = service.findByClienteId(clienteId);
            } else if (estadoId != null) {
                turnos = service.findByEstadoId(estadoId);
            } else if (fecha != null) {
                LocalDate localDate = LocalDate.parse(fecha);
                turnos = service.findByFecha(localDate);
            } else {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Al menos un parámetro de búsqueda es requerido", null));
            }
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Búsqueda completada exitosamente", turnos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error en la búsqueda: " + e.getMessage(), null));
        }
    }
}

