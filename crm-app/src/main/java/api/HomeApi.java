package api;

import com.google.gson.Gson;
import filter.CookieFunction;
import listenum.StatusListColumn;
import model.UserModel;
import payload.BasicResponse;
import service.HomeService;

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

@WebServlet(name = "HomeApi", urlPatterns = {"/api/home", "/api/home-logout"})
public class HomeApi extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BasicResponse> responseList = new ArrayList<>();

        String servletPath = req.getServletPath();
        switch (servletPath) {

            // Lấy thông tin trang Home
            case "/api/home":
                responseList = doGetHome(req);
                break;

            default:
                BasicResponse basicResponse = new BasicResponse();
                basicResponse.setMessage("Đường dẫn không tồn tại");
                basicResponse.setData(null);
                basicResponse.setStatusCode(404);
                responseList.add(basicResponse);
                break;
        }

        Gson gson = new Gson();
        String dataJson = gson.toJson(responseList);

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        PrintWriter printWriter = resp.getWriter();
        printWriter.print(dataJson);
        printWriter.flush();
        printWriter.close();

    }

    // Lấy thông tin trang Home
    private List<BasicResponse> doGetHome(HttpServletRequest req) {
        List<BasicResponse> responseList = new ArrayList<>();

        // list[0] lấy thông tin user
        Cookie[] cookies = req.getCookies();
        CookieFunction cookieFunction = new CookieFunction();
        responseList.add(getUserByEmail(cookieFunction.getUsernameByCookies(cookies)));
        UserModel userModel = (UserModel) getUserByEmail(cookieFunction.
                getUsernameByCookies(cookies)).getData();

        // list[1] thống kê các tổng tình trạng task của user
        responseList.add(getTaskStatusByUserId(userModel.getId()));

        return responseList;
    }

    // Lấy thông tin user bằng email
    private BasicResponse getUserByEmail(String email) {
        HomeService homeService = new HomeService();

        BasicResponse basicResponse = new BasicResponse();
        if (homeService.getUserByEmail(email) != null) {
            basicResponse.setData(homeService.getUserByEmail(email));
            basicResponse.setStatusCode(200);
            basicResponse.setMessage("Lấy thành công user");
        } else {
            basicResponse.setData(null);
            basicResponse.setStatusCode(400);
            basicResponse.setMessage("Lấy thất bại user");
        }

        return basicResponse;
    }

    // Thống kê các tổng tình trạng task của user
    public BasicResponse getTaskStatusByUserId(int id) {
        BasicResponse basicResponse = new BasicResponse();
        HomeService homeService = new HomeService();

        // list[0] tổng, list[1] chưa thực hiện, list[2] đang thực hiện, list[3] đã hoàn thành
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
        basicResponse.setMessage("Lấy thành công list task của user");
        return basicResponse;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BasicResponse basicResponse = new BasicResponse();
        String servletPath = req.getServletPath();
        switch (servletPath) {

            // Log out
            case "/api/home-logout":

                // Kiểm tra data
                boolean data = Boolean.parseBoolean(req.getParameter("logout"));
                basicResponse = logout(req, resp, data);
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

    // Log out
    private BasicResponse logout(HttpServletRequest req, HttpServletResponse resp, boolean data) {
        BasicResponse basicResponse = new BasicResponse();
        CookieFunction cookieFunction = new CookieFunction();

        if (cookieFunction.getCookie(req) != null && data == true) {
            cookieFunction.deleteCookie(resp, cookieFunction.getCookie(req));
            basicResponse.setStatusCode(200);
            basicResponse.setMessage("Đăng xuất thành công");
            basicResponse.setData(data);
        } else {
            basicResponse.setStatusCode(400);
            basicResponse.setData(false);
            basicResponse.setMessage("Đăng xuất thất bại");
        }

        return basicResponse;
    }
}
