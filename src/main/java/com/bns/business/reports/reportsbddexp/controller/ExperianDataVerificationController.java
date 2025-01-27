package com.bns.business.reports.reportsbddexp.controller;

import com.bns.business.reports.reportsbddexp.model.ExperianDataVerificationInputParam;
import com.bns.business.reports.reportsbddexp.service.ExperianDataVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-market-data")
public class ExperianDataVerificationController {

    @Autowired
    private ExperianDataVerificationService service;

    /**
     * Endpoint para procesar consultas al servicio de verificación de datos.
     *
     * @param params Parámetros de entrada para la consulta
     * @return Respuesta del servicio, encapsulada en un ResponseEntity
     */
    @PostMapping("/verify")
    public ResponseEntity<Object> verifyData(@RequestBody ExperianDataVerificationInputParam params) {
        return service.processQuery(params);
    }
}
