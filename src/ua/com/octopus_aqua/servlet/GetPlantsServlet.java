package ua.com.octopus_aqua.servlet;

import ua.com.octopus_aqua.data.DBConnector;
import ua.com.octopus_aqua.data.PlantRow;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/getPlants")
public class GetPlantsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StringBuilder builder = new StringBuilder();
		for(PlantRow row : DBConnector.getInstance().getAllRows()){
			builder.append(row.encodeJSON()+"&");
		}
		try(PrintWriter pw = response.getWriter()){
			pw.print(builder.toString());
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
