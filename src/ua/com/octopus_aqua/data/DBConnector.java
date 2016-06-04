package ua.com.octopus_aqua.data;

import java.sql.*;

public class DBConnector {

	private static DBConnector instance;

	private final String BASENAME = "PLANTBASE";
	private final String TABLENAME = "PLANTS";

	private final String URL_CONNECTION = "jdbc:mysql://localhost:3306/" + BASENAME + "?useSSL=false";

	private final String USER = "root";
	private final String PASS = "dbPASS42";

	private final String ID = "ID";
	private final String NAME = "NAME";
	private final String INFO = "INFO";
	private final String TYPE = "TYPE";
	private final String PIC = "PIC";
	private final String GROUPNAME = "GROUPNAME";

	private final String COUNT_ALL = "select count(*) from " + TABLENAME;
	private final String SELECT_ALL = "select * from " + TABLENAME;

	private Connection connection;
	private Statement statement;
	
	private volatile PlantRow[] allRows;		//holds the current state of table
	private volatile boolean flagChanged;	//indicates whether the table was changed

	public static DBConnector getInstance() {
		if (instance == null) {
			instance = new DBConnector();
		}
		return instance;
	}

	private DBConnector() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Log.info("JDBC driver loaded");
			connection = DriverManager.getConnection(URL_CONNECTION, USER, PASS);
			statement = connection.createStatement();
			flagChanged = true;
		} catch (ClassNotFoundException e) {
			Log.info("driver fucked " + e.toString());
		} catch (SQLException sqex) {
			sqex.printStackTrace();
			Log.info("sql driver crap " + sqex.toString());
		}
	}

	public synchronized int getCount() {
		try {
			ResultSet rs = statement.executeQuery(COUNT_ALL);
			rs.first();
			int count = rs.getInt(1);
			return count;
		} catch (SQLException sqlex) {
			Log.info("getCount failed: " + sqlex.toString());
			return -1;
		} catch (NullPointerException npex) {
			Log.info("getCount failed: " + npex.toString());
			return -1;
		}
	}

	public boolean addRow(PlantRow row) {
		String query = "insert into " + TABLENAME + "(" + NAME + ", " + INFO + ", " + TYPE + ", " + PIC + ", "+GROUPNAME
				+") values(\""+ row.getName() + "\", \"" + row.getInfo() + "\", \"" 
				+ row.getType() + "\", \"" + row.getPic() + "\", \""+row.getGroup()+"\")";
		System.out.println(query);
		try {
			statement.executeUpdate(query);
			Log.info("row added to DB: " + row.toString());
			flagChanged = true;
			return true;
		} catch (SQLException e) {
			Log.info("row not added to DB: " + e.toString());
			return false;
		}
	}

	public synchronized PlantRow getRowBySequence(int position) {
		if(flagChanged || allRows == null){
			updateRows();
		}
		if (allRows != null) {
			if (position < 0 || position > (allRows.length - 1)) {
				Log.info("Bad rowID, while gettin row from DB");
				return null;
			} else {
				Log.info("Good rowID, while gettin row from DB" + allRows[position].toString());
				return allRows[position];
			}
		} else {
			Log.info("Getting rows from DB failed");
			return null;
		}
	}
	
	public synchronized PlantRow getRowById(int rowId){
		String query = "select * from "+TABLENAME + " where "+ID+"="+rowId;
		Log.info(query);
		
		try{
			ResultSet rs = statement.executeQuery(query);
			rs.first();
			return new PlantRow(
					rs.getInt(ID),
					rs.getString(NAME),
					rs.getString(TYPE),
					rs.getString(INFO),
					rs.getString(PIC),
					rs.getString(GROUPNAME)
					);
		} catch(Exception e){
			Log.info(e.toString());
			return null;
		}
	}

	public boolean editRow(PlantRow row) {
		Log.info("Good rowID for EDIT");
		String query = "update " + TABLENAME + " set " + NAME + "=\"" + row.getName() + "\", " + INFO + "=\""
				+ row.getInfo() + "\", " + TYPE + "=\"" + row.getType() + "\", " + PIC + "=\"" + row.getPic()
				+ "\", " + GROUPNAME + "=\"" + row.getGroup() + "\" where " + ID + "=" + row.getId();
		Log.info(query);
		try {
			statement.executeUpdate(query);
			Log.info("row editted in DB");
			flagChanged = true;
			return true;
		} catch (SQLException e) {
			Log.info("row not edditted in DB: "+e.toString());
			return false;
		}

	}

	public boolean deleteRow(PlantRow row) {
		String query = "delete from " + TABLENAME + " where " + ID + "=" + row.getId();
		Log.info(query);
		try {
			statement.executeUpdate(query);
			Log.info("row deleted from DB");
			flagChanged = true;
			return true;
		} catch (SQLException e) {
			Log.info("row was not deleted from DB: "+e.toString());
			return false;
		}
	}

	private void updateRows() {
		int count = getCount();
		PlantRow[] rows = new PlantRow[count];
		try {
			ResultSet rs = statement.executeQuery(SELECT_ALL);
			rs.beforeFirst();
			Log.info("Getting all rows:");
			for (int i = 0; rs.next(); i++) {
				rows[i] = new PlantRow(rs.getInt(ID), rs.getString(NAME), rs.getString(INFO), rs.getString(TYPE),
						rs.getString(PIC), rs.getString(GROUPNAME));
				Log.info(rows[i].encodeJSON());
			}
			Log.info("getAllRows successfull");
			allRows = rows;
			flagChanged = false;
		} catch (SQLException sqlex) {
			Log.info("getAllRows failes: "+sqlex.toString());
			allRows = null;
		}
	}
	
	public synchronized PlantRow[] getAllRows(){
		if(flagChanged){
			updateRows();
		}
		return allRows;
	}

}
