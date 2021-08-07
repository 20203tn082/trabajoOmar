package mx.com.siso.model.response_file;

import mx.com.siso.model.records.BeanRecords;
import mx.com.siso.service.ConnectionMySQL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoResponse {
    Connection con;
    CallableStatement cstm;
    ResultSet rs;
    public int[] createResponse(BeanResponse_file file) throws SQLException {
        int[] resultado = new int[3];
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call create_response(?,?,?,?,?)}");
            cstm.setInt(1, file.getMinutes_id().getId_minutes());
            cstm.setString(2,file.getMinutes_id().getComment());
            cstm.setBlob(3, file.getFileResponse());
            cstm.registerOutParameter(4, java.sql.Types.INTEGER);
            cstm.registerOutParameter(5, java.sql.Types.INTEGER);
            cstm.execute();
            int errorId  = cstm.getInt(4);
            int errorAttended  = cstm.getInt(5);
            if(errorId == 0 &&  errorAttended==0){
               resultado[0] = 1;
            }else {
                if (errorId == 1){
                    resultado[1] = 1;
                }else {
                    if (errorAttended == 1){
                        resultado[2] = 1;
                    }
                }
            }
        }catch (SQLException e){
            System.out.printf("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm);
        }
        return resultado;
    }
    public boolean changeAttended(BeanResponse_file file) throws SQLException {
        boolean flag = false;
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call change_attended(?)}");
            cstm.setInt(1, file.getMinutes_id().getId_minutes());
            flag = cstm.execute();

        }catch (SQLException e){
            System.out.printf("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm);
        }
        return flag;
    }
}
