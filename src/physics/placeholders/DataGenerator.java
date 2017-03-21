package physics.placeholders;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import physics.network.ShipSetupData;
import upgrades.ShipTemplate;

public class DataGenerator {

	private static final String SETTINGS_PATH = "resources/data/";
	private static final String[] files = {"lightShip", "heavyShip", "turboShip"};

	public static ShipSetupData basicShipSetup(String nickname) {
		return new ShipSetupData(nickname, "hovercraft", "hover2Texture", new ShipTemplate());
	}

	public static ShipSetupData basicShipSetup(String nickname, int typeId) {
		return loadData(files[typeId - 1]).setNickname(nickname);
	}

	public static ShipSetupData fromJson(String json) {
		return (new Gson()).fromJson(json, ShipSetupData.class);
	}

	private static ShipSetupData loadData(String file) {
		Scanner scanner;
		try {
			scanner = new Scanner(new File(SETTINGS_PATH + file + ".json"));
			String imported = scanner.useDelimiter("\\Z").next();
			scanner.close();
			Gson gson = new GsonBuilder().create();
			return gson.fromJson(imported, ShipSetupData.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Failed to read file, falling back on default ship");
			return new ShipSetupData("xxxx", "newShip", "newShipTexture", new ShipTemplate());
		}

	}



}
