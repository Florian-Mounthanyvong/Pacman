package pacman;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main extends JFrame {

	public Main() {

		initUI();
	}

	private void initUI() {

		add(new Jeu());

		setTitle("Pacman");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(686, 850);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {

			Main jeu = new Main();
			jeu.setVisible(true);
		});
	}
}
