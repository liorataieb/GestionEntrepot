package biilomo;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
/** Classe m�re des ouvriers
 * 
 * @author Hamza Touzani et Liora Taieb
 * @version 1.0
 *
 */
public class Personne {
	
	private static int countId=0;
	private String nom;
	private String prenom;
	private int id;
	private String role;
	private int tempsRestant=0;
	private LinkedList<Personne> collegues = new LinkedList<Personne>();
	
	// getter
	
	/** getter nom
	 * @param void
	 * @return string nom de l'ouvrier
	 */
	public String getNom() {
		return this.nom;
	}
	
	/** getter prenom
	 * @param void
	 * @return string prenom de l'ouvrier
	 */
	public String getPrenom() {
		return this.prenom;
	}
	
	/** getter role
	 * @param void
	 * @return string role de l'ouvrier
	 */
	public String getRole() {
		return this.role;
	}
	
	/** getter nom
	 * @param void
	 * @return int id de l'ouvrier
	 */
	public int getId() {
		return this.id;
	}
	
	/** getter liste de collegues
	 * @param void
	 * @return LinkedList<Personne> collegues de l'ouvrier
	 */
	public LinkedList<Personne> getCollegues(){
		return this.collegues;
	}
	
	/** getter temps restant avant disponibilit� de l'ouvrier
	 * @param void
	 * @return int temps restant de l'ouvrier
	 */
	public int getTempsRestant() {
		return this.tempsRestant;
	}
	
	// fin getter
	
	// Gestion temps restant :
	
	/** setter tempsRestant
	 * @param int n  nouveau temps restant
	 * @return void
	 */
	public void setTempsRestant(int n ) {
		this.tempsRestant=n;
	}
	
	/** update de tempsRestant ( tempsRestant -=1 )
	 * @param void
	 * @return void
	 */
	public void updateTempsRestant() {
		this.tempsRestant-=1;
	}
	
	/** constructeur Personne ( jamais utilise , on construit soit un brico soit un ouvrier )
	 * @param String role ( chef ou ouvrier )
	 * @return Personne
	 */
	public Personne(String role) { // Outil pour generer nom/prenom aleatoire recupere sur : 
		// https://www.baeldung.com/java-random-string 
		int leftLimit = 97; // lettre a'
		int rightLimit = 122; // lettre 'z'
		int taille = 6;
		Random random = new Random();

		String Nom = random.ints(leftLimit, rightLimit + 1)
				.limit(taille)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		
		String Prenom= random.ints(leftLimit, rightLimit + 1)
				.limit(taille)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();

		this.nom=Nom;
		this.prenom=Prenom;
		this.role=role;
		this.id=++countId;
		this.collegues=new LinkedList<Personne>();
	}
	
	@Override
	public String toString() {
		return nom + " " + prenom + ", employe num" + id + ", " + role;
	}
	
	/** test place dans l'�quipe ( pour les chefs )
	 * @param void
	 * @return boolean true si moins de 4 ouvriers dans l'�quipe sinon false
	 */
	public boolean placeCollegues() {
		if (this.collegues.size()<4) {
			return true;
		}
		else return false;
	}

}
