import java.util.LinkedList;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputListener {
	public boolean upPressed;
	public boolean downPressed;
	public boolean rightPressed;
	public boolean leftPressed;
	public boolean aPressed;
	public boolean sPressed;
	public boolean dPressed;
	public LinkedList<String> keyCombo;
	public boolean left = true;
	
	public InputListener () {
		upPressed = false;
		downPressed = false;
		rightPressed = false;
		leftPressed = false;
		aPressed = false;
		sPressed = false;
		dPressed = false;
		keyCombo = new LinkedList<String>();
	}
	
	public void keyPressed(KeyEvent ke) {
		KeyCode code = ke.getCode();
		if (code == KeyCode.UP) {
			upPressed = true;
			keyCombo.addLast(code.toString());
		} if (code == KeyCode.DOWN) {
			downPressed = true;
			keyCombo.addLast(code.toString());
		} if (code == KeyCode.RIGHT) {
			rightPressed = true;
			if (left) {
				keyCombo.addLast("FORWARD");
			} else {
				keyCombo.addLast("BACKWARD");
			}
		} if (code == KeyCode.LEFT) {
			leftPressed = true;
			if (left) {
				keyCombo.addLast("BACKWARD");
			} else {
				keyCombo.addLast("FORWARD");
			}
		} if (code == KeyCode.A) {
			aPressed = true;
			keyCombo.addLast(code.toString());
		} if (code == KeyCode.S) {
			sPressed = true;
			keyCombo.addLast(code.toString());
		} if (code == KeyCode.D) {
			dPressed = true;
			keyCombo.addLast(code.toString());
		} 
		if (keyCombo.size() > 10) {
			discardInput();
		}
	}

	public void keyReleased(KeyEvent ke) {
		KeyCode code = ke.getCode();
		if (code == KeyCode.UP) {
			upPressed = false;
		} if (code == KeyCode.DOWN) {
			downPressed = false;
		} if (code == KeyCode.RIGHT) {
			rightPressed = false;
		} if (code == KeyCode.LEFT) {
			leftPressed = false;
		} if (code == KeyCode.A) {
			aPressed = false;
		} if (code == KeyCode.S) {
			sPressed = false;
		} if (code == KeyCode.D) {
			dPressed = false;
		}
	}
	public void setOrientation (boolean l) {
		left = l;
	}
	public void discardInput() {
		keyCombo.pollFirst();
	}

}
