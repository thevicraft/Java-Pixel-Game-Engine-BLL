package com.javapixelgame.game.resourcehandling.world;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.javapixelgame.game.api.Objective;
import com.javapixelgame.game.api.world.World;
@Deprecated
public class WorldDatReader {
	private List<Objective> dat = new ArrayList<>();
	private World world;
	public WorldDatReader(World world) {
		this.world = world;
	}
	@Deprecated
	@SuppressWarnings("unchecked")
	public ArrayList<Objective> read(String path) {
		dat = new ArrayList<>();
		// JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader(path)) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);

			JSONArray obstacleList = (JSONArray) obj;
			System.out.println(obstacleList);
			System.out.println("===");
			// Iterate over employee array
			obstacleList.forEach(emp -> {
				try {
					if (!((JSONObject) emp).isEmpty()) {
						parseObjective((JSONObject) emp);
//					System.out.println((JSONObject)emp);
						System.out.println("---");
					}
				} catch (UncompleteDescribtionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	@Deprecated
	private void parseObjective(JSONObject obstacle) throws UncompleteDescribtionException {
		// Get employee object within list
		JSONObject obstacleObject = check((JSONObject) obstacle.get("objective"));
		System.out.println(obstacleObject);

		
		String type = checkAttribute(obstacleObject, "type");
		System.out.println("type: "+type);
		if(type.equals("old_man")) {
			int x = Integer.parseInt(checkAttribute(obstacleObject, "x"));
			int y = Integer.parseInt(checkAttribute(obstacleObject, "y"));
		}
//		// Get employee first name
//		String firstName = (String) employeeObject.get("firstName");
//		System.out.println(firstName);
//
//		// Get employee last name
//		String lastName = (String) employeeObject.get("lastName");
//		System.out.println(lastName);
//
//		// Get employee website name
//		String website = (String) employeeObject.get("website");
//		System.out.println(website);
	}
	
//	private void parseOtherAttributesOn()
	@Deprecated
	private JSONObject check(JSONObject toTest) throws UncompleteDescribtionException {
		if (!toTest.isEmpty())
			return toTest;
		else
			throw new UncompleteDescribtionException("");
	}

	private String checkAttribute(JSONObject object, String attribute)
			throws UncompleteDescribtionException {
		if (object.get(attribute) != null)
			return object.get(attribute).toString();
		else
			throw new UncompleteDescribtionException(attribute);
	}
	@Deprecated
	@SuppressWarnings("serial")
	static class UncompleteDescribtionException extends Exception {
		public UncompleteDescribtionException(String attribute) {
			super("Attribute <" + attribute + "> could not be located");
		}
	}
}
