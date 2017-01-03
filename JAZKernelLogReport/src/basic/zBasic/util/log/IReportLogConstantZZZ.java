package basic.zBasic.util.log;

public interface IReportLogConstantZZZ {
	//	 Konstanten
	public static final int NOT_LOADED = -1;
	public static final int ERROR = 0;
	public static final int WARN = 1;
	public static final int INFO = 2;
	public static final int DEBUG = 3;
	public static final int DEBUG2 = 4;
	public static final int DEBUG3 = 5;

	public static final int CARRIAGE_RETURN = 13;
	public static final int LINE_FEED = 10;
	//initiale Stacktiefe, die ausgeblendet werden soll.
	//TODO: Die ReportLogger sollten das aus der Konfiguration auslesen
	public static final int INITIAL_STACK_SIZE = 7;
}