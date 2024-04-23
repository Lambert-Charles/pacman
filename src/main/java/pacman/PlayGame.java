package pacman;

import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class PlayGame extends JFrame {
 
	public static void main(String[] args) {
		PlayGame game = new PlayGame();
		game.initUI();
		EventQueue.invokeLater(() ->
				game.setVisible(true));
	}
	
	private void initUI() {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		Board board = new Board();
		add(board);
		setTitle("Pacman");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(423,468);
	}

}
