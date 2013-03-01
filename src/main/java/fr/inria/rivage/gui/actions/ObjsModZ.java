package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.GObject;
import fr.inria.rivage.elements.GObjectContainer;
import fr.inria.rivage.engine.concurrency.IConcurrencyController;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.concurrency.tools.Parameters;
import fr.inria.rivage.engine.concurrency.tools.Position;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.manager.SelectionManager;
import fr.inria.rivage.gui.WorkArea;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class ObjsModZ extends AbstractAction {

    static enum Where {

        Top, Back, Up, Down
    };
    Where where;

    ObjsModZ(Where where) {
        this.where = where;
        switch (where) {
            case Top:
                this.putValue(AbstractAction.NAME, "To Front");
                this.putValue(AbstractAction.SHORT_DESCRIPTION,
                        "Send selected Objects to the front");
                this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(
                        Application.class
                        .getResource("resources/images/object-order-front.png")));
                break;
            case Back:
                this.putValue(AbstractAction.NAME, "To Back");
                this.putValue(AbstractAction.SHORT_DESCRIPTION,
                        "Send selected Objects to the back");
                this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(
                        Application.class
                        .getResource("resources/images/object-order-back.png")));
                break;
            case Up:
                this.putValue(AbstractAction.NAME, "Up");
                this.putValue(AbstractAction.SHORT_DESCRIPTION,
                        "Move object up");
                this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(
                        Application.class
                        .getResource("resources/images/object-order-raise.png")));
                break;
            case Down:
                this.putValue(AbstractAction.NAME, "Down");
                this.putValue(AbstractAction.SHORT_DESCRIPTION,
                        "Move object Down");
                this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(
                        Application.class
                        .getResource("resources/images/object-order-lower.png")));
                break;

        }

    }

    @Override
    public boolean isEnabled() {
        try {
            return Application.getApplication().getCurrentFileController()
                    .getCurrentWorkArea().getSelectionManager().getSelObjects().size() >= 1;
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileController fc = Application.getApplication().getCurrentFileController();
        WorkArea wa = fc.getCurrentWorkArea();
        GDocument doc = fc.getDocument();
        SelectionManager sm = wa.getSelectionManager();

        List<GObject> objs = sm.getOpenGroup().getRealObjects();

        if (objs.isEmpty()) {
            return;
        }
        Position lastP = null;
        IConcurrencyController cc = wa.getFileController().getConcurrencyController();
        ID id = null;
        for (GObject obj : objs) {

            Position p;
            switch (where) {
                case Top:
                    id = cc.getNextID();
                    p = wa.getActiveLayer().getMax();
                    /*lastP=doc.setNewbetween(lastP, p, obj);
                     continue;*/


                    if (lastP == null) {
                        lastP = p.genNext(id);
                    } else {
                        lastP = lastP.genBetween(p, id);
                    }
                    break;
                case Back:
                    id = cc.getNextID();
                    p = wa.getActiveLayer().getMin();
                    /*lastP=doc.setNewbetween(p,lastP, obj);
                     continue;*/
                    if (lastP == null) {
                        lastP = p.genPrevious(id);
                    } else {
                        lastP = p.genBetween(lastP, id);
                    }
                    break;
                case Up:
                    doc.zPosUp((GObjectContainer<GObject>) obj.getParent()[0], obj);
                    continue;
                case Down:
                    doc.zPosDown((GObjectContainer<GObject>) obj.getParent()[0], obj);
                    continue;
            }
            if (lastP != null) {
                obj.deleteMeFromParent();
                obj.getParameters().setObject(Parameters.ParameterType.Zpos, lastP);
                obj.getParameters().getParameter(Parameters.ParameterType.Zpos).sendMod(id);

                obj.addMeFromParent();

            }

        }
        wa.treeChanged();
    }
}
