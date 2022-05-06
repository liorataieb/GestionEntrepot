package biilomo;
/** Classe Paires (tuple <String,int>) utilisee en interne
 * 
 * @author Hamza Touzani et Liora Taieb
 * @version 1.0
 *
 */
public class Paires {
	private String typeLot;
	private int volume;
	
	
	/** Constructeur Paires
	 * @param String typeLot, int volume
	 * @return Paires a
	 */
	public Paires(String typeLot, int volume) {
		this.typeLot=typeLot;
		this.volume=volume;
	}
	
	//getter
	
	/** getter typeLot de la paire
	 * @param void
	 * @return String typeLot
	 */
	public String getTypeLot() {
		return this.typeLot;
	}
	
	/** getter volume de la paire
	 * @param void
	 * @return int volume
	 */
	public int getVolume() {
		return this.volume;
	}
	
	//setter
	
	/** setter volume 
	 * @param int volume
	 * @return int volume de la paire
	 */
	public void setVolume(int n) {
		this.volume=n;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "("+this.getTypeLot()+", "+this.getVolume()+")";
	}

}
