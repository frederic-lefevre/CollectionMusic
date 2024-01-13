/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Logger;

import org.fl.collectionAlbum.Control;
import org.fl.collectionAlbum.mediaPath.MediaFilePath;
import org.fl.util.os.OScommand;

public class MediaFileCustomActionListener implements java.awt.event.ActionListener {

	private final static Logger albumLog = Control.getAlbumLog();
	
	public enum CustomAction { ShowInExplorer };
	
	private static Map<CustomAction, String> customActionCommands;
	
	private final MediaFilesJTable mediaFileTable;
	private final CustomAction customAction;
	
	public MediaFileCustomActionListener(MediaFilesJTable mediaFileTable, CustomAction customAction) {
		
		this.mediaFileTable = mediaFileTable;
		this.customAction = customAction;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		MediaFilePath selectedMediaFile = mediaFileTable.getSelectedMediaFile();
		
		if (selectedMediaFile != null) {
			
			if (customAction.equals(CustomAction.ShowInExplorer)) {
				// Launch a file explorer on the file path
				
				String showInExplorerCmd = customActionCommands.get(CustomAction.ShowInExplorer);
				
				if ((showInExplorerCmd != null) && (!showInExplorerCmd.isEmpty())) {
					
					Path folder = selectedMediaFile.getPath();
					if (Files.exists(folder)) {
						if (Files.isDirectory(folder)) {
							OScommand osCommand = new OScommand(showInExplorerCmd + " \"" + folder.toAbsolutePath() + "\"", false, albumLog);
							osCommand.run();
						} else {
							albumLog.warning("Cannot launch explorer on media file folder, it is not a directory : " + folder);
						}
					} else {
						albumLog.warning("Cannot launch explorer on media file folder, it does not exists : " + folder);
					}
				}
			}
		}
	}

	public static void setCustomActionCommands(Map<CustomAction, String> cac) {
		customActionCommands = cac ;
	}
}
