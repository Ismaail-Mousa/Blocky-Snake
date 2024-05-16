import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Game extends JPanel implements ActionListener, KeyListener{
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    int boardHeight;
    int boardWidth;
    int tileSize = 25;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    ArrayList<Tile> rBlocks;

    Tile food;
    Tile rFood;
    Color myColor = new Color(200, 100, 90);
    Color snakeH = new Color( 100, 165, 52);

    Random random;

    Timer gameLoop;

    int velocityX;
    int velocityY;
    boolean gameOver = false;

    Game(int width, int height) {
        boardWidth = width;
        boardHeight = height;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(myColor);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        food = new Tile(10, 10);
        rFood = new Tile(10, 10);

        rBlocks = new ArrayList<Tile>();

        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        //for (int i = 0; i < boardWidth/tileSize; i++) {
        //    g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
        //    g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        //}

        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);
        
        g.setColor(Color.orange);
        g.fill3DRect(rFood.x * tileSize, rFood.y * tileSize, tileSize, tileSize, true);
        g.setColor(snakeH);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.setColor(Color.green);
            g.fill3DRect(snakePart.x *tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }
        g.setFont(new Font("Arial", Font.PLAIN, 16));


        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }

        for (int i = 0; i < rBlocks.size(); i++) {
            g.setColor(Color.blue);
            g.fill3DRect(rBlocks.get(i).x*tileSize, rBlocks.get(i).y * tileSize, tileSize, tileSize, true);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);

        int rX = random.nextInt(boardWidth/tileSize);
        int rY = random.nextInt(boardWidth/tileSize);

        rFood.x = rX;
        rFood.y = rY;
    }

    public void placeBlock() {
        rBlocks.add(new Tile(10, 10));
        for (int i = 0; i < rBlocks.size(); i++) {
            int randomX = random.nextInt(boardWidth/tileSize);
            int randomY = random.nextInt(boardWidth/tileSize);
            if (food.x == randomX && food.y == randomY) {
                rBlocks.get(i).x = randomX + 5;
                rBlocks.get(i).y = randomY - 5;
            } else {
                rBlocks.get(i).x = randomX;
                rBlocks.get(i).y = randomY;
            }
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void move() {

        for (int i = 0; i < snakeBody.size(); i++) {
            if (collision(snakeHead, rBlocks.get(i))) {
                gameOver = true;
            }
        }
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
            placeBlock();
        }

        if (collision(snakeHead, rFood)) {
            if (snakeBody.size() > 0) {
                snakeBody.remove(snakeBody.size() - 1);
                placeFood();
            }
        }
        //for (int i = 0; i <= snakeBody.size(); i++) {
        //    placeBlock();
        //}
        if (snakeHead.x > 23) {
            gameOver = true;
        }
        if (snakeHead.x < 0) {
            gameOver = true;
        }
        if (snakeHead.y > 23) {
            gameOver = true;
        }
        if (snakeHead.y < 0) {
            gameOver = true;
        }

        for (int i = snakeBody.size() -1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;

            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
            if (collision(snakePart, rBlocks.get(i))) {
                for (int c = 0; c < snakeBody.size() - i + 1; c++) {
                    snakeBody.remove(i);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }
}
