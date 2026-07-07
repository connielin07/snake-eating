import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Item {
    public enum ItemType { APPLE, POISON, BOMB, SHOES }

    private int x, y;
    private ItemType type;
    private ImageIcon icon;

    public Item(ItemType type) {
        this.type = type;
        loadIcon();
        resetPosition(); // 預設無參數初始化，之後可再呼叫有參數版本確保不重疊
    }

    private void loadIcon() {
        try {
            switch (type) {
                case APPLE:
                    icon = new ImageIcon(getClass().getResource("apple.png"));
                    break;
                case POISON:
                    icon = new ImageIcon(getClass().getResource("poison.png"));
                    break;
                case BOMB:
                    icon = new ImageIcon(getClass().getResource("bomb.png"));
                    break;
                case SHOES:
                    icon = new ImageIcon(getClass().getResource("shoes.png"));
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error loading icon for " + type + ": " + e.getMessage());
        }
    }

    // 預設位置：無檢查
    public void resetPosition() {
        if (Main.column == 0 || Main.row == 0) return;
        Random rand = new Random();
        x = rand.nextInt(Main.column) * Main.CELL_SIZE;
        y = rand.nextInt(Main.row) * Main.CELL_SIZE;
    }

    // 確保不與蛇與水果重疊
    public void resetPosition(Snake snake, Fruit fruit) {
        resetPosition(snake, fruit, new ArrayList<>());
    }

    // 重疊檢查蛇、fruit、其他 item
    public void resetPosition(Snake snake, Fruit fruit, ArrayList<Item> existingItems) {
        Random rand = new Random();
        int newX, newY;
        boolean overlap;

        do {
            newX = rand.nextInt(Main.column) * Main.CELL_SIZE;
            newY = rand.nextInt(Main.row) * Main.CELL_SIZE;
            overlap = false;

            // 與蛇重疊
            for (Node node : snake.getSnakeBody()) {
                if (node.x == newX && node.y == newY) {
                    overlap = true;
                    break;
                }
            }

            // 與水果重疊
            for (Node fruitNode : fruit.getFruits()) {
                if (fruitNode.x == newX && fruitNode.y == newY) {
                    overlap = true;
                    break;
                }
            }

            // 與其他 item 重疊
            for (Item other : existingItems) {
                if (other.getX() == newX && other.getY() == newY) {
                    overlap = true;
                    break;
                }
            }

        } while (overlap);

        this.x = newX;
        this.y = newY;
    }

    public void draw(Graphics g) {
        if (icon != null) {
            icon.paintIcon(null, g, x, y);
        } else {
            g.setColor(Color.GRAY); // fallback 顏色
            g.fillOval(x, y, Main.CELL_SIZE, Main.CELL_SIZE);
        }
    }

    public ItemType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
