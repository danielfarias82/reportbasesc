package com.bns.business.reports.reportsbddexp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BdPri38SvcConUsuInputParam {
    private int mode; // Modo del procedimiento (siempre 0)
    private String rutUsu; // RUT del usuario
}
