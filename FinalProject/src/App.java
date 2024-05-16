import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = boardWidth;
        JFrame frame = new JFrame("Obstacle Snake! v1.1");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Game g = new Game(boardWidth, boardHeight);
        frame.add(g);
        frame.pack();
        g.requestFocus();
    }
}
