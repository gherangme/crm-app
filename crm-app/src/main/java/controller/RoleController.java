package controller;

import filter.CookieFunction;
import model.UserModel;
import service.RoleService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RoleServlet", urlPatterns = {"/roles", "/role-add", "/role-edit"})
public class RoleController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();

        // Kiểm tra quyền của user
        Cookie[] cookies = req.getCookies();
        UserService userService = new UserService();
        CookieFunction cookieFunction = new CookieFunction();
        UserModel userModel = userService.getUserByEmail(
                cookieFunction.getUsernameByCookies(cookies));

        if (userModel.getRoleModel().getId() == 1) {

            // Định nghĩa url role
            switch (url) {
                case "/roles":
                    RoleService roleService = new RoleService();
                    req.setAttribute("roles", roleService.getAllRoles());

                    req.getRequestDispatcher("role-table.jsp").forward(req, resp);
                    break;
                case "/role-add":
                    req.getRequestDispatcher("role-add.jsp").forward(req, resp);
                    break;
                case "/role-edit":
                    req.getRequestDispatcher("role-edit.jsp").forward(req, resp);
                    break;
                default:
                    break;
            }
        } else {
            req.getRequestDispatcher("403.jsp").forward(req, resp);
        }
    }
}
