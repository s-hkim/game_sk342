import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
/*
 * A sprite animation class
 * based on code taken from: http://blog.netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
 */

public class SpriteTransition extends Transition {

    private int centerOffset;
    private final ImageView imageView;
    private final int count;
    private final int offsetX;
    private final int offsetY;
    private final int[] widths;
    private final int[] heights;
    private int[] widthOffsets;
    private final boolean isLeft;
	private final int direction;
    private int lastIndex;
    
    public SpriteTransition(ImageView imageView, Duration duration, int centerOffset, SpriteObject dimensions, boolean left) {
        this.imageView = imageView;
        this.centerOffset = centerOffset;
        this.offsetY = dimensions.getyOffset();
        this.widths = dimensions.getWidths();
        this.heights = dimensions.getHeights();
        this.count = widths.length;
        int offsetX = dimensions.getxOffset();
        if (left) {
        	this.offsetX = offsetX + this.centerOffset;
        } else {
        	this.offsetX = this.centerOffset - offsetX;
        }
        widthOffsets = new int[widths.length];
        int s = 0;
        for (int i = 0; i < widths.length; i++) {
        	s = s + widths[i];
        	widthOffsets[i] = s;
        }
        
        this.isLeft = left;
        if (left) {
        	direction = 1;
        } else {
        	direction = -1;
        }
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            int x = direction* widthOffsets[index%count]  + offsetX;
            if (direction > 0) {
            	x = x - widths[index];
            }
            imageView.setViewport(new Rectangle2D(x, offsetY, widths[index], heights[index]));
            lastIndex = index;
        }
    }
    public boolean getIsLeft() {
		return isLeft;
	}
}