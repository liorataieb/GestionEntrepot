package biilomo;
import java.util.*;
import java.lang.*;

/** classe representant un entrepot. 
 * @author Liora Taieb et Hamza Touzani
 * @version 1.0
*/
public class Entrepot {

	private int m; // nombre de rangees
	private Rangee[] hangar; // tableau des etageres de l'entrepot
	private LinkedList<Personne> chefs = new LinkedList<Personne>(); 
	private HashMap<String,Integer> stockEnReserve = new HashMap<String,Integer>(); // lots attendant d'etre fournis a un brico
	private ArrayList<Commande> commandesEnCours = new ArrayList<Commande>(); 
	private ArrayList<Commande> commandesEffectuees = new ArrayList<Commande>();
	private float treso;
	private float valeurStock; // valeur associee au stock. Voir setValeurStock() pour explications
	private float risque; // indicateur de rangement. Plus le risque est eleve, plus il faut faire de la place.
	private int[] memoireLot; // nombre de fois ou l'on a recu un lot de taille k avec 1 < k <= n : sert au rangement
	private HashMap<String, Paires> memoireMaison = new HashMap<String, Paires>(); // pour chaque piece de maison : nombre de fois ou l'on a recu une commande de meuble associé à la piece en question, avec en prime le nombre D'OUVRIERS spécialise dans cette piece
	private int tensionStock; // nb de taches restantes à la fin d'une itération - nb d'ouvriers stock dispo au début de l'itération. Si > 0, alors on embauche.
	
	/** constructeur de l'entrepot : initilisation
	 * @param m le nombre de rangees, n la taille d'une rangee, treso la tresorerie initiale
	 * @return void
	*/
	public Entrepot(int m,int n,float treso) {
		this.m=m;
		hangar=new Rangee[m];
		for(int i=0;i<m;i++) {
			hangar[i]=new Rangee(n);
		}
		chefs=new LinkedList<Personne>();
		this.treso=treso;
		this.memoireLot = new int[n+1];
		for (int i = 0; i < n; i++) {
			this.memoireLot[i] = 0;
		}
		Paires p = new Paires("0", 0);
		this.memoireMaison.put("CUISINE", p);
		this.memoireMaison.put("CHAMBRE", p);
		this.memoireMaison.put("SALLEAMANGER", p);
		this.memoireMaison.put("SALON", p);
		this.memoireMaison.put("SALLEDEBAIN", p);
		this.memoireMaison.put("WC", p);
	}
	
	// GETTERS ET SETTERS
	
	/** getter de m
	 * @param void
	 * @return m le nombre de rangees de l'entrepot
	*/
	public int getM() {
		return this.m;
	}
	
	/** getter de Hangar
	 * @param void
	 * @return Rangee[] le hangar de l'entrepot
	*/
	public Rangee[] getHangar() {
		return hangar;
	}
	
	/** getter de chefs
	 * @param void
	 * @return LinkedList<Personne> la liste des chefs de l'entrepot
	*/
	public LinkedList<Personne>  getChefs(){
		return this.chefs;
	}
	
	/** getter de stockEnReserve
	 * @param void
	 * @return HashMap<String,Integer> le stock en reserve de l'entrepot
	*/
	public HashMap<String,Integer> getStockEnReserve() {
		return stockEnReserve;
	}
	
	/** getter de memoireLot
	 * @param void
	 * @return int[] l'attribut memoireLot de l'entrepot
	*/
	public int[] getMemoireLot() {
		return this.memoireLot;
	}
	
	/** getter de memoireMaison
	 * @param void
	 * @return HashMap<String, Paires> l'attribut memoireMaison de l'entrepot
	*/
	public HashMap<String, Paires> getMemoireMaison(){
		return this.memoireMaison;
	}
	
	/** getter de tensionStock
	 * @param void
	 * @return int la tension du stock de l'entrepot
	*/
	public int getTensionStock() {
		return this.tensionStock;
	}
	
	/** setter de tensionStock
	 * @param int la tension du stock de l'entrepot
	 * @return void
	*/
	public void setTensionStock(int tension) {
		this.tensionStock = tension;
	}
	
	/** getter de risque
	 * @param void
	 * @return float risque de l'entrepot
	*/
	public float getRisque() {
		return this.risque;
	}
	
	/** utile a setRisque(), renvoie si un lot de taille k est souvent recu ou non via le taux (nombre de lot de taille k recus)/(nombre de lot recus en tout)
	 * @param k un lot de taille k
	 * @return pi_k la valeur du taux
	*/
	public float pi(int k) {
		float pi_k = 0;
		int sum = 0;
		for (int i = 0; i < this.hangar[0].getN(); i++) { // nombre de lots recus depuis le debut de la simulation
			sum += this.memoireLot[i+1];
		}
		pi_k = (float) this.memoireLot[k]/sum;
		return pi_k;
	}
	
	/** utile a setRisque(), renvoie le nombre d'endroits (trous) ou l'on peut placer un lot de taille k
	 * @param k un lot de taille k
	 * @return f_k le nombre de trous
	*/
	public int grandF(int k) { 
		int f_k = 0;
		for (int i = 0; i < this.getM(); i++) { // on cherche les places disponibles
			ArrayList<Paires> l = this.hangar[i].placeDispo();
			for (Paires p : l) {
				if (p.getVolume() >= k) f_k += 1;
			}
		}
		return f_k;
	}
	
	/** setter de risque, il est arbitrairement choisi comme sum(k*pi(k)/(grandF(k)+0.1))
	 * @param void
	 * @return void
	*/
	public void setRisque() { 
		float risque = 0;
		for (int k = 1; k <= this.hangar[0].getN(); k++) {
			risque += (float) k*this.pi(k)/(this.grandF(k) + 0.1);
		}
	}
	
	/** getter de treso
	 * @param void
	 * @return float tresorerie de l'entrepot
	*/
	public float getTreso() {
		return this.treso;
	}
	
	/** setter de treso
	 * @param treso la tresorerie
	 * @return void
	*/
	public void setTreso(float treso) {
		this.treso = treso;
	}
	
