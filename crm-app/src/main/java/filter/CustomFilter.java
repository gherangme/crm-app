package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/roles", "/role-add", "/role-edit",
        "/users", "/user-add", "/user-details", "/user-edit",
        "/projects", "/project-add", "/project-details", "/project-edit",
        "/tasks", "/task-add", "/task-edit",
        "/home",
        "/profile", "/profile-edit",
        "/api/home", "/api/home-logout",
        "/api/profile", "/api/profile-task-update",
        "/api/projects", "/api/projects/add", "/api/projects/delete",
        "/api/project-edit", "/api/update-project-edit", "/api/project-detail",
        "/api/roles", "/api/roles/delete", "/api/roles/add", "/api/role-edit", "/api/update-role-edit",
        "/api/tasks", "/api/tasks/add", "/api/tasks/delete", "/api/task-edit", "/api/update-task-edit",
        "/api/users", "/api/users/delete", "/api/user-detail",
        "/api/users/add", "/api/user-edit", "/api/update-user-edit"})
public class CustomFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Xác nhận login
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            boolean isLogin = false;
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    isLogin = true;
                    break;
                } else {
                    isLogin = false;
                }
            }

            if (isLogin) {
                filterChain.doFilter(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
