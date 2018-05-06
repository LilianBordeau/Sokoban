# Sokoban (Algorithmique UE GINF 363 – 2017/2018)
L'objectif de ce projet est de programmer une version automatique du jeu de Sokoban dans laquelle un algorithme détermine une séquence de coups à jouer pour déplacer le sac sur la case but. L'affichage du terrain et la visualisation des coups se font à l'aide d'une fenêtre graphique, et le programme indique en fin de partie le score du joueur (nombre total de  mouvements effectués).

# Membres
* Mohameth Fall [(Mohameth)](https://github.com/Mohameth)
* Lilian Bordeau [(LilianBordeau)](https://github.com/LilianBordeau)

# Objectifs
* [X] Implanter un algorithme de résolution du jeu de Sokoban à un sac seulement.
* [X] Afficher le nombre de coups examinés ainsi que le nombre de mouvements du pousseur et du sac.
* [X] Ajouter un mode automatique affichant tous les états intermédiaires (i.e. tous les coups examinés).
* [X] Ajouter un mode automatique effectuant un nombre minimum de mouvements avec le pousseur.
* [X] Ajouter un mode automatique effectuant un nombre minimum de mouvements de sac.

# Compilation
```
javac -classpath .:Dessin.jar Sokoban.java
```

# Execution
```
java -classpath .:Dessin.jar Dessin.Run Sokoban
```
