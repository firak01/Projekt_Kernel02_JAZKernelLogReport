package basic.zBasic.util.log;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.IKernelZZZ;
import basic.zKernel.KernelUseObjectZZZ;
import basic.zKernel.KernelZZZ;

/** Diese Klasse dient dazu andere Objekte (hier das ReportLog-Objekt)  mit Kernel-Informationen zu versorgen.
 * Damit die ReportLog - Klasse auch in anderen Projekten eingesetzt werden kann (ohne Kernel-Objekt)
 * und sehr einfach zu handhaben ist, sind die wesentlichen Methoden static 
 * und es gibt keinen public Konstruktor.
 * 
 * Aus dem KernelContextProvider-Objekt können dann Einstellungen gezogen werden, die die DEFAULT Einstellungen überschreiben.
 * @author lindhaueradmin
 *
 */
public class KernelReportContextProviderZZZ extends KernelUseObjectZZZ {
	private String sModuleCalling=null;       //Das Modul, von der aus diese Klasse initialisiert wird. 
	private String sClassCallingOrAlias=null; //Die Klasse, von der aus diese Klasse initialisiert wird.
	private String sLogLevel=null;     //Der LogLevel wie er von ReportLogZZZ genutzt wird.
	
	private String sLog4jLevel=null;  //Der loglevel, wie er f�r log4j in der konfigurationsdatei hinterlegt ist
	private String sLog4jName=null; //Der logname, wie er f�r log4j in der konfigurationsdatei hinterlegt ist
	private String sLog4jFileConfig=null; //Der filename der properties-datei, die von log4j als Konfiguration erwartet wird.
	private String sLog4jPathConfig=null; //Der pfad zur datei, die von log4j als Konfiguration erwartet wird.
	private String sLog4jPatternPath=null; //F�r eine datei, die ein Muster der log4j configuration datei enth�lt
	private String sLog4jPatternFile=null;
	
	/** Verwende diesen Konstruktor, wenn die Informationen in einem Modul liegen, dass f�r diese Klasse selbst erstellt wurde. Also: Modulename = ReportLogZZZ.class.getName().
	* lindhaueradmin; 12.01.2007 09:22:59
	 * @param objKernel
	 */
	public KernelReportContextProviderZZZ(IKernelZZZ objKernel){
		super(objKernel);
	}
	public KernelReportContextProviderZZZ(IKernelZZZ objKernel, String sModuleCalling, String sClassCallingOrAlias){
		super(objKernel);
		this.sModuleCalling = sModuleCalling;
		this.sClassCallingOrAlias=sClassCallingOrAlias;
	}
	/** Verwende diesen Konstruktor, wenn die Informationen auf Modulebene (und nicht in irgendwelchen konfigurierten Programm-Sektionen) in der ini-Konfigurationsdatei hinterlegt wurden.
	* lindhaueradmin; 12.01.2007 09:01:01
	 * @param objKernel
	 * @param sModuleCalling
	 */
	public KernelReportContextProviderZZZ(IKernelZZZ objKernel, String sModuleCalling){
		super(objKernel);
		this.sModuleCalling = sModuleCalling;
	}
	
	public String getClassCalling(){
		/* FGL 20070111 Wenn wir von der M�glichkeit ausgehen, dass der Programm-Name fehlen darf, dann darf in diesem Fall nicht standardm��ig der ReportLogZZZ-Klassenname daf�r verwendet werden.
		if(StringZZZ.isEmpty(this.sClassCallingOrAlias)){
			this.sClassCallingOrAlias = ReportLogZZZ.class.getName();
		}*/
		return this.sClassCallingOrAlias;
	}
	public String getModuleCalling(){
		//Merke: �ber den Modulnamen kann der Kernel die konfigurierte ini-Datei finden.
		//            In der Ini-Datei kann dann unter dem Programmnamen (eine Section) der gew�nschte Parameter ausgelesen werden.
		if(StringZZZ.isEmpty(this.sModuleCalling)){
			this.sModuleCalling = ReportLogZZZ.class.getName();
		}
		return this.sModuleCalling;
	}
	
	
	//#####################################################
	// GETTER-METHODEN
	// IDEE: Das Auslesen aus dem Kernel in einem anderen Projekt ggf. durch fest verdrahtete Werte oder andere Vorgehensweise ersetzen
	
