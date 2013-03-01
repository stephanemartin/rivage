package fr.inria.rivage.tools;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class loads and saves all the configuration parameters to a file.
 */
public class Configuration {

    // Class members (do not change)
    private static final String FILENAME = "config.properties";
    public static final String VERSION_NUMBER_MINOR = "3";
    public static final String VERSION_NUMBER_MAJOR = "0";
    public static final String VERSION_NUMBER = VERSION_NUMBER_MAJOR + "." + VERSION_NUMBER_MINOR;
    private static Configuration conf = null;
    private Properties p = null;
    // Config strings
    public final int SEL_MARK_SIZE;
    public final Color MARKEE_COLOR;
    public final Color MARKEE_COLOR_XOR;
    public final Color GROUP_COLOR_ACTIVE;
    public final Color GROUP_COLOR_INACTIVE;
    public final int MIN_SEL_RECT;
    public final int POINT_TOLERANCE;
    public final int OBJECT_TOLERANCE;
    public final String LOOK_AND_FEEL_CLASS_NAME;
    public final boolean ANTIALIASING_ON;
    public final String NETWORK_MODE;
    public final String SERVER_ADDRESS;
    public final int SERVER_PORT;
    public final int MULTICAST_PORT;
    public final String MULTICAST_ADDRESS;
    public final String TEMPLATE_FOLDER;
    public final String[] CONCURRENCY_CONTROLLER_LIST;

    // Implementation part (do not change)
    public static Configuration getConfiguration() {
        if (conf == null) {
            conf = new Configuration();
        }
        return conf;
    }

    public static void saveConfiguration() {
        if (conf == null) {
            return;
        }
        try {
            conf.p.store(new FileOutputStream(FILENAME, false), "Properties file for the geditor");
        } catch (IOException ex) {
            System.err.println("Could not save properties");
        }
    }

    public static String getFilename() {
        return FILENAME;
    }

    private Configuration() {
        p = new Properties();
        try {
            p.load(new FileInputStream(FILENAME));
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).info("" + FILENAME + " not found try to create new file...");
            try {
                File fi = new File(FILENAME);
                if (fi.createNewFile()) {
                    
                }
                p.load(new FileInputStream(FILENAME));
            } catch (Exception ex2) {
               Logger.getLogger(getClass().getName()).log(Level.SEVERE,"Unable to write " + FILENAME,ex2);
            }

        }

        SEL_MARK_SIZE = getInt("SEL_MARK_SIZE", 8);
        MARKEE_COLOR = getColor("MARKEE_COLOR", 0x0000FF);
        MARKEE_COLOR_XOR = getColor("MARKEE_COLOR_XOR", 0xFFFFFF);
        GROUP_COLOR_ACTIVE = getColorAlpha("GROUP_COLOR_ACTIVE", 0x7F7FFF7F);
        GROUP_COLOR_INACTIVE = getColorAlpha("GROUP_COLOR_INACTIVE", 0x3F7F7F7F);
        MIN_SEL_RECT = getInt("MIN_SEL_RECT", 10);
        POINT_TOLERANCE = getInt("POINT_TOLERANCE", 5);
        OBJECT_TOLERANCE = getInt("OBJECT_TOLERANCE", 3);
        LOOK_AND_FEEL_CLASS_NAME = p.getProperty(
                "LOOK_AND_FEEL_CLASS_NAME",
                "javax.swing.plaf.metal.MetalLookAndFeel");
        ANTIALIASING_ON = getOnOff("ANTIALIASING");


        NETWORK_MODE = p.getProperty("NETWORK_MODE", "TCP");

        SERVER_ADDRESS = p.getProperty("SERVER_ADDRESS", "127.0.0.1");
        SERVER_PORT = getInt("SERVER_PORT", 12344);

        MULTICAST_PORT = getInt("MULTICAST_PORT", 12345);
        MULTICAST_ADDRESS = p.getProperty("MULTICAST_ADDRESS", "224.0.3.0");

        TEMPLATE_FOLDER = p.getProperty("TEMPLATE_FOLDER", "");

        CONCURRENCY_CONTROLLER_LIST = getList("CONCURRENCY_CONTROLLER_LIST");
    }

    private int getInt(String key, int def) {
        return Integer.parseInt(p.getProperty(key, def + ""));
    }

    private Color getColor(String key, long def) {
        return new Color(Integer.decode(p.getProperty(key, def + "")));
    }

    private Color getColorAlpha(String key, long def) {
        return new Color(Integer.decode(p.getProperty(key, def + "")), true);
    }

    private boolean getOnOff(String key) {
        return p.getProperty(key, "ON").toUpperCase().equals("ON");
    }

    private String[] getList(String key) {
        String s = p.getProperty(key);
        if (s != null) {
            return s.split(",");
        } else {
            return new String[]{""};
        }
    }
}
