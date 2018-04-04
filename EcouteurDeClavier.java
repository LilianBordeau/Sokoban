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
import java.awt.event.*;

import Dessin.Fenetre;

class EcouteurDeClavier implements KeyListener {
	Fenetre f;
    TerrainGraphique tg;
    Moteur m;
    Solveur s;
    
    EcouteurDeClavier(Fenetre f, TerrainGraphique tg, Moteur m, Solveur s) {
        this.f = f;
        this.tg = tg;
        this.m = m;
        this.s = s;
    }
    
    public void keyPressed(KeyEvent e) {
//    	System.out.println("KeyL = "+m.lignePousseur + "KeyC = "+m.colonnePousseur);
        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
        	action(m.lignePousseur-1, m.colonnePousseur);
//            System.out.println("Up");
            break;
        case KeyEvent.VK_RIGHT:
        	action(m.lignePousseur, m.colonnePousseur+1);
//            System.out.println("Right");
            break;
        case KeyEvent.VK_DOWN:
        	action(m.lignePousseur + 1, m.colonnePousseur);
//            System.out.println("Down");
            break;
        case KeyEvent.VK_LEFT:
        	action(m.lignePousseur, m.colonnePousseur-1);
//            System.out.println("Left");
            break;
        default:
//            System.out.println(e.getKeyCode());
        }
    }
    
    public boolean isAccessible(int x, int y)
    {
    	return m.correctMove(x, y) && m.correctBorder(x, y) && m.t.consulter(x,y).estLibre();
    }
    
    public void action(int x, int y) {
        if (m.action(x, y)) {
            // Exemple d'utilisation du statut d'une case : plus on passe par
            // une case, plus celle-ci est fonc�e.
        	
//        	if (isAccessible(x-1, y)) 
//                tg.setStatut(tg.getStatut(x-1,y).RED, x-1, y);
//
//        	if (isAccessible(x+1, y))
//                tg.setStatut(tg.getStatut(x+1,y).RED, x+1, y);
//
//        	if (isAccessible(x, y-1)) 
//                tg.setStatut(tg.getStatut(x,y-1).RED, x, y-1);
//
//        	if (isAccessible(x, y+1)) 
//                tg.setStatut(tg.getStatut(x,y+1).RED, x, y+1);
        	
            //tg.setStatut(tg.getStatut(x,y).YELLOW, x, y);
            //s.Dijkstra(x, y);
            f.tracerSansDelai(tg);
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
