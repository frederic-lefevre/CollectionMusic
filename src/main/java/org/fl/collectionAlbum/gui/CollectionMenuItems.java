/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

package org.fl.collectionAlbum.gui;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class CollectionMenuItems<T> {

	private static class CollectionMenuItem<T> {
		
		private final JMenuItem menuitem;
		private final Predicate<T> enabledPredicate;
		
		public CollectionMenuItem(JMenuItem menuitem, Predicate<T> enabledPredicate) {
			super();
			this.menuitem = menuitem;
			this.enabledPredicate = enabledPredicate;
		}

		public JMenuItem getMenuitem() {
			return menuitem;
		}

		public Predicate<T> getEnabledPredicate() {
			return enabledPredicate;
		}
	}
	
	private final List<CollectionMenuItem<T>> menuItems;
	
	public CollectionMenuItems() {
		super();
		menuItems = new ArrayList<>();
	}
	
	public void addMenuItem(String title, ActionListener act, Predicate<T> enablePredicate, JPopupMenu localJPopupMenu) {
		
		JMenuItem localJMenuItem = new JMenuItem(title);
	     localJMenuItem.addActionListener(act);
	     localJPopupMenu.add(localJMenuItem);
	     menuItems.add(new CollectionMenuItem<>(localJMenuItem, enablePredicate));
	}
	
	public void enableMenuItems(T selectedCollectionObject) {
		menuItems.forEach(menuItem -> 
			menuItem.getMenuitem().setEnabled((selectedCollectionObject != null) && menuItem.getEnabledPredicate().test(selectedCollectionObject))
		);
	}
}
