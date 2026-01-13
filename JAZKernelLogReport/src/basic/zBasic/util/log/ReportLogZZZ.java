package basic.zBasic.util.log;

/*
 * Die Sourcecodes, die diesem Buch als Beispiele beiliegen, sind
 * Copyright (c) 2006 - Thomas Ekert. Alle Rechte vorbehalten.
 * 
 * Trotz sorgf�ltiger Kontrolle sind Fehler in Softwareprodukten nie vollst�ndig auszuschlie�en.
 * Die Sourcodes werden in Ihrem Originalzustand ausgeliefert.
 * Anspr�che auf Anpassung, Weiterentwicklung, Fehlerbehebung, Support
 * oder sonstige wie auch immer gearteten Leistungen oder Haftung sind ausgeschlossen.
 * Sie d�rfen kommerziell genutzt, weiterverarbeitet oder weitervertrieben werden.
 * Voraussetzung hierf�r ist, dass f�r jeden beteiligten Entwickler, jeweils mindestens
 * ein Exemplar dieses Buches in seiner aktuellen Version als gekauftes Exemplar vorliegt.
 */
import java.io.*;
import java.util.*; 
import org.apache.log4j.*;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;

/** Ausgabe Klasse fuer Debug-Informationen
 * 
 * Kapselt einen Log4J Logger
 * Ausgabe Loglevel wird durch die Konstante DO_NOT_LOG_BEYOND_THIS beschr�nkt.
 * Log4J ist selbstkonfigurierend. Das zugeh�rige Config File wird (sofern 
 * noch nicht vorhanden) in <i>user.dir</i>/java angelegt
 * und kann dort dann auch modifiziert werden.
 * Vorbereitet ist ein NotesLogAppender, der in eine Notes Datenbank logged.
 * Diesr muss im Config File aktiviert werden.
 * Stellt einige hilfreiche Ausgabe Methoden für z.B. Enumeration, Vector, Exception
 * oder Document bereit
 * Falls getTraceStatus()==true, dann wird jeder ausgabe ein TracePfad vorangestellt,
 * so dass jeder Ausgabe die zugehörige Methode angesehen werden kann.
 * 
 * @author Thomas Ekert
 * 
 *
 * Erweitert um FGL-Kernel-Funktionalitä�t
 * 20061021, Fritz Lindhauer
 *
 * Urspr�nglicher Name DJLog.
 * Rausgenommene/ge�nderte Methoden:
 * -writeDocument - entfernt
 * -writeException  - NotesException-Teil entfernt
 * 
 * 
 */
//TSA-JAVA0136
public class ReportLogZZZ implements IConstantZZZ, IReportLogConstantZZZ {
	// --- Singleton Instanz ---
	//muss als Singleton static sein. //Muss in der Konkreten Manager Klasse definiert sein, da ja unterschiedlich
	protected static ReportLogZZZ objLogINSTANCE = new ReportLogZZZ(); //muss static sein, wg. getInstance()!!!
	
	// --- Weitere Objekte ---
	private Logger logger = null;
	private String basePath = null;
	private String configFilename=null;
	 
	//FGL Erweiterungen / Änderungen
	private KernelReportContextProviderZZZ objContext=null;
	 
	int currentLogLevel = ReportLogCommonZZZ.NOT_LOADED;
	private static boolean showTrace = true;
	
	/** Holt eine referenz auf das singleton objekt. Eigentlich nicht notwendig, da momentan alle Mehtoden eh static sind.
	 * @return ReportLogZZZ
	 *
	 * javadoc created by: 0823, 05.01.2007 - 15:19:33
	 */
	private static synchronized ReportLogZZZ getInstance(){
		return objLogINSTANCE;
	}
	
	
	/**
	 * @see write (int, String, boolean)
	 * gibt einen Trace nur dann aus, wenn f�r die Klasse ein Trace zugelassen ist.
	 * @param logLevel
	 * @param msg
	 * @throws ExceptionZZZ 
	 */
	public static void write (int logLevel, String msg) throws ExceptionZZZ {
		write (logLevel, msg, !showTrace );
	}
	
