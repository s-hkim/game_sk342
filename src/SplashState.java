import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SplashState extends GameState {
	private Game myGame;
	
	public SplashState (Game g) {
		myGame = g;
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("titlescreen.png"));
        ImageView background = new ImageView(image);
		myGame.myRoot.getChildren().add(background);
	}
	public void update(){
		if (!myGame.myInputManager.keyCombo.isEmpty()) {
			myGame.changeState(new LevelState(myGame));
		}
		myGame.myInputManager.discardInput();
	}
}
