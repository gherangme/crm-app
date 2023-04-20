package api;

import com.google.gson.Gson;
import filter.CookieFunction;
import listenum.StatusListColumn;
import model.UserModel;
import payload.BasicResponse;
import service.HomeService;
import service.ProfileService;
import service.StatusService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProfileApi", urlPatterns = {"/api/profile", "/api/profile-task-update"})
public class ProfileApi extends HttpServlet {

    String taskID = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BasicResponse> list = new ArrayList<>();
        Cookie[] cookies = req.getCookies();
        CookieFunction cookieFunction = new CookieFunction();
        String servletPath = req.getServletPath();

        switch (servletPath) {

            // Lấy thông tin trang profile
            case "/api/profile":
                list.add(getProfileByEmail(cookieFunction.getUsernameByCookies(cookies)));
                break;

            // Lấy thông tin trang profile task cần cập nhật
            case "/api/profile-task-update":
                list = getProfileTaskUpdate(req);
                break;

            default:
                BasicResponse basicResponse = new BasicResponse();
                basicResponse.setMessage("Đường dẫn không tồn tại");
                basicResponse.setData(null);
                basicResponse.setStatusCode(404);
                list.add(basicResponse);
                break;
        }
        Gson gson = new Gson();
        String dataJson = gson.toJson(list);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(dataJson);
        printWriter.flush();
        printWriter.close();

    }

    // Lấy thông tin trang profile
    private BasicResponse getProfileByEmail(String email) {
        BasicResponse basicResponse = new BasicResponse();
        HomeService homeService = new HomeService();
        ProfileService profileService = new ProfileService();

        basicResponse.setData(profileService.
                getListTasksByUserId(homeService.getUserByEmail(email).getId()));
        basicResponse.setStatusCode(200);
        basicResponse.setMessage("Lấy thành công profile");

        return basicResponse;
    }

    // Lấy thông tin trang profile task cần cập nhật
    private List<BasicResponse> getProfileTaskUpdate(HttpServletRequest req) {
        List<BasicResponse> list = new ArrayList<>();
        CookieFunction cookieFunction = new CookieFunction();

        // BasicResponse[0] lấy thông tin user
        Cookie[] cookies = req.getCookies();
        list.add(getUserByEmail(cookieFunction.getUsernameByCookies(cookies)));

        // BasicResponse[1] lấy task từ id task
        list.add(getTaskByIdTask(Integer.parseInt(taskID)));
        taskID = null;

        // BasicResponse[2] lấy danh sách status
        list.add(getAllStatus());

        return list;
    }

    // BasicResponse[0] lấy thông tin user
    private BasicResponse getUserByEmail(String email) {
        HomeService homeService = new HomeService();

        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setData(homeService.getUserByEmail(email));
        basicResponse.setStatusCode(200);
        basicResponse.setMessage("lấy thành công danh sách user");

        return basicResponse;
    }

    // BasicResponse[1] lấy task từ id task
    private BasicResponse getTaskByIdTask(int id) {
        ProfileService profileService = new ProfileService();
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setData(profileService.getTaskById(id));
        basicResponse.setMessage("Lấy task thành công");
        basicResponse.setStatusCode(200);

        return basicResponse;
    }

    // BasicResponse[2] lấy danh sách status
    private BasicResponse getAllStatus() {
        StatusService service = new StatusService();
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setData(service.getAllStatus());
        basicResponse.setMessage("Lấy danh sách status thành công");
        basicResponse.setStatusCode(200);

        return basicResponse;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BasicResponse basicResponse = new BasicResponse();
        String servletPath = req.getServletPath();
        switch (servletPath) {

            // Lấy thông tin task theo id task
            case "/api/profile":
                basicResponse = postGetTaskID(req);
                break;

            // Cập nhật status task
            case "/api/profile-task-update":
                int idTask = Integer.parseInt(req.getParameter("taskId"));
                int idStatus = Integer.parseInt(req.getParameter("statusId"));
                basicResponse = postUpdateStatusOfTask(idTask, idStatus);
                break;

            default:
                basicResponse.setMessage("Đường dẫn không tồn tại");
                basicResponse.setData(null);
                basicResponse.setStatusCode(404);
                break;
        }

        Gson gson = new Gson();
        String dataJson = gson.toJson(basicResponse);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(dataJson);
        printWriter.flush();
        printWriter.close();

    }

    // Lấy thông tin id task
    private BasicResponse postGetTaskID(HttpServletRequest req) {
        BasicResponse basicResponse = new BasicResponse();
        taskID = req.getParameter("taskID");
        if (taskID != null) {
            basicResponse.setData(taskID);
            basicResponse.setMessage("Lấy thành công task id");
            basicResponse.setStatusCode(200);
        } else {
            basicResponse.setData(null);
            basicResponse.setMessage("Lấy thất bại task id");
            basicResponse.setStatusCode(400);
        }

        return basicResponse;
    }

    // Cập nhật status task
    private BasicResponse postUpdateStatusOfTask(int idTask, int idStatus) {
        BasicResponse basicResponse = new BasicResponse();
        ProfileService profileService = new ProfileService();

        if (profileService.updateStatusOfTask(idTask, idStatus) > 0) {
            basicResponse.setData(profileService.updateStatusOfTask(idTask, idStatus));
            basicResponse.setMessage("Cập nhật thành công");
            basicResponse.setStatusCode(200);
        } else {
            basicResponse.setData(profileService.updateStatusOfTask(idTask, idStatus));
            basicResponse.setMessage("Cập nhật không thành công");
            basicResponse.setStatusCode(400);
        }

        return basicResponse;
    }
}