	/**Schreibt eine Stringnachricht ins Log, wenn das in der Konfiguration eingestellte Loglevel es zul��t. 
	 * @param logLevel - Level auf dem gelogged werden soll
	 * @param msg - Log Nachricht
	 * @param kurz - falls kurz==true, wird kein Trace (Anzeige der aufrufenden Klasse)
	 * ausgegeben.
	 * @throws ExceptionZZZ 
	 */	
	public static void write(int logLevel, String msg, boolean kurz) throws ExceptionZZZ
	{
		/* mehrere sequentielle Aufrufe von System.out.print{,ln} resultieren
		 * in jeweils einer neuen Zeile.
		 */
		 int t = ReflectCodeZZZ.callStackSize() - ReportLogZZZ.INITIAL_STACK_SIZE;
		 if (t < 1) { t = 0; }
		
		if(ReportLogZZZ.objLogINSTANCE.currentLogLevel == ReportLogZZZ.NOT_LOADED){
			//Wenn noch kein KernelContext existiert und noch keine currentLogLevel eingestellt ist.
			 //Merke: Ohne den Kernel Context kann noch nicht einmal die default log4j.conf Datei gebaut werden, darum zumindest auf der Console loggen.
			 if (kurz) {
			 		System.out.println(msg);
			 } else {	
					 System.out.println(StringZZZ.repeat(". ", t) + ReflectCodeZZZ.lastCaller("ReportLogZZZ") + ": " + msg);
			 }
						
		}else if (logLevel <= ReportLogZZZ.objLogINSTANCE.currentLogLevel) {		 
			 //Merke: Ohne den Kernel Context kann noch nicht einmal die default log4j.conf Datei gebaut werden, darum zumindest auf der Console loggen.
			 if (kurz) {
			 	if(ReportLogZZZ.objLogINSTANCE.getKernelContext()==null){
			 		System.out.println(msg);
			 	}else{
			 	    objLogINSTANCE.logger.log (ReportLogCommonZZZ.internalToLog4jLevel(logLevel), msg);
			 	}
			 } else {	
				 if(ReportLogZZZ.objLogINSTANCE.getKernelContext()==null){
					 System.out.println(StringZZZ.repeat(". ", t) + ReflectCodeZZZ.lastCaller("ReportLogZZZ") + ": " + msg);
				 	}else{
				 objLogINSTANCE.logger.log (ReportLogCommonZZZ.internalToLog4jLevel(logLevel), ReportLogCommonZZZ.logSymbol (logLevel) +" " +  StringZZZ.repeat(". ", t) + ReflectCodeZZZ.lastCaller("ReportLogZZZ") + ": " + msg);
				 	}
			 }
		}
	}

	/**
 	 * Schreibt eine Stringnachricht ins Log, wenn das im Profildokument
	 * eingestellte Loglevel es zuläßt. 
	 * @see write (int, String)
	 * @param logLevel
	 * @param msg
	 * @throws ExceptionZZZ 
	 */
	public static void writeString(int logLevel, String msg) throws ExceptionZZZ
	{
		write(logLevel, msg);
	}

	/**
	 * Schreibt eine Schl�ssel-Wert-Liste ins Log, wenn das im Profildokument
	 * eingestellte Loglevel es zul��t.
	 * @param logLevel
	 * @param keys
	 * @param values
	 * @throws ExceptionZZZ 
	 */
	public static void writePairs(int logLevel, Vector keys, Vector values) throws ExceptionZZZ
	{
		StringWriter sw = new StringWriter();
		PrintWriter ps = new PrintWriter(sw);
		ps.println("" + keys.size() + " Schl�ssel und " + values.size() + " Werte:");
		for (int i = 0; i < keys.size() || i < values.size(); i++) {
			ps.print((i < keys.size())? keys.elementAt(i): "(null)");
			ps.print("=");
			ps.println((i < values.size())? values.elementAt(i): "(null)");
		}
		write(logLevel, sw.toString());
	}

