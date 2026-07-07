import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Fruit {
    private ImageIcon img;
    private ArrayList<Node> positions;
    private Random rand = new Random();

    public Fruit(int count, Snake snake, ArrayList<Item> items) {
        img = new ImageIcon(getClass().getResource("strawberry.png")); // 可改圖
        positions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            addNewFruit(snake, items);
        }
    }

    public void addNewFruit(Snake snake, ArrayList<Item> items) {
        int x, y;
        boolean overlap;

        if (Main.column <= 0 || Main.row <= 0) return;

        // 複製外部資料避免 ConcurrentModificationException
        ArrayList<Node> snakeBody = new ArrayList<>(snake.getSnakeBody());
        ArrayList<Item> itemCopy = items != null ? new ArrayList<>(items) : new ArrayList<>();


        do {
            x = rand.nextInt(Main.column) * Main.CELL_SIZE;
            y = rand.nextInt(Main.row) * Main.CELL_SIZE;
            overlap = false;

            // 與蛇身重疊檢查
            for (Node s : snakeBody) {
                if (s.x == x && s.y == y) {
                    overlap = true;
                    break;
                }
            }

            // 與其他水果重疊
            for (Node f : positions) {
                if (f.x == x && f.y == y) {
                    overlap = true;
                    break;
                }
            }

            // 與 item 重疊檢查
            for (Item item : itemCopy) {
                if (item.getX() == x && item.getY() == y) {
                    overlap = true;
                    break;
                }
            }

        } while (overlap);

        positions.add(new Node(x, y));
    }

    public void draw(Graphics g) {
        for (Node f : positions) {
            img.paintIcon(null, g, f.x, f.y);
        }
    }

    // 若吃到水果，移除該水果並補上一個新位置
    public boolean checkAndReplaceIfEaten(int x, int y, Snake snake, ArrayList<Item> items) {
        for (int i = 0; i < positions.size(); i++) {
            Node f = positions.get(i);
            if (f.x == x && f.y == y) {
                positions.remove(i);
                addNewFruit(snake, items);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Node> getFruits() {
        return positions;
    }
}
