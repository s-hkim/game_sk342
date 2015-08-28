import java.util.PriorityQueue;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/**
 * Separate the game code from some of the boilerplate code.
 * 
 * TEMP
 */
class Game {
    public static final String TITLE = "Example JavaFX";
    public static final int KEY_INPUT_SPEED = 5;
    private static final double GROWTH_RATE = 1.1;
    private static final int BOUNCER_SPEED = 30;

    private Group myRoot;
    private Scene myScene;
    private ImageView myBouncer;
    private Rectangle myTopBlock;
    private Rectangle myBottomBlock;
    
    
    final BooleanProperty upPressed = new SimpleBooleanProperty(false);
    final BooleanProperty rightPressed = new SimpleBooleanProperty(false);
    final BooleanProperty downPressed = new SimpleBooleanProperty(false);
    final BooleanProperty leftPressed = new SimpleBooleanProperty(false);
    final BooleanBinding upAndRightPressed = upPressed.and(rightPressed);
    final BooleanBinding upAndLeftPressed = upPressed.and(leftPressed);
    private double groundLevel;
    private PriorityQueue<KeyCode> keyCombo;

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
        // Create a scene graph to organize the scene
    	keyCombo = new PriorityQueue<KeyCode>(4);
        myRoot = new Group();
        // Create a place to see the shapes
        myScene = new Scene(myRoot, width, height, Color.WHITE);
        // Make some shapes and set their properties
        Image image = new Image(getClass().getClassLoader().getResourceAsStream("duke.gif"));
        myBouncer = new ImageView(image);
        // x and y represent the top left corner, so center it
        myBouncer.setX(width / 2 - myBouncer.getBoundsInLocal().getWidth() / 2);
        myBouncer.setY(height / 2  - myBouncer.getBoundsInLocal().getHeight() / 2);
        myTopBlock = new Rectangle(width / 2 - 25, height / 2 + 50, 50, 50);
        myTopBlock.setFill(Color.RED);
        groundLevel = myTopBlock.getY();
        myBottomBlock = new Rectangle(width / 2 - 25, height / 2 - 100, 50, 50);
        myBottomBlock.setFill(Color.BISQUE);
        // order added to the group is the order in whuch they are drawn
        myRoot.getChildren().add(myBouncer);
        myRoot.getChildren().add(myTopBlock);
        myRoot.getChildren().add(myBottomBlock);
        // Respond to input
        //myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        
//        upAndRightPressed.addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> obs, Boolean werePressed, Boolean arePressed) {
//                if (arePressed) {
//                	System.out.println("up and right pressed together");
//                	moveInArc(1);
//                    //myTopBlock.setX(myTopBlock.getX() + KEY_INPUT_SPEED);
//                    //myTopBlock.setY(myTopBlock.getY() - KEY_INPUT_SPEED);
//                }
//            }
//
//        });
//        upAndLeftPressed.addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> obs, Boolean werePressed, Boolean arePressed) {
//                if (arePressed) {
//                	System.out.println("up and left pressed together");
//                	moveInArc(-1);
//                    //myTopBlock.setX(myTopBlock.getX() - KEY_INPUT_SPEED);
//                    //myTopBlock.setY(myTopBlock.getY() - KEY_INPUT_SPEED);
//                }
//            }
//        });

        // Wire up properties to key events:
        myScene.setOnKeyPressed(e -> handleKeyEventPress(e));
        myScene.setOnKeyReleased(e -> handleKeyEventRelease(e));
        
        return myScene;
    }
    private void moveInArc(double dx) {
		Timeline tl = new Timeline();
		tl.setCycleCount(99);
		KeyFrame moveJump = new KeyFrame(Duration.millis(1000/60),
				new EventHandler<ActionEvent>() {
			double dy = 2;
			public void handle(ActionEvent event) {
				dy = dy - 0.04;
				myTopBlock.setX(myTopBlock.getX() + dx);
				myTopBlock.setY(myTopBlock.getY() - dy);
				//System.out.println(myTopBlock.getY());
			}
		});
		tl.getKeyFrames().add(moveJump);
		tl.play();
	}
    private void handleKeyEventPress(KeyEvent ke) {
        if (ke.getCode() == KeyCode.UP) {
            upPressed.set(true);
        } else if (ke.getCode() == KeyCode.DOWN) {
        	downPressed.set(true);
        } else if (ke.getCode() == KeyCode.RIGHT) {
            rightPressed.set(true);
        } else if (ke.getCode() == KeyCode.LEFT) {
        	leftPressed.set(true);
        }
        keyCombo.add(ke.getCode());
    }
    private void handleKeyEventRelease(KeyEvent ke) {
        if (ke.getCode() == KeyCode.UP) {
            upPressed.set(false);
        } else if (ke.getCode() == KeyCode.DOWN) {
        	downPressed.set(false);
        } else if (ke.getCode() == KeyCode.RIGHT) {
            rightPressed.set(false);
        } else if (ke.getCode() == KeyCode.LEFT) {
        	leftPressed.set(false);
        }
    }
    /**
     * Change properties of shapes to animate them
     * 
     * Note, there are more sophisticated ways to animate shapes,
     * but these simple ways work too.
     */
    public void step (double elapsedTime) {
        // update attributes
        // check for collisions
        // with shapes, can check precisely
        Shape intersect = Shape.intersect(myTopBlock, myBottomBlock);
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            myTopBlock.setFill(Color.MAROON);
        }
        else {
            myTopBlock.setFill(Color.RED);
        }
        // with images can only check bounding box
        if (myBottomBlock.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
            myBottomBlock.setFill(Color.BURLYWOOD);
        }
        else {
            myBottomBlock.setFill(Color.BISQUE);
        }
        if (myTopBlock.getY() > groundLevel) {
        	myTopBlock.setY(groundLevel);
        }
        if (myTopBlock.getY() == groundLevel) {
        	if (upPressed.getValue() && rightPressed.getValue()) {
            	moveInArc(1);
            } else if (upPressed.getValue() && leftPressed.getValue()) {
            	moveInArc(-1);
            } else if (upPressed.getValue()) {
            	// System.out.println(myTopBlock.getY());
            	moveInArc(0);
            } else if (rightPressed.getValue()) {
            	myTopBlock.setX(myTopBlock.getX() + KEY_INPUT_SPEED);
            } else if (leftPressed.getValue()) {
            	myTopBlock.setX(myTopBlock.getX() - KEY_INPUT_SPEED);
            }
        }
        keyCombo.poll();
    }


    // What to do each time a key is pressed

    private void handleKeyInput (KeyCode code) {
        switch (code) {
            case RIGHT:
                myTopBlock.setX(myTopBlock.getX() + KEY_INPUT_SPEED);
                break;
            case LEFT:
                myTopBlock.setX(myTopBlock.getX() - KEY_INPUT_SPEED);
                break;
            case UP:
                myTopBlock.setY(myTopBlock.getY() - KEY_INPUT_SPEED);
                break;
            case DOWN:
                myTopBlock.setY(myTopBlock.getY() + KEY_INPUT_SPEED);
                break;
            default:
                // do nothing
        }
    }

}
