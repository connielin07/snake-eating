import java.awt.*;
import java.util.ArrayList;

public class Snake {
    private ArrayList<Node> snakeBody;

    public Snake(){
        snakeBody = new ArrayList<>();
        snakeBody.add(new Node(80,0));
        snakeBody.add(new Node(60,0));
        snakeBody.add(new Node(40,0));
        snakeBody.add(new Node(20,0));
    }

    public ArrayList<Node> getSnakeBody(){
        return snakeBody;
    }

    public void drawSnake(Graphics g, Color bodyColor) {
        for (int i = 0; i < snakeBody.size(); i++) {
            Node n = snakeBody.get(i);
            if (n == null) continue; // 防止 NullPointerException

            System.out.println("Segment " + i + " - x: " + n.x + ", y: " + n.y); // ⬅ 印出座標除錯

            if (i == 0) {
                g.setColor(Color.ORANGE); // 頭部顏色
            } else {
                g.setColor(bodyColor);    // 身體顏色
            }
            g.fillOval(n.x, n.y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }
}
