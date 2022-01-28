
package pacman;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import entit�.Fantome;
import entit�.Pacman;

public class Jeu extends JPanel implements ActionListener {

	private Affichage a; // Instance de la classe g�rant l'affichage
	private boolean touche; // Bool�en pour v�rifier si Pacman est touch�
	private boolean enJeu = false; // Bool�en pour savoir si la partie est commenc�e

	private final int TAILLE_BLOC = 24; // Taille en pixel d'une case du tableau
	private final int BLOCSH = 28; // Nombre de blocs/cases � l'horizontale
	private final int BLOCSV = 31; // Nombre de blocs/cases � la verticale
	private final int TAILLE_ECRANH = BLOCSH * TAILLE_BLOC; // Taille en pixels du labyrinthe � l'horizontal
	private final int TAILLE_ECRANV = BLOCSV * TAILLE_BLOC; // Taille en pixels du labyrinthe � la verticale
	private final int VITESSE_PACMAN = 6; // Vitesse de pacman
	private final int VITESSE_FANTOMES = 6;// Vitesse des fantomes
	private boolean fini; // Bool�en pour v�rifier si le jeu est termin�

	private int FANTOMES = 4;// Nombre de fant�mes
	private int vies, score;// Variables d�terminant le nombre de vies de Pacman et son score
	private boolean[] enVie = { false, false, false, false }; // Bool�en pour savoir si le fant�me est sorti de son
																// spawn

	private Timer timer; // Taux de rafraichissement
	private long temps = 0; // Temps pass� dans la partie
	public static long debut = 0;// Temps de reference pour le calcul du temps ecoul�

	private static long bonusI;// Debut du bonus invisible
	private static long bonusG;// Debut du bonus invisible
	private static long tempI;// Temps ecoul� avec le bonus invisible
	private static long tempG;// Temps ecoul� avec le bonus invincible

	private boolean pause = false;// Boolean afin de savoir si le jeu est en pause
	private boolean mort;// Boolean pour verifier si le joueur est mort
	private boolean OneUp;// Verifie si les conditions d'obtention de vie sont remplises
	private boolean invisible;// Verifie si le joueur est invisible
	private boolean invincible;// Verifie si le joueur est invincible
	private int inputx = 0, inputy = 0;// Direction entr�e par le joueur via le clavier

	private Labyrinthe l = new Labyrinthe();// Instance regroupant les donn�es li�es a la carte
	private Pacman pacman = new Pacman();// Instance disposant des coordonn�es du joueur
	private Fantome[] fantomes;// tableau de fantomes: chaque fantome regroupe les coordon�es du fantomes
								// correspondant

	public boolean isFini() {
		return fini;
	}

	public int getFANTOMES() {
		return FANTOMES;
	}

	public int getVies() {
		return vies;
	}

	public void setVies(int vies) {
		this.vies = vies;
	}

	public int getScore() {
		return score;
	}

	public long getTemps() {
		return temps;
	}

	public void setTemps(long temps) {
		this.temps = temps;
	}

	public static long getDebut() {
		return debut;
	}

	public static long getBonusI() {
		return bonusI;
	}

	public static long getBonusG() {
		return bonusG;
	}

	public boolean isPause() {
		return pause;
	}

	public boolean isMort() {
		return mort;
	}

	public boolean isOneUp() {
		return OneUp;
	}

	public void setOneUp(boolean oneUp) {
		OneUp = oneUp;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public boolean isInvincible() {
		return invincible;
	}

	public Pacman getPacman() {
		return pacman;
	}

	public Fantome[] getFantomes() {
		return fantomes;
	}

	public Jeu() {
		timer = new Timer(40, this);
		timer.start();
		Initialisation();
	}

	private void Initialisation() {
		initVariables();
		initBoard();
		initGame();
	}

	private void initBoard()// Initialise la carte
	{
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.black);
		for (int i = 0; i < l.getMap1().length; i++) {
			for (int j = 0; j < l.getMap1()[i].length; j++) {
				l.setLabyrinthe(i, j, l.getMap1()[i][j]);
			}
		}
	}

