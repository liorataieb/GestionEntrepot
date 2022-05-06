package biilomo;

/** classe representant un ouvrier Stock 
 * @author Liora Taieb et Hamza Touzani
 * @version 1.0
*/
public class Stock extends Personne {
	
	/** Constructeur ouvrier Stock
	 * @param String role ( chef ou ouvrier )
	 * @return Stock s;
	 */
	public Stock(String role) {
		super(role);
	}
	
	@Override
	public String toString() {
		return super.toString() + " stock";
	}
	
}