	/**
 	 * Schreibt eine Exception mit StackTrace ins Log, wenn das im Profildokument
	 * eingestellte Loglevel es zuläßt.
	 * @param logLevel
	 * @param e
	 * @throws ExceptionZZZ 
	 */
	public static void writeException(int logLevel, Throwable e) throws ExceptionZZZ
	{
		if (e == null) {
			write (logLevel, "unknown exeption");
		}
		else {
			StringWriter sw = new StringWriter();
			
			//Merke: Hier wurde ein Spezialfall der "NotesException" entfernt
			e.printStackTrace(new PrintWriter(sw));
			try {
				nicewrite(logLevel, "\n" + sw.toString());
			} catch (IOException ex) {
				System.err.println("FATAL: ReportLogZZZ.writeException: " + ex);
			}
		}
	}



	/* setzt lange strings so um, dass write eine saubere ausgabe auf die (Domino) Konsole macht */
	private static void nicewrite (int logLevel, String message) throws IOException, ExceptionZZZ {
		String sLine = "";			
		StringReader reader= new StringReader (message);
		for (int s =  reader.read();s>0; s =  reader.read())
			{
				if (s > 0 && s != ReportLogZZZ.CARRIAGE_RETURN && s!= ReportLogZZZ.LINE_FEED) {
					if (s!=0) { sLine += (char) s; }
				} else {
					if (sLine != null && !sLine.equals( "")) { write (logLevel,sLine, true); }
					sLine = "";
				}
			}
	}

	/**
	 * Schreibt eine Exception mit Loglevel <code>ERROR</code> ins Log,
	 * wenn das im Profildokument eingestellte Loglevel es zul��t. 
	 * @param e
	 * @throws ExceptionZZZ 
	 */
	public static void writeException(Throwable e) throws ExceptionZZZ
	{
		writeException(ReportLogZZZ.ERROR, e);
	}

	/**
	 * Schreibt eine Aufz�hlung ins Log, wenn das im Profildokument
	 * eingestellte Loglevel es zul��t.
	 * @param logLevel
	 * @param e - auszugebende Enumeration
	 * @throws ExceptionZZZ 
	 */
	public static void writeEnumeration(int logLevel, Enumeration e) throws ExceptionZZZ
	{
		if (e == null) {
			write(logLevel, "writeEnumeration: null");
			return;
		}
		write(logLevel, e.getClass().getName());
		for (int i = 0; e.hasMoreElements(); i++) {
			write(logLevel, "" + i + ": " + e.nextElement());
		}
	}
	
	/**
	 * Schreibt einen Vector ins Log
	 * @param logLevel
	 * @param v
	 * @throws ExceptionZZZ 
	 */
	public static void writeVector(int logLevel, Vector v) throws ExceptionZZZ
	{
		if (v == null) {
			write(logLevel, "writeVector: null");
			return;
		}
		write(logLevel, v.getClass().getName());
		for (int i = 0, k = v.size(); i<k; i++) {
			try {
				write(logLevel, "" + i + ": " + v.elementAt(i).toString());
			} //TSA-Java0166
				catch (Exception e) {
				write (logLevel, "" + i + ": Exception " + e.toString());
			}
		}
	}
	
	/**
	 * ändert das Loglevel.
	 * @param newLogLevel
	 */
	public static void setLogLevel(int newLogLevel) {
		objLogINSTANCE.currentLogLevel = newLogLevel;
	}
	/**
	 * Gibt das Loglevel aus.
	 * @return
	 */
	public static int getLogLevel() {
		return objLogINSTANCE.currentLogLevel;
	}
	
	/**
	 * legt fest, ob bei der Ausgabe ein Stacktrace angegeben werden soll.
	 * @param newTraceStatus - true oder false
	 */
	public static void setTraceStatus (boolean newTraceStatus) {
		showTrace = newTraceStatus;
	}
	
