package ua.com.octopus_aqua.servlet;

import ua.com.octopus_aqua.data.DBConnector;
import ua.com.octopus_aqua.data.Decoder;
import ua.com.octopus_aqua.data.Log;
import ua.com.octopus_aqua.data.PlantRow;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteRow")
public class DeleteRowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		response.getWriter().append("POST").close();
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try{
			String json = request.getParameter(Decoder.JSON_TAG);
			Log.info("Got into delete: "+json);

			PlantRow row = PlantRow.decodeJSON(json);
			Log.info("After decoding: "+row.toString());

			if (DBConnector.getInstance().deleteRow(row)) {
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}

		} catch (NullPointerException npex) {
			Log.info("npex happened while deleting: "+npex.toString());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}