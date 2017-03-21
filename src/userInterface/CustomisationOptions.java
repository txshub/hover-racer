package userInterface;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe
 * Class that designs ship customisation options.
 *
 */
public class CustomisationOptions extends GridPane {

	private int chosenId;
	private boolean clicked1;
	private boolean clicked2;
	private boolean clicked3;
	private CustomisationButton ship1Button, ship2Button, ship3Button;

	/**
	 * Constructor for the CustomisationOptions class.
	 * @throws IOException
	 */
	public CustomisationOptions() throws IOException {

		// default id - ship type 1
		setTypeId(1);
		
		//default clicked values - false
		clicked1 = false;
		clicked2 = false;
		clicked3 = false;
		
		TextStyle select = new TextStyle("SELECT A SHIP", 45);
		Text selectText = select.getTextStyled();
		
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
		
		ship1Button = new CustomisationButton(1, 250, 320, 25, ship1Image);
		ship1Button.setOnMouseClicked(event -> {
			clicked1 = true;
			ship1Button.setClicked(true);
			if(clicked2){
				clicked2 = false;
				ship2Button.setClicked(false);
				ship2Button.setSelectedEffect(Color.BLACK);
			}else if (clicked3){
				clicked3 = false;
				ship3Button.setClicked(false);
				ship3Button.setSelectedEffect(Color.BLACK);
			}
			setTypeId(1);
			ship1Button.setSelectedEffect(Color.STEELBLUE);
		});
		
		ship2Button = new CustomisationButton(2, 250, 320, 25, ship2Image);
		ship2Button.setOnMouseClicked(event -> {
			clicked2 = true;
			ship2Button.setClicked(true);
			if(clicked1){
				clicked1 = false;
				ship1Button.setClicked(false);
				ship1Button.setSelectedEffect(Color.BLACK);
			}else if (clicked3){
				clicked3 = false;
				ship3Button.setClicked(false);
				ship3Button.setSelectedEffect(Color.BLACK);
			}
			setTypeId(2);
			ship2Button.setSelectedEffect(Color.STEELBLUE);
		});
		
		ship3Button = new CustomisationButton(3, 250, 320, 25, ship2Image);
		ship3Button.setOnMouseClicked(event -> {
			clicked3 = true;
			ship3Button.setClicked(true);
			if(clicked1){
				clicked1 = false;
				ship1Button.setClicked(false);
				ship1Button.setSelectedEffect(Color.BLACK);
			}else if (clicked2){
				clicked2 = false;
				ship2Button.setClicked(false);
				ship2Button.setSelectedEffect(Color.BLACK);
			}
			setTypeId(3);
			ship3Button.setSelectedEffect(Color.STEELBLUE);
		});
		
		// add them to grid pane
		add(selectText, 0, 0);
		add(ship1Button, 0, 1);
		add(ship2Button, 1, 1);
		add(ship3Button, 2, 1);
		
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