	/** getter de valeurStock
	 * @param void
	 * @return float la valeur du stock de l'entrepot
	*/
	public float getValeurStock() {
		return this.valeurStock;
	}
	
	/** setter de valeurStock, elle est arbitrairement choisie comme etant la valeur de tous les lots + le nombre de lot dans l'entrepot fois le cout moyen de deplacement d'une personne stock. On cherche a la minimiser a chaque pas de temps.
	 * @param void
	 * @return void
	*/
	public void setValeurStock() {
		float prixLot = 0;
		float nbLot = 0;
		float cout = 0; 
		int nbCollegues = 0;
		for (int i = 0; i < this.getM(); i++) { // somme des prix des lots
			for (int j = 0; j < this.getHangar()[i].getEtagere().size(); j++) {
				if (this.getHangar()[i].getEtagere().get(j).getNom() != null) nbLot += 1;
				prixLot += this.getHangar()[i].getEtagere().get(j).getPrix() * this.getHangar()[i].getEtagere().get(j).getVolume();
			}
		}
		try {
			for (Personne p : this.getChefs()) { // determination du cout moyen de deplacement
				cout += 10 + p.getCollegues().size()*5;
				nbCollegues += p.getCollegues().size();
			}
			cout = (float) cout/(nbCollegues + this.getChefs().size());
		} catch (NullPointerException e) {
			System.out.println(e);
		}
		this.valeurStock = prixLot + nbLot*cout;
	}
	
	/** getter de commandesEnCours
	 * @param void
	 * @return ArrayList<Commande> commandes en cours de l'entrepot
	*/
	public ArrayList<Commande> getCommandesEnCours() {
		return commandesEnCours;
	}
	
	/** getter de commandesEffectuees
	 * @param void
	 * @return ArrayList<Commande> commandes effectuees de l'entrepot
	*/
	public ArrayList<Commande> getCommandesEffectuees() {
		return commandesEffectuees;
	}
	
	// EMBAUCHAGE ET DEBAUCHAGE
	
	/** indique, si l'on peut le faire, s'il faut embaucher une personne stock. On embauche lorsqu’il reste plus de tâches logistiques restantes à faire à la fin du pas de temps que d’ouvriers stock au début du pas de temps.
	 * @param void
	 * @return true si oui, false sinon
	*/
	public boolean decisionEmbaucheStock() {
		if (this.getTensionStock() > 0) return true;
		return false;
	}
	