	private void initVariables()// Initialise les differentes variables
	{
		fantomes = new Fantome[FANTOMES];
		for (int i = 0; i < FANTOMES; i++) {
			fantomes[i] = new Fantome();
		}
		vies = 3;
		score = 0;
		fini = false;
		mort = false;
		OneUp = true;
		invisible = false;
		invincible = false;
		touche = false;
		bonusI = 0;
		bonusG = 0;
		a = new Affichage(TAILLE_BLOC, BLOCSH, BLOCSV, l, this);
	}

	private void initGame()// Mets en place les conditions de depart des entit�s
	{
		l.changement_labyrinthe(l.getLabyrinthe(), l.getMap1());
		for (int i = 0; i < l.getLabyrinthe().length; i++) {
			for (int j = 0; j < l.getLabyrinthe()[i].length; j++) {
				if (l.getLabyrinthe()[i][j] == 'P') {
					pacman.setX(j * TAILLE_BLOC);
					pacman.setY(i * TAILLE_BLOC);
				}
				if (l.getLabyrinthe()[i][j] == 'F') {
					for (int k = 0; k < FANTOMES; k++) {
						fantomes[k].setY(j * TAILLE_BLOC);
						fantomes[k].setX(i * TAILLE_BLOC);
						fantomes[k].setDirx(0);
						fantomes[k].setDiry(-1);
						enVie[k] = false;
					}
				}
			}
		}
	}

	void deplacementPacman() // Gestion du deplacement de pacman
	{
		if (inputx == -pacman.getDirx() && inputy == -pacman.getDiry()) // Rend le changement de direction plus fluide
																		// pour la direction oppos�e
		{
			pacman.setDirx(inputx);
			pacman.setDiry(inputy);
		}
		
		
		if (inputx == 1 && pacman.getX() >= 642) {
			pacman.setX(0);
		}
		if (inputx == -1 && pacman.getX() <= 6) {
			pacman.setX(l.getLabyrinthe()[0].length * (TAILLE_BLOC) - VITESSE_PACMAN);
		}
		if (inputy == 1 && pacman.getY() >= 714) {
			pacman.setY(0);
		}
		if (inputy == -1 && pacman.getY() <= 6) {
			pacman.setY(l.getLabyrinthe().length * (TAILLE_BLOC) - VITESSE_PACMAN);
		}
		
		
		if (pacman.getX() % TAILLE_BLOC == 0 && pacman.getY() % TAILLE_BLOC == 0) {

			int posx = pacman.getX() / TAILLE_BLOC;
			int posy = pacman.getY() / TAILLE_BLOC;
			if ((inputx == 1 && (l.getLabyrinthe()[posy][posx + 1] == 'M' || l.getLabyrinthe()[posy][posx + 1] == 'B'))
					|| (inputx == -1
							&& (l.getLabyrinthe()[posy][posx - 1] == 'M' || l.getLabyrinthe()[posy][posx - 1] == 'B'))
					|| (inputy == 1
							&& (l.getLabyrinthe()[posy + 1][posx] == 'M' || l.getLabyrinthe()[posy + 1][posx] == 'B'))
					|| (inputy == -1 && (l.getLabyrinthe()[posy - 1][posx] == 'M'
							|| l.getLabyrinthe()[posy - 1][posx] == 'B'))) {
				pacman.setDirx(0);
				pacman.setDiry(0);
			} else {
				pacman.setDirx(inputx);
				pacman.setDiry(inputy);
			}

			char c = l.getLabyrinthe()[posy][posx];

			if (c != 'V' && c != 'S' && c != 'P') {
				if (c == 'g') {
					score = score + 100;
				}
				if (c == 'G') {
					score = score + 500;
					invincible = true;
					bonusG = System.currentTimeMillis();
				}
				if (c == 'I') {
					score = score + 300;
					invisible = true;
					bonusI = System.currentTimeMillis();
				}
				if (c == 'A') {
					score = score + 1000;
					l.changement_labyrinthe(l.getLabyrinthe(), l.getMap2());
				}
				l.getLabyrinthe()[posy][posx] = 'V';

			}
		}

		pacman.setX(pacman.getX() + VITESSE_PACMAN * pacman.getDirx());
		pacman.setY(pacman.getY() + VITESSE_PACMAN * pacman.getDiry());
	}

