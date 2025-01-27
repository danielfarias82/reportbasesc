package com.bns.business.reports.reportsbddexp.repo;

import com.bns.business.reports.reportsbddexp.model.ExperianDataVerificationInputParam;
import com.bns.business.reports.reportsbddexp.model.BdPri38SvcConUsuInputParam;
import com.bns.business.reports.reportsbddexp.model.BdPri38SvcConparInputParam;
import com.bns.business.reports.reportsbddexp.model.BdPri38SvcVisorInformesInputParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BusinessReportPrimaryRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Ejecuta el procedimiento almacenado "svc_conusu" para validar el usuario.
     */
    public Map<String, Object> executeBdPri38SvcConUsu(BdPri38SvcConUsuInputParam param) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("bd_pri_38.admi.svc_conusu");
        query.registerStoredProcedureParameter("rut_usu", String.class, ParameterMode.IN);
        query.setParameter("rut_usu", param.getRutUsu());

        return executeStoredProcedure(query);
    }

    /**
     * Ejecuta el procedimiento almacenado "svc_conpar" para obtener parámetros del
     * servicio.
     */
    public Map<String, Object> executeBdPri38SvcConpar(BdPri38SvcConparInputParam param) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("bd_pri_38.admi.svc_conpar");
        query.registerStoredProcedureParameter("emp_cod", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("cod_con", String.class, ParameterMode.IN);

        query.setParameter("emp_cod", param.getEmpCod());
        query.setParameter("cod_con", param.getCodCon());

        return executeStoredProcedure(query);
    }

    /**
     * Ejecuta el procedimiento almacenado "svc_visor_informes" para consultar o
     * insertar datos en el visor.
     */
    public Map<String, Object> executeBdPri38SvcVisorInformes(BdPri38SvcVisorInformesInputParam param) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("bd_pri_38.admi.svc_visor_informes");
        query.registerStoredProcedureParameter("szData", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("cod_accion", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("rut_cliente", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("cod_con", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("cant_det", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("szModo", String.class, ParameterMode.IN);

        query.setParameter("szData", param.getSzData());
        query.setParameter("cod_accion", param.getCodAccion());
        query.setParameter("rut_cliente", param.getRutCliente());
        query.setParameter("cod_con", param.getCodCon());
        query.setParameter("cant_det", param.getCantDet());
        query.setParameter("szModo", param.getSzModo());

        return executeStoredProcedure(query);
    }

    /**
     * Método genérico para ejecutar procedimientos almacenados y manejar el
     * resultado.
     */
    private Map<String, Object> executeStoredProcedure(StoredProcedureQuery query) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            query.execute();
            resultMap.put("#result-set-1", query.getResultList());
        } catch (Exception e) {
            resultMap.put("error", e.getMessage());
        }
        return resultMap;
    }
}
