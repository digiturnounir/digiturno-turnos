package com.unir.digiturno.turnos.controllers;

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

    @Autowired
    private TurnoService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Turno>>> list(){
        try {
            List<Turno> turnos = service.findAll();
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos obtenidos exitosamente", turnos));
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
    public ResponseEntity<ApiResponse<Turno>> create(@RequestBody Turno turno){
        try {
            Turno savedTurno = service.save(turno);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Turno creado exitosamente", savedTurno));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al crear turno: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Turno>> update(@RequestBody Turno turno, @PathVariable Long id){
        try {
            Optional<Turno> updatedTurno = service.update(turno, id);
            if(updatedTurno.isPresent()){
                return ResponseEntity.ok(new ApiResponse<>(true, "Turno actualizado exitosamente", updatedTurno.get()));
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
    @GetMapping("/business/{businessId}")
    public ResponseEntity<ApiResponse<List<Turno>>> getByBusiness(@PathVariable Integer businessId){
        try {
            List<Turno> turnos = service.findByNegocioId(businessId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos del negocio obtenidos exitosamente", turnos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos del negocio: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<ApiResponse<List<Turno>>> getByNegocio(@PathVariable Integer negocioId){
        try {
            List<Turno> turnos = service.findByNegocioId(negocioId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos del negocio obtenidos exitosamente", turnos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos del negocio: " + e.getMessage(), null));
        }
    }
    
    @PostMapping("/negocio/{negocioId}")
    public ResponseEntity<ApiResponse<List<Turno>>> getByNegocioPost(@PathVariable Integer negocioId){
        try {
            List<Turno> turnos = service.findByNegocioId(negocioId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos del negocio obtenidos exitosamente", turnos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos del negocio: " + e.getMessage(), null));
        }
    }
    
    @GetMapping("/business/{businessId}/ordered")
    public ResponseEntity<ApiResponse<List<Turno>>> getByBusinessOrdered(@PathVariable Integer businessId){
        try {
            List<Turno> turnos = service.findTurnosByNegocioIdOrderByDateTime(businessId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos del negocio ordenados obtenidos exitosamente", turnos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos del negocio: " + e.getMessage(), null));
        }
    }

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
    
    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<Turno>>> getByClientePost(@PathVariable Integer clienteId){
        try {
            List<Turno> turnos = service.findByClienteId(clienteId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos del cliente obtenidos exitosamente", turnos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos del cliente: " + e.getMessage(), null));
        }
    }

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
    
    @GetMapping("/servicio/{tipoServicio}")
    public ResponseEntity<ApiResponse<List<Turno>>> getByTipoServicio(@PathVariable String tipoServicio){
        try {
            List<Turno> turnos = this.service.findByTipoServicio(tipoServicio);
            return ResponseEntity.ok(new ApiResponse<>(true, "Turnos por servicio obtenidos exitosamente", turnos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener turnos por servicio: " + e.getMessage(), null));
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
                turnos = service.findByNegocioIdAndFecha(negocioId, localDate);
            } else if (negocioId != null) {
                turnos = service.findByNegocioId(negocioId);
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
