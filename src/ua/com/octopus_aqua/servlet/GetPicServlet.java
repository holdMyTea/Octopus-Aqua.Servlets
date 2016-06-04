package ua.com.octopus_aqua.servlet;

import ua.com.octopus_aqua.data.DBConnector;
import ua.com.octopus_aqua.data.ImageHandler;
import ua.com.octopus_aqua.data.Log;
import ua.com.octopus_aqua.data.PlantRow;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/getPic")
public class GetPicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final String ID = "id";
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter(ID)!=null){
			Integer id = Integer.parseInt(request.getParameter(ID));
			Log.info("Got an id to get: " + id);
			PlantRow row = DBConnector.getInstance().getRowById(id);
			if (row != null) {
				PrintWriter pw = response.getWriter();
				try {
					String base64 = ImageHandler.readImageToBase64(row.getPic());
					if(base64 != null){
						pw.print(base64);
						response.setStatus(HttpServletResponse.SC_OK);
					} else{
						pw.print("*");
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					
				}catch(NullPointerException npex){
					Log.info("Error while /getPic: "+npex.getMessage());
					row.setPic("*");
					DBConnector.getInstance().editRow(row);
					pw.print("*");
				}
				finally{
					pw.close();
				}
			} else{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} else{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().append("/getPic?id=").close();
			Log.info("/getRow wrong request type");
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		response.getWriter().append("GET").close();
		Log.info("/getRow wrong request type");
	}

}
