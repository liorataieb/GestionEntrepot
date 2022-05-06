package biilomo;
import java.util.*;
/** classe representant un lot. 
 * @author Liora Taieb et Hamza Touzani
 * @version 1.0
*/
public class Rangee {
	
	private int n; //longueur en metre des rangees
	private ArrayList<Lot> etagere = new ArrayList<Lot>(); //tableau des etageres
	
	/** getter n taille rangee
	 * @param void
	 * @return int n
	 */
	public int getN() {
		return this.n;
	}
	
	/** setter n taille rangee
	 * @param int n
	 * @return void
	 */
	public void setN(int n) {
		this.n = n;
	}
	
	/** getter etagere(liste des lots )
	 * @param void
	 * @return ArrayList<Lot> etagere
	 */
	public ArrayList<Lot> getEtagere(){
		return this.etagere;
	}
	
	/** Constructeur rangee
	 * @param int n
	 * @return Rangee r ( de taille n )
	 */
	public Rangee(int n) {
		this.n = n;
		Lot l = new Lot(null, 0, 0, n);
		this.etagere = new ArrayList<Lot>();
		this.etagere.add(l);
	}

	@Override
	public String toString() {
		for(Iterator it=this.etagere.iterator(); it.hasNext();) {
			System.out.println(it.next().toString()); 
		}
		return "";
	}
	
	/** test de place dans la rangee
	 * @param void
	 * @return boolean ( true si il y a de la place false sinon )
	 */
	public boolean pleine() { //la rangee est-elle pleine ?
		boolean val = true;
		for (int i = 0; i < this.etagere.size(); i++) {
			if (this.etagere.get(i).getNom() == null) val = false; 
		}
		return val;
	}
	
	/** ou sont les places disponibles
	 * @param void
	 * @return ArrayList<Paires> placeDispo ( coordonnees de la place et volume disponible sous forme de Paires )
	 */
	public ArrayList<Paires> placeDispo() { //renvoie une liste de ("place", volume). place est un int mis dans un string pour pouvoir utiliser Paires.
		ArrayList<Paires> liste = new ArrayList<Paires>();
		for (int i = 0; i < this.etagere.size(); i++) {
			if (this.etagere.get(i).getNom() == null) {
				Paires p = new Paires(String.valueOf(i), this.etagere.get(i).getVolume());
				liste.add(p); 
			}
		}
		return liste;
	}
	
