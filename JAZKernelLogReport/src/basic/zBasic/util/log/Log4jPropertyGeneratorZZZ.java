/**
 * 
 */
package basic.zBasic.util.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;

/** Diese Klasse soll falls notwendig ein Datei erzeugen, die von log4j als Konfigurationsdatei verwendet werden kann.
 * Die Details für Konfigurationswerte werden vom KernelContextProvider übergeben.
 * @author 0823
 *
 */
public class Log4jPropertyGeneratorZZZ implements IConstantZZZ{
	public Log4jPropertyGeneratorZZZ(){
		//private Konstruktor zum Verbergen. === > nut static Methoden einsetzen
		//ABER: Klassen mit private Konstruktor lassen sich nicht vererben. Daher wieder public !!!
	}
	
	/** Es werden im PatternFile die Platzhalter ersetzt. 
	 * @param PatternString
	 * @return String mit ersetzten Platzhaltern
	 * @throws ExceptionZZZ 
	 */

	public static String replacePlaceholderAll(KernelReportContextProviderZZZ objContext , String sInput) throws ExceptionZZZ {
		String sReturn = sInput;

		String sLevelName = objContext.getLog4jLevel();
		sReturn = StringZZZ.replace(sReturn, "@@LEVEL@@", sLevelName);
		
		return sReturn;
	}
	
	/**Erstellt ein log3j Konfigurationsfile, entweder basierend auf einem konfigurierten PatternFile oder auf den default-String.
	* @return boolean
	* @param objContext
	* @throws ExceptionZZZ 
	* 
	* lindhaueradmin; 06.11.2006 08:47:22
	 */
	public static boolean createFile(KernelReportContextProviderZZZ objContext) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{
			try{ 					
			//Erstelle ggf. das Verzeichnis
			String sDir = objContext.getLog4jPathConfig();
			if(StringZZZ.isEmpty(sDir)){
				sDir = ".";
			}else{
				FileEasyZZZ.makeDirectory(sDir);
			}
		
						
			//Filewriter für die Datei
			String sFile = objContext.getLog4jFileConfig();
			if(StringZZZ.isEmpty(sFile)){
				ExceptionZZZ ez = new ExceptionZZZ("Log4jFile not configured as a property in the configuration.", iERROR_CONFIGURATION_MISSING, null, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			FileWriter fw = new FileWriter(sDir + "\\" + sFile);
			
			
			//+++ Den zu schreibenden Inhalt
			//Hole ggf. ein Pattern aus der Configuration
			String sFilePattern = objContext.getLog4jFilePattern();
			String sDirPattern = null;
			String sContentPattern = "";
			if(!StringZZZ.isEmpty(sFilePattern)){
				//Es wird sonst die default Konfiguration genommen
				//ExceptionZZZ ez = new ExceptionZZZ("Log4jPatternFile not configured as a property in the configuration.", iERROR_CONFIGURATION_MISSING, null, ReflectionZZZ.getMethodCurrentName());
				//throw ez;
				
				sDirPattern = objContext.getLog4jPathPattern();		
				if(StringZZZ.isEmpty(sDirPattern)){
					sDirPattern = ".";
				}
				
				
				String stemp = new String(sDirPattern + "\\" + sFilePattern);
				FileReader objFR = new FileReader(stemp);
				BufferedReader objBFR= new BufferedReader(objFR);
				
				String sLine = null;
				sLine = objBFR.readLine();	
				if(sLine!=null) sContentPattern = sLine;
				sLine = objBFR.readLine();
				while(sLine!=null){
					//Zeile anhängen
					sContentPattern = sContentPattern + "\n" + sLine;
					sLine = objBFR.readLine();
				}
			}else{ 
				sContentPattern = getDefaultConfigPatternString();
			}
			
			//Die Platzhalter aus dem "Pattern" ersetzen
			String sContent = replacePlaceholderAll (objContext, sContentPattern); 
			fw.write(sContent);
			fw.flush(); 
			fw.close();
			 //TSA-JAVA0174 
			fw = null;
			
			bReturn = true;
			}catch(IOException e){
				ExceptionZZZ ez = new ExceptionZZZ(e.getMessage(), iERROR_RUNTIME, null, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
		}//END main:
		return bReturn;
	} 
	

	/**Entfernt das log4j Konfigurationsfile.
	 * Diese Methode wird intensiv in den JUnit-Tests verwendet (im teardown)
	* @param objContext
	* @param bRemoveDirectoryEmpty
	* @return boolean
	* @throws ExceptionZZZ
	* 
	* lindhaueradmin; 06.11.2006 08:51:47
	 */
	public static boolean removeFile(KernelReportContextProviderZZZ objContext, boolean bRemoveDirectoryEmpty) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{
//				Löschen der Datei
				String sFile = objContext.getLog4jFileConfig();
				if(StringZZZ.isEmpty(sFile)) break main;
				
				String sDir = objContext.getLog4jPathConfig();
				if(StringZZZ.isEmpty(sDir)){
					bReturn = FileEasyZZZ.removeFile(sFile);
				}else{
					bReturn = FileEasyZZZ.removeFile(sDir + "\\" + sFile);
				}				
				if(bRemoveDirectoryEmpty==false) break main;
				if(bReturn == false) break main;
				
				//### Lösche ggf. das Verzeichnis
				if(sDir.equals(".")) break main;
				
				boolean btemp = FileEasyZZZ.exists(sDir);
				if(btemp==true){
						btemp = FileEasyZZZ.isDirectoryEmpty(sDir);
						if(btemp == true){
							btemp = FileEasyZZZ.removeDirectory(sDir);						
						}
					}
		}//END main:
		return bReturn;
	}
	
	/**Default Konfiguration für log4j .
     * Alternativ ist eine Konfiguration über ein Pattern-File möglich.
	 * @return String
	 *
	 * javadoc created by: 0823, 06.11.2006 - 13:53:29
	 */
	public static String getDefaultConfigPatternString(){
		String sReturn = "#\n"
			 + "# Automatisch durch Log4jPropertyGeneratorZZZ aus Log4jPropertyGeneratorZZZ.defaultProperies generiertes property file für log4j.\n"
			 + "# Alternativ ist eine Konfiguration über ein Pattern-File möglich.\n"
			 + "# Merke: In den 'Pattern' für die Konfiguration sind immer Platzhalter mit @@xyz@@ vorgesehen,\n"
			 + "#             die Werte für diese Platzhalter werden in der (private) Methode .replacePlaceHolderAll(..) der Log4jPropertyGenaratorZZZ - Klasse ersetzt.\n"
			 + "#\n"		 
			 + "\n Merke: Bei Verwendung anderer Appender, sind diese ergänzend hinter STDOUTLOGGER oder alternativ dazu zu konfigurieren.\n"
			 + "log4j.rootLogger=@@LEVEL@@, STDOUTLOGGER\n"
			 + "\n"
			 + "#################################################################\n"
			 + "### Definition for Stdout logger\n"
			 + "#################################################################\n"
			 + "\n"
			 + "log4j.appender.STDOUTLOGGER=org.apache.log4j.ConsoleAppender\n"
			 + "log4j.appender.STDOUTLOGGER.layout=org.apache.log4j.PatternLayout\n"
			 + "\n"
			 + "# Pattern to output the caller's file name and line number.\n"
			 + "log4j.appender.STDOUTLOGGER.layout.ConversionPattern=%d [%t] %-5p %c - %m%n\n"
			 + "\n"
			 + "\n";
		return sReturn;
	}
}//END class
