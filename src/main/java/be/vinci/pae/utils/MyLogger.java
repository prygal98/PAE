package be.vinci.pae.utils;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Class used to log message to a log file.
 */
public class MyLogger {

  private static String filename = "project.log";
  private static Logger logger;
  private static Handler fileHandler;
  private static SimpleFormatter formatter;

  static {
    setFormatter();
    setFileHandler();
    setLogger();
    logger.info("logger : ok");
  }

  /**
   * Setup everything for the formatter. The formatter set the format for each log (ie: "[INFO]
   * 2022/03/02 : error in method()").
   */
  private static void setFormatter() {
    //formatter = new SimpleFormatter();
    //formatter.formatMessage();
    formatter = new SimpleFormatter() {
      private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

      @Override
      public synchronized String format(LogRecord lr) {
        return String.format(format, new Date(lr.getMillis()), lr.getLevel().getLocalizedName(),
            lr.getMessage());
      }
    };
  }

  /**
   * Setup everything for the file handler. The file handler manage access to the log file.
   */
  private static void setFileHandler() {
    try {
      fileHandler = new FileHandler(filename);
      fileHandler.setFormatter(formatter);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Setup everything for the logger itself. the logger manage the level of log (ie:INFO,
   * WARNING,...).
   */
  private static void setLogger() {
    logger = Logger.getLogger("Main Logger");
    logger.setUseParentHandlers(false); // TODO useful ???
    logger.addHandler(fileHandler);
  }

  /**
   * Log an INFO message. If the logger is currently enabled for the INFO message level then the
   * given message is forwarded to all the registered output Handler objects.
   *
   * @param msg – The string message (or a key in the message catalog).
   */
  public static void info(String msg) {
    logger.info(msg);
  }

  /**
   * Log a WARNING message. If the logger is currently enabled for the WARNING message level then
   * the given message is forwarded to all the registered output Handler objects.
   *
   * @param msg – The string message (or a key in the message catalog).
   */
  public static void warning(String msg) {
    logger.warning(msg);
  }

  /**
   * Log a CONFIG message. If the logger is currently enabled for the CONFIG message level then the
   * given message is forwarded to all the registered output Handler objects.
   *
   * @param msg – The string message (or a key in the message catalog).
   */
  public static void config(String msg) {
    logger.config(msg);
  }

  /**
   * Log a FINE message. If the logger is currently enabled for the FINE message level then the
   * given message is forwarded to all the registered output Handler objects.
   *
   * @param msg – The string message (or a key in the message catalog).
   */
  public static void fine(String msg) {
    logger.fine(msg);
  }

  /**
   * Log a FINER message. If the logger is currently enabled for the FINER message level then the
   * given message is forwarded to all the registered output Handler objects.
   *
   * @param msg – The string message (or a key in the message catalog).
   */
  public static void finer(String msg) {
    logger.finer(msg);
  }

  /**
   * Log a FINEST message. If the logger is currently enabled for the FINEst message level then the
   * given message is forwarded to all the registered output Handler objects.
   *
   * @param msg – The string message (or a key in the message catalog).
   */
  public static void finest(String msg) {
    logger.finest(msg);
  }

  /**
   * Log a SEVERE message. If the logger is currently enabled for the SEVERE message level then the
   * given message is forwarded to all the registered output Handler objects.
   *
   * @param msg – The string message (or a key in the message catalog).
   */
  public static void severe(String msg) {
    logger.severe(msg);
  }

  /**
   * Log a message, with no arguments. If the logger is currently enabled for the given message
   * level then the given message is forwarded to all the registered output Handler objects.
   *
   * @param level One of the message level identifiers, e.g., SEVERE msg.
   * @param msg   The string message (or a key in the message catalog).
   */
  public static void log(Level level, String msg) {
    logger.log(level, msg);
  }

}
