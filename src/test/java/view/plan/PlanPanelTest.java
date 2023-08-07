package view.plan;

import junit.framework.TestCase;
import model.graphs.Plan;
import util.XMLParser;
import view.MainWindow;

import javax.swing.*;
import java.io.File;

public class PlanPanelTest extends TestCase {
	private PlanPanel planPanel;
	private Plan planData;

	public void setUp() throws Exception {
		super.setUp();
		planPanel = new PlanPanel(new MainWindow());
		XMLParser parser = new XMLParser();
		planData = parser.readMap(new File("files/largeMap.xml").getAbsolutePath());
		planPanel.setPlanData(planData);
	}

	/**
	 * Test du scaling des longitudes en coordonnées x sur le composant graphique du plan.
	 * Avec largeMap.xml, les constantes sont:
	 * MaxLon: 4.9075384	MinLon: 4.8314376
	 */
	public void testScaleXCoordinateToPlan() {
		planPanel.setSize(100, 100);
		assertEquals(24, planPanel.scaleXCoordinateToPlan((float)4.85));

		planPanel.setSize(200, 100);
		assertEquals(48, planPanel.scaleXCoordinateToPlan((float)4.85));

		assertEquals(-1, planPanel.scaleXCoordinateToPlan((float)4.8));
		assertEquals(-1, planPanel.scaleXCoordinateToPlan((float)5.0));
	}

	/**
	 * Test du scaling des latitudes en coordonnées y sur le composant graphique du plan.
	 * Avec largeMap.xml, les constantes sont:
	 * MaxLat: 0.9007719    MinLat: 0.899442
	 */
	public void testScaleYCoordinateToPlan() {
		planPanel.setSize(100, 100);
		assertEquals(59, planPanel.scaleYCoordinateToPlan((float)0.9));

		planPanel.setSize(100, 200);
		assertEquals(117, planPanel.scaleYCoordinateToPlan((float)0.9));

		assertEquals(-1, planPanel.scaleYCoordinateToPlan((float)1.0));
		assertEquals(-1, planPanel.scaleYCoordinateToPlan((float)0.89));
	}

	public void testIdentifyStreet() {
		planPanel.setSize(100, 100);
		planPanel.identifyStreet(35, 65);
		assertEquals("Boulevard des Tchécoslovaques", planData.getSelectedStreetName());

		planPanel.identifyStreet(40, 54);
		assertEquals("Rue du Général Mouton-Duvernet", planData.getSelectedStreetName());
	}
}