	/** 
	 * zeigt an, ob zur Zeit ein Stacktrace beim Loggen ausgegeben wird oder nicht
	 * @return
	 */
	public static boolean getTraceStatus () {
		return showTrace;
	}
	
	/**
	 * Schreibt <i>msg</i> mit dem Level ERROR in die Logdatei.
	 * @param msg
	 * @throws ExceptionZZZ 
	 */
	public static void error(String msg) throws ExceptionZZZ {
		write(ReportLogZZZ.ERROR, msg); 
	}
	
	/**
	 * Schreibt <i>msg</i> mit dem Level WARN in die Logdatei.
	 * @param msg
	 * @throws ExceptionZZZ 
	 */
	public static void warn(String msg) throws ExceptionZZZ {
		write(ReportLogZZZ.WARN, msg);
	}
	
	/**
	 * Schreibt <i>msg</i> mit dem Level INFO in die Logdatei.
	 * @param msg
	 * @throws ExceptionZZZ 
	 */
	public static void info(String msg) throws ExceptionZZZ {
		write(ReportLogZZZ.INFO, msg);
	}
	
	/**
	 * Schreibt <i>msg</i> mit dem Level DEBUG in die Logdatei.
	 * @param msg
	 * @throws ExceptionZZZ 
	 */
	public static void debug(String msg) throws ExceptionZZZ {
		write(ReportLogZZZ.DEBUG, msg);
	}
	



	
	/**
	 * Der basePath der Log Klasse ist per Default das Verzeichnis <<user.dir>>/java
	 * Es kann mit setBasePath ver�ndert werden.
	 * In diesem Pfad werden logs und die log4j.conf gespeichert.
	 * @param path
	 */
	public static final void setBasePath (String path) {
		objLogINSTANCE.basePath=path;
	}

	/**
	 * @return
	 */
	public static String getBasePath() {
		return objLogINSTANCE.basePath;
	} 
	
	
	private ReportLogZZZ () {
		//FGL: Es muss der Methodenaufruf "loadKernelContext(..)" ausgeführt werden.		
	}
	
	
	//####################################################
	//### FGL Erweiterungen / Änderungen
	/** Liest das Konfigurierte LogLeEvel aus der Konfigurationsdatei aus.
	 *   Transformiert den konfigurierten String in einen integer-wert, 
	 *   wie er dann in .setLogLevel(int) übergeben werden kann.
	* @return int
	* 
	* lindhaueradmin; 22.10.2006 17:23:35
	 * @throws ExceptionZZZ 
	 */
	public static int readLogLevel() throws ExceptionZZZ{
		int iReturn = 0;
		main:{
			String stemp = objLogINSTANCE.objContext.getLog4jLevel();
			if(StringZZZ.isEmpty(stemp)) break main;
			

			/* Falls dieser Wert eine Zahl ist, ihn in eine Zall umwandeln
			 * Falls er ein Textstring ist, versuchen ihn in einen konfigurierten Wert umzuwandeln
				 private static final int NOT_LOADED = -1;
				public static final int ERROR = 0;
				public static final int WARN = 1;
				public static final int INFO = 2;
				public static final int DEBUG = 3;
				public static final int DEBUG2 = 4;
				public static final int DEBUG3 = 5;
			 */
			if(StringZZZ.isNumeric(stemp)){
				iReturn = Integer.parseInt(stemp);
			}else{
				if(stemp.equalsIgnoreCase("Not_Loaded")){
					iReturn = ReportLogCommonZZZ.NOT_LOADED;
				}else if(stemp.equalsIgnoreCase("Error")){
					iReturn = ReportLogCommonZZZ.ERROR;
				}else if(stemp.equalsIgnoreCase("WARN")){
					iReturn = ReportLogCommonZZZ.WARN;
				}else if(stemp.equalsIgnoreCase("INFO")){
					iReturn = ReportLogCommonZZZ.INFO;
				}else if(stemp.equalsIgnoreCase("DEBUG")){
					iReturn = ReportLogCommonZZZ.DEBUG;
				}else if(stemp.equalsIgnoreCase("DEBUG2")){
					iReturn = ReportLogCommonZZZ.DEBUG2;
				}else if(stemp.equalsIgnoreCase("DEBUG3")){
					iReturn = ReportLogCommonZZZ.DEBUG3;
				}
			}			
		}//End main
		return iReturn;
	}
	
