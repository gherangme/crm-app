package controller;

import filter.CookieFunction;
import model.UserModel;
import service.ProjectService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ProjectServlet", urlPatterns = {"/projects", "/project-add", "/project-detail", "/project-edit"})
public class ProjectController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();

        // Kiểm tra quyền của user
        Cookie[] cookies = req.getCookies();
        UserService userService = new UserService();
        CookieFunction cookieFunction = new CookieFunction();
        ProjectService projectService = new ProjectService();

        UserModel userModel = userService.getUserByEmail(
                cookieFunction.getUsernameByCookies(cookies));

        if (userModel.getRoleModel().getId() == 1 ||
                userModel.getRoleModel().getId() == 2) {

            // Định nghĩa url project
            switch (url) {
                case "/projects":
                    req.setAttribute("projects", projectService.getAllProjects());
                    req.getRequestDispatcher("project.jsp").forward(req, resp);
                    break;
                case "/project-add":
                    req.getRequestDispatcher("project-add.jsp").forward(req, resp);
                    break;
                case "/project-detail":
                    req.getRequestDispatcher("project-detail.jsp").forward(req, resp);
                    break;
                case "/project-edit":
                    req.getRequestDispatcher("project-edit.jsp").forward(req, resp);
                    break;
                default:
                    break;
            }
        } else {
            req.getRequestDispatcher("403.jsp").forward(req, resp);
        }
    }
}
