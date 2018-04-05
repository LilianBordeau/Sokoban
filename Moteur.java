import java.awt.Graphics2D;

/*
Sokoban - impl�mentation manuelle et automatique du c�l�bre jeu
Copyright (C) 2009 Guillaume Huard
Ce programme est libre, vous pouvez le redistribuer et/ou le modifier selon les
termes de la Licence Publique G�n�rale GNU publi�e par la Free Software
Foundation (version 2 ou bien toute autre version ult�rieure choisie par vous).

Ce programme est distribu� car potentiellement utile, mais SANS AUCUNE
GARANTIE, ni explicite ni implicite, y compris les garanties de
commercialisation ou d'adaptation dans un but sp�cifique. Reportez-vous � la
Licence Publique G�n�rale GNU pour plus de d�tails.

Vous devez avoir re�u une copie de la Licence Publique G�n�rale GNU en m�me
temps que ce programme ; si ce n'est pas le cas, �crivez � la Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
�tats-Unis.

Contact: Guillaume.Huard@imag.fr
         ENSIMAG - Laboratoire LIG
         51 avenue Jean Kuntzmann
         38330 Montbonnot Saint-Martin
*/
class Moteur {
    Terrain t;
    TerrainGraphique tg;
    Solveur s;
    int lignePousseur, colonnePousseur;
    int nbMove;
    int win = 0;

    Moteur(Terrain t) {
        this.t = t;
        for (int i=0; i<t.hauteur(); i++)
            for (int j=0; j<t.largeur(); j++)
                if (t.consulter(i,j).contient(Case.POUSSEUR)) {
                    lignePousseur = i;
                    colonnePousseur = j;
                }        
                else if(t.consulter(i,j) == Case.BUT)
                {
                    win++;
                }
    }
    
    public void setS(Solveur s, TerrainGraphique tg) {
        this.s = s;
        this.tg = tg;
    }
    
    // Vérifie que la case n'est pas en dehors des limites
    public boolean correctBorder(int i, int j) {
    	return !((i < 0 || j < 0) || (i >= t.hauteur() || j >= t.largeur()));
    }
    
    // Vérifie qu'on ne se déplace pas de plus d'une case
    public boolean correctMove(int i, int j) {
    	return (((lignePousseur == i +1 || lignePousseur == i-1) && (colonnePousseur == j)) || 
    	   ((colonnePousseur == j+1 || colonnePousseur == j-1) && (lignePousseur == i)));
    }
    
    public void win() {
	    for (int i=0; i<t.hauteur(); i++)
	    {
	        for (int j=0; j<t.largeur(); j++)
	        {
	        	Case courante = Case.SAC_SUR_BUT;
	        	t.assigner(courante, i, j);
	        }
	    }
    }


    public boolean action(int i, int j) {
    	// Mouvement correct
    	if(correctMove(i, j) && correctBorder(i, j) && win > 0)
    	{
            // La case est libre
            if (t.consulter(i,j).estLibre()) 
            {
                Case courante = t.consulter(lignePousseur, colonnePousseur);
                courante = courante.retrait(Case.POUSSEUR);
                t.assigner(courante, lignePousseur, colonnePousseur);
                courante = t.consulter(i, j);
                courante = courante.ajout(Case.POUSSEUR);
                t.assigner(courante, i, j);

                lignePousseur = i;
                colonnePousseur = j;
            }
            // La case est un sac
            else if(t.consulter(i, j) == Case.SAC || t.consulter(i, j) == Case.SAC_SUR_BUT) 
            {
            	// Case après le sac
                int iSac = 2*i - lignePousseur;
                int jSac = 2*j - colonnePousseur;

                // La case suivante est libre
                if(correctBorder(iSac, jSac) && (t.consulter(iSac, jSac) != Case.OBSTACLE) && (t.consulter(iSac, jSac) != Case.SAC))
                {
                    // On enlève le pousseur de la case courante
                    Case courante = t.consulter(lignePousseur, colonnePousseur);
                    courante = courante.retrait(Case.POUSSEUR);
                    t.assigner(courante, lignePousseur, colonnePousseur);
                    courante = t.consulter(i, j);

                    // La case est un sac sur un but
                    if (courante == Case.SAC_SUR_BUT)
                    {
                        courante = courante.retrait(Case.SAC_SUR_BUT);
                        courante = courante.ajout(Case.POUSSEUR_SUR_BUT);
                        win++;
                    }
                    else
                    {
                        courante = courante.retrait(Case.SAC);
                        courante = courante.ajout(Case.POUSSEUR);
                    }

                    t.assigner(courante, i, j);

                    Case newSac = t.consulter(iSac, jSac);

                    // La case suivante est un but
                    if(t.consulter(iSac, jSac) == Case.BUT)
                    {
                        newSac = newSac.ajout(Case.SAC_SUR_BUT);
                        win--;
                    }
                    else
                    {
                        newSac = newSac.ajout(Case.SAC);
                    }

                    t.assigner(newSac, iSac, jSac);
                    lignePousseur = i;
                    colonnePousseur = j;
                    sacABouge(iSac, jSac);
                }

            }
            nbMove++;
            System.out.println("nb coups: " + nbMove + "\n");
            if (win == 0) win();
            return true;
        }
        // Mouvement incorrect
    	else
    	{
            return false;
    	}
    }
    
    public void sacABouge(int i, int j) {
        s.dijkstra();
    }
}
