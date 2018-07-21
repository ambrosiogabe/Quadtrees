import java.util.List;
import java.awt.*;
import java.util.ArrayList;

public class Quadtree {
  private int MAX_OBJECTS = 10;
  private int MAX_LEVELS = 5;

  private int level;
  private List<MyRectangle> objects;
  private MyRectangle bounds;
  public Quadtree[] nodes;

  private int width;
  private int height;
  private int x;
  private int y;

//getters
public int getWidth() {
  return width;
}

public int getHeight() {
  return height;
}

public int getX() {
  return x;
}

public int getY() {
  return y;
}


//Constructor
public Quadtree(int pLevel, MyRectangle pBounds) {
 level = pLevel;
 objects = new ArrayList<MyRectangle>();
 bounds = pBounds;
 nodes = new Quadtree[4];

 width = pBounds.width;
 height = pBounds.height;
 x = pBounds.x;
 y = pBounds.y;
}

 //Clear method it clears the quadtree
 public void clear() {
   objects.clear();

   for (int i = 0; i < nodes.length; i++) {
     if (nodes[i] != null) {
       nodes[i].clear();
       nodes[i] = null;
     }
   }
 }

 //splits a node into four subnodes
 private void split() {
   int subWidth = (int)(bounds.getWidth() / 2);
   int subHeight = (int)(bounds.getHeight() / 2);
   int qx = (int)(bounds.getX());
   int qy = (int)(bounds.getY());

   nodes[0] = new Quadtree(level + 1, new MyRectangle(qx + subWidth, qy, subWidth, subHeight));
   nodes[1] = new Quadtree(level + 1, new MyRectangle(qx, qy, subWidth, subHeight));
   nodes[2] = new Quadtree(level + 1, new MyRectangle(qx, qy + subHeight, subWidth, subHeight));
   nodes[3] = new Quadtree(level + 1, new MyRectangle(qx + subWidth, qy + subHeight, subWidth, subHeight));
 }

 /*
 * Determine which node the object belongs to. -1 means
 * object cannot completely fit within a child node and is part
 * of the parent node
 */
 private int getIndex(MyRectangle pRect) {
   int index = -1;
   double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
   double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

   // Object can completely fit within the top quadrants
   boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint);
   // Object can completely fit within the bottom quadrants
   boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);

   // Object can completely fit within the left quadrants
   if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
      if (topQuadrant) {
        index = 1;
      }
      else if (bottomQuadrant) {
        index = 2;
      }
    }
    // Object can completely fit within the right quadrants
    else if (pRect.getX() > verticalMidpoint) {
     if (topQuadrant) {
       index = 0;
     }
     else if (bottomQuadrant) {
       index = 3;
     }
   }


   if(index == -1) {
     if(topQuadrant) {
       if(pRect.getX() < verticalMidpoint && pRect.getX() + pRect.halfWidth() < verticalMidpoint ) {
         index = 1;
       } else {
         index = 0;
       }
     } else if (bottomQuadrant) {
       if(pRect.getX() < verticalMidpoint && pRect.getX() + pRect.halfWidth() < verticalMidpoint) {
         index = 2;
       } else {
         index = 3;
       }
     }
   }


   return index;
 }

 /*
 * Insert the object into the quadtree. If the node
 * exceeds the capacity, it will split and add all
 * objects to their corresponding nodes.
 */
 public void insert(MyRectangle pRect) {
   if (nodes[0] != null) {
     int index = getIndex(pRect);

     if (index != -1) {
       nodes[index].insert(pRect);

       return;
     }
   }

   objects.add(pRect);

   if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
      if (nodes[0] == null) {
         split();
      }

     int i = 0;
     while (i < objects.size()) {
       int index = getIndex(objects.get(i));
       if (index != -1) {
         nodes[index].insert(objects.get(i));
         objects.remove(i);
       }
       else {
         i++;
       }
     }
    }
  }

 /*
 * Return all objects that could collide with the given object
 */
 public List<MyRectangle> retrieve(List<MyRectangle> returnObjects, MyRectangle pRect) {
   int index = getIndex(pRect);
   if (index != -1 && nodes[0] != null) {
     nodes[index].retrieve(returnObjects, pRect);
   }

   returnObjects.addAll(objects);

   return returnObjects;
 }
}
