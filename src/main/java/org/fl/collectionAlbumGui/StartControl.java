package org.fl.collectionAlbumGui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class StartControl {

	private JPanel procCtrl ;
	private JButton pStart ;

	private boolean triggered ;

	private ProgressInformationPanel pip;
	
	public StartControl(String bText, String iText, String sText) {
		
		String buttonText = "<html><p>" + bText + "</p></html>";
		
		triggered = false ;
		
		procCtrl = new JPanel() ;
		procCtrl.setLayout(new BoxLayout(procCtrl, BoxLayout.X_AXIS));
		procCtrl.setBorder(BorderFactory.createMatteBorder(10,10,10,10,Color.BLACK)) ;
		procCtrl.setPreferredSize(new Dimension(400,120)) ;
		
		pStart = new JButton(buttonText) ;
		
		Font font = new Font("Verdana", Font.BOLD, 14);
		pStart.setFont(font) ;
		pStart.setBackground(Color.GREEN) ;		
		
		procCtrl.add(pStart) ;
		
		pip = new ProgressInformationPanel() ;
		pip.setProcessStatus(sText) ;
		pip.setStepPrefixInformation(iText);
		pip.setStepInformation("");
			
		procCtrl.add(pip.getProcInfos()) ;		
	}

	public JPanel 					getProcCtrl() 	 { return procCtrl ; }
	public JButton 					getStartButton() { return pStart   ; }	
	public ProgressInformationPanel getPip() 		 { return pip 	   ; }

	public boolean isTriggered() {
		return triggered;
	}
	
	public boolean isActive() {
		return pStart.isEnabled() ;
	}
	
	public void activate() {
		triggered = false ;
		pStart.setBackground(new Color(27,224,211)) ;
		pStart.setEnabled(true) ;
	}
	
	public void deactivate() {
		pStart.setEnabled(false) ;
	}

	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
	}
}