	/** concatenation d'emplacement vide ( rangement )
	 * @param int i, int j ( coordonnees des 2 lots )
	 * @return void
	 */
	public void concat(int i, int j) { //concatenation de 2 lots vides : fonction de rangement
		try {
			Lot l1 = this.getEtagere().get(i);
			Lot l2 = this.getEtagere().get(j);
			if (l1.getNom() != null && l2.getNom() != null) return;
			else {
				l1.setVolume(l1.getVolume() + l2.getVolume());
				this.etagere.remove(l2);
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println(e);
		}
	}
	
	/** suppression de lot
	 * @param int i, int volume
	 * @return void
	 */
	public void supp(int i, int volume) { //suppression d'un lot
		try {
			Lot l = this.getEtagere().get(i);
			if (volume > l.getVolume()) return;
			else if (volume < l.getVolume()) {
				if (i == 0) {
					l.setVolume(l.getVolume() - volume);
					if (this.etagere.get(i+1).getNom() == null) {
						this.etagere.add(i, new Lot(null, 0, 0, volume));
						this.concat(i, i+1);
					} else this.etagere.add(i, new Lot(null, 0, 0, volume));
				}
				else if (i == this.getEtagere().size()-1) {
					this.getEtagere().get(i).setVolume(l.getVolume() - volume);
					if (this.etagere.get(i-1).getNom() == null) {
						this.etagere.add(i, new Lot(null, 0, 0, volume));
						this.concat(i, i-1);
					} else this.etagere.add(i, new Lot(null, 0, 0, volume));
				} else {
					this.getEtagere().get(i).setVolume(l.getVolume() - volume);
					if (this.etagere.get(i-1).getNom() == null) {
						this.etagere.add(i, new Lot(null, 0, 0, volume));
						this.concat(i, i-1);
					} else if (this.etagere.get(i+1).getNom() == null) {
						this.etagere.add(i, new Lot(null, 0, 0, volume));
						this.concat(i, i+1);
					} else this.etagere.add(i, new Lot(null, 0, 0, volume));
				}
			} else {
				if (volume == this.getN()) {
					this.etagere.remove(l);
					this.etagere.add(new Lot(null, 0, 0, this.n));
				}
				else if (i == 0) {
					this.etagere.remove(l);
					if (this.etagere.get(i).getNom() == null) {
						this.etagere.get(i).setVolume(this.etagere.get(i).getVolume() + volume);
					}
					else {
						this.etagere.add(i, new Lot(null, 0, 0, volume));
					}
				}
				else if (i == this.getEtagere().size()-1) {
					this.etagere.remove(l);
					if (this.etagere.get(i-1).getNom() == null) this.etagere.get(i-1).setVolume(this.etagere.get(i-1).getVolume() + volume);
					else this.etagere.add(new Lot(null, 0, 0, volume));
				} else {
					this.etagere.remove(l);
					if (this.etagere.get(i).getNom() == null && this.etagere.get(i-1).getNom() == null) {
						this.etagere.get(i-1).setVolume(this.etagere.get(i-1).getVolume() + volume);
						this.concat(i-1, i);
					} else if (this.etagere.get(i-1).getNom() == null) this.etagere.get(i-1).setVolume(this.etagere.get(i-1).getVolume() + volume);
					else if (this.etagere.get(i).getNom() == null) this.etagere.get(i).setVolume(this.etagere.get(i).getVolume() + volume);
					else this.etagere.add(i, new Lot(null, 0, 0, volume));
				}
			}	
		} catch (IndexOutOfBoundsException e) {
			System.out.println("on est bien la");
		}
	}
	
	/** placement d'un lot
	 * @param Lot l, int i ( placement du lot l a l'emplacement i )
	 * @return void
	 */
	public void placement(Lot l, int i) {
		try {
			if (l.getVolume() == this.getN()) {
				this.etagere.remove(0);
				this.etagere.add(l);
			}
			if (this.etagere.get(i).getNom() == null && this.etagere.get(i).getVolume() > l.getVolume()) {
				this.etagere.get(i).setVolume((this.etagere.get(i).getVolume() - l.getVolume()));
				this.etagere.add(i, l);
			}
			else if (this.etagere.get(i).getNom() == null && this.etagere.get(i).getVolume() == l.getVolume()) {
				this.etagere.remove(i);
				this.etagere.add(i, l);
			}
			
		} catch (IndexOutOfBoundsException e) {
			System.out.println(e);
		}
	}
	
	/** split un lot en 2 lots
	 * @param Lot lot, int volume
	 * @return int emplacement du lot split
	 */
	public int splitLot(Lot lot, int volume) {
		if (volume >= lot.getVolume()) return -1;
		else {
			int i = this.etagere.indexOf(lot);
			if (lot.getVolume() == n || i == 0) {
				this.etagere.add(1, new Lot(lot.getNom(), lot.getPoids(), lot.getPrix(), volume));
				lot.setVolume(lot.getVolume() - volume);
				return 1;
			} else if (i == this.etagere.size() - 1) {
				this.etagere.add(i, new Lot(lot.getNom(), lot.getPoids(), lot.getPrix(), lot.getVolume() - volume));
				lot.setVolume(volume);
				return i;
			} else {
				if (this.etagere.get(i-1).getNom() == null) {
					this.etagere.add(i, new Lot(lot.getNom(), lot.getPoids(), lot.getPrix(), lot.getVolume() - volume));
					lot.setVolume(volume);
					return i;
				} else {
					this.etagere.add(i+1, new Lot(lot.getNom(), lot.getPoids(), lot.getPrix(), volume));
					lot.setVolume(lot.getVolume() - volume);
					return i+1;
				}
			}
		}
	}	
		
}
