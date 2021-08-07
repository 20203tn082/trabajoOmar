package mx.com.siso.controler;

import mx.com.siso.model.department.BeanDepartment;
import mx.com.siso.model.department.DaoDepartment;
import mx.com.siso.model.records.DaoRecords;
import mx.com.siso.model.user_type.BeanUser_type;
import mx.com.siso.model.users.BeanUsers;
import mx.com.siso.model.users.DaoUsers;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ServletUsers", value = "/ServletUsers")
public class ServletUsers extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        switch(action) {
            case "users":
            request.setAttribute("listUsers", new DaoUsers().findAllUsers());
            request.getRequestDispatcher("/views/users/users.jsp").forward(request, response);
            break;
            case "register":
            request.setAttribute("listDepartment", new DaoDepartment().findDepartment());
            request.getRequestDispatcher("/views/users/register.jsp").forward(request, response);
            break;

            default:
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        switch(action){
            case "create":
                // do something
                int resultado3;
                String nameUser = request.getParameter("nameUser") != null ? request.getParameter("nameUser") : "";
                String password = request.getParameter("password")!= null ? request.getParameter("password") : "";
                String name = request.getParameter("name")!= null ? request.getParameter("name") : "";
                String lastname1 = request.getParameter("lastname1")!= null ? request.getParameter("lastname1") : "";
                String lastname2 = request.getParameter("lastname2")!= null ? request.getParameter("lastname2") : "";
                int departmentId = Integer.parseInt(request.getParameter("departmentId")!= null ? request.getParameter("departmentId") : "0");
                int type = Integer.parseInt(request.getParameter("type")!= null ? request.getParameter("type") : "0");
                String email = request.getParameter("email")!= null ? request.getParameter("email") : "";

                BeanUser_type beanUser_type = new BeanUser_type(type, "");
                BeanDepartment beanDepartment = new BeanDepartment(departmentId, "", "", "", 0);
                BeanUsers beanUsers = new BeanUsers(0, nameUser, password, name, lastname1, lastname2, email,0, "", null,0, "", beanDepartment, beanUser_type);

                try {
                    resultado3 = new DaoUsers().create(beanUsers);
                    switch (resultado3){
                        case 1:
                            System.out.println("Se ha registrado correctamente");
                            break;
                        case 2:
                            System.out.println("El nombre de usuario ya existe");
                            break;
                        case 3:
                            System.out.println("El correo ya existe");
                            break;
                        case 4:
                            System.out.println("El nombre de usuario ya existe");
                            System.out.println("El correo ya existe");
                            break;
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                request.setAttribute("listUsers", new DaoUsers().findAllUsers());
                request.getRequestDispatcher("/views/users/users.jsp").forward(request, response);

                break;
            case "getUserById":
                long id = Long.parseLong(request.getParameter("id"));
                request.setAttribute("user", new DaoUsers().findUserById(id));
                request.setAttribute("listDepartment", new DaoDepartment().findDepartment());
                request.getRequestDispatcher("/views/users/update.jsp").forward(request, response);
                break;
            case "update":
                // do something
                int[] resultado4 = new int[6];
                int idUser = Integer.parseInt(request.getParameter("id") != null ? request.getParameter("id") :"");
                String nameUser1 = request.getParameter("nameUser") != null ? request.getParameter("nameUser") : "";
                String password1 = request.getParameter("password")!= null ? request.getParameter("password") : "";
                String name1 = request.getParameter("name")!= null ? request.getParameter("name") : "";
                String lastname11 = request.getParameter("lastname1")!= null ? request.getParameter("lastname1") : "";
                String lastname21 = request.getParameter("lastname2")!= null ? request.getParameter("lastname2") : "";
                int departmentId1 = Integer.parseInt(request.getParameter("departmentId")!= null ? request.getParameter("departmentId") : "0");
                int type1 = Integer.parseInt(request.getParameter("type")!= null ? request.getParameter("type") : "0");
                String email1 = request.getParameter("email")!= null ? request.getParameter("email") : "";

                BeanUser_type beanUser_type1 = new BeanUser_type(type1, "");
                BeanDepartment beanDepartment1 = new BeanDepartment(departmentId1, "", "", "", 0);
                BeanUsers beanUsers1 = new BeanUsers(idUser, nameUser1, password1, name1, lastname11, lastname21, email1,0, "", null,0 ,"", beanDepartment1, beanUser_type1);
                try {
                    resultado4 = new DaoUsers().update(beanUsers1);
                    if(resultado4[0]==1){
                        System.out.println("La actualización se realizó correctamente");
                    }else{
                        if(resultado4[1]==1){
                            System.out.println("El usuario no es válido");
                        }else{
                            if (resultado4[2] ==1 && resultado4[3]==1){
                                System.out.println("El usuario ya existe");
                                System.out.println("El correo ya existe");
                            } else{
                                if(resultado4[2]==1){
                                    System.out.println("El nombre se usuario ya se encuentra registrado");
                                }else if(resultado4[3]==1){
                                    System.out.println("El correo ya se encuentra registrado");
                                }
                            }
                        }
                        if(resultado4[4]==1){
                            System.out.println("El departamento no es válido");
                        }
                        if(resultado4[5]==1){
                            System.out.println("El tipo de usuario no es válido");
                        }
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                request.setAttribute("listUsers", new DaoUsers().findAllUsers());
                request.getRequestDispatcher("/views/users/users.jsp").forward(request, response);
                break;
            case "delete":
                // do something
                int idUser1 = Integer.parseInt(request.getParameter("id")!= null ? request.getParameter("id") :"");
                BeanUsers beanUsers2 = new BeanUsers(idUser1, "", "", "", "", "", "",0, "", null, 0,"", null, null);
                try {
                    if(new DaoUsers().delete(beanUsers2)){
                        request.setAttribute("message", "Usuario eliminado correctamente");
                    } else {
                        request.setAttribute("message", "Usuario no eliminado");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                request.setAttribute("listUsers", new DaoUsers().findAllUsers());
                request.getRequestDispatcher("/views/users/users.jsp").forward(request, response);
                break;
            case "access":
                int resultado2[] = new int[2];
                int resultado[] = new int[4];
                int idUser2;
                String nameUser2 = request.getParameter("nameUser") != null ? request.getParameter("nameUser") : "";
                String password2 = request.getParameter("password")!= null ? request.getParameter("password") : "";

                BeanUsers beanUsers3 = new BeanUsers(0, nameUser2, password2, "", "", "","",0, "", null,0,"", null, null);
                idUser2 = new DaoUsers().findIdByUsername(nameUser2);
                if (idUser2 != 0){
                    resultado2 = new DaoUsers().findAttempts(idUser2);
                    if (resultado2[0] == 3 && resultado2[1] > 30){
                        new DaoUsers().restartAttempts(idUser2);
                    }
                    resultado2 = new DaoUsers().findAttempts(idUser2);
                    if (resultado2[0] < 3 && resultado2[1] == -1){
                        try {
                            resultado = new DaoUsers().login(beanUsers3);
                            if (resultado[3] != 0){
                                new DaoUsers().increaseAttempts(resultado[0]);
                                System.out.println("Contraseña incorrecta, el número de intentos que llevas son: " + (resultado2[0] + 1));
                            }else {
                                new DaoUsers().restartAttempts(resultado[0]);
                                System.out.println("Contraseña correcta");
                                System.out.println("Tipo de usuario: " + resultado[1]);
                                System.out.println("Id de usuario: " + resultado[0]);
                                if(resultado[1] == 1){
                                    request.setAttribute("listMinutes", new DaoRecords().findRecordsByAssistant(resultado[0]));
                                    request.getRequestDispatcher("/views/users/mainAssistant.jsp").forward(request, response);
                                    request.getSession().setAttribute("usuariom2",nameUser2);
                                    request.getSession().setAttribute("contram2",password2);
                                }else if(resultado[1] == 2){
                                    request.setAttribute("listMinutes", new DaoRecords().findAllRecordsByManager(resultado[0]));
                                    request.getRequestDispatcher("/views/users/mainManager.jsp").forward(request, response);
                                    request.getSession().setAttribute("usuariom1",nameUser2);
                                    request.getSession().setAttribute("contram1",password2);
                                }else if(resultado[1] == 3){
                                    request.setAttribute("listMinutes", new DaoRecords().findAllRecords());
                                    request.getRequestDispatcher("/views/users/mainOficialia.jsp").forward(request, response);
                                    request.getSession().setAttribute("usuariom",nameUser2);
                                    request.getSession().setAttribute("contram",password2);
                                }else if(resultado[1] == 4){
                                    request.setAttribute("listMinutes", new DaoRecords().findAllRecords(resultado[0]));
                                    request.getRequestDispatcher("/views/users/mainAdmin.jsp").forward(request, response);
                                }else{
                                    request.getRequestDispatcher("/views/users/login.jsp").forward(request, response);
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else if (resultado2[0] == 3 && resultado2[1] >= 0 && resultado2[1] < 31){
                        System.out.println("Estas bloqueado por 30 minutos");
                    }
                }else {
                    System.out.println("El usuario no existe");
                }
                break;
            default:
                // no supported
                break;
        }
    }
}
