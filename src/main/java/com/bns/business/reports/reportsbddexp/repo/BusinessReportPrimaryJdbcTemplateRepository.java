package com.bns.business.reports.reportsbddexp.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BusinessReportPrimaryJdbcTemplateRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Ejecuta el procedimiento almacenado "svc_conusu" para validar el usuario.
     *
     * @param rutUsu Identificador del usuario a validar.
     * @return Lista de mapas con la información del resultado.
     */
    public List<Map<String, Object>> executeBdPri38SvcConUsu(String rutUsu) {
        String sql = "{call bd_pri_38.admi.svc_conusu(?)}";
        return jdbcTemplate.query(sql, ps -> ps.setString(1, rutUsu), (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("ind_res", rs.getInt("ind_res"));
            return row;
        });
    }

    /**
     * Ejecuta el procedimiento almacenado "svc_conpar" para obtener parámetros del
     * servicio.
     *
     * @param empCod Código de la empresa.
     * @param codCon Código de la consulta.
     * @return Lista de mapas con los resultados de `tb_par`.
     */
    public List<Map<String, Object>> executeBdPri38SvcConpar(String empCod, String codCon) {
        String sql = "{call bd_pri_38.admi.svc_conpar(?, ?)}";
        return jdbcTemplate.query(sql, ps -> {
            ps.setString(1, empCod);
            ps.setString(2, codCon);
        }, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("cod_con", rs.getString("cod_con"));
            row.put("glo_con", rs.getString("glo_con"));
            row.put("emp_cod", rs.getString("emp_cod"));
            row.put("nom_ext_con", rs.getString("nom_ext_con"));
            row.put("prd_con", rs.getInt("prd_con"));
            row.put("tip_res", rs.getInt("tip_res"));
            row.put("reg_jou", rs.getInt("reg_jou"));
            row.put("can_det", rs.getInt("can_det"));
            return row;
        });
    }

    /**
     * Ejecuta el procedimiento almacenado "svc_visor_informes" para consultar o
     * insertar datos en el visor.
     *
     * @param szData     Datos de entrada.
     * @param codAccion  Código de la acción (insertar o consultar).
     * @param rutCliente Identificador del cliente.
     * @param codCon     Código de la consulta.
     * @param cantDet    Cantidad de detalles.
     * @param szModo     Modo de operación.
     * @return Lista de resultados obtenidos del visor.
     */
    public List<Map<String, Object>> executeBdPri38SvcVisorInformes(String szData, String codAccion, String rutCliente,
            String codCon, int cantDet, String szModo) {
        String sql = "{call bd_pri_38.admi.svc_visor_informes(?, ?, ?, ?, ?, ?)}";
        return jdbcTemplate.queryForList(sql, szData, codAccion, rutCliente, codCon, cantDet, szModo);
    }
}
