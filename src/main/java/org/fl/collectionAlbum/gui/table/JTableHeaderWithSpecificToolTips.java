/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

package org.fl.collectionAlbum.gui.table;

import java.awt.event.MouseEvent;
import java.util.function.Function;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class JTableHeaderWithSpecificToolTips extends JTableHeader {

	private static final long serialVersionUID = 1L;
	
	private final Function<Integer, String> toolTipsGetter;
	
	public JTableHeaderWithSpecificToolTips() {
		super();
		this.toolTipsGetter = null;
	}
	
	public JTableHeaderWithSpecificToolTips(TableColumnModel cm) {
		super(cm);
		this.toolTipsGetter = null;
	}
	
	public JTableHeaderWithSpecificToolTips(TableColumnModel cm, Function<Integer, String> toolTipsGetter) {
		super(cm);
		this.toolTipsGetter = toolTipsGetter;
	}
	
	@Override
    public String getToolTipText(MouseEvent e) {
		if (toolTipsGetter != null) {
	        java.awt.Point p = e.getPoint();
	        int index = columnModel.getColumnIndexAtX(p.x);
	        int realIndex = columnModel.getColumn(index).getModelIndex();
	        return toolTipsGetter.apply(realIndex);
		} else {
			return null;
		}
    }
}
