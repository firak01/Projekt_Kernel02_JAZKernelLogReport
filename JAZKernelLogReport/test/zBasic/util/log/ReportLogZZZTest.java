package zBasic.util.log;

import junit.framework.TestCase;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.log.KernelReportContextProviderZZZ;
import basic.zBasic.util.log.ReportLogCommonZZZ;
import basic.zBasic.util.log.ReportLogZZZ;
import basic.zKernel.KernelZZZ;

public class ReportLogZZZTest extends TestCase {
	// +++ Test setup
	private static boolean doCleanup = true; // default = true false -> kein
												// Aufr�umen um tearDown().

	// Kernel und Log-Objekt
	private KernelZZZ objKernel = null;

	// Das zu testende Objekt
	// Merke: ReportLogZZZ hat nur statische Methoden, die man von aussen testen
	// kann

	protected void setUp() {
		try {
			// Kernel + Log - Object dem TestFixture hinzuf�gen. Siehe
			// test.zzzKernel.KernelZZZTest
			objKernel = new KernelZZZ("TEST", "01", "",
					"ZKernelConfigReportLog_test.ini", (String) null);

			// Weil die ReportLogZZZ-Klasse keinen Konstruktor hat, wird ein
			// ContextProvider verwendet
			KernelReportContextProviderZZZ objContext = new KernelReportContextProviderZZZ(
					objKernel, ReportLogZZZ.class.getName(), this.getClass()
							.getName());
			ReportLogZZZ.loadKernelContext(objContext, true); // Mit dem true
																// bewirkt man,
																// dass das file
																// immer neu aus
																// dem
																// ConfigurationsPattern
																// erzeugt wird.

		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		}

	}// END setup

	public void tearDown() throws Exception {
		if (doCleanup) {
			cleanUp();
		}
	}

	/**************************************************************************/
	/**** diese Aufr�um-Methode muss mit Leben gef�llt werden *****************/
	/**************************************************************************/
	private void cleanUp() {
		// do your cleanup Work
		// this.objContextTest.recycle();

		// Merke: Es wird bei Erzeugung des DJAgentContext immer ein
		// Noesdocument k�nstlich erzeugt.
		// Dies kann man ggf. hier l�schen.
		/*
		 * if (nlDoc != null) { try { nlDoc.remove(true); } catch
		 * (NotesException e) { e.printStackTrace(); } }
		 */
	}

	// ###################################################
	// Die Tests
	/*
	 * weil es keinen public Konstruktor gibt, auskommentiert public void
	 * testContructor(){
	 * System.out.println(ReflectionZZZ.getMethodCurrentName()+"#Start");
	 * 
	 * }//END testConstructor
	 */

	public void testLogLevel() {
		try {
			// Den Log Level aus dem Konfigurationsfile auslesen
			int itemp = ReportLogZZZ.readLogLevel();

			ReportLogZZZ.setLogLevel(itemp);
			int itemp2 = ReportLogZZZ.getLogLevel();
			assertEquals(itemp2, itemp);
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}

	public void testWriting() {
		ReportLogZZZ.write(ReportLogZZZ.DEBUG, "Das ist ein Test");

		ReportLogZZZ.write(ReportLogZZZ.DEBUG, "Zweiter Eintrag -TEST");
	}

}// END class

