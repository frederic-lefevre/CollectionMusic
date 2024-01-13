/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.collectionAlbumGui;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.fl.collectionAlbum.OsAction;
import org.fl.collectionAlbum.disocgs.DiscogsInventory.DiscogsAlbumRelease;
import org.fl.collectionAlbumGui.DiscogsReleaseCustomActionListener.CustomAction;

public class DiscogsInventoryMouseAdapter extends MouseAdapter {

	private final DiscogsReleaseJTable discogsReleaseJTable;
	private final JPopupMenu localJPopupMenu;
	
	private final List<DiscogsReleaseMenuItem> discogsReleaseMenuItems;
	
	private static class DiscogsReleaseMenuItem {
		
		private final JMenuItem menuitem;
		private final Predicate<DiscogsAlbumRelease> enabledPredicate;
		
		public DiscogsReleaseMenuItem(JMenuItem menuitem, Predicate<DiscogsAlbumRelease> enabledPredicate) {
			super();
			this.menuitem = menuitem;
			this.enabledPredicate = enabledPredicate;
		}

		public JMenuItem getMenuitem() {
			return menuitem;
		}

		public Predicate<DiscogsAlbumRelease> getEnabledPredicate() {
			return enabledPredicate;
		}
	}
	
	public DiscogsInventoryMouseAdapter(DiscogsReleaseJTable discogsReleaseJTable, List<OsAction<DiscogsAlbumRelease>> osActions) {
		
		this.discogsReleaseJTable = discogsReleaseJTable;
		localJPopupMenu = new JPopupMenu();
		
		discogsReleaseMenuItems = new ArrayList<>();
		
		osActions.forEach(osAction -> 
			addMenuItem(
					osAction.getActionTitle(),
					new DiscogsReleaseCommandListener(discogsReleaseJTable, osAction), 
					osAction.getCommandParameter().getActionValidityPredicate()));
		
		ActionListener infoListener = new DiscogsReleaseCustomActionListener(discogsReleaseJTable, CustomAction.SHOW_INFO);
		addMenuItem("Afficher les informations", infoListener, (release) -> release != null);
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		if (evt.isPopupTrigger()) {
			enableMenuItems();
			localJPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if (evt.isPopupTrigger()) {
			enableMenuItems();
			localJPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}
	
	private void enableMenuItems() {
		
		DiscogsAlbumRelease release = discogsReleaseJTable.getSelectedDisocgsRelease();
		if (release != null) {
			discogsReleaseMenuItems.forEach(menuItem -> menuItem.getMenuitem().setEnabled(menuItem.getEnabledPredicate().test(release)));
		}
	}
	
	private void addMenuItem(String title, ActionListener act, Predicate<DiscogsAlbumRelease> enabledPredicate) {
		JMenuItem localJMenuItem = new JMenuItem(title);
	     localJMenuItem.addActionListener(act);
	     localJPopupMenu.add(localJMenuItem);
	     discogsReleaseMenuItems.add(new DiscogsReleaseMenuItem(localJMenuItem, enabledPredicate));
	}
}
