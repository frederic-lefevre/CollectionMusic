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

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProgressInformationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProgressInformationPanel.class.getName());
	
	private final JLabel lblStepPrefixInformation;
	private final JLabel lblStepInformation;
	private final JLabel lblStatus;
	
	private static final String dateFrancePattern = " EEEE dd MMMM yyyy à HH:mm:ss";
	private final DateTimeFormatter dateTimeFormatter;
	
	public ProgressInformationPanel() {
		
		super();
		dateTimeFormatter = DateTimeFormatter.ofPattern(dateFrancePattern, Locale.FRANCE);
		Font font = new Font("Verdana", Font.BOLD, 14);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.WHITE));
		setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel statusPane = new JPanel();
		statusPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblStatusTitle = new JLabel("Etat: ");
		lblStatus = new JLabel("");
		lblStatusTitle.setFont(font);
		lblStatusTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblStatusTitle.setBackground(Color.WHITE);
		lblStatus.setFont(font);
		lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblStatus.setBackground(Color.WHITE);
		statusPane.add(lblStatusTitle);
		statusPane.add(lblStatus);
		statusPane.setBackground(Color.WHITE);
		statusPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(statusPane);

		JPanel infoStep = new JPanel();
		infoStep.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblStep = new JLabel("Progression: ");
		lblStepPrefixInformation = new JLabel("");
		lblStepInformation = new JLabel("");
		lblStep.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblStep.setFont(font);
		lblStepInformation.setFont(font);
		lblStepInformation.setBackground(Color.WHITE);
		lblStepPrefixInformation.setFont(font);
		lblStepPrefixInformation.setBackground(Color.WHITE);
		lblStep.setBackground(Color.WHITE);
		infoStep.add(lblStep);
		infoStep.add(lblStepPrefixInformation);
		infoStep.add(lblStepInformation);
		infoStep.setAlignmentX(Component.LEFT_ALIGNMENT);
		infoStep.setBackground(Color.WHITE);
		add(infoStep);
	}
	
	public void setProgressInformation(ProgressInformation progressInformation) {
		
    	String processStatus = progressInformation.getProcessStatus();
    	String stepPrefixInformation = progressInformation.getStepPrefixInformation();
    	String stepInformation = progressInformation.getStepInformation();
    	
    	if (processStatus != null) {
    		lblStatus.setText(processStatus + dateTimeFormatter.format(LocalDateTime.now()));
    		logger.info(processStatus);
    	}
    	if (stepPrefixInformation != null) {
    		lblStepPrefixInformation.setText(stepPrefixInformation);
    	}
    	if (stepInformation != null) {
    		lblStepInformation.setText(stepInformation);
    	}
	}
}
