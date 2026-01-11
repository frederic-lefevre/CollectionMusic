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

package org.fl.collectionAlbum.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.function.BooleanSupplier;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class StartControl implements ActivableElement {

	private final JPanel processControlPanel;
	private final JButton processStartButton;
	private final BooleanSupplier activationCondition;

	private final ProgressInformationPanel progressInformationPanel;
	
	public StartControl(String bText, String iText, String sText, BooleanSupplier activationCondition) {
		
		this.activationCondition = activationCondition;
		
		String buttonText = "<html><p>" + bText + "</p></html>";
		
		processControlPanel = new JPanel();
		processControlPanel.setLayout(new BoxLayout(processControlPanel, BoxLayout.Y_AXIS));
		processControlPanel.setBorder(BorderFactory.createMatteBorder(10,10,10,10,Color.BLACK));
		
		processStartButton = new JButton(buttonText);
		
		Font font = new Font("Verdana", Font.BOLD, 14);
		processStartButton.setFont(font);
		processStartButton.setBackground(Color.GREEN);		
		
		processControlPanel.add(processStartButton);
		
		progressInformationPanel = new ProgressInformationPanel();
		progressInformationPanel.setProgressInformation(new ProgressInformation(sText, iText, ""));

		processControlPanel.add(progressInformationPanel);		
	}

	public JPanel getProcessControlPanel() {
		return processControlPanel;
	}

	public JButton getStartButton() {
		return processStartButton;
	}

	public ProgressInformationPanel getProgressInformationPanel() {
		return progressInformationPanel;
	}
	
	@Override
	public void activate() {
		if (activationCondition.getAsBoolean()) {
			processStartButton.setEnabled(true);
		}
	}
	
	@Override
	public void deactivate() {
		processStartButton.setEnabled(false);
	}

}
