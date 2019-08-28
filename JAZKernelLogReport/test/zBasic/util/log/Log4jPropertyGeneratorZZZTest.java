/**
 * 
 */
package zBasic.util.log;

import junit.framework.TestCase;
import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.log.KernelReportContextProviderZZZ;
import basic.zBasic.util.log.Log4jPropertyGeneratorZZZ;
import basic.zBasic.util.log.ReportLogZZZ;
import basic.zKernel.KernelZZZ;
 
/**
 * @author 0823
 * 
 */
public class Log4jPropertyGeneratorZZZTest extends TestCase {
	// +++ Test setup
	private static boolean doCleanup = true; // default = true false -> kein
												// Aufräumen um tearDown().

	// Kernel und Log-Objekt
	private KernelZZZ objKernel = null;
	private KernelReportContextProviderZZZ objContext = null;

	// Das zu testende Objekt
	// Merke: Log4jPropertyGeneratorZZZ hat nur statische Methoden, dei man von
	// aussen testen kann

	protected void setUp() {
		try {
			// Kernel + Log - Object dem TestFixture hinzuf�gen. Siehe
			// test.zzzKernel.KernelZZZTest
			objKernel = new KernelZZZ("TEST", "01", "test",
					"ZKernelConfigReportLog_test.ini", (String) null);

			// Weil die ReportLogZZZ-Klasse keinen Konstruktor hat, wird ein
			// ContextProvider verwendet.
			objContext = new KernelReportContextProviderZZZ(objKernel,
					ReportLogZZZ.class.getName(), this.getClass().getName());

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
		try {
			Log4jPropertyGeneratorZZZ.removeFile(objContext, true);
		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		}
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

	public void testFileHandling() {
		try {
			boolean btemp = Log4jPropertyGeneratorZZZ.createFile(objContext);
			assertTrue(btemp);

			btemp = Log4jPropertyGeneratorZZZ.removeFile(objContext, true);
			assertTrue(btemp);

		} catch (ExceptionZZZ ez) {
			fail("Method throws an exception." + ez.getMessageLast());
		}
	}

}// END class
