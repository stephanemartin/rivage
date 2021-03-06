package fr.inria.rivage.gui.actions;

import fr.inria.rivage.gui.dialog.ChatFrame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class ShowChatFrame extends AbstractAction {

	ShowChatFrame() {		
		this.putValue(AbstractAction.NAME, "Chat");
		this.putValue(AbstractAction.SHORT_DESCRIPTION, "Open the Chat Frame");
	}
	
	@Override
	public boolean isEnabled() {
		return false;
	}
	
    @Override
	public void actionPerformed(ActionEvent e) {
		ChatFrame.showChatFrame();
	}

}
