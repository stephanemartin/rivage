package fr.inria.rivage;

import fr.inria.rivage.elements.GTemplate;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.gui.MainFrame;
import fr.inria.rivage.net.group.FileControllerManager;
import fr.inria.rivage.net.overlay.IOverlay;
import fr.inria.rivage.net.overlay.MulticastNetwork;
import fr.inria.rivage.net.overlay.rmi.RMINetwork;
import fr.inria.rivage.net.overlay.tcp.TCPServerWithDiscover;
import fr.inria.rivage.tools.Clipboard;
import fr.inria.rivage.tools.Configuration;
import fr.inria.rivage.users.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;


public class Application {

    /*public static int TIMEOUT_ACK_JOIN = 30000;
    public static int TIMEOUT_READY_JOIN = 30000;
    public static int TIMEOUT_SAVE_FILE = 30000;
    public static int TIMEOUT_QUIESCENCE = 3000;
    public static int TIMEOUT_USER_UPDATE = 30000;
    public static int GARBAGE_COLLECTOR_TIMER = 5000;
    public static final int MANUAL = 1;
    public static final int AUTOMATIC = 2;*/
    private MainFrame mainFrame;
    private User user;
    private IOverlay network;
    private FileControllerManager fcManager;
    private int garbagecollectorOperationMode;
    private Clipboard clipboard = new Clipboard();
    private static Application application;
    private static final Logger log=Logger.getLogger(Class.class.getName());
    public Application(String username) {
        user = new User(username);
        
    }

   

    public IOverlay getNetwork() {
        return network;
    }
    /**
     * Part to manage open files.
     */
    private FileController currentFileController;
    //private HashMap<String, FileController> fileControllerHash;

    /* public synchronized void addOpenedFile(FileController fc) {
     fileControllerHash.put(fc.getFileID(), fc);
     }*/

    /*public synchronized void removeOpenedFile(String fileID) {
     fileControllerHash.remove(fileID);
     }

     public synchronized void removeOpenedFile(FileController fc) {
     fileControllerHash.remove(fc.getFileID());
     }*/
    public synchronized void setCurrentFileController(FileController fc) {
        this.currentFileController = fc;
        mainFrame.repaint();
    }

    public synchronized FileController getCurrentFileController() {
        return currentFileController;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public int getGarbagecollectorOperationMode() {
        return garbagecollectorOperationMode;
    }

    public FileController[] getFileControllers() {
        return (FileController[]) fcManager.getFiles().toArray();

    }
   
    public void setGarbagecollectorOperationMode(
            int garbagecollectorOperationMode) {
        this.garbagecollectorOperationMode = garbagecollectorOperationMode;
    }

    public static void main(String args[]) throws Exception {
        /**
         * Initialize logging
         */
        //PropertyConfigurator.configure(Application.class.getResource("resources/log4jconfig.conf"));

        // Logger.getRootLogger().info("User login display");

        //new LoginFrame();

       
        application= new Application(InetAddress.getLocalHost().getHostName());
        application.start();
        

    }

    public void start(){
        
        
        //this.fileControllerHash = new HashMap<String, FileController>();

        //this.garbagecollectorOperationMode = MANUAL;
        try {
            if (Configuration.getConfiguration().NETWORK_MODE.equals("RMI")) {
                network = new RMINetwork();
                log.info("RMI initialized.");
            } else if (Configuration.getConfiguration().NETWORK_MODE.equals("TCP")) {
                network = new TCPServerWithDiscover();
                log.info("TCP/IP initialized.");

            } else if (Configuration.getConfiguration().NETWORK_MODE.equals("MULTICAST")) {
                network = new MulticastNetwork();
                log.info("MulticastNetwork initialized.");
            } else if (Configuration.getConfiguration().NETWORK_MODE.equals("WEB")) {
                log.info("Web Network initialized (if it would be implemented).");
            } else {
                log.severe("No valid networking model choosen.");
                throw new RuntimeException("Network mode not recognized.");
            }
            /*serverNetwork = new OTServerNetwork(
             Configuration.getConfiguration().SERVER_ADDRESS,
             * 
             Configuration.getConfiguration().SERVER_PORT);*/
            /*  network.start();*/

            fcManager = new FileControllerManager(network);
            log.info("ServerNetwork successfully initialized.");
        } catch (Exception ioe) {
            log.log(
                    Level.SEVERE, "The communication with the server could not be initialized. The error message is: {0}", ioe);
            System.err.println("Server not found, please check it's state and configuration files.");
            System.exit(1);
        }
        this.network.start();
         /* try {
            GTemplate.loadDir(new File(
                    Configuration.getConfiguration().TEMPLATE_FOLDER));
        } catch (FileNotFoundException fnfe) {
            log.log(Level.SEVERE, "Templates could not be loaded{0}", fnfe);
        }*/
        //application = this;
        this.mainFrame = new MainFrame(this, "RIVAGE : Real-tIme Vector grAphic Group Editor "
                + Configuration.VERSION_NUMBER);
        
    }
    


    public static Application getApplication() {
        if (application == null) {
            throw new RuntimeException("Application not yet initialized");
        }
        return application;
    }

    public FileControllerManager getFileManagerController() {
        return fcManager;
    }

    public User getUser() {
        return user;
    }

    /**
     * Should only be used in emergency cases, where we couldn't go on working
     * anyway. For example when the SiteID cannot register input or output
     * queues.
     *
     */
    public void fatalTermination(Exception e) {
        JOptionPane.showMessageDialog(null, "The program will be terminated due to a fatal exception", "Termination due to fatal error", JOptionPane.ERROR_MESSAGE);
        log.log(Level.SEVERE, "fatalTermination was called.{0}", e);
        System.exit(1);
    }

    public Clipboard getClipboard() {
        return clipboard;
    }
}
