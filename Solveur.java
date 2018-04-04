import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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


public class Solveur {
	Moteur m;
	TerrainGraphique tg;
	HashMap<Position,Integer> vecteur = new HashMap<Position,Integer>();
	LinkedList<Position> Q = new LinkedList<Position>();
	
	Position src;
	Position dest;
	
	
	public Solveur (Moteur m, TerrainGraphique tg) {
		this.m = m;
		this.tg = tg;
		initListe();
		
		Dijkstra(src.getX(), src.getY());
	}
	
	private void initListe() {
            for (int i=0; i<m.t.hauteur(); i++)
            {
                for (int j=0; j<m.t.largeur(); j++)
                {
                    /*int position[] = new int[3];
                    position[0] = i;
                    position[1] = j;*/
                    Position pos = new Position(i, j);
                    Integer d = Integer.MAX_VALUE ;

                    if(m.t.consulter(i, j) == Case.POUSSEUR)  {
                        d = 1 ;
                    }
                    else if(m.t.consulter(i, j) == Case.OBSTACLE)  {
                        d = -1 ;
                    }
                    else if (m.t.consulter(i, j) == Case.SAC) {
                        src = new Position(i, j);
                    } 
                    else if (m.t.consulter(i, j) == Case.BUT) {
                        dest = new Position(i, j);
                    }

                    vecteur.put(pos,d);
                }
            }
	}
	
	public void Dijkstra(int x, int y) {
                vecteur = new HashMap<Position,Integer>();
                Q = new LinkedList<Position>();
		initListe();
		
		src = new Position(x,y);
		
		Q.add(src);
		int pathLength = 0;
                
		while(!Q.isEmpty()) {
			Position u = findMinDInQ();
			//System.out.println("u : " +u);
			//tg.setStatut(tg.getStatut(u.getX(),u.getY()).BLUE, u.getX(),u.getY());
			
			Q.remove(u);
			
			for(Position p : getVoisins(u)) {
				//System.out.println("p : " + p);
				if (vecteur.get(u) + 1 < vecteur.get(p)) {
					vecteur.put(p, vecteur.get(u) + 1);
					Q.addLast(p);
				}
			}
                        
                        //pathLength++;
		}
		
		traceChemin();
                System.out.println("FINI");
    }
	
	public void traceChemin() {
		System.out.println("**** Chemin ****");
		Position cur = new Position(dest.getX(),dest.getY());
		while (!cur.equals(src)) {
                    List<Position> liste = new LinkedList(getVoisins(cur));
                    Position min = new Position(liste.get(0).getX(), liste.get(0).getY());
                    
                    for (Position p : liste) {
                        System.out.println("p : " + p);
                        if (vecteur.get(p) < vecteur.get(min)) {
                            min = p;
                        }
                    }
                    
                    cur = new Position(min.getX(), min.getY());

                    tg.setStatut(tg.getStatut(cur.getX(),cur.getY()).BLUE, cur.getX(),cur.getY());
                    //tg.setStatut(Color.BLUE, cur.getX(),cur.getY());
		}
		
	}
	
	public Position findMinDInQ() {
		
		Position min = Q.getFirst();
		for (Position p : Q) {
			if ((vecteur.get(p) != null && vecteur.get(min) != null) && vecteur.get(p) < vecteur.get(min)) {
				min = p;
			}
		}
		
		return min;
	}
	
	public List<Position> getVoisins(Position p) {
		List<Position> voisins = new ArrayList<Position>();
		
		Position p1 = new Position(p.getX(),p.getY() - 1);
		Position p2 = new Position(p.getX(),p.getY() + 1);
		Position p3 = new Position(p.getX() - 1,p.getY());
		Position p4	= new Position(p.getX() + 1,p.getY());
		
		if (vecteur.containsKey(p1) && vecteur.get(p1) != -1) voisins.add(p1);
		if (vecteur.containsKey(p2) && vecteur.get(p2) != -1) voisins.add(p2);
		if (vecteur.containsKey(p3) && vecteur.get(p3) != -1) voisins.add(p3);
		if (vecteur.containsKey(p4) && vecteur.get(p4) != -1) voisins.add(p4);
		
		
		/*/*HashMap<Position, Integer> voisins = new HashMap<Position, Integer>();
		Position p1 = new Position(p.getX(),p.getY() - 1);
		Position p2 = new Position(p.getX(),p.getY() + 1);
		Position p3 = new Position(p.getX() - 1,p.getY());
		Position p4	= new Position(p.getX() + 1,p.getY());
		
		voisins.put(p1, vecteur.get(p1));
		voisins.put(p2, vecteur.get(p2));
		voisins.put(p3, vecteur.get(p3));
		voisins.put(p4, vecteur.get(p4));*/
		return voisins;
	}
	
}

