package DropThatFile.engines;

import org.apache.log4j.*;
import org.apache.log4j.nt.NTEventLogAppender;
/**
 * Created by Nicol on 21/03/2017.
 *
 * Classe pour le logger log4j
 */
public class LogManagement {

    //region Attributs

    private Logger logger;

    //endregion

    //region Contructeur privé
    /**
     * Création du template Log4J.
     * @param caller
     */
    private LogManagement(Object caller){
        // On ne fait pas la gestion de la clé de registre ici, on la fait pendant l'installation
        logger = Logger.getLogger(caller.getClass());
        String mySource = "DropThatFile";
        PatternLayout myLayout = new PatternLayout("[%c][%l][%p][%thread]: %m%n");
        NTEventLogAppender eventLogAppender= new NTEventLogAppender(mySource,myLayout);
        logger.addAppender(eventLogAppender);
        logger.setLevel(Level.WARN);
    }
    //endregion

    //region Méthode : getInstanceLogger
    /**
     * Récupération du logger Log4J pour l'écriture dans le journal d'évenements.
     * @param caller
     * @return Logger
     */
    public static Logger getInstanceLogger(Object caller){
        return new LogManagement(caller).logger;
    }
    //endregion
}
