package userInterface;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class SoundSlider extends HBox {
	
	private Slider soundSlider;
	private Text value;
	
	public SoundSlider(){
		
		soundSlider = new Slider(0,10,5);

		value = new Text(Integer.toString((int)soundSlider.getValue()));
		
		try {
			Font f = Font.loadFont(new FileInputStream(new File("res/fonts/War is Over.ttf")), 30);
			value.setFont(f);
			value.setFill(Color.WHITE);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		soundSlider.setBlockIncrement(1);
		
		soundSlider.valueProperty().addListener(
			(observable, oldValue, newValue) -> {	
					int i = newValue.intValue();
					value.setText(Integer.toString(i));
					
					//TUDOR ADD AUDIO STUFF
				}
				
			);
		
		getChildren().addAll(value,soundSlider);
		setWidth(400);
	}

}
