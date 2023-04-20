package controller;

import filter.CookieFunction;
import model.UserModel;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = {"/users", "/user-details", "/user-add", "/user-edit"})
public class UserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();

        // Kiểm tra quyền của user
        Cookie[] cookies = req.getCookies();
        UserService userService = new UserService();
        CookieFunction cookieFunction = new CookieFunction();
        UserModel userModel = userService.getUserByEmail(
                cookieFunction.getUsernameByCookies(cookies));

        if (userModel.getRoleModel().getId() == 1 ||
                userModel.getRoleModel().getId() == 2) {

            // Định nghĩa url user
            switch (url) {
                case "/users":
                    req.getRequestDispatcher("user-table.jsp").forward(req, resp);
                    break;
                case "/user-details":
                    req.getRequestDispatcher("user-details.jsp").forward(req, resp);
                    break;
                case "/user-add":
                    req.getRequestDispatcher("user-add.jsp").forward(req, resp);
                    break;
                case "/user-edit":
                    req.getRequestDispatcher("user-edit.jsp").forward(req, resp);
                    break;
            }
        } else {
            req.getRequestDispatcher("403.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
