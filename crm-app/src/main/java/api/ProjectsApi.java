package api;

import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;
import listenum.StatusListColumn;
import model.ProjectModel;
import payload.BasicResponse;
import service.HomeService;
import service.ProjectService;
import service.TaskService;

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

@WebServlet(name = "ProjectsApi", urlPatterns = {"/api/projects", "/api/projects/add", "/api/projects/delete",
        "/api/project-edit", "/api/update-project-edit", "/api/project-detail"})
public class ProjectsApi extends HttpServlet {

    int idProject = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        BasicResponse basicResponse = new BasicResponse();
        switch (url) {

            // Lấy danh sách project
            case "/api/projects":
                basicResponse = getAllProject();
                break;

            // Xoá project bằng id
            case "/api/projects/delete":
                basicResponse = deleteProjectById(req);
                break;

            // Lấy thông tin project edit
            case "/api/project-edit":
                basicResponse = getProjectById(idProject);
                break;

            // Lấy thông tin project detail
            case "/api/project-detail":
                basicResponse = getProjectDetailById(idProject);
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

    // Lấy thông tin project edit
    private BasicResponse getProjectById(int id) {
        BasicResponse basicResponse = new BasicResponse();
        ProjectService projectService = new ProjectService();

        basicResponse.setData(projectService.getProjectById(id));
        basicResponse.setMessage("Lấy thành công thông tin project");
        basicResponse.setStatusCode(200);

        idProject = 0;

        return basicResponse;
    }

    // Lấy thông tin project detail
    private BasicResponse getProjectDetailById(int id) {
        BasicResponse basicResponse = new BasicResponse();
        List<BasicResponse> list = new ArrayList<>();
        list.add(getTaskStatusByProjectId(id));
        list.add(getProjectById(id));
        list.add(getAllTaskByProjectId(id));

        basicResponse.setData(list);
        return basicResponse;
    }

    // Lấy toàn bộ danh sách task
    private BasicResponse getAllTaskByProjectId(int id) {
        BasicResponse basicResponse = new BasicResponse();
        TaskService taskService = new TaskService();
        basicResponse.setData(taskService.getAllTaskByProjectId(id));
        basicResponse.setMessage("Lấy thành công all tasks by project id");
        basicResponse.setStatusCode(200);

        return basicResponse;
    }

    // Lấy status task bằng id project
    private BasicResponse getTaskStatusByProjectId(int id) {
        BasicResponse basicResponse = new BasicResponse();
        TaskService taskService = new TaskService();

        // list[0] tổng, list[1] chưa thực hiện, list[2] đang thực hiện, list[3] đã hoàn thành
        int[] list = {0, 0, 0, 0};
        list[0] = taskService.getAllStatusTasksByProjectId(id).size();
        for (int i : taskService.getAllStatusTasksByProjectId(id)) {
            if (i == StatusListColumn.UNDO.getValue()) {
                list[1]++;
            } else if (i == StatusListColumn.DOING.getValue()) {
                list[2]++;
            } else {
                list[3]++;
            }
        }

        basicResponse.setData(list);
        basicResponse.setStatusCode(200);
        basicResponse.setMessage("Lấy thành công all status task của project");
        return basicResponse;

    }

    // Xoá project bằng id
    private BasicResponse deleteProjectById(HttpServletRequest req) {
        BasicResponse response = new BasicResponse();
        ProjectService projectService = new ProjectService();

        int id = Integer.parseInt(req.getParameter("id"));

        // Kiểm tra relationship data
        if (projectService.checkJob(id)) {
            response.setData(false);
            response.setMessage("Dự án này có chứa dữ liệu công việc, " +
                    "vui lòng xoá hoặc chuyển dữ liệu trước khi xoá dự án !");
            response.setStatusCode(400);
        } else {
            response.setData(projectService.deleteProjectByid(id));
            response.setMessage("Xóa thành công");
            response.setStatusCode(200);
        }

        return response;
    }

    // Lấy toàn bộ project
    private BasicResponse getAllProject() {
        BasicResponse response = new BasicResponse();
        ProjectService projectService = new ProjectService();

        response.setData(projectService.getAllProjects());
        response.setStatusCode(200);

        return response;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        BasicResponse basicResponse = new BasicResponse();
        switch (url) {

            // Thêm project
            case "/api/projects/add":
                basicResponse = addNewProject(req);
                break;

            // Lấy id project edit
            case "/api/project-edit":
                basicResponse = getIdProjectEdit(req);
                break;

            // Lấy id project detail
            case "/api/project-detail":
                basicResponse = getIdProjectDetail(req);
                break;

            // Cập nhật project
            case "/api/update-project-edit":
                basicResponse = updateProject(req);
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

    // Lấy id project detail
    private BasicResponse getIdProjectDetail(HttpServletRequest req) {
        BasicResponse basicResponse = new BasicResponse();
        idProject = Integer.parseInt(req.getParameter("id"));

        if (idProject != 0) {
            basicResponse.setData(true);
            basicResponse.setMessage("Lấy thành công id");
            basicResponse.setStatusCode(200);
        } else {
            basicResponse.setData(false);
            basicResponse.setMessage("Lấy thất bại id");
            basicResponse.setStatusCode(400);
        }

        return basicResponse;
    }

    // Cập nhật project
    private BasicResponse updateProject(HttpServletRequest req) {
        BasicResponse basicResponse = new BasicResponse();
        ProjectService projectService = new ProjectService();

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");

        // Kiểm tra thông tin user nhập vào
        if (StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(startDate) ||
                StringUtils.isNullOrEmpty(endDate)) {
            basicResponse.setData(-3);
            basicResponse.setStatusCode(400);
            basicResponse.setMessage("Nhâp thiếu thông tin, vui lòng kiểm tra lại !");

            return basicResponse;
        }

        // Cập nhật
        if (projectService.updateProject(id, name, startDate, endDate) == 1) {
            basicResponse.setData(1);
            basicResponse.setMessage("Cập nhật thành công");
            basicResponse.setStatusCode(200);
        } else {
            basicResponse.setData(-1);
            basicResponse.setMessage("Tên dự án đã tồn tại, vui lòng nhập tên khác !");
            basicResponse.setStatusCode(400);
        }
        return basicResponse;
    }

    // Lấy id project edit
    private BasicResponse getIdProjectEdit(HttpServletRequest req) {
        BasicResponse basicResponse = new BasicResponse();

        idProject = Integer.parseInt(req.getParameter("id"));

        if (idProject != 0) {
            basicResponse.setData(true);
            basicResponse.setMessage("Lấy thành công id project");
            basicResponse.setStatusCode(200);
        } else {
            basicResponse.setData(false);
            basicResponse.setMessage("Lấy thất bại id project");
            basicResponse.setStatusCode(400);
        }

        return basicResponse;
    }

    // Thêm project
    private BasicResponse addNewProject(HttpServletRequest req) {
        BasicResponse response = new BasicResponse();
        ProjectService projectService = new ProjectService();

        String name = req.getParameter("name-project");
        Date startDate = Date.valueOf(req.getParameter("start-date"));
        Date endDate = Date.valueOf(req.getParameter("end-date"));

        // Kiểm tra thông tin user nhập vào
        if (StringUtils.isNullOrEmpty(name) || StringUtils.isNullOrEmpty(startDate.toString()) ||
                StringUtils.isNullOrEmpty(endDate.toString())) {
            response.setData(-3);
            response.setStatusCode(400);
            response.setMessage("Nhâp thiếu thông tin, vui lòng kiểm tra lại !");

            return response;
        }

        // Thêm dự án
        if (projectService.addNewProject(name, startDate, endDate) == 1) {
            response.setData(1);
            response.setMessage("Thêm dự án thành công");
            response.setStatusCode(200);
        } else if (projectService.addNewProject(name, startDate, endDate) == -1) {
            response.setData(-1);
            response.setMessage("Tên dự án đã tồn tại, vui lòng nhập tên khác !");
            response.setStatusCode(400);
        } else {
            response.setData(-2);
            response.setMessage("Thêm dự án thất bại !");
            response.setStatusCode(400);
        }

        return response;
    }
}
