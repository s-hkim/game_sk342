import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

/**
 * Separate the game code from some of the boilerplate code.
 * 
 * TEMP
 */
class Game {
    public static final String TITLE = "Mediocre Road Warrior";
    public Group myRoot;
    public Scene myScene;
    public GameState currentState;
    public InputListener myInputManager;
    
    /**
     * Returns name of the game.
     */
    public String getTitle () {
        return TITLE;
    }

    /**
     * Create the game's scene
     */
    public Scene init (int width, int height) {
    	myInputManager = new InputListener();
        // Create a scene graph to organize the scene
        myRoot = new Group();
        // Create a place to see the shapes
        myScene = new Scene(myRoot, width, height, Color.WHITE);
        // Wire up properties to key events:
        myScene.setOnKeyPressed(e -> myInputManager.keyPressed(e));
        myScene.setOnKeyReleased(e -> myInputManager.keyReleased(e));
        currentState = new SplashState(this);
        // currentState = new LevelState(this, myInputManager);
        return myScene;
    }
    
    /**
     * Change properties of shapes to animate them
     * 
     * Note, there are more sophisticated ways to animate shapes,
     * but these simple ways work too.
     */
    public void step (double elapsedTime) {
    	currentState.update();    	
    }
    
    public void changeState (GameState newState) {
    	currentState = newState;
    }
    
}
