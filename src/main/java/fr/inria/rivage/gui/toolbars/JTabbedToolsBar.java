/*
 *  Replication Benchmarker
 *  https://github.com/score-team/replication-benchmarker/
 *  Copyright (C) 2012 LORIA / Inria / SCORE Team
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
package fr.inria.rivage.gui.toolbars;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

/**
 *
 * @author Stephane Martin <stephane.martin@loria.fr>
 */
public class JTabbedToolsBar extends JToolBar{
    JTabbedPane jtab= new JTabbedPane();
    Dimension dim=new Dimension(0,0);
    
    
    int count=0;
    public JTabbedToolsBar() {
        super(JToolBar.VERTICAL);
        
    }

    public JTabbedToolsBar(int orientation) {
        super(orientation);
       
        
    }

    public JTabbedToolsBar(String name) {
        super(name);
       
    }

    public JTabbedToolsBar(String name, int orientation) {
        super(name, orientation);
        
    }
  
    void max(Dimension d1){
        this.dim=new Dimension((int)Math.max(d1.getWidth(), dim.getWidth()),
                (int)Math.max(d1.getHeight(), dim.getHeight()));
      
    }
    public void addTabbe(String name,JComponent jcomp){
        jtab.addTab(name,jcomp);
        count++;
        
        if(count==1){
            this.removeAll();
             JLabel title = new JLabel(jcomp.getName());
             System.out.println("--"+jcomp.getName());
		title.setForeground(Color.blue);
		add(title, BorderLayout.NORTH);
            this.add(jcomp,BorderLayout.CENTER);
            this.setName(name);
            this.dim=jcomp.getPreferredSize();
           
        }
        if (count==2){
            this.removeAll();
            this.add(jtab,BorderLayout.CENTER);
            this.setName("");
            this.max(jcomp.getPreferredSize());
        }
        this.setPreferredSize(this.dim);      
        jtab.setPreferredSize(dim);
        this.repaint();
        this.updateUI();
        revalidate();
    }
    public void addTabbe(JComponent jcomp){
        this.addTabbe(jcomp.getName(),jcomp);
    }
    public void removeTabbe(JComponent jcomp){
        jtab.remove(jcomp);
         if(count==1){
            this.removeAll();
            this.add(jcomp);
            this.setName(jtab.getTitleAt(0));
            
        }
        if (count==0){
            this.removeAll();
            this.setName("");
        }
        this.dim=new Dimension(0,0);
        for(Component jc:jtab.getComponents()){
            max(jc.getPreferredSize());
        }
         this.setPreferredSize(this.dim);       
         jtab.setPreferredSize(dim);
        this.repaint();
    }
}
