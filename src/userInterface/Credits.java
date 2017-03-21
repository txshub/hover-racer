package userInterface;

import javafx.geometry.HPos;
import javafx.scene.CacheHint;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * 
 * @author Andreea Gheorghe Class that implements the design of the credits
 *         window, that is displayed in the game menu.
 *
 */

public class Credits extends GridPane {

	private Text contribution1Text;
	private Text contribution2Text;
	private Text contribution3Text;
	private Text contribution4Text;
	private Text contribution5Text;
	private Text contribution6Text;

	/**
	 * Constructor for the Credits class that defines the style for the UI
	 * elements.
	 */
	public Credits() {

		setVgap(10);
		setHgap(100);

		TextStyle team = new TextStyle("TEAM E1", 60);
		Text teamText = team.getTextStyled();

		MenuButton btn1 = new MenuButton("MACIEK BOGACKI", 350, 70, 30);
		btn1.setOnMouseEntered(e -> {

			TextStyle contribution1 = new TextStyle("PHYSICS ENGINE & NETWORKING", 30);
			contribution1Text = contribution1.getTextStyled();

			GridPane.setHalignment(contribution1Text, HPos.CENTER);
			add(contribution1Text, 0, 2);

		});

		btn1.setOnMouseExited(e -> {

			getChildren().remove(contribution1Text);
		});

		MenuButton btn2 = new MenuButton("REECE BENNETT", 350, 70, 30);
		btn2.setOnMouseEntered(e -> {

			TextStyle contribution2 = new TextStyle("AI & RENDERER & IN-GAME UI", 30);
			contribution2Text = contribution2.getTextStyled();

			GridPane.setHalignment(contribution2Text, HPos.CENTER);
			add(contribution2Text, 1, 2);

		});

		btn2.setOnMouseExited(e -> {

			getChildren().remove(contribution2Text);
		});

		MenuButton btn3 = new MenuButton("ANDREEA GHEORGHE", 350, 70, 30);
		btn3.setOnMouseEntered(e -> {

			TextStyle contribution3 = new TextStyle("USER INTERFACE", 30);
			contribution3Text = contribution3.getTextStyled();

			GridPane.setHalignment(contribution3Text, HPos.CENTER);
			add(contribution3Text, 0, 4);

		});

		btn3.setOnMouseExited(e -> {

			getChildren().remove(contribution3Text);
		});

		MenuButton btn4 = new MenuButton("REECE MEEK", 350, 70, 30);
		btn4.setOnMouseEntered(e -> {

			TextStyle contribution4 = new TextStyle("GAME ENGINE & RENDERER", 30);
			contribution4Text = contribution4.getTextStyled();

			GridPane.setHalignment(contribution4Text, HPos.CENTER);
			add(contribution4Text, 1, 4);

		});

		btn4.setOnMouseExited(e -> {

			getChildren().remove(contribution4Text);
		});

		MenuButton btn5 = new MenuButton("TUDOR SURUCEANU", 350, 70, 30);
		btn5.setOnMouseEntered(e -> {

			TextStyle contribution5 = new TextStyle("AUDIO & GAME LOGIC", 30);
			contribution5Text = contribution5.getTextStyled();

			GridPane.setHalignment(contribution5Text, HPos.CENTER);
			add(contribution5Text, 0, 6);

		});

		btn5.setOnMouseExited(e -> {

			getChildren().remove(contribution5Text);
		});

		MenuButton btn6 = new MenuButton("SIMON WEST", 350, 70, 30);
		btn6.setOnMouseEntered(e -> {

			TextStyle contribution6 = new TextStyle("NETWORKING & TRACK GENERATION", 30);
			contribution6Text = contribution6.getTextStyled();

			add(contribution6Text, 1, 6);
			GridPane.setHalignment(contribution6Text, HPos.CENTER);

		});

		btn6.setOnMouseExited(e -> {

			getChildren().remove(contribution6Text);
		});

		// GRID PANE LAYOUT //

		add(teamText, 0, 0);
		add(btn1, 0, 1);
		add(btn2, 1, 1);
		add(btn3, 0, 3);
		add(btn4, 1, 3);
		add(btn5, 0, 5);
		add(btn6, 1, 5);
		
		this.setCache(true);
		this.setCacheHint(CacheHint.SPEED);

	}
}
