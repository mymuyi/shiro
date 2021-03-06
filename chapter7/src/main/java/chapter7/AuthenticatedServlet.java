package chapter7;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: yangke
 * @Date: 2019/9/9
 */
@WebServlet(name = "authenticatedServlet", urlPatterns = "/authenticated")
public class AuthenticatedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("authenticated servlet get");
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            req.getRequestDispatcher("/WEB-INF/jsp/authenticated.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }
}
