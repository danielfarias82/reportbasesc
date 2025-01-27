package com.bns.business.reports.reportsbddexp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperianDataVerificationInputParam {
    private String fecha;
    private String usuario;
    private String codigoSucursal;
    private String tipoConsulta;
    private String rut;
    private String serie;
    private String temporalidad;
    private String descripcion;
    private String validaNuevos;
    private String institucion;
}
