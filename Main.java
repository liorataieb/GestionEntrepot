package biilomo;
import java.util.*;
import java.io.*;
/** classe main ou on realise la simulation.
 * @author Liora Taieb et Hamza Touzani
 * @version 1.0
*/
public class Main {
	
	private static String currentLine;   // ligne du fichier ou du terminal actuelle
	private static String[] currentOrder;  // ligne traduite en instructions : Commande / Lot / rien + donn�es qui vont avec
	private static int lineCount;  // compteur ligne en cours : utile pour indiquer les erreurs dans un fichier !
	private static int temps;// compteurs pas de temps
	private static int seuil; // seuil de risque pour l'embauche
	private static double seuilRisque; //seuil de risque pour la debauche
	private static int seuilHangar; // seuil d'occupation de l'entrepot
	private static int modeEmbauche; // 1 si bricos, stock sinon
	
	/** setter risques pour la simulation
	 * @param String mode ( 1 : risques par defaut , al�atoire sinon )
	 * @return void
	 */
	public static void setRisques(String mode) {
		if (mode.equals("1")) {
			seuil=90;
			seuilRisque=50;
			seuilHangar=90;
		}
		else {
		seuil=(int)Math.random()*10;
		seuilRisque=Math.random()*10;
		seuilHangar=90;
		}
		
	}
	
	/** test si la pieceMaison donnee par la console est valide
	 * @param String s
	 * @return boolean
	 */
	public static boolean testMaison(String s) { // TEST SI LA PIECE DE LA MAISON EXISTE
		String maison[]= {"CUISINE","CHAMBRE","SALLEAMANGER","SALON","SALLEDEBAIN","WC"};
		for(String test : maison) {
			if (test.equals(s)){
				return true;
			}
		}
		return false;	
		
	}
	
	/** lecture d'une ligne du fichier et conversion en instruction
	 * @param Scanner scanner
	 * @return void
	 */
	public static void traitementLigneFile(Scanner scanner) {  // return l'instruction atraiter ce pas de temps ( a partir d'un fichier )
			currentLine=scanner.nextLine();      
			currentOrder=currentLine.split(" ");   // instructions sous forme d'un tableau de parametres 
		
			
	}
	
	/** lecture d'une instruction a partir de la console
	 * @param Scanner scanner
	 * @return void
	 */
	public static void traitementLigneCommand(Scanner scanner) {  // return l'instruction a traiter ce pas de temps ( a partir de la ligne de commande )
		currentLine="1 "; // ajoute un "id" de ligne obsolete pour garder la meme forme  de currentOrder a traiter dans simulation().
		System.out.println("Quelles instructions : rien ou lot ou meuble (commande)");
		
		String currentMot=scanner.next();
		currentLine+=currentMot+" ";
		if (currentMot.equals("rien")) {
			
		}
		if (currentMot.equals("lot")) {
			System.out.println("Specifiez dans l'ordre : nom\n poids\n prix\n volume");
			for (int i=0;i<4;i++) {
				currentLine+=scanner.next()+" ";
			}
		}
		if (currentMot.equals("meuble")) {
			System.out.println("Specifiez le nom du meuble :");
			currentLine+=scanner.next()+" ";
			System.out.println("Specifiez la piece de la maison (en maj): SALON |SALLEAMANGER | SALLEDEBAIN | WC | CUISINE | CHAMBRE");
			boolean test=false;
			do {String currentPiece=scanner.next();  
			if(testMaison(currentPiece)) {
				test=true;
				currentLine+=currentPiece+" ";
			}
			else{System.out.println("Piece de la maison inexistante ! Corrigez:");}}
			while(test==false);
			System.out.println("Quelle est la duree de construction?");
			
			try {
				int duree=scanner.nextInt();
				currentLine+=Integer.toString(duree)+" ";
			}catch(Exception e) {
				System.out.println("Erreur ! Ce n'est pas un entier");
			}
			System.out.println("Combien de pieces differentes sont necessaires? :");

			try {
				int nb=scanner.nextInt();
				for(int i=1;i<=nb;i++) {
					System.out.println("type de pieces "+i+" et volume: ");
					currentLine+=scanner.next()+" ";
					currentLine+=scanner.next()+" ";
				}
			}
			catch(Exception e) {System.out.println(" Erreur ! ce n'est pas un entier !");}
		}
		currentOrder=currentLine.split(" ");}
		
	
		
