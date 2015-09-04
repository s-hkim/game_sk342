import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class VictoryState extends GameState{
	private Game myGame;

	public VictoryState (Game g, int loserID) {
		myGame = g;
		myGame.getMyRoot().getChildren().clear();
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("endscreen.png"));
		ImageView background = new ImageView(image);
		myGame.getMyRoot().getChildren().add(background);
		Text text = new Text();
		text.setFont(new Font("Courier", 100));
		text.setX(320);
		text.setY(150);
		if (loserID == 1) {
			text.setText("P1:");
		} else {
			text.setText("P2:");
		}
		myGame.getMyRoot().getChildren().add(text);
	}
	public void update(){
		InputListener inputs = myGame.getMyInputManager();
		if (inputs.isSpacePressed()) {
			myGame.changeState(new SplashState(myGame));
		}
		myGame.getMyInputManager().discardInputsFromBoth();
	}

}
