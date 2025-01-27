package com.bns.business.reports.reportsbddexp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BdPri38SvcVisorInformesInputParam {
    private int mode; // Modo del procedimiento (siempre 0)
    private String szData; // Data en formato texto
    private String codAccion; // Acción: I (Insertar), A (Actualizar), C (Consultar)
    private String rutCliente; // RUT del cliente
    private String codCon; // Código de consulta
    private int cantDet; // Cantidad de detalles
    private String szModo; // Modo del procedimiento
}
