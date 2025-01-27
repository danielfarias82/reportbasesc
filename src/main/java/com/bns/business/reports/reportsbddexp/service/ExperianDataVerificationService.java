package com.bns.business.reports.reportsbddexp.service;

import com.bns.business.reports.reportsbddexp.model.BdPri38SvcConUsuInputParam;
import com.bns.business.reports.reportsbddexp.model.BdPri38SvcConparInputParam;
import com.bns.business.reports.reportsbddexp.model.BdPri38SvcVisorInformesInputParam;
import com.bns.business.reports.reportsbddexp.model.ExperianDataVerificationInputParam;
import com.bns.business.reports.reportsbddexp.model.ResultJson;
import com.bns.business.reports.reportsbddexp.repo.BusinessReportPrimaryRepository;
import com.bns.business.reports.reportsbddexp.util.CallExperianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class ExperianDataVerificationService {

    @Autowired
    private BusinessReportPrimaryRepository repository;

    @Autowired
    private CallExperianService callExperianService;

    public ResponseEntity<Object> processQuery(ExperianDataVerificationInputParam params) {
        ResultJson resultOutput = new ResultJson();

        try {
            // 1. Validación del usuario (SP: svc_conusu)
            Map<String, Object> resultSvcConUsu = repository.executeBdPri38SvcConUsu(
                    new BdPri38SvcConUsuInputParam(0, params.getUsuario()));

            ArrayList<Map> svcConUsu = (ArrayList<Map>) resultSvcConUsu.get("#result-set-1");

            if (svcConUsu == null || svcConUsu.isEmpty()) {
                resultOutput.setCode(403);
                resultOutput.setMessage("Usuario no autorizado.");
                return ResponseEntity.status(403).body(resultOutput);
            }

            // 2. Obtención de parámetros del servicio (SP: svc_conpar)
            Map<String, Object> resultSvcConpar = repository.executeBdPri38SvcConpar(
                    new BdPri38SvcConparInputParam(0, "1", params.getTipoConsulta()));

            ArrayList<Map> svcConPar = (ArrayList<Map>) resultSvcConpar.get("#result-set-1");

            if (svcConPar == null || svcConPar.isEmpty()) {
                resultOutput.setCode(404);
                resultOutput.setMessage("Parámetros del servicio no encontrados.");
                return ResponseEntity.status(404).body(resultOutput);
            }

            String conCod = svcConPar.get(0).get("nom_ext_con").toString();
            int prdCon = Integer.parseInt(svcConPar.get(0).get("prd_con").toString());

            // 3. Consulta al visor de informes (SP: svc_visor_informes)
            Map<String, Object> resultSvcVisorInformes = repository.executeBdPri38SvcVisorInformes(
                    new BdPri38SvcVisorInformesInputParam(0, "", "C", params.getRut(), conCod, 1, "A"));

            Object resultDataVisor = resultSvcVisorInformes.get("#result-set-1");

            // 4. Validación de datos existentes o expirados
            if (resultDataVisor == null || !isDataValid(resultDataVisor, prdCon, params.getFecha())) {
                // 5. Llamada al servicio externo (Experian)
                String url = getPartialUrl(params.getTipoConsulta());
                Map<String, String> input = getInput(params);
                Object response = callExperianService.callExperian(url, HttpMethod.POST, input);

                if (response instanceof ResultJson) {
                    ResultJson result = (ResultJson) response;

                    if (result.getCode() == 200) {
                        String dataVisor = result.getResult().toString();
                        repository.executeBdPri38SvcVisorInformes(
                                new BdPri38SvcVisorInformesInputParam(0, dataVisor, "I", params.getRut(), conCod, 1,
                                        "E"));
                    } else {
                        return ResponseEntity.status(400).body(result);
                    }
                }
            }

            resultOutput.setCode(200);
            resultOutput.setMessage("Consulta realizada con éxito.");
            return ResponseEntity.status(200).body(resultOutput);

        } catch (Exception e) {
            resultOutput.setCode(500);
            resultOutput.setMessage("Error interno: " + e.getMessage());
            return ResponseEntity.status(500).body(resultOutput);
        }
    }

    private boolean isDataValid(Object resultDataVisor, int prdCon, String fechaConsulta) {
        try {
            String businessDate = extractDateFromVisorData(resultDataVisor.toString());
            LocalDate fechaVisor = LocalDate.parse(businessDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
            LocalDate fechaActual = LocalDate.parse(fechaConsulta, DateTimeFormatter.ofPattern("yyyyMMdd"));
            long diffDays = ChronoUnit.DAYS.between(fechaVisor, fechaActual);
            return diffDays <= prdCon;
        } catch (Exception e) {
            return false;
        }
    }

    private String extractDateFromVisorData(String visorData) {
        return visorData.substring(18, 26);
    }

    private Map<String, String> getInput(ExperianDataVerificationInputParam params) {
        return Map.of(
                "rut", params.getRut(),
                "serie", params.getSerie());
    }

    private String getPartialUrl(String tipoConsulta) {
        switch (tipoConsulta) {
            case "IR07":
                return "cl/cr/irut0702/v1/infracciones-laborales/obtener";
            default:
                throw new IllegalArgumentException("Tipo de consulta no soportado");
        }
    }
}