	void deplacementFantomes(Graphics g) // gestion de deplacement des fantomes
	{

		int posx;
		int posy;
		Random rand = new Random();

		for (int i = 0; i < FANTOMES; i++) {
			if (fantomes[i].getDirx() == 1 && fantomes[i].getX() >= 642) {
				fantomes[i].setX(0);
			}
			if (fantomes[i].getDirx() == -1 && fantomes[i].getX() <= 6) {
				fantomes[i].setX(l.getLabyrinthe()[0].length * (TAILLE_BLOC) - VITESSE_FANTOMES);
			}
			if (fantomes[i].getDiry() == 1 && fantomes[i].getY() >= 714) {
				fantomes[i].setY(0);
			}
			if (fantomes[i].getDiry() == -1 && fantomes[i].getY() <= 6) {
				fantomes[i].setY(l.getLabyrinthe().length * (TAILLE_BLOC) - VITESSE_FANTOMES);
			}
			
			
			if (fantomes[i].getX() % TAILLE_BLOC == 0 && fantomes[i].getY() % TAILLE_BLOC == 0) {

				posx = fantomes[i].getX() / TAILLE_BLOC;
				posy = fantomes[i].getY() / TAILLE_BLOC;
				if (l.getLabyrinthe()[posy][posx] == 'B') {
					enVie[i] = true;
				}
				while (l.getLabyrinthe()[posy + fantomes[i].getDiry()][posx + fantomes[i].getDirx()] == 'M'
						|| (l.getLabyrinthe()[posy + fantomes[i].getDiry()][posx + fantomes[i].getDirx()] == 'B'
								&& enVie[i] == true)) {
					switch (rand.nextInt(4)) {
					case 0:
						fantomes[i].setDirx(-1);
						fantomes[i].setDiry(0);
						break;
					case 1:
						fantomes[i].setDirx(0);
						fantomes[i].setDiry(1);
						;
						break;
					case 2:
						fantomes[i].setDirx(0);
						fantomes[i].setDiry(-1);
						break;
					case 3:
						fantomes[i].setDirx(1);
						fantomes[i].setDiry(0);
						break;
					}
				}
			}
			
			if (invincible || fantomes[i].getX() % TAILLE_BLOC == (VITESSE_FANTOMES / 2)
					|| fantomes[i].getY() % TAILLE_BLOC == (VITESSE_FANTOMES / 2)
					|| fantomes[i].getY() % TAILLE_BLOC == TAILLE_BLOC - (VITESSE_FANTOMES / 2)
					|| fantomes[i].getX() % TAILLE_BLOC == TAILLE_BLOC - (VITESSE_FANTOMES / 2)) {
				fantomes[i].setX(fantomes[i].getX() + fantomes[i].getDirx() * (VITESSE_FANTOMES / 2));
				fantomes[i].setY(fantomes[i].getY() + fantomes[i].getDiry() * (VITESSE_FANTOMES / 2));
			} else {
				fantomes[i].setX(fantomes[i].getX() + fantomes[i].getDirx() * VITESSE_FANTOMES);
				fantomes[i].setY(fantomes[i].getY() + fantomes[i].getDiry() * VITESSE_FANTOMES);
			}
			if (pacman.getX() > (fantomes[i].getX() - 20) && pacman.getX() < (fantomes[i].getX() + 20)
					&& pacman.getY() > (fantomes[i].getY() - 20) && pacman.getY() < (fantomes[i].getY() + 20)
					&& enJeu) {
				if (invincible) {
					enVie[i] = false;
					for (int k = 0; k < l.getLabyrinthe().length; k++) {
						for (int j = 0; j < l.getLabyrinthe()[i].length; j++) {
							if (l.getLabyrinthe()[k][j] == 'F') {
								fantomes[i].setX(j * TAILLE_BLOC);
								fantomes[i].setY(k * TAILLE_BLOC);
								fantomes[i].setDirx(0);
								fantomes[i].setDiry(-1);
							}
						}
					}
				} else if (invisible) {
				} else {
					touche = true;
				}
			}
		}
	}