	/** 1 pas de temps de la simulation de l'evolution de l'entrepot
	 * @param Entrepot e
	 * @return void
	 */
	public static void simulation(Entrepot entrepot) {
		lineCount++;
	
		try {
			if (temps==1) {  // au temps 1 on embauche un chef stock
				entrepot.embaucheChefStock();
			}
			if (temps==2) { // au temps 2 on embauche un chef brico
				entrepot.embaucheChef();
			}
			if (temps==3) entrepot.embaucheBrico("CUISINE");
			if (temps==4) entrepot.embaucheBrico("SALON");
			if (temps==5) entrepot.embaucheBrico("WC");
			if (temps==6) entrepot.embaucheBrico("SALLEAMANGER");
			if (temps==7) entrepot.embaucheBrico("CHAMBRE");
			if (temps==8) entrepot.embaucheBrico("SALLEDEBAIN");
		
		if (currentOrder[1].equals("meuble")) {  // Quand on recoit une commande de meuble
			// ArrayList<Commande> comCours=entrepot.getCommandesEnCours();
	
			if (testMaison(currentOrder[3])) {
				LinkedList<Paires> pieces= new LinkedList<Paires>();
				for (int i=5;i<currentOrder.length;i+=2) {					
					pieces.add(new Paires(currentOrder[i],Integer.parseInt(currentOrder[i+1])));
					
				}
				Commande comCours=new Commande(currentOrder[2],currentOrder[3],Integer.parseInt(currentOrder[4]),pieces);
				
				if (entrepot.accepterCommande(comCours)) {
					entrepot.affectationCommande(comCours);
					entrepot.choixPieces(comCours);
				}
			}
			else { System.out.println("Piece de la maison inexistante pour la commande ligne :"+lineCount+
					"\nOn considere qu'aucune commande n'a ete recue.");}
		}
		else if (currentOrder[1].equals("rien")) {
			System.out.println("On ne reçoit rien à ce pas de temps");
			
		}
		
		else if (currentOrder[1].equals("lot")) {  // si c'est un lot que l'on recoit
			Lot newLot= new Lot(currentOrder[2],Float.parseFloat(currentOrder[3]),Float.parseFloat(currentOrder[4]), 
					Integer.parseInt(currentOrder[5])); // creation du lot 
			
			if (entrepot.accepterLot(newLot)) { // on place le lot si on a la place
				entrepot.placement(newLot);
			}
		}
		else {
			System.out.println("Instruction non reconnu ! Rappel : lot meuble ou rien\n Ligne:"+lineCount);}
		
		ArrayList<Integer> l=entrepot.decisionStock(currentOrder[1]);
		
		entrepot.actionBrico();
		entrepot.actionStock(l);
		entrepot.salairePersonnel();
		
		
		//PHASE 3
		 if(temps>8) {
			if(entrepot.plein()>=seuilHangar){entrepot.jeterLot(); }
			if (modeEmbauche==1  && temps%3!=0) {
				if(entrepot.decisionEmbaucheBrico(seuil)) {
					if(entrepot.embaucheChefOuOuv().equals("chef")) {
						entrepot.embaucheChef();
					}
					else {
						entrepot.embaucheBrico(entrepot.choixSpeEmbaucheBrico());
					}
					
				}
				else {
					if (entrepot.decisionEmbaucheStock()){
						if(entrepot.embaucheChefOuOuv().equals("chef")) {
							entrepot.embaucheChef();
						}
						else entrepot.embaucheStock();
					}	
				}
				
				if (entrepot.decisionDebaucheBrico(100-seuil)){
					if (entrepot.debaucheChefOuOuv().equals("chef")) {
						entrepot.debaucheChef();
					}
					else {
						String str = entrepot.choixSpeDebaucheBrico();
						entrepot.debaucheBrico(str);
						
					}
				}
		
				else {
					if(entrepot.decisionDebaucheStock((float)seuilRisque)) {
						if(entrepot.debaucheChefOuOuv().equals("chef")) {
							entrepot.debaucheChef();
						}
						else entrepot.debaucheStock();
					}
				}
			}
			
			else {
				
				if (entrepot.decisionEmbaucheStock()){
					if(entrepot.embaucheChefOuOuv().equals("chef")) {
						entrepot.embaucheChef();
					}
					else entrepot.embaucheStock();
				}
				else {
					if(entrepot.decisionEmbaucheBrico(seuil)) {
						if(entrepot.embaucheChefOuOuv().equals("chef")) {
							entrepot.embaucheChef();
						}
						else {
							entrepot.embaucheBrico(entrepot.choixSpeEmbaucheBrico());
						}
						
					}	
				}
				if(entrepot.decisionDebaucheStock((float)seuilRisque)) {
					if(entrepot.debaucheChefOuOuv().equals("chef")) {
						entrepot.debaucheChef();
					}
					else entrepot.debaucheStock();
				}
				else {
					if (entrepot.decisionDebaucheBrico(100-seuil)){
						if (entrepot.debaucheChefOuOuv().equals("chef")) {
							entrepot.debaucheChef();
						}
						else {
							String str = entrepot.choixSpeDebaucheBrico();
							entrepot.debaucheBrico(str);
							
						}
					}
				}	
			}
		 }
		 } catch (Exception e) {
			 System.out.println("Une erreur s'est produite \n Ligne : "+lineCount);
		 }
	}

