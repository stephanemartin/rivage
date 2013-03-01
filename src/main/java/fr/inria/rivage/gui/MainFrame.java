package fr.inria.rivage.gui;

import fr.inria.rivage.gui.toolbars.LayersToolBar;
import fr.inria.rivage.gui.toolbars.ColorChooserTool;
import fr.inria.rivage.gui.toolbars.UsersToolbar;
import fr.inria.rivage.gui.toolbars.ZoomToolbar;
import fr.inria.rivage.gui.toolbars.StandardButtonToolbar;
import fr.inria.rivage.gui.toolbars.DrawToolBar;
import fr.inria.rivage.gui.toolbars.OperationsToolBar;
import fr.inria.rivage.Application;
import fr.inria.rivage.elements.handlers.GHandler;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.gui.actions.Actions;
import fr.inria.rivage.gui.menus.MenuBar;
import fr.inria.rivage.tools.Configuration;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class MainFrame extends JFrame {
        private static final Logger log=Logger.getLogger(Class.class.getName());
	private MyDesktop desktop;
	private DrawToolBar drawToolBar;
	private StandardButtonToolbar standardButtonToolBar;
	private ZoomToolbar zoomToolbar;
	private MenuBar menuBar;
	private StatusBar statusbar;
	private Application application;

	private ObjectInspector objectInspector;
	private UsersToolbar usersToolbar;
        private ConnectedComputer connectedComputer;
	private LayersToolBar layersTool;
	private OperationsToolBar operationsToolBar;
	private ConflictFrame conflictFrame;
        private ColorChooserTool colorChooserTool;


	/**
	 * Communication component
	 */

	public MainFrame(Application application, String title) {
		// frame initialization
		super(title);
                
                initLookAndFeel();
                
		this.application = application;
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		frameSize.height = screenSize.height;
		frameSize.width = screenSize.width;
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				Actions.QUIT.doAction();
			}
		});
		//create desktop object
		desktop = new MyDesktop();
		//create draw & standard butt. toolbar
		drawToolBar = new DrawToolBar(application, this);
		//create standard button bar
		standardButtonToolBar = new StandardButtonToolbar();
		zoomToolbar = new ZoomToolbar();
		//create menubar
		menuBar = new MenuBar();
		setJMenuBar(menuBar);
		//create statusbar
		statusbar = new StatusBar(application, this);
		//create object inspector
		objectInspector = new ObjectInspector();
		//TreeManager.addGeneralTreeListener(objectInspector);
		//create userToolbar
		usersToolbar = new UsersToolbar(application);
                connectedComputer = new ConnectedComputer();
		//create layersToolBar
		layersTool = new LayersToolBar();
                /*ColorChooserTool cc=new ColorChooserTool();
                JTabbedToolsBar northEast=new JTabbedToolsBar();
                northEast.addTabbe(cc);*/
                
                
		//TreeManager.addGeneralTreeListener(layersTool);
		//create operation toolbar
		operationsToolBar = new OperationsToolBar();
		//TreeManager.addGeneralTreeListener(operationsToolBar);
		//create the conflict history frame
		conflictFrame = new ConflictFrame(this);
		//additional componets
                
                colorChooserTool=new ColorChooserTool();
                
		JPanel panelNorth = new JPanel();
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.X_AXIS));
		panelNorth.add(standardButtonToolBar);
		panelNorth.add(operationsToolBar);
		panelNorth.add(zoomToolbar);
		//additional componets
		JPanel panelEast = new JPanel(new BorderLayout());
		panelEast.add(objectInspector, BorderLayout.CENTER);
		panelEast.add(layersTool, BorderLayout.NORTH);
                //panelEast.add(northEast, BorderLayout.NORTH);
                
		panelEast.add(usersToolbar, BorderLayout.SOUTH);
		panelEast.add(connectedComputer, BorderLayout.SOUTH);
                //add components to the frame
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panelNorth, BorderLayout.NORTH);
		getContentPane().add(desktop, BorderLayout.CENTER);
		getContentPane().add(drawToolBar, BorderLayout.WEST);
		getContentPane().add(panelEast, BorderLayout.EAST);
		//getContentPane().add(statusbar, BorderLayout.SOUTH);
                getContentPane().add(colorChooserTool, BorderLayout.SOUTH);
		statusbar.display("Welcome to GEditor version "
				+ Configuration.VERSION_NUMBER);
		setIconImage((new ImageIcon(Application.class.getResource("resources/images/file.gif"))).getImage());
		setVisible(true);

		pack();
	}

         private void initLookAndFeel() {
        String lcOSName = System.getProperty("os.name").toLowerCase();
        boolean IS_MAC = lcOSName.startsWith("mac os x");
        if (IS_MAC) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "GEditor");
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                log.log(Level.SEVERE,"Look and feel error",ex);
            }

        } else {

            try {
                try {
                    UIManager.setLookAndFeel(Configuration.getConfiguration().LOOK_AND_FEEL_CLASS_NAME);
                } catch (Exception e) {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
            } catch (Exception e1) {
                log.log(Level.SEVERE,"Look and feel error",e1);
            }
        }
    }
        
	/**
	 * Standard methods
	 */

	// drawing bar methods
	public void setDrawingMode(GHandler handler) {
		//set the specified drawing mode to the current document
		FileController fc = application.getCurrentFileController();
		WorkArea wa = fc.getCurrentWorkArea();
		wa.setMode(handler);
	}

	/**
	 * Setter and getter methods
	 */
	public MyDesktop getDesktop() {
		return desktop;
	}

	public StatusBar getStatusBar() {
		return statusbar;
	}

	public DrawToolBar getDrawToolBar() {
		return drawToolBar;
	}

	public StandardButtonToolbar getStandardButtonToolbar() {
		return standardButtonToolBar;
	}

	public UsersToolbar getUserToolBar() {
		return usersToolbar;
	}
	
	public LayersToolBar getLayersToolBar(){
		return layersTool;
	}

	public ObjectInspector getObjectInspector() {
		return objectInspector;
	}

	public OperationsToolBar getOperationToolBar() {
		return operationsToolBar;
	}
	
	public ZoomToolbar getZoomToolbar() {
		return zoomToolbar;
	}

	public MenuBar getMyMenuBar() {
		return menuBar;
	}

	public ConflictFrame getConflictFrame() {
		return conflictFrame;
	}
	
}

