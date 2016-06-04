package ua.com.octopus_aqua.servlet;

import ua.com.octopus_aqua.data.DBConnector;
import ua.com.octopus_aqua.data.Log;
import ua.com.octopus_aqua.data.PlantRow;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/getRow")
public class GetRowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Log.info("Got /getRow request: " + request.getQueryString());

		int id = Integer.parseInt(request.getParameter("id"));
		Log.info("Got an id to get: " + id);
		PlantRow row = DBConnector.getInstance().getRowBySequence(id);
		if (row != null) {
			try (PrintWriter pw = response.getWriter()) {
				// response.setCharacterEncoding("UTF-8");
				// response.setContentType("text/html");
				Log.info("Sending on /getRow: "+row.encodeJSON());
				pw.println(row.encodeJSON());
				response.setStatus(HttpServletResponse.SC_OK);
				Log.info("getRow ended OK");
			} catch (NullPointerException npex) {
				Log.info("/getRow npex" + npex.toString());
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} else {
			Log.info("getRow got empty data from base");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		response.getWriter().append("GET").close();
		Log.info("/getRow wrong request type");
	}

}
