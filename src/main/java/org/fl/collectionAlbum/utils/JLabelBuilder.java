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

package org.fl.collectionAlbum.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class JLabelBuilder {
	
	private JLabelBuilder() {
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private JLabel jLabel;
		
		private boolean opaque;
		private int horizontalAlignment;
		private String text;
		private Color backgroundColor;
		private Color foregroundColor;
		private Border border;
		private Dimension dimension;
		private Font font;
		private List<MouseMotionListener> mouseMotionListeners;
		
		private Builder() {	
			// Default value set/change
			opaque = true;
			horizontalAlignment = SwingConstants.CENTER;
			text = "";
			backgroundColor = Color.WHITE;
			foregroundColor = Color.BLACK;
			border = BorderFactory.createLineBorder(Color.BLACK);
			dimension = null;
			font = null;
			mouseMotionListeners = new ArrayList<>();
		}
		
		public Builder text(String text) {
			this.text = text;
			return this;
		}
		
		public Builder backgroundColor(Color backgroundColor) {
			this.backgroundColor = backgroundColor;
			return this;
		}
		
		public Builder foregroundColor(Color foregroundColor) {
			this.foregroundColor = foregroundColor;
			return this;
		}
			
		public Builder border(Border border) {
			this.border = border;
			return this;
		}
		
		public Builder preferredSize(Dimension dimension) {
			this.dimension = dimension;
			return this;
		}
		
		public Builder font(Font font) {
			this.font = font;
			return this;
		}
		
		public Builder opaque(boolean opaque) {
			this.opaque = opaque;
			return this;
		}
		
		public Builder horizontalAlignment(int horizontalAlignment) {
			this.horizontalAlignment = horizontalAlignment;
			return this;
		}
		
		public Builder mouseMotionListener(MouseMotionListener listener) {
			this.mouseMotionListeners.add(listener);
			return this;
		}
		
		public JLabel build() {
			jLabel = new JLabel();
			jLabel.setOpaque(opaque);
			jLabel.setHorizontalAlignment(horizontalAlignment);
			jLabel.setText(text);
			jLabel.setBackground(backgroundColor);
			jLabel.setForeground(foregroundColor);
			jLabel.setBorder(border);
			if (dimension != null) {
				jLabel.setPreferredSize(dimension);
			}
			if (font != null) {
				jLabel.setFont(font);
			}
			if (! mouseMotionListeners.isEmpty()) {
				mouseMotionListeners.forEach(m -> jLabel.addMouseMotionListener(m));
			}
			return jLabel;
		}
	}
}