	void touche() // S'effectue quand pacman entre en collision avec un fantome
	{
		touche = false;
		vies--;
		for (int i = 0; i < l.getLabyrinthe().length; i++) {

			for (int j = 0; j < l.getLabyrinthe()[i].length; j++) {
				if (l.getLabyrinthe()[i][j] == 'P') {
					pacman.setX(j * TAILLE_BLOC);
					pacman.setY(i * TAILLE_BLOC);
				}
			}
		}
		invisible = true;
		bonusI = System.currentTimeMillis() - 5 * 1000; // 2 secondes d'invisibilit� lorsqu'on perd une vie
		if (vies == 0) {
			mort = true;
		}
	}

	void verifications() // Verifie que les timers de bonus sont ecoul�s et qu'il reste des gommes sur la
							// carte
	{
		fini = true;
		if ((System.currentTimeMillis() - bonusG) / 1000 == 5) {
			invincible = false;
		}
		if ((System.currentTimeMillis() - bonusI) / 1000 == 7) {
			invisible = false;
		}
		for (int i = 0; i < l.getLabyrinthe().length; i++) {
			for (int j = 0; j < l.getLabyrinthe()[i].length; j++) {
				if (l.getLabyrinthe()[i][j] == 'G' || l.getLabyrinthe()[i][j] == 'g' || l.getLabyrinthe()[i][j] == 'A'
						|| l.getLabyrinthe()[i][j] == 'I') {
					fini = false;
				}
			}
		}
		if (fini || mort) {
			enJeu = false;
			fini = true;
		}
	}

	private void playGame(Graphics g) // Gere les mechaniques de jeu dynamique, mouvementet affiche pacman et fantomes
	{
		if (touche) {
			touche();
			a.drawPacman(g);
			a.drawGhost(g);
		} else {
			deplacementPacman();
			a.drawPacman(g);
			deplacementFantomes(g);
			a.drawGhost(g);
			verifications();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	private void doDrawing(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, TAILLE_ECRANH, TAILLE_ECRANV);
		a.drawMaze(g);
		if (enJeu) {
			playGame(g);
		} else {
			a.EcranTexte(g);
		}
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	class TAdapter extends KeyAdapter // Detecte les touches du clavier
	{
		@Override
		public void keyPressed(KeyEvent e) // Donne la touche du clavier press�e et la convertie en orientation in-game
		{
			int key = e.getKeyCode();
			if (enJeu) {
				if (key == 'q' || key == 'Q' || key == KeyEvent.VK_LEFT) {
					inputx = -1;
					inputy = 0;
				} else if (key == 'd' || key == 'D' || key == KeyEvent.VK_RIGHT) {
					inputx = 1;
					inputy = 0;
				} else if (key == 'z' || key == 'Z' || key == KeyEvent.VK_UP) {
					inputx = 0;
					inputy = -1;
				} else if (key == 's' || key == 'S' || key == KeyEvent.VK_DOWN) {
					inputx = 0;
					inputy = 1;
				} else if ((key == KeyEvent.VK_ESCAPE) && timer.isRunning()) {
					enJeu = false;
					debut = 0;
					Initialisation();

				} else if (key == 'p' || key == 'P') {
					pause = true;
					tempG = System.currentTimeMillis() - bonusG;
					tempI = System.currentTimeMillis() - (bonusI);
					enJeu = false;
				}
			} else {
				if ((key == 'r' || key == 'R') && enJeu == false && pause == false) {
					enJeu = true;
					pause = false;
					debut = System.currentTimeMillis();
					Initialisation();
				}
				if ((key == 'u' || key == 'U') && enJeu == false && pause) {
					enJeu = true;
					pause = false;
					bonusG = System.currentTimeMillis() - tempG;
					bonusI = System.currentTimeMillis() - tempI;
					debut = System.currentTimeMillis() - (temps * 1000);

				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

}
