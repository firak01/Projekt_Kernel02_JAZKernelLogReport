package basic.zBasic.util.log;

import org.apache.log4j.Level;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;

/**Dies Klasse enthält nur statische MEthodnen und Konstanten, die dann von allen ...ReportLog-Klassen genutzt werden können.
 * @author lindhaueradmin
 *
 */
public class ReportLogCommonZZZ implements IConstantZZZ, IReportLogConstantZZZ{	
	/** Konvertiert die Konstanten aus ReportLogCommonZZZ in Level Objekte aus Log4J
	 * @param source
	 * @return int
	 */
	public static final Level internalToLog4jLevel (int source) {
		switch (source) {
			case (ERROR) : return Level.FATAL;
			case (WARN) : return Level.WARN; 
			case (INFO) : return Level.INFO; 
			case (DEBUG) : return Level.DEBUG; 
			case (DEBUG2) : return Level.ALL; 
			case (DEBUG3) : return Level.ALL; 
			default : return Level.ALL; 
		}
	}
	
	/** Konvertiert einen String, der in Log4jLevel der Kernel-ini-Konfiguration angegeben worden ist in eine integer Zahl, die den LogLevel-Konstanten entspricht.
	* @return int
	* @param sLevel
	* 
	* lindhaueradmin; 05.11.2006 10:07:58
	 * @throws ExceptionZZZ 
	 */
	public static final int configToReportLogConstantLevel(String sLevel) throws ExceptionZZZ{
		int iReturn=-99;
		main:{
			if(StringZZZ.isEmpty(sLevel)){
				ExceptionZZZ ez = new ExceptionZZZ("Level String", iERROR_PARAMETER_MISSING, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
			
			if(StringZZZ.isNumeric(sLevel)){
				iReturn = Integer.parseInt(sLevel);
				//TODO: Ggf ist noch eine überprüfung auf den konkreten wert notwendig, d.h. die Zahl muss einer der Konstanten entsprechen
			}else{
				if(sLevel.equalsIgnoreCase("FATAL") | sLevel.equalsIgnoreCase("ERROR")){
					iReturn = ReportLogCommonZZZ.ERROR;
				}else if(sLevel.equalsIgnoreCase("WARN")){
					iReturn = ReportLogCommonZZZ.WARN;
				}else if(sLevel.equalsIgnoreCase("INFO")){
					iReturn = ReportLogCommonZZZ.INFO;
				}else if(sLevel.equalsIgnoreCase("DEBUG")){
					iReturn = ReportLogCommonZZZ.DEBUG;
				}else if(sLevel.equalsIgnoreCase("'DEBUG2")){
					iReturn = ReportLogCommonZZZ.DEBUG2;
				}else if(sLevel.equalsIgnoreCase("DEBUG3")){
					iReturn = ReportLogCommonZZZ.DEBUG3;
				}else{
					iReturn = ReportLogCommonZZZ.DEBUG3;
				}
			}			
		}//END main:
		return iReturn;
	}
	
	/**Gibt einen für Log4j gültigen String zurück, wie er z.B. im derm Log4j-Konfigurations-File verwendet werden kann
	* @return String
	* @param iReportLogLevel
	* 
	* lindhaueradmin; 05.11.2006 10:22:00
	 */
	public static final String ReportLogCommonConstantToLog4jLevelString(int iReportLogLevel){
		String sReturn;
		main:{
			switch(iReportLogLevel){
				case (ReportLogCommonZZZ.ERROR) : sReturn = "FATAL"; break;
				case (ReportLogCommonZZZ.WARN) : sReturn = "WARN"; break;
				case (ReportLogCommonZZZ.INFO) :  sReturn = "INFO"; break;
				case (ReportLogCommonZZZ.DEBUG) : sReturn = "DEBUG"; break;
				case (ReportLogCommonZZZ.DEBUG2) :sReturn = "ALL"; break;
				case (ReportLogCommonZZZ.DEBUG3) : sReturn = "ALL"; break;
				default: sReturn = "ALL"; break;
			}
		}
		return sReturn;
	}
	
	/**
	 * Erzeugt Symbole, um im Log die verschiedenen Level unterscheiden zu können.
	 * @param i
	 * @return
	 */
	public static String logSymbol (int i) {
		switch (i) {
			case ReportLogCommonZZZ.ERROR:
				return "[!!!]"; 
			case ReportLogCommonZZZ.WARN:
				return "[ ! ]";
			case ReportLogCommonZZZ.INFO:
				return "[ - ]";
			default:
				return "     ";
		}
	}
}
