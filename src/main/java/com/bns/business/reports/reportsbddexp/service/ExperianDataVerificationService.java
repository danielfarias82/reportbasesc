package com.bns.business.reports.reportsbddexp.service;

import com.bns.business.reports.reportsbddexp.model.ExperianDataVerificationInputParam;
import com.bns.business.reports.reportsbddexp.model.ResultJson;
import com.bns.business.reports.reportsbddexp.repo.BusinessReportPrimaryJdbcTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExperianDataVerificationService {

    @Autowired
    private BusinessReportPrimaryJdbcTemplateRepository repository;

    public ResponseEntity<Object> processQuery(ExperianDataVerificationInputParam params) {
        ResultJson resultOutput = new ResultJson();

        try {
            // Validación del usuario
            System.out.println("Validando usuario: " + params.getUsuario());
            List<Map<String, Object>> resultSvcConUsu = repository.executeBdPri38SvcConUsu(params.getUsuario());

            if (resultSvcConUsu == null || resultSvcConUsu.isEmpty()) {
                resultOutput.setCode(403);
                resultOutput.setMessage("Usuario no autorizado para realizar esta consulta.");
                return ResponseEntity.status(403).body(resultOutput);
            }
            System.out.println("Usuario validado con éxito.");

            // Llamada al procedimiento svc_conpar
            String empCod = "1"; // Código de empresa fijo
            String codCon = params.getTipoConsulta();

            if (codCon == null || codCon.isEmpty()) {
                resultOutput.setCode(400);
                resultOutput.setMessage("El campo 'tipoConsulta' es obligatorio y no puede ser nulo.");
                return ResponseEntity.status(400).body(resultOutput);
            }

            System.out.println("Consultando svc_conpar con empCod=" + empCod + " y codCon=" + codCon);

            List<Map<String, Object>> resultSvcConpar = repository.executeBdPri38SvcConpar(empCod, codCon);

            // Validar que resultSvcConpar tenga datos
            if (resultSvcConpar == null || resultSvcConpar.isEmpty()) {
                resultOutput.setCode(404);
                resultOutput.setMessage("No se encontraron datos válidos en svc_conpar.");
                return ResponseEntity.status(404).body(resultOutput);
            }

            // Procesar los resultados directamente de la lista
            Map<String, Object> firstRow = resultSvcConpar.get(0);

            if (firstRow == null) {
                resultOutput.setCode(500);
                resultOutput.setMessage("Formato inesperado de datos en svc_conpar.");
                return ResponseEntity.status(500).body(resultOutput);
            }

            String conCod = firstRow.get("nom_ext_con") != null ? firstRow.get("nom_ext_con").toString() : null;
            Integer canDet = firstRow.get("can_det") != null ? Integer.parseInt(firstRow.get("can_det").toString()) : null;

            if (conCod == null || canDet == null) {
                resultOutput.setCode(500);
                resultOutput.setMessage("Datos incompletos en el resultado de svc_conpar.");
                return ResponseEntity.status(500).body(resultOutput);
            }

            System.out.println("Datos capturados de svc_conpar: conCod=" + conCod + ", canDet=" + canDet);

            // Llamada a svc_visor_informes
            List<Map<String, Object>> visorResult = repository.executeBdPri38SvcVisorInformes(
                    params.getSerie(), "C", params.getRut(), conCod, canDet, "A");

            if (visorResult == null) {
                resultOutput.setCode(404);
                resultOutput.setMessage("No se encontraron datos en el visor de informes.");
                return ResponseEntity.status(404).body(resultOutput);
            }

            // Si el resultado es una lista vacía
            if (visorResult.isEmpty()) {
                resultOutput.setCode(404);
                resultOutput.setMessage("No se encontraron datos en el visor de informes.");
                return ResponseEntity.status(404).body(resultOutput);
            }

            // Respuesta exitosa
            resultOutput.setCode(200);
            resultOutput.setMessage("Consulta realizada con éxito.");
            resultOutput.setResult(visorResult);
            return ResponseEntity.status(200).body(resultOutput);

        } catch (Exception e) {
            e.printStackTrace();
            resultOutput.setCode(500);
            resultOutput.setMessage("Error interno: " + e.getMessage());
            return ResponseEntity.status(500).body(resultOutput);
        }
    }
}
