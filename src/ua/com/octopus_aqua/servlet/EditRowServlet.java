package ua.com.octopus_aqua.servlet;

import ua.com.octopus_aqua.data.DBConnector;
import ua.com.octopus_aqua.data.ImageHandler;
import ua.com.octopus_aqua.data.Log;
import ua.com.octopus_aqua.data.PlantRow;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/editRow")
public class EditRowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final String IMG_TAG = "img";
	public static final String JSON_TAG = "json";
	public static final String CONTROL_LENGTH_TAG = "len";

	public static final String HEADER_PARAM_COUNT = "Params-Count";
	
	public static final int NO_IMG = 1;
	public static final int IS_IMG = 3;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		response.getWriter().append("POST").close();
		Log.info("/getRow wrong request type");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {

			Log.info("Got /edit row: "+request.getQueryString());
			int paramCount = Integer.parseInt(request.getHeader(HEADER_PARAM_COUNT));

			if (paramCount == NO_IMG) {
				
				Log.info("No parts in editRow");
				
				/*PlantRow row = PlantRow.decodeJSON(
						Decoder.queryToMap(
								Decoder.decodeString(request.getReader().readLine())).get(Decoder.JSON_TAG));*/
				
				PlantRow row = PlantRow.decodeJSON(request.getParameter(JSON_TAG));
				Log.info("Got in /editRow: "+row.toString());
				
				
				if (DBConnector.getInstance().editRow(row)) {
					response.setStatus(HttpServletResponse.SC_OK);
					Log.info("no file editRow successfull");
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					Log.info("no file editRow failed");
				}

			} else if (paramCount == IS_IMG) {
				
				Log.info("Are parts in editRow");
				
				PlantRow row = PlantRow.decodeJSON(request.getParameter(JSON_TAG));
				
				String img = request.getParameter(IMG_TAG);
				
				int imgLength = Integer.parseInt(request.getParameter(CONTROL_LENGTH_TAG));
				
				Log.info("Got JSON: "+row.toString()
				+"\n\rGot control img length: "+imgLength
				+"\n\rGot img:\n\r"+img);
				
				
				String picPath = ImageHandler.saveImage(img);
				
				if (picPath != null) {
					row.setPic(picPath);
					Log.info("editRow pic saved");
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					Log.info("editRow pic saving failed");
					return;
				}

				if (DBConnector.getInstance().editRow(row)) {
					response.setStatus(HttpServletResponse.SC_OK);
					Log.info("editRow with file successfull");
				} else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					Log.info("editRow with file failed");
				}
			} else {
				response.getWriter().append("Wrong number of parametres");
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
