package biilomo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCaseEntrepot {
	private Entrepot entrepotUnderTest;
	
	
	
	@BeforeEach
	public void initEntrepot() {
		entrepotUnderTest=new Entrepot(10,10,500);
	}
	
	@AfterEach
	public void undefEntrepot() {
		entrepotUnderTest=null;
	}
	
	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void embaucheChef_embauche_listeChefSizePlus1() {
		int i=entrepotUnderTest.getChefs().size();
		entrepotUnderTest.embaucheChef();
		assertEquals(i+1,entrepotUnderTest.getChefs().size());
			
	}
	
	@Test
	public void affectationCommande_commandeAcceptee_AffectationNonNulle() {
		Commande test=new Commande("test","SALON",4,new LinkedList<Paires>());
		entrepotUnderTest.affectationCommande(test);
		assertNotSame(null,test.getOuvrier());
	}
	
	@Test
	public void debaucheChef_debauche__listeChefsSizeMinus1() {
		int i=entrepotUnderTest.getChefs().size();
		entrepotUnderTest.debaucheChef();
		assertEquals(i-1,entrepotUnderTest.getChefs().size());
	}
	
	public void choixPieces_commandeAcceptee_allChanges() {
		Commande test=new Commande("test","SALON",4,new LinkedList<Paires>());
		int i=entrepotUnderTest.getCommandesEnCours().size();
		entrepotUnderTest.choixPieces(test);
		assertEquals(i+1,entrepotUnderTest.getCommandesEnCours().size());
		assertNotSame(null,test.getTachesRestantes());
				
	}

}
