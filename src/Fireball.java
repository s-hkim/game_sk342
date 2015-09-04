import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class Fireball{
	private Character myOrigin;
	private Circle myHitbox;
	private ImageView myImage;
	private int myDamage;
	private Timeline myTimeline;
	
	public Fireball (Character o, Circle h, Timeline tl, int d, ImageView imageView) {
		myImage = imageView;
		myOrigin = o;
		myHitbox = h;
		myTimeline = tl;
		myDamage = d;
	}
	public void executeAction() {
		myTimeline.play();
	}
	public void updateImage() {
		if (myImage == null) {
			return;
		}
		double centerX = myHitbox.getCenterX();
		double centerY = myHitbox.getCenterY();
		myImage.setX(centerX - myImage.getBoundsInParent().getWidth()/2);
		myImage.setY(centerY - myImage.getBoundsInParent().getHeight()/2);
	}
	public Character getMyOrigin() {
		return myOrigin;
	}
	public ImageView getMyImage() {
		return myImage;
	}
	public Circle getMyHitbox() {
		return myHitbox;
	}
	public int getMyDamage() {
		return myDamage;
	}
	public Timeline getMyTimeline() {
		return myTimeline;
	}
}
