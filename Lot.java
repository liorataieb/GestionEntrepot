package biilomo;
/** classe representant un lot. 
 * @author Liora Taieb et Hamza Touzani
 * @version 1.0
*/
public class Lot {
	
	private int id; //id du lot
	private static int countId = 0; //utile a l'incrementation de l'id
	private String nom; //type de lot, eventuellement null pour des lots vides
	private float poids; //poids unitaire du lot
	private float prix; // prix unitaire du lot
	private int volume; // volume du lot
	private int reserve = -1;
	
	//getter
	
	/** getter id du lot
	 * @param void
	 * @return int id du lot
	 */
	public int getId() {
		return this.id;
	}
	
	/** setter id du lot
	 * @param int id
	 * @return void
	 */
	public void setId(int id) {
		this.id=id;
	}
	
	/** getter nom du lot ( quelle pi�ce )
	 * @param void
	 * @return String nom
	 */
	public String getNom() {
		return this.nom;
	}
	
	/** setter nom du lot
	 * @param String nom
	 * @return void
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/** getter poids du lot
	 * @param void
	 * @return float poids
	 */
	public float getPoids() {
		return this.poids;
	}
	
	/** setter id de la commande
	 * @param float poids
	 * @return void
	 */
	public void setPoids(float poids) {
		this.poids = poids;
	}
	
	/** getter prix du lot
	 * @param void
	 * @return float prix
	 */
	public float getPrix() {
		return this.prix;
	}
	
	/** setter prix du lot
	 * @param float prix
	 * @return void
	 */
	public void setPrix(float prix) {
		this.prix = prix;
	}
	
	/** getter volume du lot
	 * @param void
	 * @return int volume du lot
	 */
	public int getVolume() {
		return this.volume;
	}
	
	/** setter volume du lot
	 * @param int volume
	 * @return void
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	/** getter reserve de lot
	 * @param void
	 * @return int reserve
	 */
	public int getReserve() {
		return this.reserve;
	}
	
	/** setter reserve de lot
	 * @param int reserve ( -1 �tant non reserve sinon c'est reserve )
	 * @return void
	 */
	public void setReserve(int reserve) {
		this.reserve=reserve;
	}
	
	/** Constructeur de lot
	 * @param String nom, float poids, float prix , int volume 
	 * @return Lot l
	 */
	public Lot(String nom, float poids, float prix, int volume) {
		this.id = countId + 1;
		countId++;
		this.nom = nom;
		this.poids = poids;
		this.prix = prix;
		this.volume = volume;

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Lot n°"+String.valueOf(this.id)+", objet : "+this.nom+", poids : "+String.valueOf(this.poids)+", prix : "+String.valueOf(this.prix)+", volume : "+String.valueOf(this.volume)+" statut : "+this.reserve;
	}
	
}
