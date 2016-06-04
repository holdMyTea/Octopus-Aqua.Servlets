package ua.com.octopus_aqua.servlet;

import ua.com.octopus_aqua.data.DBConnector;
import ua.com.octopus_aqua.data.Log;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/getCount")
public class GetCountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("Got /getCount request: " + request.getQueryString());

		Log.info("Got /getCount request: " + request.getQueryString());

		int count = DBConnector.getInstance().getCount();

		if (count != (-1)) {
			try (PrintWriter pw = response.getWriter()) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				pw.print(Integer.toString(count));
				response.setStatus(HttpServletResponse.SC_OK);
				Log.info("/getCount OK");
			}
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			Log.info("/getCount BAD");
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		response.getWriter().append("GET").close();
		Log.info("/getCount wrong request type");
	}

}
