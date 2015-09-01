import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VictoryState extends GameState{
private Game myGame;
	
	public VictoryState (Game g) {
		myGame = g;
		myGame.myRoot.getChildren().clear();
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("endscreen.png"));
        ImageView background = new ImageView(image);
		myGame.myRoot.getChildren().add(background);
	}
	public void update(){
		myGame.myInputManager.discardInput();
	}

}
