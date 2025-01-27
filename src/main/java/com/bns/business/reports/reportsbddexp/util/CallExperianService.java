package com.bns.business.reports.reportsbddexp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CallExperianService {

    private static final Logger logger = LoggerFactory.getLogger(CallExperianService.class);
    private final RestTemplate restTemplate;

    public CallExperianService() {
        this.restTemplate = new RestTemplate(); // RestTemplate para realizar la llamada HTTP
    }

    /**
     * Realiza una llamada a un servicio externo y solo registra la salida.
     *
     * @param url     URL del servicio a llamar
     * @param method  Método HTTP a usar (por ejemplo, POST o GET)
     * @param payload Cuerpo de la solicitud
     * @return Un objeto genérico que representa la respuesta, actualmente null
     */
    public Object callExperian(String url, HttpMethod method, Map<String, String> payload) {
        try {
            // Crear la solicitud y enviar
            logger.info("Iniciando llamada al servicio Experian...");
            logger.info("URL: {}", url);
            logger.info("Método: {}", method);
            logger.info("Payload: {}", payload);

            ResponseEntity<String> response = restTemplate.postForEntity(url, payload, String.class);

            // Log de la respuesta
            logger.info("Respuesta del servicio Experian: {}", response.getBody());

            // Por ahora, no se procesa la salida
            return null;

        } catch (Exception e) {
            // Manejo de excepciones en la llamada
            logger.error("Error al llamar al servicio Experian: {}", e.getMessage());
            return null;
        }
    }
}
