package controller;

import config.MysqlConfig;
import filter.CookieFunction;
import service.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Định nghĩa url login
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Kiểm tra xác nhận login
        LoginService loginService = new LoginService();
        boolean isSuccess = loginService.checkLogin(email, password);

        if (isSuccess) {

            // Lưu cookie
            CookieFunction cookieFunction = new CookieFunction();
            cookieFunction.createCookie(resp, email);

            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}