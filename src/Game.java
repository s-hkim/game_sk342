import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

class Game {
    private static final String TITLE = "Mediocre Road Warrior";
    private Group myRoot;
    private Scene myScene;
    private GameState myCurrentState;
    private InputListener myInputManager;
    
    public String getTitle () {
        return TITLE;
    }
    public Group getMyRoot() {
		return myRoot;
	}
	public Scene getMyScene() {
		return myScene;
	}
	public GameState getCurrentState() {
		return myCurrentState;
	}
	public InputListener getMyInputManager() {
		return myInputManager;
	}

    public Scene init (int width, int height) {
    	myInputManager = new InputListener();
        // Create a scene graph to organize the scene
        myRoot = new Group();
        // Create a place to see the shapes
        myScene = new Scene(myRoot, width, height, Color.WHITE);
        // Wire up properties to key events:
        myScene.setOnKeyPressed(e -> myInputManager.keyPressed(e));
        myScene.setOnKeyReleased(e -> myInputManager.keyReleased(e));
        myCurrentState = new SplashState(this);
        return myScene;
    }
    
    public void step (double elapsedTime) {
    	myCurrentState.update();    	
    }
    
    public void changeState (GameState newState) {
    	this.myCurrentState = newState;
    }
    
}
