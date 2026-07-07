import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;
import java.util.Timer;

public class Main extends JPanel implements KeyListener {
    public static final int CELL_SIZE = 20;
    public static int width = 400;
    public static int height = 400;
    public static int row = height / CELL_SIZE;
    public static int column = width / CELL_SIZE;

    private Snake snake;
    private Fruit fruit;
    private ArrayList<Item> items;
    private Timer gameTimer;
    private Timer effectTimer;
    private static String direction;
    private boolean allowKeyPress;
    private int score;
    private int highest_score;
    private int timeLeft = 180; // 600 seconds
    private long messageTime = 0; // 記錄訊息顯示時間
    private final long MESSAGE_DURATION = 1500; // 設定氣泡框顯示時間為 1.5 秒 (1500 毫秒)
    private Color snakeColor = Color.GREEN;
    private String snakeMessage = "Let's go!";
    private int tickSpeed = 200;
    private String myFile = "snake_eating_score.txt";
    private Random rand = new Random();
    private String[] messages = {"I'm getting longer!", "Delicious!", "Watch out!"};

    public Main() {
        read_highest_score();
        reset();
        addKeyListener(this);
    }

    private void reset() {
        score = 0;
        timeLeft = 180;
        tickSpeed = 200;
        snakeColor = Color.GREEN;
        direction = "right";
        allowKeyPress = true;
        snakeMessage = messages[rand.nextInt(messages.length)];

        if (snake != null) snake.getSnakeBody().clear();
        snake = new Snake();

        items = new ArrayList<>(); // 初始化 item 列表（暫時為空，之後填入）

        // 初始化 fruit 並傳入空 items
        fruit = new Fruit(3, snake, items);  // fruit constructor 會參考空的 items 清單避免重疊

        // 接著再填入 items，並每個 item 建立時參考已生成的 fruit 座標
        for (int i = 0; i < 5; i++) {
            Item apple = new Item(Item.ItemType.APPLE);
            apple.resetPosition(snake, fruit, items);
            items.add(apple);
        }
        for (int i = 0; i < 10; i++) {
        Item poison = new Item(Item.ItemType.POISON);
        poison.resetPosition(snake, fruit, items);
        items.add(poison);
        }
        for (int i = 0; i < 20; i++) {
        Item bomb = new Item(Item.ItemType.BOMB);
        bomb.resetPosition(snake, fruit, items);
        items.add(bomb);
        }

            Item shoes = new Item(Item.ItemType.SHOES);
            shoes.resetPosition(snake, fruit, items);
            items.add(shoes);


        setTimers();
    }

