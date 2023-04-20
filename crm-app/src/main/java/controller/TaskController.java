package controller;

import filter.CookieFunction;
import model.UserModel;
import service.ProjectService;
import service.TaskService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "TaskServlet", urlPatterns = {"/tasks", "/task-add", "/task-edit"})
public class TaskController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        ProjectService projectService = new ProjectService();

        // Kiểm tra quyền của user
        Cookie[] cookies = req.getCookies();
        UserService userService = new UserService();
        CookieFunction cookieFunction = new CookieFunction();
        UserModel userModel = userService.getUserByEmail(
                cookieFunction.getUsernameByCookies(cookies));


        if (userModel.getRoleModel().getId() == 1 ||
                userModel.getRoleModel().getId() == 2) {

            // Định nghĩa url task
            switch (url) {
                case "/tasks":
                    TaskService taskService = new TaskService();
                    req.setAttribute("tasks", taskService.getAllTasks());
                    req.getRequestDispatcher("task.jsp").forward(req, resp);
                    break;
                case "/task-add":
                    req.setAttribute("projects", projectService.getAllProjects());
                    req.setAttribute("users", userService.getAllUsers());
                    req.getRequestDispatcher("task-add.jsp").forward(req, resp);
                    break;
                case "/task-edit":
                    req.setAttribute("projects", projectService.getAllProjects());
                    req.setAttribute("users", userService.getAllUsers());
                    req.getRequestDispatcher("task-edit.jsp").forward(req, resp);
                    break;
                default:
                    break;
            }
        } else {
            req.getRequestDispatcher("403.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

    }
}
