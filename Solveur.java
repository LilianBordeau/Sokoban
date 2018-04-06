import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/*
Contact: Guillaume.Huard@imag.fr
         ENSIMAG - Laboratoire LIG
         51 avenue Jean Kuntzmann
         38330 Montbonnot Saint-Martin
*/


public class Solveur 
{
	Moteur m;
	TerrainGraphique tg;
    HashMap<ConfigPlateau,Integer> D;
    HashSet<ConfigPlateau> alreadyChecked;
	LinkedList<ConfigPlateau> Q;
	
	Position sacPosition;
    //Position pousseurPosition;
	Position dest;
	
	public Solveur (Moteur m, TerrainGraphique tg) 
	{
		this.m = m;
		this.tg = tg;
	}
	
	private void init(){
        
	    for (int i=0; i<m.t.hauteur(); i++)
	    {
	        for (int j=0; j<m.t.largeur(); j++)
	        {                	
	             
	            if(m.t.consulter(i, j) == Case.SAC) 
	            {
	                sacPosition = new Position(i, j);
	        		tg.setStatut(Color.WHITE, i,j);
	            } 
	            else if(m.t.consulter(i, j) == Case.BUT) 
	            {
	                dest = new Position(i, j);
	        		tg.setStatut(Color.WHITE, i,j);
	            }
	            else
	            {
	        		tg.setStatut(Color.WHITE, i,j);
	            }
	        }
	    }
	}
        
    public Integer getDistance(ConfigPlateau c) {
    	if (D.containsKey(c)) return D.get(c);
    	return 10000;
    }
	
	public void aStar() 
	{
        init();

        Q = new LinkedList<ConfigPlateau>();
        ConfigPlateau configInitiale = new ConfigPlateau(new Position(m.lignePousseur, m.colonnePousseur), sacPosition, null);
        Q.add(configInitiale);
        D = new HashMap<>();
        
        D.put(configInitiale, 0);
        
        int n = 0;

        while(!Q.isEmpty()) 
        {
            n++;
            ConfigPlateau u = findMinimum();
            configInitiale = u;
            Q.remove(u);
            if (u.sacPosition.equals(dest)) {
                System.out.println("Succès en " + n + " configurations testées.");
                printPath(u);
                return;
            }

            for(ConfigPlateau v : getVoisins(u))
            {

                if(getDistance(u) + 1 < getDistance(v)) {
                    //System.out.println(getDistance(u));
                    D.put(v, getDistance(u) + 1);
                    Q.add(v);
                }
            }
        }
        printPath(configInitiale);
        System.out.println("Erreur avec "+n + " configurations testées.");
    }
	
	public void printPath(ConfigPlateau c) 
	{    
			ConfigPlateau prec = c ;
			int nbMovePousseur = 0 ; int nbMoveSac = 0 ;
            while(c != null) {
            	nbMovePousseur++;
                tg.setStatut(tg.getStatut(c.pousseurPosition.getX(), c.pousseurPosition.getY()).darker(), c.pousseurPosition.getX(), c.pousseurPosition.getY());
                if(!(tg.getStatut(c.sacPosition.getX(), c.sacPosition.getY()) == Color.GREEN))
                {
                	nbMoveSac++;
                    tg.setStatut(Color.GREEN, c.sacPosition.getX(),c.sacPosition.getY());
                }
                c = c.prec;
                tg.f.tracer(tg);
            }
            System.out.println(nbMovePousseur + " mouvements pousseur - " + nbMoveSac + " mouvements sac.");
	}
	
    private int heuristique(Position c1, Position c2) {
		return Math.abs(c1.getX()-c2.getX()) + Math.abs(c1.getY()-c2.getY());
	}
        
    private int getValue(ConfigPlateau c) {
        return getDistance(c) + heuristique(c.pousseurPosition, c.sacPosition) + heuristique(c.sacPosition, dest);
        //return getDistance(c) + heuristique(c.pousseurPosition, c.sacPosition);
    }
        
	public ConfigPlateau findMinimum() 
	{
            ConfigPlateau min = Q.getFirst();
            for (ConfigPlateau c : Q) 
            {
                //c.cout = getValue(c);
                if (getValue(c) < getValue(min))
                    min = c;
            }
            return min;
	}
	
	public ArrayList<Position> getVoisins(Position p) 
	{
		ArrayList<Position> voisins = new ArrayList<Position>();
		
		Position p1 = new Position(p.getX(),p.getY() - 1);
		Position p2 = new Position(p.getX(),p.getY() + 1);
		Position p3 = new Position(p.getX() - 1,p.getY());
		Position p4 = new Position(p.getX() + 1,p.getY());
		
        voisins.add(p1);
		voisins.add(p2);
		voisins.add(p3);
		voisins.add(p4);
		
		return voisins;
	}
        
    public ArrayList<ConfigPlateau> getVoisins(ConfigPlateau c)
	{
            ArrayList<ConfigPlateau> voisins = new ArrayList<ConfigPlateau>();
            
            for (Position p : getVoisins(c.pousseurPosition)) {
                if (m.correctBorder(p.getX(), p.getY()) && (m.t.consulter(p.getX(), p.getY()) != Case.OBSTACLE)) {
                    if (p.equals(c.sacPosition)) {
                        Position newSacPosition = new Position(2*c.sacPosition.getX() - c.pousseurPosition.getX(), 2*c.sacPosition.getY() - c.pousseurPosition.getY());
                        if (m.correctBorder(newSacPosition.getX(), newSacPosition.getY()) && 
                                (m.t.consulter(newSacPosition.getX(), newSacPosition.getY()) != Case.OBSTACLE)) {
                            voisins.add(new ConfigPlateau(p, newSacPosition, c));
                        }
                    } 
                    else {
                        voisins.add(new ConfigPlateau(p, c.sacPosition, c));
                    }
                }
            }
            
            return voisins;
        }
        
        /*public boolean caseCorrecte(ConfigPlateau c) {
            return 
        }*/
}

