package api;

import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;
import model.RoleModel;
import payload.BasicResponse;
import service.RoleService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "RolesApi", urlPatterns = {"/api/roles", "/api/roles/delete", "/api/roles/add", "/api/role-edit",
        "/api/update-role-edit"})
public class RolesApi extends HttpServlet {

    int idRoleDetail = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        BasicResponse basicResponse = new BasicResponse();
        switch (url) {

            // Lấy toàn bộ danh sách roles
            case "/api/roles":
                basicResponse = getAllRole();
                break;

            // Xoá role
            case "/api/roles/delete":
                int id = Integer.parseInt(req.getParameter("id"));
                basicResponse = deleteRoleById(id);
                break;

            // Lấy role edit
            case "/api/role-edit":
                basicResponse = getRoleEditById(idRoleDetail);
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

    // Lấy role edit
    private BasicResponse getRoleEditById(int id) {
        BasicResponse basicResponse = new BasicResponse();
        RoleService roleService = new RoleService();

        basicResponse.setData(roleService.getRoleModelById(id));
        basicResponse.setStatusCode(200);
        basicResponse.setMessage("Lấy thành công thông tin role");

        idRoleDetail = 0;

        return basicResponse;
    }

    // Xoá role
    private BasicResponse deleteRoleById(int id) {
        BasicResponse response = new BasicResponse();
        RoleService roleService = new RoleService();

        // Kiểm tra relationship data
        if (roleService.checkRole(id)) {
            response.setStatusCode(400);
            response.setMessage("Vị trí này có chứa dữ liệu người dùng, " +
                    "vui lòng chuyển dữ liệu người dùng trước khi xoá vị trí !");
            response.setData(false);
        } else {
            response.setStatusCode(200);
            response.setMessage("Xóa thành công");
            response.setData(roleService.deleteRoleById(id));
        }

        return response;
    }

    // Lấy toàn bộ danh sách roles
    private BasicResponse getAllRole() {
        BasicResponse response = new BasicResponse();
        RoleService roleService = new RoleService();
        List<RoleModel> list = roleService.getAllRoles();
        response.setStatusCode(200);
        response.setData(list);

        return response;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getServletPath();
        BasicResponse basicResponse = new BasicResponse();
        switch (url) {

            // Thêm role
            case "/api/roles/add":
                basicResponse = addNewRole(req);
                break;

            // Lấy id role edit
            case "/api/role-edit":
                basicResponse = getIdRoleEdit(req);
                break;

            // Cập nhật role
            case "/api/update-role-edit":
                basicResponse = updateRoleEdit(req);
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

    // Cập nhật role
    private BasicResponse updateRoleEdit(HttpServletRequest req) {
        BasicResponse basicResponse = new BasicResponse();
        RoleService roleService = new RoleService();

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String desc = req.getParameter("description");

        // Kiểm tra thông tin user nhập vào
        if (StringUtils.isNullOrEmpty(name)) {
            basicResponse.setData(-3);
            basicResponse.setStatusCode(400);
            basicResponse.setMessage("Tên quyền không được bỏ trống !");

            return basicResponse;
        }

        // Cập nhật
        if (roleService.updateRole(id, name, desc) == 1) {
            basicResponse.setData(1);
            basicResponse.setMessage("Thay đổi thành công");
            basicResponse.setStatusCode(200);
        } else if (roleService.updateRole(id, name, desc) == -1) {
            basicResponse.setData(-1);
            basicResponse.setMessage("Tên quyền đã tồn tại, vui lòng nhập tên khác !");
            basicResponse.setStatusCode(400);
        } else {
            basicResponse.setData(-2);
            basicResponse.setMessage("Thêm thất bại !");
            basicResponse.setStatusCode(400);
        }

        return basicResponse;
    }

    // Lấy id role edit
    private BasicResponse getIdRoleEdit(HttpServletRequest req) {
        BasicResponse basicResponse = new BasicResponse();

        idRoleDetail = Integer.parseInt(req.getParameter("id"));

        if (idRoleDetail != 0) {
            basicResponse.setData(true);
            basicResponse.setMessage("Lấy thành công id role");
            basicResponse.setStatusCode(200);
        } else {
            basicResponse.setData(false);
            basicResponse.setMessage("Lấy thất bại id role");
            basicResponse.setStatusCode(400);
        }

        return basicResponse;
    }

    // Thêm role
    private BasicResponse addNewRole(HttpServletRequest req) {
        BasicResponse response = new BasicResponse();
        RoleService roleService = new RoleService();

        String name = req.getParameter("name");
        String desc = req.getParameter("desc");

        // Kiểm tra thông tin user nhập vào
        if (StringUtils.isNullOrEmpty(name)) {
            response.setData(-3);
            response.setStatusCode(400);
            response.setMessage("Tên quyền không được bỏ trống !");

            return response;
        }

        // Thêm role
        if (roleService.addNewRole(name, desc) == 1) {
            response.setStatusCode(200);
            response.setMessage("Thêm thành công");
            response.setData(1);
        } else if (roleService.addNewRole(name, desc) == -1) {
            response.setStatusCode(400);
            response.setMessage("Tên quyền đã tồn tại, vui lòng nhập tên khác !");
            response.setData(-1);
        } else {
            response.setStatusCode(400);
            response.setMessage("Thêm không thành công !");
            response.setData(-2);
        }

        return response;
    }
}