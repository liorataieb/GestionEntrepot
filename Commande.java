package biilomo;
import java.util.*;

/** classe representant une Commande
 * @author Hamza Touzani et Liora Taieb
 * @version 1.0
*/
public class Commande {
	
	private static int countId;
	private int id;// Reference
	private String nomDuMeuble;
	private String pieceMaison; 
	private int tempsConstruction; // Nb de pas de temps n�cessaire
	private LinkedList<Paires> listePieces=new LinkedList<Paires>(); // Listes de pi�ces n�cessaires � la commande
	private LinkedList<Paires> listePiecesCpt = new LinkedList<Paires>();
	private Personne ouvrierAffecte;  // Un seul ouvrier affect� par commande
	private LinkedList<Tache> tachesRestantes = new LinkedList<Tache>();
	private float prix; 
	
	/** Constructeur Commande
	 * @param String nom de la commande, String pieceMaison, int tempsConstruction, LinkedList<Paires> pieces necessaires
	 * @return Commande c
	 */
	
	public Commande(String nom,String pieceMaison,int tempsConstruction, LinkedList<Paires> pieces) {
		this.id=++countId;
		this.nomDuMeuble=nom;
		this.pieceMaison=pieceMaison;
		this.tempsConstruction=tempsConstruction;
		this.listePieces = (LinkedList<Paires>) pieces.clone();
		this.listePiecesCpt = (LinkedList<Paires>) pieces.clone();
	}
	
	//getter
	
	/** getter id de la commande
	 * @param void
	 * @return int id de la commande
	 */
	public int getId() {
		return this.id;
	}
	
	/** getter nomDuMeuble de la commande
	 * @param void
	 * @return String nomDuMeuble
	 */
	public String getNomDuMeuble() {
		return this.nomDuMeuble;
	}
	
	/** getter pieceMaison de la commande
	 * @param void
	 * @return String pieceMaison
	 */
	public String getPieceMaison() {
		return this.pieceMaison;
	}
	
	/** getter tempsConstruction de la commande
	 * @param void
	 * @return int tempsConstructions
	 */
	public int getTempsConstruction() {
		return this.tempsConstruction;
	}
	
	/** getter prix de la commande
	 * @param void
	 * @return float prix
	 */
	public float getPrix() {
		return this.prix;
	}
	
	/** getter listePieces de la commande
	 * @param void
	 * @return LinkedList<Paires> listePieces
	 */
	public LinkedList<Paires> getListePieces(){
		return this.listePieces;
	}
	
	/** getter copie mutable listePieces de la commande
	 * @param void
	 * @return LinkedList<Paires> listePiecesCpt
	 */
	public LinkedList<Paires> getListePiecesCpt(){
		return this.listePiecesCpt;
	}
	
	/** getter ouvrier affecte a la commande
	 * @param void
	 * @return Personne ouvrierAffecte
	 */
	public Personne getOuvrier() {
		return this.ouvrierAffecte;
	}
	
	/** getter tachesRestantes de la commande
	 * @param void
	 * @return LinkedList<Tache> tachesRestantes
	 */
	public LinkedList<Tache> getTachesRestantes(){
		return this.tachesRestantes;
	}
	
	// setter 
	
	/** set prix de la commande
	 * @param float prix
	 * @return void
	 */
	public void setPrix(float a) {
		this.prix=a;
	}
	
	/** set ouvrierAffecte de la commande
	 * @param Personne ouvrier
	 * @return void
	 */
	public void affectation(Personne ouvrier) {
		this.ouvrierAffecte=ouvrier;
	}

	@Override
	public String toString() {
		return this.listePiecesCpt.toString();
	}
 }
