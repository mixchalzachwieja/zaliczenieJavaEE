import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Michał
 */
@WebServlet("/App")
public class App extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            validateRole(request);
            validateAthenticate(request, response);
        } catch (IllegalAccessException e) {
            setUp(response);

            String name = request.getParameter("name");
            String surname = request.getParameter("surname");

            displayInformation(response, name, surname);
            addCookie(response, name, surname);
            addToSession(name, surname);
        }
    }

    private void validateRole(HttpServletRequest request) throws IllegalAccessException {
        boolean isAdmin = request.isUserInRole("ADMIN");
        if (!isAdmin) {
            throw new IllegalAccessException("You are not an admin!");
        }
    }

    private void validateAthenticate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, IllegalAccessException {
        boolean isAuthenticate = request.authenticate(response);
        if (!isAuthenticate) {
            throw new IllegalAccessException("You are not authenticate!");
        }
    }

    private void setUp(HttpServletResponse response) {
        response.setContentType("text/html");
    }

    private void displayInformation(HttpServletResponse response, String name, String surname) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<p>Your name is " + name + ", your surname is " + surname + "</p>");
    }

    private void addCookie(HttpServletResponse response, String name, String surname) {
        response.addCookie(new Cookie("name", name));
        response.addCookie(new Cookie("surname", surname));
    }

    private void addToSession(String name, String surname) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("name", name);
        context.getExternalContext().getSessionMap().put("surname", surname);
    }

}