    private void setTimers() {
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
                repaint();
            }
        }, 0, tickSpeed);

        effectTimer = new Timer();
        effectTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeLeft--;
                //每 10 秒減少一節蛇尾（如果蛇身還有）
                if (snake.getSnakeBody().size() > 0 && timeLeft % 10 == 0) {
                    snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
                }
                //如果蛇身變空或時間歸零，就遊戲結束
                if (snake.getSnakeBody().isEmpty() || timeLeft <= 0) {
                    gameOver();
                }
            }
        }, 1000, 1000);
    }

    private void tick() {
        if (messageTime > 0 && System.currentTimeMillis() - messageTime > MESSAGE_DURATION) {
            snakeMessage = "";
            messageTime = 0;
        }

        ArrayList<Node> snakeBody = snake.getSnakeBody();
        Node head = snakeBody.get(0);

        int newX = head.x;
        int newY = head.y;

        switch (direction) {
            case "right": newX += CELL_SIZE; break;
            case "left": newX -= CELL_SIZE; break;
            case "up": newY -= CELL_SIZE; break;
            case "down": newY += CELL_SIZE; break;
        }

        // 確保 snakeBody 中不會有 null 值
        if (snakeBody.contains(null)) {
            System.out.println("Detected null in snake body");
            snakeBody.removeIf(Objects::isNull); // 移除所有 null 元素
        }
        //撞牆則結束
        if (newX < 0 || newY < 0 || newX >= width || newY >= height) {
            gameOver();
            return;
        }

        // 自撞檢查
        for (int i = 0; i < snakeBody.size(); i++) {
            if (newX == snakeBody.get(i).x && newY == snakeBody.get(i).y) {
                gameOver();
                return;
            }
        }

        boolean grew = false;

        if (fruit.checkAndReplaceIfEaten(newX, newY, snake, items)) {
            score++;
            snakeColor = new Color(rand.nextInt(256),
                    rand.nextInt(256), rand.nextInt(256)); //產生新隨機顏色並指定為目前蛇身顏色
            snakeMessage = messages[rand.nextInt(messages.length)]; //顯示一段隨機訊息
            messageTime = System.currentTimeMillis(); //控制訊息顯示時間
            grew = true;
        }
        if (!grew) {
            snakeBody.remove(snakeBody.size() - 1);
        }


        for (Item item : items) {
            if (newX == item.getX() && newY == item.getY()) {
                switch (item.getType()) {
                    case APPLE:
                        item.resetPosition(snake, fruit);
                        score++;
                        snakeMessage = "+1!";
                        messageTime = System.currentTimeMillis();
                        break;
                    case POISON:
                        //蛇身長度大於 1 才能扣一格蛇尾
                        if (snakeBody.size() > 1) snakeBody.remove(snakeBody.size() - 1);
                        item.resetPosition(snake, fruit);
                        snakeMessage = "Ouch!";
                        messageTime = System.currentTimeMillis();
                        break;
                    case BOMB:
                        gameOver();
                        return;
                    case SHOES:
                        tickSpeed = Math.max(50, tickSpeed - 50); //遊戲加速（最低為 50ms）
                        resetGameTimer(); //重設 timer 套用新速度
                        item.resetPosition(snake, fruit);
                        snakeMessage = "Zoom!";
                        messageTime = System.currentTimeMillis();
                        break;
                }
            }
        }

        // 加入新的蛇頭
        Node newHead = new Node(newX, newY);
        snakeBody.add(0, newHead);

        if (snakeBody.size() > 5) {
            int newSpeed = Math.max(50, 200 - Math.min((snakeBody.size() - 4) * 10, 150));
            if (newSpeed != tickSpeed) {
                tickSpeed = newSpeed;
                resetGameTimer(); // 只在速度真的變化時才重設
            }
        }
        allowKeyPress = true;
    }

    private void resetGameTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
                repaint();
            }
        }, 0, tickSpeed);
    }

    private void gameOver() {
        gameTimer.cancel();
        effectTimer.cancel();
        write_a_file(score);
        int response = JOptionPane.showOptionDialog(this, "Game Over! Score: " + score +
                        "\nHighest Score: " + Math.max(score, highest_score) +
                        "\nTime Left: " + timeLeft +
                        "\nTry again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, null, JOptionPane.YES_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            reset();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        snake.drawSnake(g, snakeColor);
        fruit.draw(g);
        for (Item item : items) item.draw(g);

        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Time: " + timeLeft + "s", 10, 40);

        if (!snakeMessage.isEmpty()) { //如果目前有訊息內容（非空字串）才顯示泡泡
            //設定氣泡格式與計算氣泡出現位置
            g.setColor(new Color(255, 255, 255, 200));
            int bubbleWidth = g.getFontMetrics().stringWidth(snakeMessage) + 20;
            int bubbleHeight = 30;
            int bubbleX = snake.getSnakeBody().get(0).x + (CELL_SIZE / 2) - (bubbleWidth / 2);
            int bubbleY = snake.getSnakeBody().get(0).y - 30;
            g.fillRoundRect(bubbleX, bubbleY, bubbleWidth, bubbleHeight, 10, 10);
            //設定文字為黑色並畫出訊息文字
            g.setColor(Color.BLACK);
            g.drawString(snakeMessage, bubbleX + 10, bubbleY + (bubbleHeight / 2) + 5);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Snake Enhanced");
        Main panel = new Main();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(panel);
        window.pack();
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!allowKeyPress) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                System.out.println("Direction changed to LEFT");
                if (!direction.equals("right")) {
                    direction = "left";
                }
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("Direction changed to RIGHT");
                if (!direction.equals("left")) {
                    direction = "right";
                }
                break;
            case KeyEvent.VK_UP:
                System.out.println("Direction changed to UP");
                if (!direction.equals("down")) {
                    direction = "up";
                }
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("Direction changed to DOWN");
                if (!direction.equals("up")) {
                    direction = "down";
                }
                break;
        }

        allowKeyPress = false;
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) { allowKeyPress = true; }

    private void read_highest_score() {
        try (Scanner reader = new Scanner(new File(myFile))) {
            highest_score = reader.nextInt();
        } catch (Exception e) {
            highest_score = 0;
            try (FileWriter writer = new FileWriter(myFile)) {
                writer.write("0");
            } catch (IOException ignored) {}
        }
    }

    private void write_a_file(int score) {
        if (score > highest_score) {
            try (FileWriter writer = new FileWriter(myFile)) {
                writer.write("" + score);
            } catch (IOException ignored) {}
        }
    }
}
