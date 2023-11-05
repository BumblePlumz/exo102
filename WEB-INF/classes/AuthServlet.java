import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

	// @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		String login = req.getParameter("email");
		String password = req.getParameter("password");
		
		if ( login == null || password == null ) throw new ServletException("no login/password");
		boolean succeed = "admin@greta.fr".equals(login) && "admin".equals(password);

		if (succeed){
		  // add something in session for next calls,
			HttpSession session = req.getSession();
			session.setAttribute("email", login);
			session.setAttribute("un", "ça marche !");
		  // then redirect to "welcome.jsp"
			String welcome = "welcome.jsp";
			resp.sendRedirect(welcome);
		}

		// TODO : if auth KO
		if (!succeed){
		  // set an "errorMessage" in request attribute
			String errorMessage = "Vos identifiants ne sont pas valides.";
			req.setAttribute("error", errorMessage);
		  // forward to auth.jsp with request dispatcher
			RequestDispatcher dispatcher = req.getRequestDispatcher("/auth.jsp");
			dispatcher.forward(req, resp);
		}

	}
	
	// TODO : allow to disconnect with a GET to /auth with any parameter "logout" value
	// @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
	  // TODO : check for "logout" parameter
	  //   if so : disconnect and show auth.jsp
	  //   if not : Error 500

		//   if so : disconnect and show auth.jsp
		// check for "logout" parameter
		String logout = (String) req.getParameter( "logout" );
		System.out.print("logout:"+logout);
		if(logout!=null){
			//suppression de la session
			HttpSession session = req.getSession();
			session.removeAttribute("login");
			session.invalidate();

			//redirection
			RequestDispatcher rd =
					req.getRequestDispatcher( "auth.jsp" );

			rd.forward(req, resp);
		}

		//   if not : Error 500
		else {

			System.out.println("error500");
			resp.sendError(SC_INTERNAL_SERVER_ERROR);
		}
	}

}