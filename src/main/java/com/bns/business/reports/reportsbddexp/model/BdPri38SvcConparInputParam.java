package com.bns.business.reports.reportsbddexp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BdPri38SvcConparInputParam {
    private int mode; // Modo del procedimiento (siempre 0)
    private String empCod; // Código de la empresa
    private String codCon; // Código de consulta
}
