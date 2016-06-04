package ua.com.octopus_aqua.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.com.octopus_aqua.data.DBConnector;
import ua.com.octopus_aqua.data.ImageHandler;
import ua.com.octopus_aqua.data.Log;
import ua.com.octopus_aqua.data.PlantRow;

@WebServlet("/addRow")
public class AddRowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		response.getWriter().append("POST").close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			Log.info("Got /add row: "+request.getQueryString());
			int paramCount = Integer.parseInt(request.getHeader(EditRowServlet.HEADER_PARAM_COUNT));

			if (paramCount == EditRowServlet.NO_IMG) {
				
				Log.info("No parts in /addRow");
				
				/*PlantRow row = PlantRow.decodeJSON(
						Decoder.queryToMap(
								Decoder.decodeString(request.getReader().readLine())).get(Decoder.JSON_TAG));*/
				
				PlantRow row = PlantRow.decodeJSON(request.getParameter(EditRowServlet.JSON_TAG));
				Log.info("Got in /addRow: "+row.toString());
				
				
				if (DBConnector.getInstance().addRow(row)) {
					response.setStatus(HttpServletResponse.SC_OK);
					Log.info("no file /addRow successfull");
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					Log.info("no file /addRow failed");
				}

			} else if (paramCount == EditRowServlet.IS_IMG) {
				
				Log.info("Are parts in /addRow");
				
				PlantRow row = PlantRow.decodeJSON(request.getParameter(EditRowServlet.JSON_TAG));
				
				String img = request.getParameter(EditRowServlet.IMG_TAG);
				
				int imgLength = Integer.parseInt(request.getParameter(EditRowServlet.CONTROL_LENGTH_TAG));
				
				Log.info("Got JSON: "+row.toString()
				+"\n\rGot control img length: "+imgLength
				+"\n\rGot img:\n\r"+img);
				
				
				String picPath = ImageHandler.saveImage(img);
				
				if (picPath != null) {
					row.setPic(picPath);
					Log.info("/addRow pic saved");
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					Log.info("/addRow pic saving failed");
					return;
				}

				if (DBConnector.getInstance().addRow(row)) {
					response.setStatus(HttpServletResponse.SC_OK);
					Log.info("/addRow with file successfull");
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					Log.info("/addRow with file failed");
				}
			} else {
				response.getWriter().append("Wrong number of parametres\n\r");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			}

		} catch (IOException ioex) {
			//maybe change status, after checking throws
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (NullPointerException npex) {
			//maybe change status, after checking throws			
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}
