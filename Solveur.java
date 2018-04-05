import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
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
	HashMap<Position,Integer> vecteur;
	LinkedList<Position> Q;
	
	Position src;
	Position dest;
	
	public Solveur (Moteur m, TerrainGraphique tg) 
	{
		this.m = m;
		this.tg = tg;
	}
	
	private void init(){
        vecteur = new HashMap<Position,Integer>();
        Position pos;
        Integer dis;
        
	    for (int i=0; i<m.t.hauteur(); i++)
	    {
	        for (int j=0; j<m.t.largeur(); j++)
	        {                	
	            pos = new Position(i, j);
	            dis = Integer.MAX_VALUE ;
	
	            if(m.t.consulter(i, j) == Case.OBSTACLE)  
	            {
	                dis = -1 ;
	            }
	            else if(m.t.consulter(i, j) == Case.SAC) 
	            {
	                src = new Position(i, j);
	                dis = 0;
	            } 
	            else if(m.t.consulter(i, j) == Case.BUT) 
	            {
	                dest = new Position(i, j);
	            }
	            else
	            {
	            	dis = isValid(i,j);
	            }
	
	            vecteur.put(pos,dis);
	        }
	    }
	}
	
	public int isValid(int i, int j)
	{
		if(i == 0 || i == m.t.hauteur()-1)
		{
			if(j == 0 || j == m.t.largeur()-1)
			{
				tg.setStatut(Color.RED, i,j);
				return -1;
			}
			else if(m.t.consulter(i, j+1) == Case.OBSTACLE || m.t.consulter(i, j-1) == Case.OBSTACLE)
			{
				tg.setStatut(Color.RED, i,j);
				return -1;
			}
		}
		else if(j == 0 || j == m.t.largeur()-1)
		{
			if(m.t.consulter(i+1, j) == Case.OBSTACLE || m.t.consulter(i-1, j) == Case.OBSTACLE)
			{
				tg.setStatut(Color.RED, i,j);
				return -1;
			}
		}
		else if(m.t.consulter(i, j+1) == Case.OBSTACLE || m.t.consulter(i, j-1) == Case.OBSTACLE)
		{
			if(m.t.consulter(i+1, j) == Case.OBSTACLE || m.t.consulter(i-1, j) == Case.OBSTACLE)
			{
				tg.setStatut(Color.RED, i,j);
				return -1;
			}
		}
		
		tg.setStatut(Color.WHITE, i,j);
		return Integer.MAX_VALUE;
	}
	
	public void dijkstra() 
	{
        System.out.println("--- début Dijkstra");
		init();

        Q = new LinkedList<Position>();
		Q.addFirst(src);
                
		while(!Q.isEmpty()) 
		{
			Position u = findMinimum();
			//System.out.println("u : " +u);
			//tg.setStatut(tg.getStatut(u.getX(),u.getY()).BLUE, u.getX(),u.getY());
			Q.remove(u);
			
			for(Position v : getVoisins(u))
			{
				//System.out.println("v : " + v);
				if (vecteur.get(u) + 1 < vecteur.get(v)) 
				{
					vecteur.put(v, vecteur.get(u) + 1);
					Q.addLast(v);
				}
			}
		}
		printPath();
		printBestMove();
        System.out.println("--- fin Dijkstra");
    }
	
	public void printPath() 
	{
		System.out.println("--- printPath");
		Position cur = new Position(dest.getX(),dest.getY());
		while (!cur.equals(src)) 
		{
	        ArrayList<Position> liste;
	        liste = getVoisins(cur);
	        Position min = new Position(liste.get(0).getX(), liste.get(0).getY());
	        
	        for (Position p : liste) 
	        {
	            //System.out.println("p : " + p);
	            if (vecteur.get(p) < vecteur.get(min)) 
	            {
	                min = p;
	            }
	        }
	        
	        cur = new Position(min.getX(), min.getY());
	        tg.setStatut(Color.GRAY, cur.getX(),cur.getY());
		}
	}
	
	public void printBestMove() 
	{
		System.out.println("--- printBestMove");
		if(src.getX() == 0 || src.getX() == m.t.hauteur()-1)
		{
			if(tg.getStatut(src.getX(), src.getY()-1) == Color.GRAY)
			{
		        tg.setStatut(Color.GREEN, src.getX(),src.getY()+1);
			}
			else if(tg.getStatut(src.getX(), src.getY()+1) == Color.GRAY)
			{
		        tg.setStatut(Color.GREEN, src.getX(),src.getY()-1);
			}
		}
		else if(src.getY() == 0 || src.getY() == m.t.largeur()-1)
		{
			if(tg.getStatut(src.getX()+1, src.getY()) == Color.GRAY)
			{
		        tg.setStatut(Color.GREEN, src.getX()-1,src.getY());
			}
			else if(tg.getStatut(src.getX()-1, src.getY()) == Color.GRAY)
			{
		        tg.setStatut(Color.GREEN, src.getX()+1,src.getY());
			}
		}
		else
		{
			if(tg.getStatut(src.getX()+1, src.getY()) == Color.GRAY)
			{
				if(vecteur.get(new Position(src.getX()-1, src.getY())) == -1)
				{
					System.out.println("meilleur mouvement pas défini");
				}
				else
				{
			        tg.setStatut(Color.GREEN, src.getX()-1,src.getY());	
				}
			}
			else if(tg.getStatut(src.getX(), src.getY()+1) == Color.GRAY)
			{
				if(vecteur.get(new Position(src.getX(), src.getY()-1)) == -1)
				{
					System.out.println("meilleur mouvement pas défini");
				}
				else
				{
			        tg.setStatut(Color.GREEN, src.getX(),src.getY()-1);	
				}
			}
			else if(tg.getStatut(src.getX()-1, src.getY()) == Color.GRAY)
			{
				if(vecteur.get(new Position(src.getX()+1, src.getY())) == -1)
				{
					System.out.println("meilleur mouvement pas défini");
				}
				else
				{
			        tg.setStatut(Color.GREEN, src.getX()+1,src.getY());	
				}
			}
			else if(tg.getStatut(src.getX(), src.getY()-1) == Color.GRAY)
			{
				if(vecteur.get(new Position(src.getX(), src.getY()+1)) == -1)
				{
					System.out.println("meilleur mouvement pas défini");
				}
				else
				{
			        tg.setStatut(Color.GREEN, src.getX(),src.getY()+1);	
				}
			}
			else
			{
				System.out.println("pas de mouvement possible");
			}
		}
	}
	
	public Position findMinimum() 
	{
		Position min = Q.getFirst();
		for (Position p : Q) 
		{
			if ((vecteur.get(p) != null && vecteur.get(min) != null) && vecteur.get(p) < vecteur.get(min)) 
			{
				min = p;
			}
		}
		return min;
	}
	
	public ArrayList<Position> getVoisins(Position p) 
	{
		ArrayList<Position> voisins = new ArrayList<Position>();
		
		Position p1 = new Position(p.getX(),p.getY() - 1);
		Position p2 = new Position(p.getX(),p.getY() + 1);
		Position p3 = new Position(p.getX() - 1,p.getY());
		Position p4	= new Position(p.getX() + 1,p.getY());
		
		if (vecteur.containsKey(p1) && vecteur.get(p1) != -1) voisins.add(p1);
		if (vecteur.containsKey(p2) && vecteur.get(p2) != -1) voisins.add(p2);
		if (vecteur.containsKey(p3) && vecteur.get(p3) != -1) voisins.add(p3);
		if (vecteur.containsKey(p4) && vecteur.get(p4) != -1) voisins.add(p4);
		
		return voisins;
	}
}

