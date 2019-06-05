package org.fl.collectionAlbumGui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProgressInformationPanel {


	private JPanel procInfos ;
	private JLabel lblStep;
	private JLabel lblStepInfo;
	private JLabel lblStatusTitle;
	private JLabel lblStatus;
	private static String dateFrancePattern = " EEEE dd MMMM yyyy à HH:mm:ss" ;
	private DateTimeFormatter dateTimeFormatter ;
	
	public ProgressInformationPanel() {

		dateTimeFormatter = DateTimeFormatter.ofPattern(dateFrancePattern, Locale.FRANCE) ;
		Font font = new Font("Verdana", Font.BOLD, 14);
		procInfos = new JPanel() ;
		procInfos.setLayout(new BoxLayout(procInfos, BoxLayout.Y_AXIS));
		procInfos.setBackground(Color.WHITE) ;
		procInfos.setPreferredSize(new Dimension(800, 300)) ;
		procInfos.setBorder(BorderFactory.createMatteBorder(10,10,10,10,Color.WHITE)) ;
		procInfos.setAlignmentX(Component.LEFT_ALIGNMENT) ;
		
		JPanel statusPane = new JPanel() ;
		statusPane.setLayout(new FlowLayout(FlowLayout.LEFT)) ;
		lblStatusTitle = new JLabel("Etat: ");	
		lblStatus = new JLabel("");
		lblStatusTitle.setFont(font) ;
		lblStatusTitle.setAlignmentX(Component.LEFT_ALIGNMENT) ;
		lblStatusTitle.setBackground(Color.WHITE) ;
		lblStatus.setFont(font) ;
		lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT) ;
		lblStatus.setBackground(Color.WHITE) ;
		statusPane.add(lblStatusTitle) ;
		statusPane.add(lblStatus) ;
		statusPane.setBackground(Color.WHITE) ;
		statusPane.setAlignmentX(Component.LEFT_ALIGNMENT) ;
		procInfos.add(statusPane) ;
		
		JPanel infoStep = new JPanel() ;
		infoStep.setLayout(new FlowLayout(FlowLayout.LEFT)) ;
		lblStep = new JLabel("Progression: ");
		lblStepInfo = new JLabel("");
		lblStep.setAlignmentX(Component.LEFT_ALIGNMENT) ;
		lblStep.setFont(font) ;
		lblStepInfo.setFont(font) ;
		lblStepInfo.setBackground(Color.WHITE) ;
		lblStep.setBackground(Color.WHITE) ;
		infoStep.add(lblStep) ;
		infoStep.add(lblStepInfo) ;
		infoStep.setAlignmentX(Component.LEFT_ALIGNMENT) ;
		infoStep.setBackground(Color.WHITE) ;
		procInfos.add(infoStep) ;
	}

	public JPanel getProcInfos() {
		return procInfos;
	}
	
	public void setStepInfos(String info) {
		 lblStepInfo.setText(info);
	}
	
	public void setProcessStatus(String st) {
		 lblStatus.setText(st + dateTimeFormatter.format(LocalDateTime.now()));
	}
}
