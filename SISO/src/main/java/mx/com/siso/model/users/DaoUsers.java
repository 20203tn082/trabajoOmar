package mx.com.siso.model.users;

import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import mx.com.siso.model.department.BeanDepartment;
import mx.com.siso.model.user_type.BeanUser_type;
import mx.com.siso.model.users.BeanUsers;

import mx.com.siso.service.ConnectionMySQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoUsers {
    Connection con;
    CallableStatement cstm;
    ResultSet rs;
    static Logger logger = LoggerFactory.getLogger(DaoUsers.class);


    public int create(BeanUsers user) throws SQLException {
        int resultado = 0;
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call create_user(?,?,?,?,?,?,?,?,?,?,?,?)}");
            cstm.setString(1,user.getNameUser() );
            cstm.setString(2, user.getPasswordUser());
            cstm.setString(3, user.getName());
            cstm.setString(4,user.getLastname1());
            cstm.setString(5,user.getLastname2());
            cstm.setString(6, user.getEmail());
            cstm.setInt(7,user.getDepartment_id().getIdDepartment());
            cstm.setInt(8, user.getType_id().getIdType());
            cstm.registerOutParameter(9, java.sql.Types.INTEGER);
            cstm.registerOutParameter(10, java.sql.Types.INTEGER);
            cstm.registerOutParameter(11, java.sql.Types.INTEGER);
            cstm.registerOutParameter(12, java.sql.Types.INTEGER);
            cstm.execute();
            int errorName = cstm.getInt(9);
            int errorEmail = cstm.getInt(10);
            if(errorName ==0 && errorEmail==0){
                System.out.println("Se registro correctamente");
                resultado = 1;
            }else {
                if (errorName ==1 && errorEmail==1){
                    resultado = 4;
                } else{
                    if(errorName==1){
                        System.out.println("El nombre se usuario ya se encuentra registrado");
                        resultado = 2;
                    }else if(errorEmail==1){
                        System.out.println("El correo ya se encuentra registrado");
                        resultado = 3;
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("Error: " + e);
        }finally{
            con.close();
        }
        return resultado;
    }

    public List<BeanUsers> findAllUsers(){
        List<BeanUsers> listUsers = new ArrayList<>();
        try {
            // SELECT * FROM users AS U INNER JOIN persons AS P ON U.idPerson = P.id INNER JOIN roles AS R ON U.idRole = R.id;
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call find_users}");
            rs = cstm.executeQuery();

            while(rs.next()){
                BeanUser_type beanUserType =new BeanUser_type();
                BeanDepartment beanDepartment = new BeanDepartment();
                BeanUsers beanUsers = new BeanUsers();

                beanUserType.setIdType(rs.getInt("type_id"));
                beanUserType.setNameType(rs.getString("type_name"));

                beanDepartment.setIdDepartment(rs.getInt("department_id"));
                beanDepartment.setNameDepartment(rs.getString("department_name"));
                beanDepartment.setDescription(rs.getString("description"));
                beanDepartment.setTelephoneNumber(rs.getString("phone_number"));
                beanDepartment.setCurrentDepartment(0);

                beanUsers.setId_user(rs.getInt("user_id"));
                beanUsers.setNameUser(rs.getString("username"));
                beanUsers.setPasswordUser(rs.getString("user_password"));
                beanUsers.setName(rs.getString("name"));
                beanUsers.setLastname1(rs.getString("lastname_1"));
                beanUsers.setLastname2(rs.getString("lastname_2"));
                beanUsers.setEmail(rs.getString("email"));
                beanUsers.setCurrentUser(rs.getInt("user_status"));
                beanUsers.setDepartment_id(beanDepartment);
                beanUsers.setType_id(beanUserType);

                listUsers.add(beanUsers);
            }
        }catch (SQLException e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm, rs);
        }
        return listUsers;
    }

    public BeanUsers findUserById(long id){
        BeanUsers beanUsers = null;
        try {
            // SELECT * FROM users AS U INNER JOIN persons AS P ON U.idPerson = P.id INNER JOIN roles AS R ON U.idRole = R.id;
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call find_user_byId(?)}");
            cstm.setLong(1, id);
            rs = cstm.executeQuery();

            if(rs.next()){
                BeanUser_type beanUserType =new BeanUser_type();
                BeanDepartment beanDepartment = new BeanDepartment();
                beanUsers = new BeanUsers();

                beanDepartment.setIdDepartment(rs.getInt("department_id"));
                beanDepartment.setNameDepartment(rs.getString("department_name"));
                beanUserType.setIdType(rs.getInt("type_id"));
                beanUserType.setNameType(rs.getString("type_name"));

                beanUsers.setId_user(rs.getInt("user_id"));
                beanUsers.setNameUser(rs.getString("username"));
                beanUsers.setPasswordUser(rs.getString("user_password"));
                beanUsers.setName(rs.getString("name"));
                beanUsers.setLastname1(rs.getString("lastname_1"));
                beanUsers.setLastname2(rs.getString("lastname_2"));
                beanUsers.setEmail(rs.getString("email"));
                beanUsers.setCurrentUser(rs.getInt("user_status"));
                beanUsers.setDepartment_id(beanDepartment);
                beanUsers.setType_id(beanUserType);


            }
        }catch (SQLException e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm, rs);
        }
        return beanUsers;
    }


    public static void main(String[] args) {
        DaoUsers daoUsers = new DaoUsers();
        BeanUsers beanUsers = new BeanUsers();
        daoUsers.findUserById(2);
        System.out.println(beanUsers.getNameUser());
    }

    public int[] update(BeanUsers user) throws SQLException {
        int[] resultado = new int[6];
        System.out.println(user.getId_user());
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call  modify_user(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            cstm.setInt(1,user.getId_user());
            cstm.setString(2,user.getNameUser() );
            cstm.setString(3, user.getPasswordUser());
            cstm.setString(4, user.getName());
            cstm.setString(5,user.getLastname1());
            cstm.setString(6,user.getLastname2());
            cstm.setString(7, user.getEmail());
            cstm.setInt(8,user.getDepartment_id().getIdDepartment());
            cstm.setInt(9, user.getType_id().getIdType());
            cstm.registerOutParameter(10, java.sql.Types.INTEGER);
            cstm.registerOutParameter(11, java.sql.Types.INTEGER);
            cstm.registerOutParameter(12, java.sql.Types.INTEGER);
            cstm.registerOutParameter(13, java.sql.Types.INTEGER);
            cstm.registerOutParameter(14, java.sql.Types.INTEGER);
            cstm.registerOutParameter(15, java.sql.Types.INTEGER);

            cstm.execute();
            int errorUser = cstm.getInt(10);
            int errorName = cstm.getInt(11);
            int errorEmail = cstm.getInt(12);
            int errorDepartament = cstm.getInt(13);
            int errorType = cstm.getInt(14);
            int succes = cstm.getInt(15);
            if(succes==1){
                resultado[0] = 1;
            }else{
                if(errorUser==1){
                    resultado[1] = 1;
                }else{
                    if(errorName==1){
                        resultado[2] = 1;
                    }
                    if(errorEmail==1){
                        resultado[3] = 1;
                    }
                }
                if(errorDepartament==1){
                    resultado[4] = 1;
                }
                if(errorType==1){
                    resultado[5] = 1;
                }
            }

        }catch(SQLException e){
            System.out.println("Error: " + e);
        }finally{
            ConnectionMySQL.closeConnection(con, cstm);
        }
        return resultado;
    }

    public boolean delete(BeanUsers user) throws SQLException {
        boolean flag = false;
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call delete_user(?,?)}");
            cstm.setInt(1, user.getId_user());
            cstm.registerOutParameter(2, java.sql.Types.INTEGER);
            cstm.execute();
            int errorCurrent = cstm.getInt(2);
            if(errorCurrent==0){
                System.out.println("Se elimino correctamente");
                flag = true;
            }else {
                System.out.println("El usuario no existe");
            }

        }catch(SQLException e){
            System.out.println("Error: " + e);
        }finally{
            ConnectionMySQL.closeConnection(con, cstm);
        }
        return flag;
    }
    public int[] login(BeanUsers user) throws SQLException {
        int resultado[] = new int[4];
        int error1 = 0, error2=0;
        try{
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call login(?,?,?,?,?,?)}");
            cstm.setString(1, user.getNameUser());
            cstm.setString(2, user.getPasswordUser());
            cstm.registerOutParameter(3, java.sql.Types.INTEGER);
            cstm.registerOutParameter(4, java.sql.Types.INTEGER);
            cstm.registerOutParameter(5, java.sql.Types.INTEGER);
            cstm.registerOutParameter(6, java.sql.Types.INTEGER);
            cstm.execute();

            resultado[0] = cstm.getInt(3);
            resultado[1] = cstm.getInt(4);
            int errorNameUser = cstm.getInt(5);
            int errorPassUser = cstm.getInt(6);
            System.out.println(errorPassUser);
            if(resultado[1]!=0){
                System.out.println("Se accedio correctamente");
                System.out.println("El usuario es un tipo: "  + resultado[1]);
            }else{
                if(errorNameUser  == 1){
                    resultado[2] = 1;
                }else{
                    resultado[2] = 0;
                    if (errorPassUser == 1){
                        resultado[3] = 1;
                    } else {
                        resultado[3] = 0;
                    }
                }
            }
        }catch(SQLException e){
            System.out.println("Error: " + e);
        }finally{
            con.close();
        }
        return resultado;
    }
    public boolean checkEmail(BeanUsers user){
        boolean flag = false;
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call check_email(?,?,?)}");
            cstm.setString(1, user.getEmail());
            cstm.setString(2, user.getToken());
            cstm.registerOutParameter(3, java.sql.Types.INTEGER);
            cstm.execute();
            int errorEmail = cstm.getInt(3);
            if(errorEmail==0){
                flag = true;
            }

        }catch (SQLException e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm);
        }
        return flag;
    }
    public boolean checkToken(BeanUsers user){
        boolean flag = false;
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call check_token(?,?,?,?,?)}");
            cstm.setString(1, user.getPasswordUser());
            cstm.setString(2, user.getToken());
            cstm.registerOutParameter(3, java.sql.Types.INTEGER);
            cstm.registerOutParameter(4, java.sql.Types.INTEGER);
            cstm.registerOutParameter(5, java.sql.Types.INTEGER);
            rs = cstm.executeQuery();

            int errorToken = cstm.getInt(3);
            int errorHour = cstm.getInt(4);
            int success = cstm.getInt(5);
            if(success==1){
                flag = true;
            }else{
                if(errorToken==1){
                    flag = false;
                }else{
                    if(errorHour==1){
                        flag = false;
                    }
                }
            }

        }catch (SQLException e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm, rs);
        }
        return flag;
    }
    public List<BeanUsers> findAllAssitant(){
        List<BeanUsers> listUsers = new ArrayList<>();
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call find_assistant}");
            rs = cstm.executeQuery();

            while(rs.next()){
                BeanUsers beanUsers = new BeanUsers();

                beanUsers.setId_user(rs.getInt("user_id"));
                beanUsers.setNameUser(rs.getString("username"));
                beanUsers.setName(rs.getString("name"));
                listUsers.add(beanUsers);
            }
        }catch (SQLException e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm, rs);
        }
        return listUsers;
    }
    public int[] findAttempts(int id){
        int[] resultado = new int[2];
        try {
            System.out.println("El id es: " + id);
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call find_attempts(?, ?,?)}");
            cstm.setInt(1, id);
            cstm.setInt(2, Types.INTEGER);
            cstm.setString(3, String.valueOf(Types.VARCHAR));
            cstm.execute();
            resultado[0] = cstm.getInt(2);
            System.out.println("Los intentos son: " + resultado[0]);
            resultado[1] = Integer.parseInt(cstm.getString(3));
            System.out.println("Los minutos son: " + resultado[1]);
        }catch (SQLException e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm, rs);
        }
        return resultado;
    }
    public void increaseAttempts(int id){
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call increase_attempts(?)}");
            cstm.setInt(1, id);
            cstm.execute();
        }catch (SQLException e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm, rs);
        }
    }
    public void restartAttempts(int id){
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call restart_attempts(?)}");
            cstm.setInt(1, id);
            cstm.execute();
        }catch (SQLException e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm, rs);
        }
    }
    public int findIdByUsername(String username){
        int  resultado = 0;
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall("{call find_idByUsername(?, ?)}");
            cstm.setString(1, username);
            cstm.setInt(2, Types.INTEGER);
            cstm.execute();
            resultado = cstm.getInt(2);

        }catch (SQLException e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnection(con, cstm, rs);
        }
        return resultado;
    }
}