	//Properties, die vom ReportLogZZZ benutzt werden. Kann das gleiche sein wie log4jLevel, muss aber nicht.
	public String getLogLevel() throws ExceptionZZZ{
		if(StringZZZ.isEmpty(this.sLog4jLevel)){
			//Damit ist es das gleiche wie log4jLevel, das muss aber nicht sein
			if(StringZZZ.isEmpty(this.getClassCalling())){
				this.sLogLevel = objKernel.getParameterByModuleAlias(this.getModuleCalling(), "Log4jLevel");
			}else{
				this.sLogLevel = objKernel.getParameterByProgramAlias(this.getModuleCalling(), this.getClassCalling(), "Log4jLevel");
			}
		}
		return this.sLogLevel;
	}
		
	//Properties, die vom Log4jPropertyGenerator benutzt werden
	public String getLog4jLevel() throws ExceptionZZZ{
		if(StringZZZ.isEmpty(this.sLog4jLevel)){
			if(StringZZZ.isEmpty(this.getClassCalling())){
				this.sLog4jLevel = objKernel.getParameterByModuleAlias(this.getModuleCalling(), "Log4jLevel");
			}else{
				this.sLog4jLevel = objKernel.getParameterByProgramAlias(this.getModuleCalling(), this.getClassCalling(), "Log4jLevel");
			}
		}
		return this.sLog4jLevel;
	}
	
	public String getLog4jName() throws ExceptionZZZ{
		if(StringZZZ.isEmpty(this.sLog4jName)){
			if(StringZZZ.isEmpty(this.getClassCalling())){
				this.sLog4jName = objKernel.getParameterByModuleAlias(this.getModuleCalling(), "Log4jName");
			}else{
				this.sLog4jName = objKernel.getParameterByProgramAlias(this.getModuleCalling(), this.getClassCalling(), "Log4jName");
			}
		}
		return this.sLog4jName;
	}
	
	public String getLog4jPathConfig() throws ExceptionZZZ {
		if(StringZZZ.isEmpty(this.sLog4jPathConfig)){
			if(StringZZZ.isEmpty(this.getClassCalling())){
				this.sLog4jPathConfig = objKernel.getParameterByModuleAlias(this.getModuleCalling(), "Log4jPathConfig" );
			}else{
				this.sLog4jPathConfig = objKernel.getParameterByProgramAlias(this.getModuleCalling(), this.getClassCalling(), "Log4jPathConfig" );
			}
		}
		return this.sLog4jPathConfig;
	}
	
	public String getLog4jFileConfig() throws ExceptionZZZ {
		if(StringZZZ.isEmpty(this.sLog4jFileConfig)){
			if(StringZZZ.isEmpty(this.getClassCalling())){
				this.sLog4jFileConfig = objKernel.getParameterByModuleAlias(this.getModuleCalling(), "Log4jFileConfig" );
			}else{
				this.sLog4jFileConfig = objKernel.getParameterByProgramAlias(this.getModuleCalling(), this.getClassCalling(), "Log4jFileConfig" );
			}
		}
		return this.sLog4jFileConfig;
	}
	
	public String getLog4jPathPattern() throws ExceptionZZZ{
		if(StringZZZ.isEmpty(this.sLog4jPatternPath)){
			if(StringZZZ.isEmpty(this.getClassCalling())){
				this.sLog4jPatternPath = objKernel.getParameterByModuleAlias(this.getModuleCalling(), "Log4jPathPattern" );
			}else{
				this.sLog4jPatternPath = objKernel.getParameterByProgramAlias(this.getModuleCalling(), this.getClassCalling(), "Log4jPathPattern");
			}
		}
		return this.sLog4jPatternPath;
	}
	
	public String getLog4jFilePattern() throws ExceptionZZZ {
		if(StringZZZ.isEmpty(this.sLog4jPatternFile)){
			if(StringZZZ.isEmpty(this.getClassCalling())){
				this.sLog4jPatternFile = objKernel.getParameterByModuleAlias(this.getModuleCalling(), "Log4jFilePattern" );
			}else{
				this.sLog4jPatternFile = objKernel.getParameterByProgramAlias(this.getModuleCalling(), this.getClassCalling(), "Log4jFilePattern" );
			}
		}
		return this.sLog4jPatternFile;
	}	
}	//END class
	
	
	