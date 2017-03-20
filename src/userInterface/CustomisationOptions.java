package userInterface;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * 
 * @author Andreea Gheorghe
 * Class that designs ship customisation options.
 *
 */
public class CustomisationOptions extends GridPane {

	private int chosenId;

	/**
	 * Constructor for the CustomisationOptions class.
	 * @throws IOException
	 */
	public CustomisationOptions() throws IOException {

		// default id - ship type 1
		setTypeId(1);
		
		// get file from path
		InputStream is = Files.newInputStream(Paths.get("src/resources/img/ship1.png"));
		Image ship1 = new Image(is);
		
		is = Files.newInputStream(Paths.get("src/resources/img/ship2.png"));
		Image ship2 = new Image(is);

		is.close();
		
		ImageView ship1Image = new ImageView(ship1);
		ship1Image.setFitWidth(230);
		ship1Image.setFitHeight(200);
		
		ImageView ship2Image = new ImageView(ship2);
		ship2Image.setFitWidth(230);
		ship2Image.setFitHeight(200);

		// create buttons
		
		CustomisationButton ship1Button = new CustomisationButton(1, 250, 320, 25, ship1Image);
		ship1Button.setOnMouseClicked(event -> {
			setTypeId(1);
		});
		
		CustomisationButton ship2Button = new CustomisationButton(2, 250, 320, 25, ship2Image);
		ship2Button.setOnMouseClicked(event -> {
			setTypeId(2);
		});
		
		CustomisationButton ship3Button = new CustomisationButton(3, 250, 320, 25, ship2Image);
		ship3Button.setOnMouseClicked(event -> {
			setTypeId(3);
		});
		
		// add them to grid pane
		
		add(ship1Button, 0, 0);
		add(ship2Button, 1, 0);
		add(ship3Button, 2, 0);
		
		setHgap(20);
		
		this.setCache(true);
		this.setCacheHint(CacheHint.SPEED);
	}

	/**
	 * Get the id of the ship that has been selected.
	 * @return The type id.
	 */
	public int getTypeId() {
		return this.chosenId;
	}
	
	/**
	 * Set the id of the selected ship.
	 * @param typeId
	 */
	public void setTypeId(int typeId){
		this.chosenId = typeId;
	}

}
