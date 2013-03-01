package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GGroup;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.manager.SelectionManager;
import fr.inria.rivage.engine.operations.CreateGroup;
import fr.inria.rivage.gui.WorkArea;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

public class GroupObjs extends AbstractAction {

    private static Logger log=Logger.getLogger(Class.class.getName());

    GroupObjs() {
       // log = Logger.getLogger(getClass());
        this.putValue(AbstractAction.NAME, "Group");
        this.putValue(AbstractAction.SHORT_DESCRIPTION,
                "Group selected Objects");
        this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(
                Application.class.getResource("resources/images/group.gif")));
        this.putValue(AbstractAction.MNEMONIC_KEY, KeyEvent.VK_G);
    }

    /*
     * If the selected objs belong to different layers, 
     * the group operation is not available
     */
    @Override
    public boolean isEnabled() {
        WorkArea wa;
        try {
            wa = Application.getApplication().getCurrentFileController()
                    .getCurrentWorkArea();
        } catch (NullPointerException ex) {
            return false;
        }
        if (wa.getSelectionManager().getSelObjects().size() < 2) {
            return false;
        }
        if (wa.getSelectionManager().getOpenGroup() != null) {
            return true;
        }
        return false;
        /*ArrayList<GObject> selObjs = wa.getSelectionManager().getSelObjects();
         ID parentID = selObjs.get(0).getParent()[0].getId();
         for (GObject o : selObjs) {
         if (o.getParent()[0].getId() != parentID) {
         return false;
         }
         }
         return true;*/
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileController fc = Application.getApplication().getCurrentFileController();
        SelectionManager sm = fc.getCurrentWorkArea().getSelectionManager();
        WorkArea wa = fc.getCurrentWorkArea();
        //ArrayList so = sm.getSelObjects();
        GGroup gg = (GGroup) sm.getOpenGroup();
        
        //sm.openGroup(new GGroup());
        sm.createNewGroup();
        /*ID groupID = fc.getConcurrencyController().getNextID();
         gg.addAllObject(so);
         gg.setId(groupID);*/
        wa.assignIDAndParent(gg);
        // fc.doAndSendOperation(wa.getCreateOperation(sm.getOpenGroup()));
        fc.doAndSendOperation(new CreateGroup(gg));
        sm.setSelObject(gg);
        fc.getCurrentWorkArea().repaint();

    }
}
