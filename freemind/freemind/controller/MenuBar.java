/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000  Joerg Mueller <joergmueller@bigfoot.com>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*$Id: MenuBar.java,v 1.6 2000-10-27 21:44:35 ponder Exp $*/

package freemind.controller;

import freemind.main.FreeMind;
import java.util.ListIterator;
import java.util.List;
import java.util.LinkedList;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


/**This is the menu bar for FreeMind. Actions are defined in MenuListener. */
public class MenuBar extends JMenuBar {

    JMenu mapsmenu;
    private JMenu filemenu = new JMenu(FreeMind.getResources().getString("file"));
    private JMenu editmenu = new JMenu(FreeMind.getResources().getString("edit"));
    Controller c;

    public MenuBar(Controller controller) {
	this.c = controller;

	this.add(filemenu);
	this.add(editmenu);

	//Mapsmenu
	mapsmenu = new JMenu(FreeMind.getResources().getString("mindmaps"));
	this.add(mapsmenu);

	//Modesmenu
	JMenu modesmenu = new JMenu(FreeMind.getResources().getString("modes"));
	this.add(modesmenu);

	ActionListener modesMenuActionListener = new ModesMenuActionListener();
	List keys = new LinkedList(c.getModes().keySet());
	for (ListIterator i = keys.listIterator(); i.hasNext();) {
	    String key = (String)i.next();
	    JMenuItem newItem = new JMenuItem(key);
	    modesmenu.add(newItem);
	    String keystroke = FreeMind.userProps.getProperty("keystroke_mode_"+key);
	    if (keystroke != null) {
		newItem.setAccelerator(KeyStroke.getKeyStroke(keystroke));
	    }
	    newItem.addActionListener(modesMenuActionListener);
	    //if (key.equals(c.getMode().toString())) {
	    //		newItem.setBackground(Color.blue);
		//}
	}

	//Helpmenu
	JMenu helpmenu = new JMenu(FreeMind.getResources().getString("help"));
	this.add(helpmenu);

	helpmenu.add(c.documentation);

	helpmenu.add(c.license);
	
	helpmenu.add(c.about);
	
    }//Constructor

    public void updateMapsMenu() {
	ActionListener mapsMenuActionListener = new MapsMenuActionListener();
	mapsmenu.removeAll();
	if (c.getMapModules() == null) {
	    return;
	}
	List keys = new LinkedList(c.getMapModules().keySet());
	for (ListIterator i = keys.listIterator(); i.hasNext();) {
	    String key = (String)i.next();
	    JMenuItem newItem = new JMenuItem(key);
	    mapsmenu.add(newItem);
	    newItem.addActionListener(mapsMenuActionListener);
	    if (c.getMapModule() != null) {
		if (key.equals(c.getMapModule().toString())) {
		    //This could be done more elegant
		    newItem.setBackground(Color.blue);
		}
	    }
	}
    }

    public void updateFileMenu() {
	filemenu.removeAll();
	if ((c.getMode() != null) && (c.getMode().getModeFileMenu() != null)) {
	    copyMenuItems(c.getMode().getModeFileMenu(), filemenu);
	}

	filemenu.addSeparator();
	JMenuItem print = filemenu.add(c.print);

	filemenu.addSeparator();

	JMenuItem close = filemenu.add(c.close);
	close.setAccelerator(KeyStroke.getKeyStroke(FreeMind.userProps.getProperty("keystroke_close")));
	
	JMenuItem quit = filemenu.add(c.quit);
	quit.setAccelerator(KeyStroke.getKeyStroke(FreeMind.userProps.getProperty("keystroke_quit")));
    }

    public void updateEditMenu() {
	editmenu.removeAll();

	if ((c.getMode() != null) && (c.getMode().getModeEditMenu() != null)) {
	    copyMenuItems(c.getMode().getModeEditMenu(), editmenu);
	}

	editmenu.addSeparator();

	JMenuItem moveToRoot = editmenu.add(c.moveToRoot);
	moveToRoot.setAccelerator(KeyStroke.getKeyStroke(FreeMind.userProps.getProperty("keystroke_moveToRoot")));

	JMenuItem previousMap = editmenu.add(c.previousMap);
	previousMap.setAccelerator(KeyStroke.getKeyStroke(FreeMind.userProps.getProperty("keystroke_previousMap")));

	JMenuItem nextMap = editmenu.add(c.nextMap);
	nextMap.setAccelerator(KeyStroke.getKeyStroke(FreeMind.userProps.getProperty("keystroke_nextMap")));

// 	JMenuItem cut = editmenu.add(c.cut);
// 	cut.setAccelerator(KeyStroke.getKeyStroke(FreeMind.userProps.getProperty("keystroke_cut")));
	
// 	JMenuItem paste = editmenu.add(c.paste);
// 	paste.setAccelerator(KeyStroke.getKeyStroke(FreeMind.userProps.getProperty("keystroke_paste")));

	editmenu.addSeparator();

	JMenu preferences = new JMenu(FreeMind.getResources().getString("preferences"));
	editmenu.add(preferences);

	preferences.add(c.background);
    }

    /**
     * This method simpy copy's all elements of the source Menu
     * to the end of the second menu.
     */
    private void copyMenuItems (JMenu source, JMenu dest) {
	Component[] items = source.getMenuComponents();
	for (int i=0; i<items.length; i++) {
	    dest.add(items[i]);
	}
    }

    private class MapsMenuActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    c.changeToMapModule(e.getActionCommand());
	}
    }

    private class ModesMenuActionListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    c.changeToMode(e.getActionCommand());
	}
    }
}
