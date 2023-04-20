package api;

import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;
import filter.CookieFunction;
import listenum.StatusListColumn;
import model.RoleModel;
import model.TaskModel;
import model.UserModel;
import payload.BasicResponse;
import service.*;

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

@WebServlet(name = "UsersApi", urlPatterns = {"/api/users", "/api/users/delete", "/api/user-detail",
        "/api/users/add", "/api/user-edit", "/api/update-user-edit"})
public class UsersApi extends HttpServlet {

    String emailUserTable = null;
    String emailUserDetail = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        List<BasicResponse> responseList = new ArrayList<>();
        BasicResponse basicResponse = new BasicResponse();
        switch (url) {

            // Lấy toàn bộ danh sách user
            case "/api/users":
                responseList.add(getAllUsers());
                break;

            // Lấy user detail
            case "/api/user-detail":
                responseList = getUserDetail(emailUserTable);
                break;

            // Lấy user edit
            case "/api/user-edit":
                responseList.add(getUserByEmail(emailUserDetail));
                break;

            default:
                basicResponse.setData(false);
                basicResponse.setStatusCode(404);
                basicResponse.setMessage("Đường dẫn không tồn tại !");
                responseList.add(basicResponse);
                break;
        }

        Gson gson = new Gson();
        String dataJson = gson.toJson(responseList);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print(dataJson);
        printWriter.flush();
        printWriter.close();
    }

    // Lấy toàn bộ danh sách users
    private BasicResponse getAllUsers() {
        BasicResponse response = new BasicResponse();
        UserService userService = new UserService();
        response.setData(userService.getAllUsers());
        response.setStatusCode(200);
        return response;
    }

    // Lấy user detail
    private List<BasicResponse> getUserDetail(String email) {
        List<BasicResponse> list = new ArrayList<>();

        // Lấy thông tin user
        list.add(getUserByEmail(email));

        // Lấy status task của user
        UserModel userModel = (UserModel) getUserByEmail(email).getData();
        list.add(getTaskStatusByUserId(userModel.getId()));

        // Lấy list tasks của user
        list.add(getListTasksByIdUser(userModel.getId()));

        emailUserTable = null;

        return list;
    }

    // Lấy thông tin user
    private BasicResponse getUserByEmail(String email) {
        BasicResponse basicResponse = new BasicResponse();
        UserService userService = new UserService();
        basicResponse.setData(userService.getUserByEmail(email));
        basicResponse.setMessage("Lấy thành công user bằng email");
        basicResponse.setStatusCode(200);

        emailUserDetail = null;

        return basicResponse;
    }

    // Lấy status task của user
    public BasicResponse getTaskStatusByUserId(int id) {
        BasicResponse basicResponse = new BasicResponse();
        HomeService homeService = new HomeService();
        int[] list = {0, 0, 0, 0};
        list[0] = homeService.getTaskStatusByUserId(id).size();
        for (int i : homeService.getTaskStatusByUserId(id)) {
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
        basicResponse.setMessage("Lay thanh cong status task cua user");
        return basicResponse;
    }

    // Lấy list tasks của user
    private BasicResponse getListTasksByIdUser(int id) {
        ProfileService profileService = new ProfileService();
        BasicResponse basicResponse = new BasicResponse();
        basicResponse.setData(profileService.getListTasksByUserId(id));
        basicResponse.setMessage("Lấy thành công danh sách nhiệm vụ của người dùng");
        basicResponse.setStatusCode(200);

        return basicResponse;
    }

    // POST
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        BasicResponse basicResponse = new BasicResponse();
        emailUserTable = req.getParameter("emailUserTable");

        // Lấy thông tin email
        Cookie[] cookies = req.getCookies();
        UserService userService = new UserService();
        CookieFunction cookieFunction = new CookieFunction();
        UserModel userModel = userService.getUserByEmail(
                cookieFunction.getUsernameByCookies(cookies));

        switch (url) {

            // Thêm user
            case "/api/users/add":
                basicResponse = addUser(req, userModel);
                break;

            // Xoá user
            case "/api/users/delete":
                basicResponse = deleteUserById(req, userModel);
                break;

            // Lấy email user detail
            case "/api/user-edit":
                basicResponse = getEmailUserDetail(req);
                break;

            // Cập nhật user edit
            case "/api/update-user-edit":
                basicResponse = updateUser(req);
                break;
            default:
                basicResponse.setStatusCode(404);
                basicResponse.setMessage("Đường dẫn không tồn tại !");

                break;
        }

        Gson gson = new Gson();
        String dataJson = gson.toJson(basicResponse);

        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = resp.getWriter();
        printWriter.print(dataJson);
        printWriter.flush();
        printWriter.close();
    }

    // Lấy thông tin email
    private BasicResponse getEmailUserDetail(HttpServletRequest req) {
        emailUserDetail = req.getParameter("email");
        BasicResponse basicResponse = new BasicResponse();
        if (emailUserDetail != null) {
            basicResponse.setData(true);
            basicResponse.setMessage("Lấy thành công email user detail");
            basicResponse.setStatusCode(200);
        } else {
            basicResponse.setData(true);
            basicResponse.setMessage("Lấy không thành công email user detail");
            basicResponse.setStatusCode(400);
        }

        return basicResponse;
    }

    // Thêm user
    private BasicResponse addUser(HttpServletRequest req, UserModel userModel) {
        BasicResponse response = new BasicResponse();
        UserService userService = new UserService();

        String fullName = req.getParameter("fullname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        int roleId = Integer.parseInt(req.getParameter("role_id"));
        String avatar = req.getParameter("avatar");
        String roleIdCheck = Integer.toString(roleId);

        // Kiểm tra thông tin user nhập vào
        if (StringUtils.isNullOrEmpty(fullName) || StringUtils.isNullOrEmpty(email) ||
                StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(roleIdCheck)) {
            response.setStatusCode(400);
            response.setMessage("Nhập thiếu hoặc sai thông tin, vui lòng kiểm tra lại !");
            response.setData(-3);

            return response;
        }

        int isSuccess = userService.addUser(fullName, email, password, roleId, avatar);

        // Kiểm tra quyền của user
        if (userModel.getRoleModel().getId() == 1) {

            // Thêm user
            if (isSuccess == 1) {
                response.setStatusCode(200);
                response.setMessage("Thêm user thành công");
                response.setData(isSuccess);
            } else if (isSuccess == -1) {
                response.setStatusCode(400);
                response.setMessage("Email đã tồn tại, vui lòng chọn email khác !");
                response.setData(isSuccess);
            } else {
                response.setStatusCode(400);
                response.setMessage("Thêm thất bại !");
                response.setData(isSuccess);
            }
        } else {
            response.setData(-2);
            response.setMessage("Bạn không có quyền này !");
            response.setStatusCode(403);
        }

        return response;
    }

    // Xoá user
    private BasicResponse deleteUserById(HttpServletRequest req, UserModel userModel) {
        BasicResponse response = new BasicResponse();
        UserService userService = new UserService();
        TaskService taskService = new TaskService();

        int id = Integer.parseInt(req.getParameter("id"));

        // Kiểm tra quyền của user
        if (userModel.getRoleModel().getId() == 1) {

            // Kiểm tra relationship data
            if (taskService.getTaskByUserId(id) == false) {

                // Xoá user
                response.setData(userService.deleteUserById(id));
                response.setMessage("Xóa user thành công");
                response.setStatusCode(200);
            } else {
                response.setData(false);
                response.setMessage("Người này đang còn dữ liệu công việc, " +
                        "vui lòng xoá hoặc chuyển dữ liệu công việc cho người khác trước khi xoá người dùng !");
                response.setStatusCode(400);
            }
        } else {
            response.setData(false);
            response.setMessage("Bạn không có quyền này !");
            response.setStatusCode(403);
        }
        return response;
    }

    // Cập nhật user
    private BasicResponse updateUser(HttpServletRequest req) {
        BasicResponse basicResponse = new BasicResponse();
        UserService userService = new UserService();

        // Lấy data from user
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        int roleId = Integer.parseInt(req.getParameter("roleId"));
        String roleIdCheck = Integer.toString(roleId);
        String avatar = req.getParameter("avatar");

        // Kiểm tra thông tin user nhập vào
        if (StringUtils.isNullOrEmpty(fullName) || StringUtils.isNullOrEmpty(email) ||
                StringUtils.isNullOrEmpty(password) || StringUtils.isNullOrEmpty(roleIdCheck)) {
            basicResponse.setStatusCode(400);
            basicResponse.setMessage("Nhập thiếu hoặc sai thông tin, vui lòng kiểm tra lại !");
            basicResponse.setData(-3);

            return basicResponse;
        }

        // Cập nhật
        if (userService.updateUser(email, password, fullName, avatar, roleId)) {
            basicResponse.setData(1);
            basicResponse.setMessage("Cập nhật thành công");
            basicResponse.setStatusCode(200);
        } else {
            basicResponse.setData(-1);
            basicResponse.setMessage("Cập nhật thất bại");
            basicResponse.setStatusCode(400);
        }
        return basicResponse;
    }
}
