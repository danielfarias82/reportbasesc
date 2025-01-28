package com.bns.business.reports.reportsbddexp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BdPri38SvcConparInputParam {
    private int returnValue; // Valor de retorno (RETURN_VALUE)
    private String empCod; // Código de empresa (@emp_cod)
    private String codCon; // Código de consulta (@cod_con)
}
