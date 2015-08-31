import java.util.ArrayList;
import java.util.LinkedList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class LevelState extends GameState {
	private Game myGame;
	private InputListener inputs;
	private ImageView playerImage;
	private ImageView enemyImage;
	private Character myPlayer;
	private Character myEnemy;
	private Text p1Text;
	private Text p2Text;
	private Text inputText;
    
	private Scene myScene;
	private Group myRoot;
	double width;
	double height;
	private int groundLevel = 300;
	private int frameCount;

    public InputTree moveTree;
	
	public LevelState (Game g) {
		myGame = g;
		myScene = myGame.myScene;
		myRoot = myGame.myRoot;
		inputs = myGame.myInputManager;
		inputs.keyCombo.clear();
		
		frameCount = 0;
		myRoot.getChildren().clear();
		width = myScene.getWidth();
		height = myScene.getHeight();
		
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("background.png"));
        ImageView background = new ImageView(image);
        myRoot.getChildren().add(background);
        image = new Image(getClass().getClassLoader().getResourceAsStream("duke.gif"));
		
		playerImage = new ImageView(image);
        playerImage.setX(width / 2 - playerImage.getBoundsInLocal().getWidth() / 2 - 200);
        playerImage.setY(height / 2  - playerImage.getBoundsInLocal().getHeight() / 2 + 50);
        myPlayer = new Character(myGame, playerImage, true);
        
        enemyImage = new ImageView(image);
        enemyImage.setX(width / 2 - enemyImage.getBoundsInLocal().getWidth() / 2 + 150);
        enemyImage.setY(height / 2  - enemyImage.getBoundsInLocal().getHeight() / 2 + 50);
        myEnemy = new Character(myGame, enemyImage, false);
        
		myRoot.getChildren().add(playerImage);
        myRoot.getChildren().add(enemyImage);
		
        p1Text = new Text();
        p1Text.setX(100);
        p1Text.setY(50);
        p1Text.setText(""+myPlayer.getHealth());
        myRoot.getChildren().add(p1Text);
        p2Text = new Text();
        p2Text.setX(700);
        p2Text.setY(50);
        p2Text.setText(""+myEnemy.getHealth());
        myRoot.getChildren().add(p2Text);
        inputText = new Text();
        inputText.setX(50);
        inputText.setY(100);
        myRoot.getChildren().add(inputText);
        
		moveTree = new InputTree();
		String[] codes = {KeyCode.A.toString(), "FORWARD", KeyCode.DOWN.toString()};
    	String[] codes2 = {KeyCode.A.toString(), "BACKWARD"};
//    	LinkedList<String> inputCodes = new LinkedList<String>();
//    	inputCodes.add(KeyCode.DOWN.toString());
//    	inputCodes.add("FORWARD");
//    	inputCodes.add(KeyCode.A.toString());
    	moveTree.addMove(codes, "QCFL");
    	moveTree.addMove(codes2, "BL");
    	codes = new String[]{KeyCode.S.toString(), "FORWARD", KeyCode.DOWN.toString()};
    	moveTree.addMove(codes, "QCFM");
    	codes = new String[]{KeyCode.D.toString(), "FORWARD", KeyCode.DOWN.toString()};
    	moveTree.addMove(codes, "QCFH");
//    	moveTree.printTree(moveTree.myRoot);
//    	System.out.println("");
//    	System.out.println(moveTree.parseInput(inputCodes));
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		double myPlayerWidth = playerImage.getBoundsInLocal().getWidth();
        double myPlayerHeight = playerImage.getBoundsInLocal().getHeight();
		double myEnemyCenter = enemyImage.getX() + enemyImage.getBoundsInLocal().getWidth() / 2;
		double myPlayerCenter = playerImage.getX() + myPlayerWidth;
		if (myEnemyCenter - myPlayerCenter > 0) {
			myPlayer.setOrientation(true);
		} else {
			myPlayer.setOrientation(false);
		}
		inputs.setOrientation(myPlayer.getOrientation());
		ArrayList<Fireball> playerHitboxes = myPlayer.getHitboxes();
		ArrayList<Fireball> tempArray = new ArrayList<Fireball>(playerHitboxes);
		for (Fireball f: tempArray) {
			Circle c = f.myHitbox;
    		double cx = c.getCenterX();
    		double cr = c.getRadius();
    		if (cx - cr < 0 || cx + cr > myScene.getWidth()) {
    			myRoot.getChildren().remove(c);
    		}
    		if (c.getBoundsInParent().intersects(enemyImage.getBoundsInParent())) {
    			myEnemy.inflictDamage(f.myDamage);
    			p2Text.setText(""+myEnemy.getHealth());
    			myRoot.getChildren().remove(c);
    			playerHitboxes.remove(f);
    			
    		}
    	}
		inputText.setText(inputs.keyCombo.toString());
    	
    		// TODO: enemy hitboxes
    	
        if (playerImage.getY() + myPlayerHeight / 2> groundLevel) {
        	playerImage.setY(groundLevel - myPlayerHeight / 2);
        }
        if (!myEnemy.getInAir()) {
        	myEnemy.executeAction("U");
        }
        if (!myPlayer.getInAir() && !myPlayer.getAttacking()) {

        	LinkedList<String> inputCodes = inputs.keyCombo;
        	String instruction = moveTree.parseInput(inputCodes);
        	if (instruction != null) {
        		inputCodes.clear();
        		myPlayer.executeAction(instruction);
        	}
        	//        if (inputs.aPressed) {
        	//        	myPlayer.executeAction("QCFL");
        	//        }

        	if (playerImage.getY() == groundLevel - myPlayerHeight / 2) {
        		if (inputs.upPressed && inputs.rightPressed) {
        			myPlayer.executeAction("UF");
        		} else if (inputs.upPressed && inputs.leftPressed) {
        			myPlayer.executeAction("UB");
        		} else if (inputs.upPressed) {
        			// System.out.println(myTopBlock.getY());
        			myPlayer.executeAction("U");
        		} else if (inputs.rightPressed) {
        			playerImage.setX(playerImage.getX() + 5);
        		} else if (inputs.leftPressed) {
        			playerImage.setX(playerImage.getX() - 5);
        		}
        		if (playerImage.getX() + myPlayerWidth > myScene.getWidth()) {
        			playerImage.setX(myScene.getWidth() - myPlayerWidth);
        		} else if (playerImage.getX() < 0) {
        			playerImage.setX(0);
        		}
        	}
        }
        if (frameCount == 30) {
            myGame.myInputManager.discardInput();
        }
        frameCount++;
        if (frameCount > 30) { 
        	frameCount = 0;
        }
	}

}