	/** embauche d'un ouvrier stock
	 * @param void
	 * @return void
	*/
	public void embaucheStock() {
		try {
			for (int i=0;i<chefs.size();i++) { // on chercher ou placer l'ouvrier
				if (chefs.get(i).placeCollegues()) {
					chefs.get(i).getCollegues().add(new Stock("ouvrier"));
					i = chefs.size();
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("embaucheStock de Entrepot, catch n°1");
		}
		System.out.println("On embauche un ouvrier stock");
	}
	
	/** indique, si l'on peut le faire, s'il faut embaucher une personne brico. On embauche si le pourcentage de chefs occupes est au dessus d'un certain seuil.
	 * @param seuil strategique en pourcentage
	 * @return true si oui, false sinon
	*/
	public boolean decisionEmbaucheBrico(int seuil) {
		int nbBrico = 0;
		int nbActifs = 0;
		for (int i = 0; i < this.getChefs().size(); i++) { // on compte le nombre de chefs bricos, et de chefs bricos actifs
			if (this.getChefs().get(i).getClass().getSimpleName().equals("Brico") == true) {
				nbBrico += 1;
				if (this.getChefs().get(i).getTempsRestant() > 0) nbActifs += 1;
			}
		}
		if (nbActifs*100/nbBrico >= seuil) return true;
		return false;
	}
	
	/** embauche d'un ouvrier brico
	 * @param spe specialite de l'ouvrier brico a embaucher
	 * @return void
	*/
	public void embaucheBrico(String spe) {
		if (spe.equals("")) return;
		try {
			for (int i=0;i<chefs.size();i++) { // on chercher ou placer l'ouvrier
				if (chefs.get(i).placeCollegues()) chefs.get(i).getCollegues().add(new Brico("ouvrier",spe));
				i=chefs.size();
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("embaucheBrico de Entrepot, catch n°1");
		}
		Paires p = this.getMemoireMaison().get(spe); // on incremente de 1 le nombre d'ouvriers specialises dans memoireMaison.
		int vol = p.getVolume();
		p.setVolume(vol + 1);
		this.getMemoireMaison().replace(spe, p);
		System.out.println("On embauche un ouvrier brico specialise en "+spe);

	}
	
	/** embauche d'un chef brico
	 * @param void
	 * @return void
	*/
	public void embaucheChef() {
		this.chefs.add(new Brico("chef","toutes"));
		System.out.println("On embauche un chef brico");
	}
	
	/** embauche d'un chef stock
	 * @param void
	 * @return void
	*/
	public void embaucheChefStock() {  
		this.chefs.add(new Stock("chef")); 
		System.out.println("On embauche un chef stock");
	}
	
	/** indique, si l'on a pris la decision d'embaucher, s'il vaut mieux prendre un chef ou un ouvrier. On embauche un chef que si tous les chefs ont deja une equipe complete.
	 * @param void
	 * @return "chef" s'il faut embaucher un chef, "ouvrier" sinon
	*/
	public String embaucheChefOuOuv() { 
		if (this.getChefs().size() == 0) return "chef";
		else {
			int i = 0;
			boolean val = false;
			while (i < this.getChefs().size() && val == false) {  // on regarde si un chef a de la place dans son equipe
				if (this.getChefs().get(i).placeCollegues()) val = true;
				i++;
			}
			if (val == false) return "chef";
			else return "ouvrier";
		}
	}
	
	/** indique, si l'on a pris la decision de debaucher, s'il vaut mieux licencier un chef ou un ouvrier. On souhaite garder au maximum des equipes completes.
	 * @param void
	 * @return "chef" s'il faut debaucher un chef, "ouvrier" sinon
	*/
	public String debaucheChefOuOuv() { 
		int chefs = 0;
		int ouv = 0;
		try {
			for (int i = 0; i < this.getChefs().size(); i++) { // on compte le nombre d'ouvriers et de chefs pour savoir si un des chefs a de la place
				chefs += 1;
				ouv += this.getChefs().get(i).getCollegues().size();
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("debaucheChefOuOuv de Entrepot, catch n°1");
		}
		if (ouv == 4*(chefs - 1)) return "chef";
		else return "ouvrier";
	}
	
	/** indique, si l'on peut le faire, s'il faut debaucher une personne stock. On debauche lorsqu’il ne reste plus de tâches logistiques à faire à la fin du pas de temps, et que l'entrepôt est bien rangé. On souhaite toujours garder au moins un chef stock
	 * @param seuil la reference de si l'entrepot est bien range ou non
	 * @return true si oui, false sinon
	*/
	public boolean decisionDebaucheStock(float seuil) { 
		int nbStock = 0;
		for (int i = 0; i < this.getChefs().size(); i++) { // on compte le nombre d'ouvriers stock
			if (this.getChefs().get(i).getClass().getSimpleName().equals("Stock") == true) {
				nbStock += 1;
				for (Personne p : this.getChefs().get(i).getCollegues()) {
					if (p.getClass().getSimpleName().equals("Stock") == true) nbStock += 1;
				}
			}
		}
		this.setRisque();
		if (this.getRisque() <= seuil && this.getTensionStock() < 0 && nbStock > 1) return true;
		return false;
	}
	
	/** debauche d'un ouvrier stock
	 * @param void
	 * @return void
	*/
	public void debaucheStock() {
		boolean val = false;
		for (Personne p : this.getChefs()) {
			int i = 0;
			while (val == false && i < p.getCollegues().size()) { // on cherche l'ouvrier a licencier
				if (p.getCollegues().get(i).getClass().getSimpleName().equals("Stock") == true) {
					val = true;
					p.getCollegues().remove(p.getCollegues().get(i));
				}
				i++;
			}
		}
		System.out.println("On debauche un ouvrier stock");
	}
	
	/** indique, si l'on peut le faire, s'il faut debaucher une personne brico. On debauche si le pourcentage de chefs occupes est en dessous d'un certain seuil. On souhaite toujours garder au moins une personne brico
	 * @param seuil la reference de si l'entrepot est bien range ou non
	 * @return true si oui, false sinon
	*/
	public boolean decisionDebaucheBrico(int seuil) { 
		int nbChefBrico = 0;
		int cuisine = 0;
		int chambre = 0;
		int wc = 0;
		int salon = 0;
		int salleDeBain = 0;
		int salleAManger = 0;
		int nbChefActifs = 0;
		for (int i = 0; i < this.getChefs().size(); i++) { // on compte le nombre de chefs et d'ouvriers bricos, et s'il sont actifs ou non
			if (this.getChefs().get(i).getClass().getSimpleName().equals("Brico") == true) {
				nbChefBrico += 1;
				if (this.getChefs().get(i).getTempsRestant() > 0) nbChefActifs += 1;
			}
			for (Personne p : this.getChefs().get(i).getCollegues()) {
				if (p.getClass().getSimpleName().equals("Brico") == true) {
					if (((Brico) p).getSpecialite().equals("CUISINE")) cuisine +=1;
					else if (((Brico) p).getSpecialite().equals("CHAMBRE")) chambre +=1;
					else if (((Brico) p).getSpecialite().equals("SALON")) salon +=1;
					else if (((Brico) p).getSpecialite().equals("WC")) wc +=1;
					else if (((Brico) p).getSpecialite().equals("SALLEAMANGER")) salleAManger +=1;
					else if (((Brico) p).getSpecialite().equals("SALLEDEBAIN")) salleDeBain +=1;
				}
			}
		}
		if (nbChefActifs*100/nbChefBrico <= seuil && nbChefBrico > 1 && cuisine > 1 && chambre > 1 && salon > 1 && salleAManger > 1 && salleDeBain > 1 && wc > 1) return true;
			return false;
	}
	
	/** debauche d'un ouvrier brico 
	 * @param spe specialisation de l'ouvrier a debaucher
	 * @return void
	*/
	public void debaucheBrico(String spe) {
		boolean val = false;
		for (Personne p : this.getChefs()) {
			int i = 0;
			while (val == false && i < p.getCollegues().size()) { // on regarde quel ouvrier il faut licencier
				if (p.getCollegues().get(i).getClass().getSimpleName().equals("Brico") == true && ((Brico) p.getCollegues().get(i)).getSpecialite().equals(spe)) {
					val = true;
					p.getCollegues().remove(p.getCollegues().get(i));
					System.out.println("On debauche un ouvrier brico spécialisé en "+spe);
				}
				i++;
			}
		}
	}
	
	/** debauche d'un chef brico 
	 * @param void
	 * @return void
	*/
	public void debaucheChef() {
		int i = 0;
		boolean val = false;
		while (i < this.getChefs().size() && val == false) {
			if (this.getChefs().get(i).getCollegues().size() == 0) { // on choisit le chef qui n'a pas d'ouvriers
				val = true;
				this.getChefs().remove(this.getChefs().get(i));
			}
			i++;
		}
		System.out.println("On debauche un chef brico");
	}
	
	/** trie par valeurs et de manière décroissante un dictionnaire
	 * @param map le dictionnaire en question
	 * @return Hashmap le meme dictionnaire trie
	*/
	private HashMap sort(HashMap  map) { //trouvée ici : https://waytolearnx.com/2018/11/trier-un-hashmap-par-cle-et-par-valeur-en-java.html
	       List linkedlist = new LinkedList(map.entrySet());
	       Collections.sort(linkedlist, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	                  .compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = linkedlist.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
	
	/** decide de quelle specialite on embauche un nouvel ouvrier brico.
	 * @param void
	 * @return String la specialite choisie
	*/
	public String choixSpeEmbaucheBrico() { 
		int chefsDispo = 0;
		try {
			for (int m = 0; m < this.getChefs().size(); m++) {
				if (this.getChefs().get(m).getTempsRestant() == 0) chefsDispo += 1;	
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("choixSpeEmbaucheBrico de Entrepot, catch n°1");
		}
		HashMap <String, Float> l = new HashMap<String, Float>();
		for (String str : this.getMemoireMaison().keySet()) {
			l.put(str, (float) Integer.parseInt(this.getMemoireMaison().get(str).getTypeLot())/(this.getMemoireMaison().get(str).getVolume() + (float) (1/6)*chefsDispo + (float) 0.1));
			//on determine quelle piece de la maison a le plus besoin d'ouvriers en calculant les taux pieces/nb_ouvriers, puis en triant le dictionnaire
		}
		l = sort(l); 
		Set<String> s = l.keySet();
		ArrayList<String> l2 = new ArrayList<String>(s);
		boolean val = false;
		String choix = "";
		int i = 0;
		while (val == false && i < l2.size()) { //on choisit la premiere piece dans le dictionnaire trie qui n'a pas d'ouvriers disponibles, sinon on choisit simplement la premiere.
			choix = l2.get(i);
			int j = 0;
			boolean val2 = false;
			try {
				while (val2 == false && j < this.getChefs().size()) {
					if (this.getChefs().get(j).getCollegues().size() != 0) {
						for (Personne pers : this.getChefs().get(i).getCollegues() ) {
							if (pers.getClass().getSimpleName().equals("Brico") == true && pers.getTempsRestant() == 0) {
								if (((Brico) pers).getSpecialite().equals(choix)) {
									val2 = true;
									val = true;
								}
							}
						}
					}
					j++;
				}
				i++;
			} catch (IndexOutOfBoundsException e) {
				return "";
			}
		}
		if (val == false) choix = l2.get(0);
		return choix;
	}
	
	/** trie par valeurs et de maniere croissante un dictionnaire
	 * @param map le dictionnaire en question
	 * @return Hashmap le meme dictionnaire trie
	*/
	private HashMap sortReverse(HashMap  map) { //trouvée ici : https://waytolearnx.com/2018/11/trier-un-hashmap-par-cle-et-par-valeur-en-java.html
	       List linkedlist = new LinkedList(map.entrySet());
	       Collections.sort(linkedlist, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o1)).getValue())
	                  .compareTo(((Map.Entry) (o2)).getValue());
	            }
	       });
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = linkedlist.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
	
	/** decide de quelle specialite on debauche un nouvel ouvrier brico.
	 * @param void
	 * @return String la specialite choisie
	*/
	public String choixSpeDebaucheBrico() { 
		int chefsDispo = 0;
		try {
			for (int m = 0; m < this.getChefs().size(); m++) {
				if (this.getChefs().get(m).getTempsRestant() == 0) chefsDispo += 1;	
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("choixSpeDebaucheBrico de Entrepot, catch n°1");
		}
		HashMap <String, Float> l = new HashMap<String, Float>();
		for (String str : this.getMemoireMaison().keySet()) {
			l.put(str, (float) Integer.parseInt(this.getMemoireMaison().get(str).getTypeLot())/(this.getMemoireMaison().get(str).getVolume() + (float) (1/6)*chefsDispo + (float) 0.1));
			//on determine qu'elle piece de la maison a le plus besoin d'ouvriers en calculant les taux pieces/nb_ouvriers, puis en triant le dictionnaire
		}
		l = sortReverse(l); 
		Set<String> s = l.keySet();
		ArrayList<String> l2 = new ArrayList<String>(s);
		boolean val = false;
		int i = 0;
		String choix = "";
		while (val == false && i < l2.size()) { //on choisit la premiere piece dans le dictionnaire trie qui a un ouvrier disponible, sinon on choisit simplement la premiere.
			choix = l2.get(i);
			int j = 0;
			boolean val2 = false;
			try {
				while (val2 == false && j < this.getChefs().size()) {
					for (int k = 0; k < this.getChefs().get(j).getCollegues().size(); k++) {
						if (this.getChefs().get(j).getCollegues().get(k).getClass().getSimpleName().equals("Brico") == true && this.getChefs().get(j).getCollegues().get(k).getTempsRestant() == 0) {
							if (((Brico) this.getChefs().get(j).getCollegues().get(k)).getSpecialite().equals(choix)) {
								val2 = true;
								val = true;
							}
						}
					}
					j++;
				}
				i++;
			} catch (IndexOutOfBoundsException e) {
				System.out.println("choixSpeDebaucheBrico de Entrepot, catch n°2");
				return "";
			}
		}
		if (val == false) choix = l2.get(0);
		return choix;
	}
	
	// METHODES LIEES AUX ACTIONS SUR L'ENTREPOT + AFFICHAGE
	
	/** decrement la tresorerie des salaires du personnel
	 * @param void
	 * @return void
	*/
	public void salairePersonnel() {
		int countOuvrier=0;
		int countChef=chefs.size();
		try {
			for (int i=0;i<chefs.size();i++) {
				countOuvrier+=chefs.get(i).getCollegues().size();
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("salairePersonnel de Entrepot, catch n°1");
		}
		this.treso-=countOuvrier*5+countChef*10;
	}
	
	/** supprime un lot de l'entrepot
	 * @param i la rangee du lot a supprimer, j sa place dans la rangee et volume le volume que l'on souhaite supprimer
	 * @return void
	*/
	public void supp(int i, int j, int volume) {
		this.getHangar()[i].supp(j, volume);
	}

	/** renvoie en pourcentage l'occupation de l'entrepot
	 * @param void
	 * @return int le pourcentage d'occupation de l'entrepot
	*/
	public int plein() { 
		int cpt = 0;
		for (int i = 0; i < this.getM(); i++) {
			ArrayList<Paires> l = this.getHangar()[i].placeDispo();
			for (int j = 0; j < l.size(); j++) {
				if (l.get(j).getTypeLot()!=null) {
					cpt += l.get(j).getVolume();
					}
				
			}
		}
		return 100 - cpt*100/(this.getM()*this.getHangar()[0].getN());
	}

	/** jette le lot le moins utile de l'entrepot, i.e. le lot qui fait baisser la valeur du stock le moins possible
	 * @param void
	 * @return void
	*/
	public void jeterLot() { 
		float cout = 0;
		int nbCollegues = 0;
		try {
			for (Personne pers : this.getChefs()) { // on calcule le cout moyen de deplacement d'une personne stock
				cout += 10 + pers.getCollegues().size()*5;
				nbCollegues += pers.getCollegues().size();
			}
				cout = (float) cout/(nbCollegues + this.getChefs().size());
		} catch (NullPointerException e) {
			System.out.println("jeterLot de Entrepot, catch n°1");
		}
		this.setValeurStock();
		float valStock = this.getValeurStock();
		boolean test = false;
		Lot choix = new Lot(null, 0, 0, 0);
		int ligne = 0;
		int col = 0;
		int k = 0;
		while (k < this.getM() && test == false) {
			int l = 0;
			while (l < this.getHangar()[k].getEtagere().size() && test == false) {
				Lot lo = this.getHangar()[k].getEtagere().get(l);
				if (lo.getNom() != null) {
					test = true;
					choix = lo;
					ligne = k;
					col = l;
				}
				l++;
			}
			k++;
		}
		for (int i = 0; i < this.getM(); i++) {
			for (int j = 0; j< this.getHangar()[i].getEtagere().size(); j++) {
				Lot lot = this.getHangar()[i].getEtagere().get(j);
				if (lot.getReserve() == -1 && lot.getNom() != null) { // on teste seulement les lot qui ne sont pas deja reserves pour des commandes
					float valStock2 = valStock - cout - lot.getPrix()*lot.getVolume();
					if (valStock2 > valStock) {
						valStock = valStock2;
						choix = lot;
						ligne = i;
						col = j;
					}
				}
			}
		}
		this.supp(ligne, col, choix.getVolume());
		System.out.println("On jette un lot car l'entrepot est trop plein.");
	}
	
	/** affecte les personnes stock aux differentes taches, renvoie une liste de 3 entiers : le premier indique le nombre d'ouvriers requisitionnes pour ranger un nouveau lot dans l'entrepot, le deuxieme indique le nb d'ouvriers affectes aux taches logistiques et le troisieme le nb d'ouvriers affectes au rangement.
	 * @param action l'instruction du pas de temps ("rien", "lot", "meuble")
	 * @return (a, b, c) avec a = 0 si action == lot, 1 sinon, b et c des entiers compris entre 0 et le nombre de personnes stock
	*/
	public ArrayList<Integer> decisionStock(String action) {//
		int nbStock = 0;
		int tachesRestantes = 0;
		ArrayList<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < this.getChefs().size(); i++) { //on compte le nombre d'ouvriers (et chefs) stock
			if (this.getChefs().get(i).getClass().getSimpleName().equals("Stock") == true) nbStock += 1;
			if (this.getChefs().get(i).getCollegues().size() != 0) {
				for (int j = 0; j < this.getChefs().get(i).getCollegues().size(); j++) {
					if (this.getChefs().get(i).getCollegues().get(j).getClass().getSimpleName().equals("Stock") == true) nbStock += 1;
				}
			}
		}
		int nbStockIni = nbStock;
		if (action.equals("lot")) { //si on recoit un lot, on affecte un ouvrier pour le ranger
			l.add(1);
			nbStock -= 1;
		}
		else l.add(0);
		for (Commande c : this.getCommandesEnCours()) {
			tachesRestantes += c.getTachesRestantes().size();
		}
		if (tachesRestantes >= nbStock) { //on priorise les commandes au rangement
			l.add(nbStock);
			l.add(0);
		}
		else { //on ne range que si toutes les taches restantes sont prises en charge
			l.add(tachesRestantes);
			l.add(nbStock - tachesRestantes);
		}
		this.setTensionStock(tachesRestantes - nbStockIni);
		return l;
	} 

	/** realise les actions des personnes stock
	 * @param (a, b, c) avec a = 0 si action == lot, 1 sinon, b et c des entiers compris entre 0 et le nombre de personnes stock
	 * @return void
	*/
	public void actionStock(ArrayList<Integer> l) {
		for (Commande c : this.getCommandesEnCours()) {
			while (c.getTachesRestantes().size() != 0 && l.get(1) != 0) {
				l.set(1, l.get(1)-1);
				int ligne = Integer.parseInt(c.getTachesRestantes().getFirst().getEndroit().getTypeLot());
				int col = c.getTachesRestantes().getFirst().getEndroit().getVolume();
				Lot lot = this.getHangar()[ligne].getEtagere().get(col);
				this.supp(ligne, col, c.getTachesRestantes().getFirst().getVolume()); // on recupere les lots de l'entrepot
				this.getStockEnReserve().replace(lot.getNom(), this.getStockEnReserve().get(lot.getNom()) - c.getTachesRestantes().getFirst().getVolume());
				c.getTachesRestantes().remove(c.getTachesRestantes().getFirst());
			}
		}
		int i = 0;
		while (i < this.getM() && l.get(2) != 0) { // on range
			ArrayList<Paires> l2 = this.getHangar()[i].placeDispo();
			while (l2.size() > 2 && l.get(2) != 0) {
				this.getHangar()[i].concat(Integer.parseInt(l2.get(0).getTypeLot()), Integer.parseInt(l2.get(1).getTypeLot()));
				l2 = this.getHangar()[i].placeDispo();
				l.set(2, l.get(2)-1);
			}
			i++;
		}
	}
	
	/** realise les actions des personnes bricos
	 * @param void
	 * @return void
	*/
	public void actionBrico() {
		for(Commande c: this.getCommandesEnCours()) { // update des temps restants
			if(c.getTachesRestantes().size()==0) {
				c.getOuvrier().updateTempsRestant();
				if(c.getOuvrier().getTempsRestant()==0) {
					this.setTreso(this.getTreso()+c.getPrix());
				}
			}
		}
		int cpt=0;
		while (cpt<this.getCommandesEnCours().size()) { // suppression des commandes finies de commandesEnCours
			if(this.getCommandesEnCours().get(cpt).getOuvrier().getTempsRestant()==0) {
				//this.setTreso(this.getCommandesEnCours().get(cpt).getPrix());
				this.getCommandesEffectuees().add(this.getCommandesEnCours().get(cpt));
				this.getCommandesEnCours().remove(cpt);
			}
			else cpt++;
		}
	}
	
	/** renvoie l'inventaire de l'entrepot
	 * @param void
	 * @return HashMap<String, Integer> un dictionnaire de la forme (typeDeLot, quantite)
	*/
	public HashMap<String, Integer> inventaire(){ 
		HashMap<String, Integer> inv = new HashMap<String, Integer>();
		for (int i=0; i < this.getM(); i++) {
			for (int j = 0; j < this.getHangar()[i].getEtagere().size(); j++) {
				if (inv.containsKey(this.getHangar()[i].getEtagere().get(j).getNom()) == false) inv.put(this.getHangar()[i].getEtagere().get(j).getNom(), this.getHangar()[i].getEtagere().get(j).getVolume());
				else inv.put(this.getHangar()[i].getEtagere().get(j).getNom(), this.getHangar()[i].getEtagere().get(j).getVolume() + inv.get(this.getHangar()[i].getEtagere().get(j).getNom()));
			}
		}

		inv.remove(null);
		return inv;

	}

	/** dessine l'entrepot
	 * @param void
	 * @return void
	*/
	public void dessinEntrepot() { //
		int taille = 0;
		for (String str : this.inventaire().keySet()) {
			if (str.length() > taille) taille = str.length();
		}
		if (taille < 4) taille = 4;
		for (int i = 0; i < this.getM(); i++) {
			for (int j = 0; j < this.getHangar()[i].getN(); j++) {
				for (int k = 0; k < taille + 3; k++) {
					System.out.print("-");
				}
			}
			System.out.print("-");
			System.out.println("");
			for (int j = 0; j < this.getHangar()[i].getEtagere().size(); j++) {
				for (int k = 0; k < this.getHangar()[i].getEtagere().get(j).getVolume(); k++) {
					System.out.print("| ");
					if (this.getHangar()[i].getEtagere().get(j).getNom() != null) {
						System.out.print(this.getHangar()[i].getEtagere().get(j).getNom());
						for (int l = 0; l < taille - this.getHangar()[i].getEtagere().get(j).getNom().length() + 1; l++) {
							System.out.print(" ");
						}
					}
					else {
						for (int l = 0; l < taille + 1; l++) {
							System.out.print(" ");
						}
					}
				}
			}
			System.out.println("|");
		}
		for (int l = 0; l < this.getHangar()[0].getN(); l++) {
			for (int k = 0; k < taille + 3; k++) {
				System.out.print("-");
			}
		}
		System.out.print("-");
		System.out.println("");
	}

	/** affichage des informations liees a l'entrepot
	 * @param void
	 * @return void
	*/
	public void infos() { 
		System.out.println("Inventaire de l'entrepot :");

		for (String str : this.inventaire().keySet()) {
			System.out.println(str+" : "+this.inventaire().get(str)); // affichage de l'inventaire
		}
		System.out.println("");

		System.out.println("Tresorerie acutelle : "+treso+"e"); // affichage tresorerie
		System.out.println("");
		System.out.println("Vous avez "+commandesEnCours.size()+" commandes en cours."); // affichage du nombres de commandes en cours et deja effectuees
		System.out.println("");
		System.out.println("Vous avez "+commandesEffectuees.size()+" commandes deja� effectuees.");
		System.out.println("");
		System.out.println("Votre entrepot est rempli a "+this.plein()+"%."); 
		System.out.println("");
		System.out.println("Liste du personnel :");
		for (Personne p : this.getChefs()) { // affichage personnel
			if (p.getClass().getSimpleName().equals("Brico") == true)  System.out.println(((Brico) p).toString());
			else if (p.getClass().getSimpleName().equals("Stock") == true) System.out.println(((Stock) p).toString());
			for (Personne p2 : p.getCollegues()) {
				if (p2.getClass().getSimpleName().equals("Brico") == true)  System.out.println(((Brico) p2).toString());
				else if (p2.getClass().getSimpleName().equals("Stock") == true)  System.out.println(((Stock) p2).toString());
			}
		}
		System.out.println("");
		System.out.println("Voici une representation de l'entrepot : \n");
		this.dessinEntrepot(); 
	}
	
	// METHODES LIEES AUX COMMANDES

	/** determine si une commande peut etre acceptee ou non
	 * @param c la commande en question
	 * @return true si la commande peut etre acceptee, false sinon
	*/
	public boolean accepterCommande(Commande c) {
		boolean brico = false; // avons-nous une personne brico dispo ?
		boolean stock = false; // avons-nous une personne stock ?
		boolean pieces = true; // avons-nous les pieces necessaires ?
		int i = 0;
		while (i < this.getChefs().size() && (brico == false || stock == false)) {
			if (this.getChefs().get(i).getClass().getSimpleName().equals("Stock") == true) {
				stock = true;
				int j = 0;
				while (j < this.getChefs().get(i).getCollegues().size() && brico == false ) {
					if (this.getChefs().get(i).getCollegues().get(j).getClass().getSimpleName().equals("Brico") == true && this.getChefs().get(i).getTempsRestant() == 0) {
						if (((Brico) this.getChefs().get(i).getCollegues().get(j)).getSpecialite().equals(c.getPieceMaison())) brico = true;
					}
					j++;
				}
			}
			if (this.getChefs().get(i).getClass().getSimpleName().equals("Brico") == true && this.getChefs().get(i).getTempsRestant() == 0) {
				brico = true;
				int j = 0;
				while (j < this.getChefs().get(i).getCollegues().size() && stock == false ) {
					if (this.getChefs().get(i).getCollegues().get(j).getClass().getSimpleName().equals("Stock") == true) stock = true;
					j++;
				}
			}
			i++;
		}
		for (Paires p : c.getListePieces()) {
			if (this.getStockEnReserve().containsKey(p.getTypeLot()) == false) {
				if (this.inventaire().containsKey(p.getTypeLot()) == false) pieces = false;
				else {
					if (this.inventaire().get(p.getTypeLot()) < p.getVolume()) pieces = false;
				}
			}
			else {
				if (this.inventaire().get(p.getTypeLot()) - this.getStockEnReserve().get(p.getTypeLot()) < p.getVolume()) pieces = false;
			}
		}
		if (!brico) System.out.println("Commande refusee, manque d'ouvriers brico");
		else if (!pieces) System.out.println("Commande refusee, manque de lots");
		else if (!stock) System.out.println("Commande refusee, manque d'ouvriers stock");
		else System.out.println("Commande acceptee");
		return (stock && brico && pieces);
	}
	
	/** fixe le prix d'une commande, reserve les pieces necessaires et incremente la liste des taches restantes
	 * @param c la commande en question
	 * @return void
	*/
	public void choixPieces(Commande c) { 
		this.getCommandesEnCours().add(c);
		Paires pieces = new Paires(String.valueOf(Integer.parseInt(this.getMemoireMaison().get(c.getPieceMaison()).getTypeLot()) + 1), this.getMemoireMaison().get(c.getPieceMaison()).getVolume());
		this.getMemoireMaison().replace(c.getPieceMaison(), pieces);
		float prix = 0;
		while (c.getListePiecesCpt().size() != 0) { // pour chaque piece
			Paires p = c.getListePiecesCpt().get(0);
			ArrayList<Paires> l = new ArrayList<Paires>(); // on garde tous les lots de la piece en question, on met dans l les paires nous indiquant les emplacements des lots
														   // liste de ("rangee", place_dans_la_rangee). rangee est un int mis dans un string pour pouvoir utiliser Paires.
			for (int i = 0; i < this.getM(); i++) {
				for (int j = 0; j < this.getHangar()[i].getEtagere().size(); j++) {
					if(this.getHangar()[i].getEtagere().get(j).getNom()!=null) {
						if (this.getHangar()[i].getEtagere().get(j).getNom().equals(p.getTypeLot()) && this.getHangar()[i].getEtagere().get(j).getReserve() == -1) {
							Paires possibilites = new Paires(String.valueOf(i), j); //
							l.add(possibilites);
						}
					}
				}
			}
			float cout = 0;
			int nbCollegues = 0;
			try {
				for (Personne pers : this.getChefs()) {
					cout += 10 + pers.getCollegues().size()*5;
					nbCollegues += pers.getCollegues().size();
				}
					cout = (float) cout/(nbCollegues + this.getChefs().size());
			} catch (NullPointerException e) {
				System.out.println("choixPieces de Entrepot, catch n1");
			}
			Paires choix = l.get(0);
			Lot lot = this.getHangar()[Integer.parseInt(choix.getTypeLot())].getEtagere().get(choix.getVolume());
			float tension; // on y met la valeur du stock quand on enleve le premier lot de l'entrepot
			if (lot.getVolume() <= p.getVolume()) { 
				tension = this.getValeurStock() - lot.getVolume()*lot.getPrix() - cout;
				l.remove(choix);
			} else {
				tension = this.getValeurStock() - lot.getVolume()*lot.getPrix();
				l.remove(choix);
			}
			if (l.size() != 0) {
				for (Paires p2 : l) {
					float tension2;
					Lot lot2 = this.getHangar()[Integer.parseInt(p2.getTypeLot())].getEtagere().get(p2.getVolume());
					if (lot2.getVolume() <= p.getVolume()) tension2 = this.getValeurStock() - lot2.getVolume()*lot2.getPrix() - cout; 
					else tension2 = this.getValeurStock() - lot2.getVolume()*lot2.getPrix();
					if (tension2 < tension) { // parmi les lots de l, on choisit celui qui minimise la valeur du stock une fois retire de l'entrepot
						tension = tension2;
						choix = p2;
					}
				}
			}
			lot = this.getHangar()[Integer.parseInt(choix.getTypeLot())].getEtagere().get(choix.getVolume());
			if (lot.getVolume() > p.getVolume()) {
				int indice = this.getHangar()[Integer.parseInt(choix.getTypeLot())].splitLot(lot, p.getVolume());
				choix.setVolume(indice);
			}
			this.getHangar()[Integer.parseInt(choix.getTypeLot())].getEtagere().get(choix.getVolume()).setReserve(c.getId()); // on réserve le lot en question
			if (this.getStockEnReserve().containsKey(p.getTypeLot()) == true) this.getStockEnReserve().put(p.getTypeLot(), choix.getVolume() + this.getStockEnReserve().get(p.getTypeLot()));
			else this.getStockEnReserve().put(p.getTypeLot(), choix.getVolume()); // on incrémente stockEnReserve
			if (this.getHangar()[Integer.parseInt(choix.getTypeLot())].getEtagere().get(choix.getVolume()).getVolume() < p.getVolume()) { // si le lot selectionne est de taille < a la quantite souhaitee, on met à jour la quantite encore necessaire 
				p.setVolume(p.getVolume() - this.getHangar()[Integer.parseInt(choix.getTypeLot())].getEtagere().get(choix.getVolume()).getVolume());
				prix += this.getHangar()[Integer.parseInt(choix.getTypeLot())].getEtagere().get(choix.getVolume()).getPrix() * this.getHangar()[Integer.parseInt(choix.getTypeLot())].getEtagere().get(choix.getVolume()).getVolume();
			} else {
				c.getListePiecesCpt().remove(p); // sinon on passe a la piece suivante.
				prix += this.getHangar()[Integer.parseInt(choix.getTypeLot())].getEtagere().get(choix.getVolume()).getPrix() * p.getVolume();
			}
			Tache t = new Tache(c.getId(), choix, c.getOuvrier(), p.getVolume());
			c.getTachesRestantes().add(t); // on incremente la liste de taches restantes
		}
		c.setPrix(prix); // prix total de la commande
	} 
	
	/** affecte une personne brico a une commande et rajoute la commande dans commande en cours
	 * @param c la commande en question
	 * @return void
	*/
	public void affectationCommande(Commande c) { 
		boolean val = false;
		Personne p = new Personne("chef");
		int i = 0;
		while (i < this.getChefs().size() && val == false) {
			if (this.getChefs().get(i).getClass().getSimpleName().equals("Brico") == true && this.getChefs().get(i).getTempsRestant() == 0) p = this.getChefs().get(i);
			int j = 0;
			while (j < this.getChefs().get(i).getCollegues().size()) { // on prend le premier ouvrier disponible
				if (this.getChefs().get(i).getCollegues().get(j).getClass().getSimpleName().equals("Brico") == true && this.getChefs().get(i).getCollegues().get(j).getTempsRestant() == 0) {
					if (((Brico) this.getChefs().get(i).getCollegues().get(j)).getSpecialite().equals(c.getPieceMaison())) {
						val = true;
						this.getChefs().get(i).getCollegues().get(j).setTempsRestant(c.getTempsConstruction());
						c.affectation((Brico) this.getChefs().get(i).getCollegues().get(j));
					}
				}
				j++;
			}
			i++;
		}
		if (val == false) { // si pas d'ouvrier dispo, on prend un chef
			p.setTempsRestant(c.getTempsConstruction());
			c.affectation(p);
		}
	}
	
	// METHODES LIEES AUX LOTS
	
	/** determine si un lot peut etre accepte ou non
	 * @param l le lot en question
	 * @return true si le lot peut etre accepte, false sinon
	*/
	public boolean accepterLot(Lot l) {
		boolean val = false;
		int i = 0;
		while (i < this.getM() && val == false) { // avons-nous la place pour recevoir le lot ?
			ArrayList<Paires> liste = this.getHangar()[i].placeDispo();
			int j = 0;
			while (j < liste.size() && val == false) {
				if (liste.get(j).getVolume() >= l.getVolume()) val = true;
				j++;
			}
			i++;
		}
		boolean val2 = false;
		int j = 0;
		while (j < this.getChefs().size() && val2 == false) { // avons-nous une personne stock ?
			if (this.getChefs().get(j).getClass().getSimpleName().equals("Stock") == true) val2 = true;
			int k = 0;
			while (k < this.getChefs().get(j).getCollegues().size() && val2 == false) {
				if (this.getChefs().get(i).getCollegues().get(j).getClass().getSimpleName().equals("Stock") == true) val2 = true;
				k++;
			}
			j++;
		}
		if (!val) System.out.println("Lot refuse, manque de place");
		else if (!val2) System.out.println("Lot refuse, manque d'ouvriers stock");
		else System.out.println("Lot accepte");
		return (val && val2);
	}
	
	/** place le lot dans l'entrepot a l'endroit le plus opportun, i.e. celui qui minime le risque de rangement
	 * @param l le lot en question
	 * @return void
	*/
	public void placement(Lot lot) {
		ArrayList<Paires> l = new ArrayList<Paires>();
		for (int i = 0; i < this.getM(); i++) { // on liste les endroits disponibles pour recevoir le lot
			ArrayList<Paires> l2 = this.getHangar()[i].placeDispo();
			for (int j = 0; j < l2.size(); j++) {
				if (l2.get(j).getVolume() >= lot.getVolume()) {
					Paires p = new Paires (String.valueOf(i), Integer.parseInt(l2.get(j).getTypeLot()));
					l.add(p);
				}
			}
		}
		float risque;
		Paires choix = l.get(0);
		int ligne = Integer.parseInt(choix.getTypeLot());
		int col = choix.getVolume();
		this.getHangar()[ligne].placement(lot, col);
		this.setRisque();
		risque = this.getRisque();
		this.supp(ligne, col, lot.getVolume());
		for (Paires p2 : l) { // on teste si l'emplacement est le meilleur ou non
			float risque2;
			ligne = Integer.parseInt(choix.getTypeLot());
			col = choix.getVolume();
			this.getHangar()[ligne].placement(lot, col);
			this.setRisque();
			risque2 = this.getRisque();
			this.supp(ligne, col, lot.getVolume());
			if (risque2 < risque) {
				risque = risque2;
				choix = p2;
			}
		}
		ligne = Integer.parseInt(choix.getTypeLot());
		col = choix.getVolume();
		this.getHangar()[ligne].placement(lot, col);
		this.memoireLot[lot.getVolume()] += 1;
	}
	
	
	// METHODE QUI GENERE UN FICHIER
	
	/*public static void generateFile(String s, int nbLines) {
		Random r = new Random();
		String line;
		//To be parameterized : here we give examples
		final int NOTHING_AT_BEGINNING = nbLines/10;
		final int NOTHING_AT_END = nbLines/10;
		final double PROB_LOT = 0.5;
		final double PROB_MEUBLE = 0.3;
		final int NB_LOTS = nbLines/20;
		final int NB_MEUBLES = nbLines/25;
		final int VOLUME_MIN = 1;
		final int VOLUME_MAX = 4;
		final double POIDS_MAX = 10;
		final double POIDS_MIN = 5;
		final double PRIX_MAX = 100;
		final double PRIX_MIN = 30;
		final int DUREE_CONSTRUCTION_MAX = 3;
		final int NB_MAX_COMPOSITION = 3;
		Lot[] lots = new Lot[NB_LOTS];
		Commande[] meubles = new Commande[NB_MEUBLES];
		for(int i = 0; i < NB_LOTS ; i++) {
			lots[i] = //generate random Lot;
		}
		for(int i = 0; i < NB_MEUBLES ; i++) {
			meubles[i] = //generate random meuble;
		}
		try {
			FileWriter f = new FileWriter(s);
			for(int i =0; i< NOTHING_AT_BEGINNING; i++)
				f.write((i+1) + " rien\n");
			for(int i =0; i < nbLines - NOTHING_AT_BEGINNING - NOTHING_AT_END; i++) {
				double p = r.nextDouble();
				line = String.valueOf(i + NOTHING_AT_BEGINNING + 1);
				if(p < PROB_LOT) {
					int l = r.nextInt(NB_LOTS);
					line =  //Create line for Lot l;
				}
				else{
					if(p < PROB_LOT + PROB_MEUBLE) {
						int m = r.nextInt(NB_MEUBLES);
						line = //create line for meuble m;
					}
					else
						line = line + " rien\n";
				}
				f.write(line);
			}
			for(int i =0; i< NOTHING_AT_END;i++)
				f.write((nbLines - NOTHING_AT_END+ i+1) + " rien\n");
			f.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
}	

