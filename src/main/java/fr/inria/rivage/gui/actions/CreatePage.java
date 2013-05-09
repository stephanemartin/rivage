package fr.inria.rivage.gui.actions;

import fr.inria.rivage.Application;
import fr.inria.rivage.elements.GDocument;
import fr.inria.rivage.elements.Page;
import fr.inria.rivage.engine.concurrency.tools.ID;
import fr.inria.rivage.engine.manager.FileController;
import fr.inria.rivage.engine.operations.CreateOperation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class CreatePage extends AbstractAction {

    private ID w;

    CreatePage() {
        this.putValue(AbstractAction.NAME, "New blank Page");
        this.putValue(AbstractAction.SHORT_DESCRIPTION,
                "Create a new blank page");
    }

    @Override
    public boolean isEnabled() {
        return Application.getApplication().getCurrentFileController() != null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileController fc = Application.getApplication()
                .getCurrentFileController();
        
        /*String name = JOptionPane.showInputDialog(Application.getApplication()
                .getMainFrame(), "Enter a name for the new page:",
                "New blank Page", JOptionPane.QUESTION_MESSAGE);

        if (name == null) {
            return;
        }
        if (name.length() == 0) {
            name = "New Page";
        }
**/
        /*List <GObject> pages = fc.getDocument();
		
         //long w = NewPageOperation.AFTER_LAST;
         for (int i = 0; i < pages.size() - 1; i++)
         if (pages.get(i) == fc.getInnerWindow().getSelectedPage())
         {
         w = pages.get(i + 1).getId();
         break;
         }*/
        GDocument doc=fc.getDocument();
        ID id =fc.getConcurrencyController().getNextID();
        Page p = new Page(doc,id,"Page "+(doc.size()+1),new Dimension(2000, 2000));
        fc.getDocument().assignZPos(p, fc.getDocument().getMax());
        fc.doAndSendOperation(
                new CreateOperation(p));
        CreateLayer.createLayer(p, fc);
        
    }
    
}
