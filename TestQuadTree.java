import java.awt.*;
import javax.swing.JFrame;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;

public class TestQuadTree extends JFrame implements Runnable {
  private Image dbImage;
  private Graphics dbg;

  final int SCREEN_WIDTH = 1000;
  final int SCREEN_HEIGHT = 1000;
  final int RECT_WIDTH = 30;
  final int RECT_HEIGHT = 10;
  MyRectangle[] rectangles = new MyRectangle[250];
  MyRectangle r1;
  MyRectangle player = new MyRectangle(250, 250, 30, 30);
  MyRectangle currentRectangle;
  int keyCode;

  Quadtree quad;
  List<MyRectangle> newObjects = new ArrayList<MyRectangle>();

  public TestQuadTree() {
    setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    setTitle("Quadtree testing box collision");
    setResizable(false);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    quad = new Quadtree(0, new MyRectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT));

    for(int i=0; i < rectangles.length; i++) {
      Random ranX = new Random();
      int randX = ranX.nextInt(1001);

      Random ranY = new Random();
      int randY = ranY.nextInt(501);

      Random ranDy = new Random();
      int randomDy = ranDy.nextInt(4) + 1;

      Random ranDx = new Random();
      int randomDx = ranDx.nextInt(4) + 1;

      rectangles[i] = new MyRectangle(randX, randY, RECT_WIDTH, RECT_HEIGHT);
      rectangles[i].dx = randomDx;
      rectangles[i].dy = randomDy;
      rectangles[i].color = Color.blue;
    }
    addKeyListener(new AL());

  }

  public class AL extends KeyAdapter {
    public void keyPressed(KeyEvent e) {
      int keyCode = e.getKeyCode();
      if(keyCode == e.VK_UP) {
        setYDirection(-5);
      }
      if(keyCode == e.VK_DOWN) {
        setYDirection(5);
      }
      if(keyCode == e.VK_LEFT) {
        setXDirection(-5);
      }
      if(keyCode == e.VK_RIGHT) {
        setXDirection(5);
      }
    }
    public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();
      if(keyCode == e.VK_UP) {
        setYDirection(0);
      }
      if(keyCode == e.VK_DOWN) {
        setYDirection(0);
      }
      if(keyCode == e.VK_LEFT) {
        setXDirection(0);
      }
      if(keyCode == e.VK_RIGHT) {
        setXDirection(0);
      }
    }
  }

  public void setXDirection(int newX) {
    player.dx = newX;
  }

  public void setYDirection(int newY) {
    player.dy = newY;
  }

  public void run() {
    try {
      while(true) {
        move();

        Thread.sleep(20);
      }
    } catch(Exception e) {
      System.out.println("ERROR");
    }
  }

  public void move() {

    player.x += player.dx;
    player.y += player.dy;

    for(int i=0; i < rectangles.length; i++) {
      r1 = rectangles[i];

      r1.x += r1.dx;
      r1.y += r1.dy;

      if(r1.x >= SCREEN_WIDTH - r1.width) {
        r1.x = SCREEN_WIDTH - r1.width;
        r1.dx = -1 * r1.dx;
      } else if (r1.x <= 0) {
        r1.x = 0;
        r1.dx = -1 * r1.dx;
      }

      if(r1. y >= SCREEN_HEIGHT - r1.height) {
        r1.y = SCREEN_HEIGHT - r1.height;
        r1.dy = -1 * r1.dy;
      } else if (r1.y <= 0) {
        r1.y = 0;
        r1.dy = -1 * r1.dy;
      }
    }



    //Collision detection
    quad.clear();
    for(int i=0; i < rectangles.length; i++) {
      quad.insert(rectangles[i]);
    }

    newObjects.clear();
    newObjects = quad.retrieve(newObjects, player);

    for(int j=0; j < newObjects.size(); j++) {
      MyRectangle r5 = newObjects.get(j);
      if(r5.intersects(player)) {
        r5.color = Color.red;
      } else {
        r5.color = Color.blue;
      }
    }

  }

  public void paint(Graphics g) {
    dbImage = createImage(getWidth(), getHeight());
    dbg = dbImage.getGraphics();
    paintComponent(dbg);
    g.drawImage(dbImage, 0, 0, this);
  }

  public void paintComponent(Graphics g) {

    for(int i=0; i < rectangles.length; i++) {
      r1 = rectangles[i];

      g.setColor(r1.color);
      g.fillRect(r1.x, r1.y, r1.width, r1.height);
    }

    drawNode(quad, g);

    g.setColor(Color.green);
    g.fillRect(player.x, player.y, player.width, player.height);

    repaint();
  }

  public void drawNode(Quadtree node, Graphics g) {
    g.setColor(Color.black);
    g.drawRect(node.getX(), node.getY(), node.getWidth(), node.getHeight() );

    Quadtree[] nodes = node.nodes;
    int length = nodes.length;

    if(nodes != null && length != 0) {
      for(int i=0; i < length; i++) {
        if(nodes[i] != null)
          drawNode(nodes[i], g);
      }
    }
  }

  public static void main(String[] args) {
    TestQuadTree game = new TestQuadTree();

    Thread t1 = new Thread(game);
    t1.start();
  }
}
