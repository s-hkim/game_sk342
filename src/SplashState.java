import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SplashState extends GameState {
	private Game myGame;
	private InputListener myInputManager;
	
	public SplashState (Game g) {
		myGame = g;
		Group root = myGame.getMyRoot();
		myInputManager = myGame.getMyInputManager();
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("backgroundSprite.png"));
		ImageView background = new ImageView(image);
		background.setViewport(new Rectangle2D(0,0,800,336));
		root.getChildren().add(background);
		SpriteTransition backgroundAnimation = new SpriteTransition(background, 
				Duration.millis(1000), 0, 0, 0, new int[]{800,800,800,800,800,800,800,800}, 
				new int[]{336,336,336,336,336,336,336,336}, true);
		backgroundAnimation.setCycleCount(Animation.INDEFINITE);
		backgroundAnimation.play();
		image = new Image(getClass().getClassLoader().getResourceAsStream("titlescreen.png"));
        ImageView backgroundOverlay = new ImageView(image);
		myGame.getMyRoot().getChildren().add(backgroundOverlay);
	}
	public void update(){
		if (myInputManager.checkKeyCombos()) {
			myGame.changeState(new LevelState(myGame));
		}
		myInputManager.discardInputsFromBoth();
	}
}
