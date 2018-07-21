
import java.awt.*;

public class MyRectangle extends Rectangle {
  public int dx, dy;
  public Color color;
  public boolean colliding;

  public MyRectangle(int newX, int newY, int newWidth, int newHeight) {
    x = newX;
    y = newY;
    width = newWidth;
    height = newHeight;
  }

  public void setIsColliding(boolean value) {
    if(value)
      colliding = true;
    else
      colliding = false;
  }

  private int centerx() {
		int centerx = this.x + (this.width / 2);
		return centerx;
	}

	private int centery() {
		int centery = this.y + (this.height / 2);
		return centery;
	}

	public int halfWidth() {
		int halfWidth = width / 2;
		return halfWidth;
	}

	private int halfHeight() {
		int halfHeight = height / 2;
		return halfHeight;
	}

	public int blockRectangle(MyRectangle r1, MyRectangle r2) {
		//For collision side 1 = 'top'   2 = 'bottom'   3 = 'left'   4 = 'right'
		int collisionSide, overlapX, overlapY;

		int vx = r1.centerx() - r2.centerx();
		int vy = r1.centery() - r2.centery();

		int combinedHalfWidths = r1.halfWidth() + r2.halfWidth();
		int combinedHalfHeights = r1.halfHeight() + r2.halfHeight();

		if(Math.abs(vx) < combinedHalfWidths) {
			if(Math.abs(vy) < combinedHalfHeights) {
				overlapX = combinedHalfWidths - Math.abs(vx);
				overlapY = combinedHalfHeights - Math.abs(vy);

				if(overlapX >= overlapY) {
					if(vy > 0) {
						collisionSide = 1;
						r1.y += overlapY;
					} else {
						collisionSide = 2;
						r1.y -= overlapY;
					}
				} else {
					if(vx > 0) {
						collisionSide = 3;
						r1.x += overlapX;
					} else {
						collisionSide = 4;
						r1.x -= overlapX;
					}
				}
			} else {
				collisionSide = 0;
			}
		} else {
			collisionSide = 0;
		}

		return collisionSide;
	}
}
