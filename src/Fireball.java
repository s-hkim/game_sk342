import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class Fireball{
	private Character myOrigin;
	private Circle myHitbox;
	private ImageView myImage;
	private int myDamage;
	public ImageView getMyImage() {
		return myImage;
	}
	private Timeline myTimeline;
	public Character getMyOrigin() {
		return myOrigin;
	}
	public void setMyOrigin(Character myOrigin) {
		this.myOrigin = myOrigin;
	}
	public Circle getMyHitbox() {
		return myHitbox;
	}
	public void setMyHitbox(Circle myHitbox) {
		this.myHitbox = myHitbox;
	}
	public int getMyDamage() {
		return myDamage;
	}
	public void setMyDamage(int myDamage) {
		this.myDamage = myDamage;
	}
	public Timeline getMyTimeline() {
		return myTimeline;
	}
	public void setMyTimeline(Timeline myTimeline) {
		this.myTimeline = myTimeline;
	}
	
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

}
