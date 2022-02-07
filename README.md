# Pacman

Le projet consiste en la réalisation d’un jeu s’inspirant du jeu Pacman https://fr.wikipedia.org/wiki/Pac-Man.
Plus précisément, la partie se déroule sur une grille 2D de cases correspondant à un labyrinthe vu de dessus. Le
jeu consiste à déplacer Pacman, un personnage dans un labyrinthe afin de lui faire manger toutes les pacgommes
qui s’y trouvent. Quatre fantômes hantent le labyrinthe et s’y déplacent aléatoirement. Si un des fantômes touche le
pacman alors le pacman perd une des ses trois vies. En plus des pacgommes classique (bleus), il existe aussi quatre
pacgommes bonus (autre couleurs). Ces bonus ont un effet sur le pacman et/ou les fantômes et/ou le labyrinthe.
Le personnage peut emprunter des passages situés de chaque côté de l’écran, produisant un effet de wraparound,
le faisant réapparaître de l’autre côté du labyrinthe. Le tableau suivant indiques les différents points et effets des
pacgommes.

Couleur Points Effet
Bleu     100     −
Violet   300    Le pacman devient invisible pour les fantômes. Sa couleur devient jaune pâle.
Orange   500    Le pacman devient un superpacman, sa couleur est alors orange et les fantômes deviennent alors bleus.
Vert     1000   Modifie la structure du labyrinthe

A cela on rajoute les règles suivantes :
— Initialement il a trois vies.
— Si le joueur dépasse les 5000 points, il obtient une vie supplémentaire.
— Chaque fantôme se déplace dans une direction jusqu’à ce qu’il atteigne un mur, puis choisit une nouvelle
direction aléatoirement.
— Quand le pacman est invisible, il peut traverser les fantômes sans perdre de vie.
— Quand le pacman est un superpacman, les fantômes deviennent vulnérables. Dans ce cas, ils se déplacent
deux fois plus lentement et ils reviennent au centre du labyrinthe s’ils sont touchés par le pacman.
— Le jeu se termine quand il n’y a plus de pacgommes et la partie est gagnée, ou quand le pacman a perdu
toutes ses vies et la partie est perdue.

Le non respect des règles ci-dessous sera considéré comme une erreur grave et entrainera un
malus allant de 5 à 10 points ;
— le code doit compiler et s’exécuter sans erreur,
— les méthodes doivent être courtes, lisibles même sans commentaire et avoir des noms clairs ;
— l’absence de duplication de code (absence de copier-coller, factorisation),
— les responsabilités de chaque objet (ses méthodes) doivent être clairement identifiées ;
— les objets ne doivent avoir accès qu’aux informations nécessaires pour assumer leurs responsabilités.
— l’utilisation d’images ou de sprites est strictement interdite.
L’accent sera donc mis sur la propreté et la structuration du code et aucunement sur les graphismes ou la
diversité des niveaux. En particulier, un niveau avec lequel on peut tester toutes les fonctionnalités est largement
suffisant.
