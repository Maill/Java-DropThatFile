package DropThatFile.engines;

import org.apache.log4j.*;
import org.apache.log4j.nt.NTEventLogAppender;
/**
 * Created by Nicol on 21/03/2017.
 *
 * Logger Log4J' class
 */
public class LogManagement {

    //region Attributes

    private Logger logger;

    //endregion

    /**
     * Creation of the Log4J' template
     * @param caller Instance of the object concerned
     */
    private LogManagement(Object caller){
        logger = Logger.getLogger(caller.getClass());
        String mySource = "DropThatFile";
        PatternLayout myLayout = new PatternLayout("[%c][%l][%p][%thread]: %m%n");
        NTEventLogAppender eventLogAppender= new NTEventLogAppender(mySource,myLayout);
        logger.addAppender(eventLogAppender);
        logger.setLevel(Level.WARN);
    }

    /**
     * Récupération du logger Log4J pour l'écriture dans le journal d'évenements.
     * Retrieve the Log4J' logger to write in the event log
     * @param caller Instance of the object concerned
     * @return Logger
     */
    public static Logger getInstanceLogger(Object caller){
        return new LogManagement(caller).logger;
    }
}
