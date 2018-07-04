
/**
 * Created by matthewnguyen99 on Jul 02, 2018.
 */
public final class Logger {
    public static int logLevel = 0;

    public static int DEBUG = 0;
    public static int INFO = 1;
    public static int ERROR = 2;

    public static void debug(String tag, String msg) {
        if (DEBUG >= logLevel) print("DEBUG", tag, msg);
    }

    public static void info(String tag, String msg) {
        if (INFO >= logLevel) print("INFO", tag, msg);
    }

    public static void error(String tag, String msg) {
        if (ERROR >= logLevel) print("ERROR", tag, msg);
    }

    private static void print(String type, String tag, String msg) {
        System.out.printf("[%s]%s: %s\n", type, tag, msg);
    }
}
