package api;

import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;
import model.TaskModel;
import payload.BasicResponse;
import service.ProjectService;
import service.TaskService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "TasksApi", urlPatterns = {"/api/tasks", "/api/tasks/add", "/api/tasks/delete", "/api/task-edit"
        , "/api/update-task-edit"})
public class TasksApi extends HttpServlet {

    int idTaskDetail = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        BasicResponse basicResponse = new BasicResponse();
        switch (url) {

            // Lấy toàn bộ danh sách tasks
            case "/api/tasks":
                basicResponse = getAllTasks();
                break;

            // Xoá task
            case "/api/tasks/delete":
                int id = Integer.parseInt(req.getParameter("id"));
                basicResponse = deleteTaskById(id);
                break;

            // Lấy task edit
            case "/api/task-edit":
                basicResponse = getTaskEditByIdTask(idTaskDetail);
                break;

            default:
                basicResponse.setStatusCode(404);
                basicResponse.setMessage("Đường dẫn không tồn tại !");
                break;
        }

        Gson gson = new Gson();
        String dataJson = gson.toJson(basicResponse);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(dataJson);
        printWriter.flush();
        printWriter.close();
    }

    // Lấy task edit
    private BasicResponse getTaskEditByIdTask(int id) {
        BasicResponse basicResponse = new BasicResponse();
        TaskService taskService = new TaskService();
        ProjectService projectService = new ProjectService();
        UserService userService = new UserService();
        List<Object> list = new ArrayList<>();

        list.add(taskService.getTaskEditByIdTask(id));
        list.add(projectService.getAllProjects());
        list.add(userService.getAllUsers());

        basicResponse.setData(list);
        basicResponse.setStatusCode(200);
        basicResponse.setMessage("Lấy thành công thông tin dự án");

        idTaskDetail = 0;

        return basicResponse;
    }

    // Lấy toàn bộ danh sách tasks
    private BasicResponse getAllTasks() {
        BasicResponse response = new BasicResponse();
        TaskService taskService = new TaskService();

        response.setData(taskService.getAllTasks());
        response.setStatusCode(200);

        return response;
    }

    // Xoá task
    private BasicResponse deleteTaskById(int id) {
        BasicResponse response = new BasicResponse();
        TaskService taskService = new TaskService();
        response.setData(taskService.deleteTaskById(id));
        response.setStatusCode(200);
        response.setMessage("Xoa task thanh cong");

        return response;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        BasicResponse basicResponse = new BasicResponse();
        switch (url) {

            // Thêm task
            case "/api/tasks/add":
                basicResponse = addNewTask(req);
                break;

            // Lấy id task edit
            case "/api/task-edit":
                basicResponse = getIdTask(req);
                break;

            // Cập nhật task
            case "/api/update-task-edit":
                basicResponse = updateTask(req);
                break;

            default:
                basicResponse.setStatusCode(404);
                basicResponse.setMessage("Đường dẫn không tồn tại !");
                break;
        }

        Gson gson = new Gson();
        String dataJson = gson.toJson(basicResponse);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(dataJson);
        printWriter.flush();
        printWriter.close();
    }

    // Cập nhật task
    private BasicResponse updateTask(HttpServletRequest req) {
        BasicResponse basicResponse = new BasicResponse();
        TaskService taskService = new TaskService();

        int id = Integer.parseInt(req.getParameter("id"));
        int idProject = Integer.parseInt(req.getParameter("idProject"));
        String nameTask = req.getParameter("nameTask");
        int idUser = Integer.parseInt(req.getParameter("idUser"));
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        // Kiểm tra thông tin user nhập vào
        if (StringUtils.isNullOrEmpty(nameTask) || StringUtils.isNullOrEmpty(startDate) ||
                StringUtils.isNullOrEmpty(endDate)) {
            basicResponse.setStatusCode(400);
            basicResponse.setMessage("Nhập thiếu hoặc sai thông tin, vui lòng kiểm tra lại !");
            basicResponse.setData(-3);

            return basicResponse;
        }

        // Cập nhật
        if (taskService.updateTask(id, idUser, idProject, nameTask, startDate, endDate) == 1) {
            basicResponse.setData(1);
            basicResponse.setStatusCode(200);
            basicResponse.setMessage("Thêm công việc thành công");
        } else {
            basicResponse.setData(-1);
            basicResponse.setStatusCode(200);
            basicResponse.setMessage("Thêm công việc thất bại");
        }

        return basicResponse;
    }

    // Lấy id task edit
    private BasicResponse getIdTask(HttpServletRequest req) {
        BasicResponse basicResponse = new BasicResponse();
        idTaskDetail = Integer.parseInt(req.getParameter("id"));

        if (idTaskDetail != 0) {
            basicResponse.setData(true);
            basicResponse.setMessage("Lấy thành công id task");
            basicResponse.setStatusCode(200);
        } else {
            basicResponse.setData(false);
            basicResponse.setMessage("Lấy thất bại id task");
            basicResponse.setStatusCode(400);
        }

        return basicResponse;
    }

    // Thêm task
    private BasicResponse addNewTask(HttpServletRequest req) {
        BasicResponse response = new BasicResponse();
        TaskService taskService = new TaskService();

        String name = req.getParameter("addtask-nametask");
        Date start_date = Date.valueOf(req.getParameter("addtask-startdate"));
        Date end_date = Date.valueOf(req.getParameter("addtask-enddate"));
        int user_id = Integer.parseInt(req.getParameter("addtask-userid"));
        String projectName = req.getParameter("addtask-projectname");

        // Kiểm tra thông tin user nhập vào
        if (StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(start_date.toString()) ||
                StringUtils.isNullOrEmpty(end_date.toString())) {
            response.setStatusCode(400);
            response.setMessage("Nhập thiếu hoặc sai thông tin, vui lòng kiểm tra lại !");
            response.setData(-3);

            return response;
        }

        // Thêm task
        if (taskService.addNewTask(name, start_date, end_date, user_id, projectName) == 1) {
            response.setData(1);
            response.setStatusCode(200);
            response.setMessage("Thêm công việc thành công");
        } else {
            response.setData(-1);
            response.setStatusCode(200);
            response.setMessage("Thêm công việc thất bại");
        }

        return response;
    }

}
