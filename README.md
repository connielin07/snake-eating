# Snake Eating

Snake Eating is a Java-based Snake game project developed as a programming practice project.  
The game is based on the classic Snake gameplay and adds different custom items to make the game more challenging and interactive.

「Snake Eating」是一個以 Java 製作的貪食蛇遊戲專案。此專案以經典貪食蛇玩法為基礎，加入不同道具與分數機制，練習 Java 物件導向設計、鍵盤事件處理、遊戲邏輯與碰撞判斷。

## Project Purpose

This project was created to practice Java programming through a small desktop game.  
Instead of only writing basic console programs, this project helped me understand how object-oriented programming can be used to manage game objects, player movement, scoring rules, and item effects.

## Features

-   Classic Snake-style gameplay
    
-   Keyboard control for snake movement
    
-   Score tracking
    
-   Different food and item types
    
-   Collision detection
    
-   Game-over condition handling
    
-   Packaged `.jar` file for running the game
    
-   Object-oriented structure for snake, nodes, fruits, and items
    

## Tech Stack

-   Java
    
-   Java GUI programming
    
-   Object-Oriented Programming
    
-   Keyboard event handling
    
-   Basic game loop logic
    

## Project Structure

```text
snake-eating/
├── src/
│   ├── Main.java
│   ├── Snake.java
│   ├── Node.java
│   ├── Fruit.java
│   ├── Item.java
│   ├── apple.png
│   ├── bomb.png
│   ├── poison.png
│   ├── shoes.png
│   └── strawberry.png
├── dist/
│   └── snake_eating.jar
├── snake_eating_score.txt
└── README.md
```

## Main Components

### `Main.java`

The main entry point of the game.  
It initializes the game window, starts the game process, and connects the main game components.

### `Snake.java`

Controls the snake’s movement, body structure, growth, and collision-related behavior.

### `Node.java`

Represents each unit of the snake’s body.  
This helps separate the snake structure into smaller reusable objects.

### `Fruit.java`

Represents food objects in the game.  
When the snake eats fruit, the score or snake length may change.

### `Item.java`

Represents special items that make the game more challenging.  
Different item images such as bombs, poison, shoes, apples, and strawberries are used in the game.

## How to Run

### Option 1: Run the JAR file

If Java is installed on your computer, you can run the packaged JAR file:

```bash
java -jar dist/snake_eating.jar
```

### Option 2: Run from source code

1.  Clone this repository:
    

```bash
git clone https://github.com/connielin07/snake-eating.git
```

2.  Open the project in a Java IDE such as IntelliJ IDEA.
    
3.  Run `Main.java`.
    

## What I Learned

Through this project, I practiced how to turn basic Java syntax into an interactive desktop game.  
I learned how to design classes for different game objects, handle keyboard input, update the game state, detect collisions, and organize image assets inside a Java project.

This project also helped me understand that game development requires both programming logic and user interaction design. Even a simple Snake game involves many important programming concepts, such as object-oriented structure, event handling, state updates, and error checking.

## Notes

This project is a first-year Java programming practice project.  
It is mainly intended to demonstrate Java fundamentals, object-oriented design, and basic game logic implementation.
