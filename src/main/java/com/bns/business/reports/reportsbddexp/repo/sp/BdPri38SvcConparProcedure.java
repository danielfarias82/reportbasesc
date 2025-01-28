package com.bns.business.reports.reportsbddexp.repo.sp;

import com.bns.business.reports.reportsbddexp.model.BdPri38SvcConparInputParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class BdPri38SvcConparProcedure implements CallableStatementCreator {

    private static final Logger logger = LoggerFactory.getLogger(BdPri38SvcConparProcedure.class);

    // Parámetros del procedimiento almacenado
    private static final List<SqlParameter> params = java.util.Arrays.asList(
            new SqlParameter(Types.INTEGER), // RETURN_VALUE
            new SqlParameter(Types.CHAR), // @emp_cod
            new SqlParameter(Types.CHAR) // @cod_con
    );

    private final BdPri38SvcConparInputParam param;

    public BdPri38SvcConparProcedure(BdPri38SvcConparInputParam param) {
        this.param = param;
    }

    @Override
    public CallableStatement createCallableStatement(Connection connection) {
        final String sql = "{call bd_pri_38.admi.svc_conpar (?,?)}"; // Ajusta a solo dos parámetros
        try {
            CallableStatement callableStatement = connection.prepareCall(sql);

            // Configuración de parámetros
            callableStatement.setString(1, param.getEmpCod().trim()); // Ajuste para char(1)
            callableStatement.setString(2, param.getCodCon().trim()); // Ajuste para char(4)

            return callableStatement;

        } catch (SQLException e) {
            logger.error("[createCallableStatement] Error: Failed to prepare call to " + sql, e);
            throw new RuntimeException("Error al preparar el SP svc_conpar", e);
        }
    }

    public List<SqlParameter> getParams() {
        return params;
    }
}
