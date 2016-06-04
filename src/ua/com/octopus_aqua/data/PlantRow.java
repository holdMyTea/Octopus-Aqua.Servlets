package ua.com.octopus_aqua.data;

import org.json.JSONException;
import org.json.JSONObject;

public class PlantRow {

	private int id;
	private String name;
	private String info;
	private String type;
	private String pic;
	private String group;

	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String INFO = "info";
	public static final String TYPE = "type";
	public static final String PIC = "pic";
	public static final String GROUP = "group";

	PlantRow(int id, String name, String info, String type, String pic, String group) {
		this.id = id;
		this.name = name;
		this.info = info;
		this.type = type;
		this.pic = pic;
		this.group = group;
	}

	public String encodeJSON() {
		JSONObject row = new JSONObject();

		row.put(ID, id);
		row.put(NAME, name);
		row.put(INFO, info);
		row.put(TYPE, type);
		row.put(PIC, pic);
		row.put(GROUP, group);

		return row.toString();
	}

	public static PlantRow decodeJSON(String objJSON) {
		try {
			JSONObject row = new JSONObject(objJSON);
			int id = row.getInt(ID);
			String name = row.getString(NAME);
			String info = row.getString(INFO);
			String type = row.getString(TYPE);
			String pic = row.getString(PIC);
			String group = row.getString(GROUP);
			return new PlantRow(id, name, info, type, pic,group);
		} catch (JSONException jsex) {
			return null;
		}
	}
	
	@Override
	public String toString(){
		return "id:"+id
				+" name:"+name
				+" type:"+type
				+" info:"+info
				+" pic:"+pic
				+" group:"+group;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getInfo() {
		return info;
	}

	public String getType() {
		return type;
	}

	public String getPic() {
		return pic;
	}
	
	public String getGroup() {
		return group;
	}
}
