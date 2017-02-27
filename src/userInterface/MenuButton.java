package userInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import audioEngine.AudioMaster;
import audioEngine.Sounds;
import audioEngine.Source;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import serverComms.Lobby;

/**
 * 
 * @author Andreea Gheorghe
 *
 */
public class MenuButton extends StackPane {
	
	private Text buttonText;
	
	// Tudor - audio source
	private Source audioSource;
	
	/**
	 * Constructor for the MenuButton class that creates a menu button
	 * with a certain style, and with the given name as the label. 
	 * @param name The name of the button.
	 */
	public MenuButton (String name) {
		
		buttonText = new Text(name);
		
		// Tudor - initialize source
		audioSource = AudioMaster.createSFXSource();
		
		try {
			Font f = Font.loadFont(new FileInputStream(new File("res/fonts/War is Over.ttf")), 27);
			buttonText.setFont(f);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//create button
		Rectangle bg = new Rectangle (250, 50);
		bg.setOpacity(0.7);
		bg.setFill(Color.BLACK);
		//blur the button colour
		GaussianBlur blur = new GaussianBlur(3.6);
		bg.setEffect(blur);
		
		buttonText.setFill(Color.WHITE);
		
		//customise position
		this.setAlignment(Pos.CENTER);
		//setRotate(-0.8);
		
		//add button to stack with text over the background
		getChildren().addAll(bg, buttonText);
	
		
		//hover over button
		this.setOnMouseEntered(event -> {
			bg.setTranslateX(6);
			buttonText.setTranslateX(6);
			bg.setFill(Color.DIMGRAY);
			buttonText.setFill(Color.WHITE);
			
			// Tudor - play sound
			audioSource.play(Sounds.BUTTON_HOVER);
			
		});
		
		//stop hovering over button
		this.setOnMouseExited(event -> {
			bg.setTranslateX(0);
			buttonText.setTranslateX(0);
			bg.setFill(Color.BLACK);
			buttonText.setFill(Color.WHITE);
		});
		
		//create glow effect to let user know they have clicked a button
		DropShadow effect = new DropShadow (50, Color.WHITE);
		effect.setInput(new Glow());
		
		//clicked on button
		this.setOnMousePressed(event -> {
			setEffect(effect);
			// Tudor - play sound
			audioSource.play(Sounds.BUTTON_CLICK);
		});
		
		//released button
		this.setOnMouseReleased(event -> {
			setEffect(null);
		});
		
	}
	
	public void setButtonText(String text){
		
		Text newText = new Text(text);
		this.buttonText = newText;
		
	}

	public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() { return onAction; }
	
    public final void setOnAction(EventHandler<ActionEvent> value) { onActionProperty().set(value); }
    
    public final EventHandler<ActionEvent> getOnAction() { return onActionProperty().get(); }
    
    private ObjectPropertyBase<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

       
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };
	
}
