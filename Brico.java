package biilomo;

import java.util.ArrayList;
import java.util.LinkedList;

/** classe representant un ouvrier Brico
 * @author Liora Taieb et Hamza Touzani
 * @version 1.0
*/
public class Brico extends Personne {
	
	private String specialite = null;
	private ArrayList<Commande> commandesEffectuees = new ArrayList<Commande>();
	private Commande commandeEnCours;
	
	/** constructeurs
	 * @param String role ( chef ou ouvrier ) String specialite ( une des pi�ces de la maison )
	 * @return un objet brico
	 */
	public Brico(String role,String specialite) {
		super(role);
		this.specialite=specialite;	
	}
	
	//getter
	/** getter specialite de l'ouvrier brico
	 * @param void
	 * @return specialite de l'ouvrier brico
	 */
	public String getSpecialite() {
		return this.specialite;
	}
	/** getter commandes effectuees de l'ouvrier brico
	 * @param void
	 * @return ArrayList<Commande> commande effectuees par l'ouvrier
	 */
	public ArrayList<Commande> getEffectuees() {
		return this.commandesEffectuees;
	}
	/** getter commandes en cours de l'ouvrier brico
	 * @param void
	 * @return Commande c en cours de l'ouvrier brico
	 */
	public Commande getEnCours() {
		return this.commandeEnCours;
	}
	
	@Override
	public String toString() {
		return super.toString() + " brico, spécialité " + this.specialite;
	}
	
}
