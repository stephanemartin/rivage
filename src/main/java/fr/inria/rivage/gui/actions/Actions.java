package fr.inria.rivage.gui.actions;

import fr.inria.rivage.gui.GButton;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

public enum Actions {

    OPEN_FILE(new OpenFile()),
    NEW_FILE(new NewFile()),
    SAVE_FILE(new SaveFile()),
    EXPORT_SVG(new ExportSVG()),
    IMPORT_SVG(new ImportSVG()),
    CLOSE_FILE(new CloseFile()),
    CLOSE_ALL_FILES(new CloseAllFiles()),
    PRINT_FILE(new PrintFile()),
    QUIT(new Quit()),
    SHOW_STATUSBAR(new ShowStatusbar()),
    SHOW_OP_BUTTON_BAR(new ShowOpButtonBar()),
    SHOW_CHAT_FRAME(new ShowChatFrame()),
    SHOW_HISTORY(new ShowHistory()),
    SHOW_CONFLICTS(new ShowConflicts()),
    SHOW_SETTINGS(new ShowSettings()),
    SHOW_HELP(new ShowHelp()),
    SHOW_ABOUT(new ShowAbout()),
    GROUP_OBJS(new GroupObjs()),
    UNGROUP_OBJS(new UngroupObjs()),
    OBJS_TO_FRONT(new ObjsModZ(ObjsModZ.Where.Top)),
    OBJS_UP(new ObjsModZ(ObjsModZ.Where.Up)),
    OBJS_DOWN(new ObjsModZ(ObjsModZ.Where.Down)),
    OBJS_TO_BACK(new ObjsModZ(ObjsModZ.Where.Back)),
    UNDO(new Undo()),
    REDO(new Redo()),
    /*UNDO_LOCAL (new UndoLocal()),
     REDO_LOCAL (new RedoLocal()),
     UNDO_GLOBAL (new UndoGlobal()),
     REDO_GLOBAL (new RedoGlobal()),*/
    CUT_OBJS(new CutCopyObj(true)),
    COPY_OBJS(new CutCopyObj(false)),
    PASTE_OBJS(new Paste()),
    DELETE_OBJS(new DeleteObjs()),
    CREATE_NEW_TEMPLATE(new CreateNewTemplate()),
    //MOVE_OBJS_TO_LAYER (new MoveObjectsToLayer()),
    DELETE_LAYER(new DeleteLayer()),
    CREATE_LAYER(new CreateLayer()),
    UP_LAYER(new MoveLayer(MoveLayer.Action.Up)),
    DOWN_LAYER(new MoveLayer(MoveLayer.Action.Down)),
    NEW_PAGE(new CreatePage()),
    REMOVE_PAGE(new RemovePage()),
    COPY_PAGE(new CopyPage()),
    CHANGE_PAGE(new ChangePageSettings()),
    CONNECT_TO(new ConnectTo());
    
    public final AbstractAction action;

    private Actions(AbstractAction action) {
        this.action = action;
    }

    public void doAction() {
        action.actionPerformed(null);
    }

    public JMenuItem createMenuItem() {
        return new JMenuItem(action);
    }

    public GButton createButton() {
        return new GButton(action);
    }
}
