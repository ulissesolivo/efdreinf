package efdreinf.util;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LogUtil {

    public static void inicializaConfiguracaoLog() {

        LogManager.resetConfiguration();

        PatternLayout layout = new PatternLayout("[%d{yyyy-MM-dd HH:mm:ss}] %-5p [%C{1}] %m%n");

        try {
            String logfile = SegurancaUtils.get().getPastaLogs() + "/log.out";
            FileAppender appender = new FileAppender(layout, logfile, true);
            Logger.getRootLogger().addAppender(appender);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