	/** Simulation a partir d'un fichier ( Scanner input file )
	 * @param Scanner scanner, Entrepot entrepot
	 * @return void
	 */
	public static void simulationFile(Scanner scanner,Entrepot entrepot) { // Simulation a partir d'un fichier
		while (scanner.hasNextLine()){
			temps+=1;
			traitementLigneFile(scanner);
			
			simulation(entrepot);
		}
		entrepot.infos();	
	}
	
	/** Simulation entiere a partir de la console ( scanner input System.in )
	 * @param Scanner scanner, Entrepot entrepot
	 * @return void
	 */
	public static void simulationCommandLine(Scanner scanner, Entrepot entrepot) { // Simulation a partir du terminal
		
		boolean end=false;
		while(end==false) {
			temps+=1;
			System.out.println("Entrez la ligne d'instruction :");
			traitementLigneCommand(scanner);
			simulation(entrepot);
			System.out.println("Voulez vous voir l'etat de l'entrepot actuel? tapez \"ETAT\" sinon tapez n'importe quoi d'autre");
			String etat=scanner.next();
			if (etat.equals("ETAT")) {
				entrepot.infos();
			}
			System.out.println("Voulez vous finir la simulation? si oui ecrivez \"FIN\" sinon tapez une autre lettre");
			String fin=scanner.next();
			
			if (fin.equals("FIN")) {
				end=true;
				}
			}
		return;
	}

	/** main a lancer en premier
	 * @param String fichier ( optionnel )
	 * @return void
	 */
	public static void main(String[] args) {
		Scanner input= new Scanner(System.in); // On lance le stream d'input
		System.out.println("Donner les dimensions de l'entrepot : \nnombre de rangees m :\n ");
		int m=input.nextInt();  // Nombre de rangees
		System.out.println("Taille des rangees :\n ");
		int n=input.nextInt(); // Taille des rangees
		System.out.println("Tresorerie initiale :\n");
		float treso=input.nextFloat(); // Treso initiale
		Entrepot entrepot = new Entrepot(m,n,treso); // Initialisation de l'entrepot
		System.out.println("Des seuils de risques sont a prendre en compte dans la simulation : generation aleatoire ou risques par d�faut? tapez 1 pour par d�faut sinon autre touche ");
		String mode=input.next();
		setRisques(mode);
		System.out.println("On propose de choisir une priorite a l'embauche :ouvriers bricos ou ouvriers stocks? tapez 1 pour bricos ou autre touche pour stocks");
		String md=input.next();
		if(md.equals("1"))modeEmbauche=1;
		else modeEmbauche=2;
		if (args.length>0) { // y a t-il un argument?
		try {
            Scanner scanner = new Scanner(new File(args[0]));
            simulationFile(scanner, entrepot);
            scanner.close();
        } catch (FileNotFoundException e) {  // Si mauvais nom/path vers le fichier
        	System.out.println("Pas de fichier de ce nom");
            e.printStackTrace();
        } }
		else {
			simulationCommandLine(input, entrepot);
			input.close();
		}
	}

}
