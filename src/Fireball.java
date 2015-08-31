import javafx.animation.Timeline;
import javafx.scene.shape.Circle;

public class Fireball{
	public Character myOrigin;
	public Circle myHitbox;
	public int myDamage;
	public Timeline timeline;
	public Fireball (Character o, Circle h, Timeline tl, int d) {
		myOrigin = o;
		myHitbox = h;
		timeline = tl;
		myDamage = d;
	}
	public void executeAction() {
		timeline.play();
	}

}
