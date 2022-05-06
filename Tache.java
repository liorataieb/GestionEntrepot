package biilomo;
/** classe representant un lot. 
 * @author Liora Taieb et Hamza Touzani
 * @version 1.0
*/
public class Tache {
	
	private int idCommande; //commande liee a la tache
	private Paires endroit; //endroit où aller récupérer le lot
	private Personne ouvrier; //id de l'ouvrier affecte a la commande
	private int volume; //quantité à aller récupérer
	
	/** getter id de la commande associee
	 * @param void
	 * @return int id 
	 */
	public int getIdCommande() {
		return this.idCommande;
	}
	
	/** setter id de la commande associee
	 * @param void
	 * @return int id 
	 */
	public void setIdCommande(int idCommande) {
		this.idCommande = idCommande;
	}
	
	/** getter endroit de la tache
	 * @param void
	 * @return Paires endroit
	 */
	public Paires getEndroit() {
		return this.endroit;
	}
	
	/** setter endroit de la tache
	 * @param Paires endroit
	 * @return void
	 */
	public void setEndroit(Paires endroit) {
		this.endroit = endroit;
	}
	
	/** getter ouvrier faisant la tache
	 * @param void
	 * @return Personne ouvrier
	 */
	public Personne getOuvrier() {
		return this.ouvrier;
	}
	
	/** setter ouvrier faisant la tache
	 * @param Brico ouvrier
	 * @return void
	 */
	public void setOuvrier(Brico ouvrier) {
		this.ouvrier = ouvrier;
	}
	
	/** getter volume a deplacer
	 * @param void
	 * @return int volume a deplacer
	 */
	public int getVolume() {
		return this.volume;
	}
	
	/** setter volume a deplacer
	 * @param int volume a deplacer
	 * @return void
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	/** Constructeur tache
	 * @param int idCommande, Paires endroit, Personne ouvrier, int volume
	 * @return Tache t
	 */
	public Tache(int idCommande, Paires endroit, Personne ouvrier, int volume) {
		this.idCommande = idCommande;
		this.endroit = endroit;
		this.ouvrier = ouvrier;
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "(idCommande = "+idCommande+", element = "+endroit+", ouvrier = "+ouvrier+", volume = "+volume+")";
	}
	
}
