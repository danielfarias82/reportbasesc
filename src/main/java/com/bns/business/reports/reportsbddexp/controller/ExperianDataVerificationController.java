package com.bns.business.reports.reportsbddexp.controller;

import com.bns.business.reports.reportsbddexp.model.ExperianDataVerificationInputParam;
import com.bns.business.reports.reportsbddexp.service.ExperianDataVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/experian")
public class ExperianDataVerificationController {

    @Autowired
    private ExperianDataVerificationService service;

    /**
     * Endpoint para procesar la consulta de Experian.
     *
     * @param params Parámetros de entrada
     * @return Respuesta de la operación
     */
    @PostMapping("/process-query")
    public ResponseEntity<Object> processQuery(@RequestBody ExperianDataVerificationInputParam params) {
        try {
            return service.processQuery(params);
        } catch (Exception e) {
            // Manejo de errores global en el controlador
            return ResponseEntity.status(500).body("Error interno del servidor: " + e.getMessage());
        }
    }
}