	/** FGL Erweiterung/�nderung: Lies den Namen des verwendeten Logs aus der Kernel-ProgramKonfiguration aus.
	 * Merke: Dieser Name wird zur statischen Erzeugung der log4j Logger-Objekts verwendet.
	* @return String
	* 
	* lindhaueradmin; 24.10.2006 09:54:49
	 * @throws ExceptionZZZ 
	 */
	public static String readLogName() throws ExceptionZZZ{
		return objLogINSTANCE.objContext.getLog4jName();
	}
	
	public static String readLog4jPathConfig() throws ExceptionZZZ{
		return objLogINSTANCE.objContext.getLog4jPathConfig();
	}
	
	public static String readLog4jFileConfig() throws ExceptionZZZ{
		return objLogINSTANCE.objContext.getLog4jFileConfig();
	}
	
	
	/** FGL Änderung/Erweiterung: Neben dem Setzen des KernelContext-Objekts in das Singleton,
	 * werden auch die grundlegenden Informationen aus der Kernel-Config-Ini-Datei ausgelesen und gesetzt.
	* @return boolean
	* @param objContext
	* 
	* lindhaueradmin; 24.10.2006 10:08:09
	 * @throws ExceptionZZZ 
	 */
	public static boolean loadKernelContext(KernelReportContextProviderZZZ objContext, boolean bRecreateFileConfig) throws ExceptionZZZ{
		boolean bReturn = false;
		main:{
			ReportLogZZZ.setKernelContext(objContext);
			
			//1. Verzeichnis.
			String stemp  = ReportLogZZZ.readLogName();
			objLogINSTANCE.logger = Logger.getLogger(stemp);
			
			int itemp = ReportLogZZZ.readLogLevel();
			objLogINSTANCE.currentLogLevel = itemp;
			
			stemp = ReportLogZZZ.readLog4jPathConfig();
			
			File objDirectory = FileEasyZZZ.searchDirectory(stemp);//Suche Verzeichnis, egal ob relativer Pfad und auf WebServer oder in Eclipse Workspace.
			String sDirectory = objDirectory.getAbsolutePath();
			objLogINSTANCE.basePath= sDirectory + File.separator;
			
			//2. Dateiname
			stemp = ReportLogZZZ.readLog4jFileConfig();
			objLogINSTANCE.configFilename = stemp;
			
			//nun setupLog4j(sFile); ersetzen
			if (FileEasyZZZ.exists(objLogINSTANCE.basePath + objLogINSTANCE.configFilename) && bRecreateFileConfig == false) {
				System.out.println (ReflectCodeZZZ.getMethodCurrentName() + "#Loading Log4j Config from file: "+ objLogINSTANCE.basePath+objLogINSTANCE.configFilename);				
			} else {
				System.out.println ("Generating NEW Log4j Default Config in file: "+ objLogINSTANCE.basePath+objLogINSTANCE.configFilename);
				boolean btemp = Log4jPropertyGeneratorZZZ.createFile(objContext);	
				if(btemp==false){
					ExceptionZZZ ez = new ExceptionZZZ("unable to create log4j-properties file", iERROR_RUNTIME, objLogINSTANCE, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
			}
			PropertyConfigurator.configure (objLogINSTANCE.basePath+objLogINSTANCE.configFilename);
						
			bReturn = true;
		}//END main
		return bReturn;
	}

	//### Getter / Setter
	public static void setKernelContext(KernelReportContextProviderZZZ objContext) {
	 objLogINSTANCE.objContext = objContext;
	}
	public KernelReportContextProviderZZZ getKernelContext(){
		return this.objContext;
	}
}//END class
