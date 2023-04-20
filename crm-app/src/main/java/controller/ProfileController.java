package controller;

import model.TaskModel;
import model.UserModel;
import payload.BasicResponse;
import service.HomeService;
import service.ProfileService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ProfileController", urlPatterns = {"/profile", "/profile-edit"})
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();

        // Định nghĩa url profile
        switch (url) {

            case "/profile":
                req.getRequestDispatcher("profile.jsp").forward(req, resp);
                break;
            case "/profile-edit":
                req.getRequestDispatcher("profile-edit.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
