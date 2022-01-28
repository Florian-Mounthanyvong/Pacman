package pacman;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Affichage extends JPanel {

	private final int TAILLE_BLOC;
	private final int TAILLE_ECRANH;
	private final int TAILLE_ECRANV;
	private Labyrinthe l;
	private Jeu d;

	public Affichage(int TAILLE_BLOC, int BLOCSH, int BLOCSV, Labyrinthe l, Jeu d) {
		this.TAILLE_BLOC = TAILLE_BLOC;
		TAILLE_ECRANH = BLOCSH * TAILLE_BLOC;
		TAILLE_ECRANV = BLOCSV * TAILLE_BLOC;
		this.l = l;
		this.d = d;
	}

	public void drawGhost(Graphics g) // Methode appellée dans playGame dessine les fantomes
	{
		for (int i = 0; i < d.getFANTOMES(); i++) {
			if (i == 2 || d.isInvincible()) {
				g.setColor(new Color(0, 0, 255));
			} else if (i == 1) {
				g.setColor(new Color(0, 255, 0));
			} else if (i == 0) {
				g.setColor(new Color(255, 0, 2));
			} else {
				g.setColor(new Color(255, 0, 200));
			}
			g.fillOval(d.getFantomes()[i].getX() + 1, d.getFantomes()[i].getY() + 1, 20, 20);
		}
	}

	public void drawPacman(Graphics g) // Methode appellée dans playGame dessine pacman
	{
		if (d.isInvincible()) {
			g.setColor(new Color(255, 153, 0));
		} else if (d.isInvisible()) {
			g.setColor(new Color(1f, 1f, 0.5f, 0.7f));
		} else {
			g.setColor(new Color(255, 255, 0));
		}
		int x = d.getPacman().getX() + 3;
		int y = d.getPacman().getY() + 2;
		g.fillOval(x, y, 20, 20);
	}

	void EcranTexte(Graphics g) // pop up lors de pause, de mort ou victoire
	{
		String s;
		if (d.isFini()) {
			if (d.isMort()) {
				s = "GAME OVER! Press r to restart";
			} else {
				s = "YOU WIN! Press r to restart";
			}
		} else {
			if (d.isPause()) {
				s = "PAUSE: Press u to unpause";
				drawPacman(g);
				drawGhost(g);
			} else {
				s = "Press r to start";
			}

		}
		g.setColor(new Color(0, 32, 48));
		g.fillRect(50, TAILLE_ECRANV / 2 - 30, TAILLE_ECRANH - 100, 50);// Intérieur rectangle
		g.setColor(Color.YELLOW);
		g.drawRect(50, TAILLE_ECRANV / 2 - 30, TAILLE_ECRANH - 100, 50);// Bordure
		Font police = new Font("Comic Sans MS", Font.BOLD, 20);// Texte
		FontMetrics metr = this.getFontMetrics(police);
		g.setColor(Color.YELLOW);// Couleur texte
		g.setFont(police);
		g.drawString(s, (TAILLE_ECRANH - metr.stringWidth(s)) / 2, TAILLE_ECRANV / 2);
	}

	void drawMaze(Graphics g) // Dessine le labyrinthe et son contenu
	{
		int i = 0;
		int j = 0;
		for (int y = 0; y < TAILLE_ECRANV; y += TAILLE_BLOC) {
			for (int x = 0; x < TAILLE_ECRANH; x += TAILLE_BLOC) {
				g.setColor(new Color(0, 0, 170));
				if (l.getLabyrinthe()[i][j] == 'M') {
					g.fillRect(x, y, TAILLE_BLOC, TAILLE_BLOC); // Mur bleu
				}
				if (l.getLabyrinthe()[i][j] == 'g') {
					g.setColor(new Color(0, 0, 255)); // Gomme bleue
					g.fillOval(x + 10, y + 10, 6, 6);
				}
				if (l.getLabyrinthe()[i][j] == 'B') {
					g.setColor(new Color(255, 0, 200));// Barrière rose
					g.fillRect(x, y + 7, TAILLE_BLOC, TAILLE_BLOC / 2);
				}
				if (l.getLabyrinthe()[i][j] == 'G') {
					g.setColor(new Color(255, 170, 0)); // Gomme orange
					g.fillOval(x + 5, y + 5, 15, 15);
				}
				if (l.getLabyrinthe()[i][j] == 'I') {
					g.setColor(new Color(200, 0, 200)); // Gomme violette
					g.fillOval(x + 5, y + 5, 15, 15);
				}
				if (l.getLabyrinthe()[i][j] == 'A') {
					g.setColor(new Color(0, 255, 0)); // Gomme verte
					g.fillOval(x + 5, y + 5, 15, 15);
				}
				j++;
			}
			i++;
			j = 0;
		}
		// Affichage score+temps
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		g.setColor(new Color(96, 128, 255));
		g.drawString("Score: " + d.getScore(), TAILLE_ECRANH / 2 + 200, TAILLE_ECRANV + 24);
		if ( d.getScore() >=5000 && d.isOneUp()) {
			d.setVies(d.getVies() + 1);
			d.setOneUp(false);
		}
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		if (d.isPause() == false) {
			if (d.getDebut() == 0) {
				d.setTemps(0);
			} else {
				d.setTemps((System.currentTimeMillis() - d.getDebut()) / 1000);
			}
			g.drawString("Time: " + d.getTemps() + "s", TAILLE_ECRANH / 2 - 100, TAILLE_ECRANV + 24);
			if (d.isInvincible()) {
				g.drawString("Invincibility: " + (5 - (System.currentTimeMillis() - d.getBonusG()) / 1000) + "s",
						TAILLE_ECRANH / 2 - 100, TAILLE_ECRANV + 44);
			} else if (d.isInvisible()) {
				g.drawString("Invisibility: " + (7 - (System.currentTimeMillis() - d.getBonusI()) / 1000) + "s",
						TAILLE_ECRANH / 2 - 100, TAILLE_ECRANV + 44);
			}
		} else {
			g.drawString("Time: " + d.getTemps() + "s", TAILLE_ECRANH / 2 - 100, TAILLE_ECRANV + 24);
		}
		// Affichage vies
		for (int k = 0; k < d.getVies(); k++) {
			g.setColor(new Color(255, 0, 0));
			g.fillOval(k * 28 + 8, TAILLE_ECRANV + 15, 15, 15);
		}
	}

}
