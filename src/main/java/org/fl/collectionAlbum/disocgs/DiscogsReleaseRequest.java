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

package org.fl.collectionAlbum.disocgs;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;
import javax.swing.text.JTextComponent;

import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.discogsInterface.Release;

public class DiscogsReleaseRequest extends SwingWorker<Release, String> {

	private static final String ERROR_MESSAGE = "<html><body>Erreur de communication avec discogs .... Reéssayer</body></html>";
	
	private static final Logger logger = Logger.getLogger(DiscogsReleaseRequest.class.getName());
	
	private final DiscogsAlbumRelease discogsAlbumRelease;
	private final JTextComponent textComponent;
	
	public DiscogsReleaseRequest(DiscogsAlbumRelease discogsAlbumRelease, JTextComponent textComponent) {
		this.discogsAlbumRelease = discogsAlbumRelease;
		this.textComponent = textComponent;
	}
	
	@Override
	protected Release doInBackground() throws Exception {
		
		if (discogsAlbumRelease == null) {
			return null;
		} else {
			return discogsAlbumRelease.discogsRelease();
		}
	}

	@Override
	public void done() {
		
		try {
			Release release = get();
			if (release != null) {
				textComponent.setText(CollectionUtils.getHtmlForDiscogsRelease(release, discogsAlbumRelease.inventoryCsvAlbum()));
			} else {
				textComponent.setText(ERROR_MESSAGE);
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.log(Level.SEVERE, "DiscogsReleaseRequest exception", e);
			textComponent.setText(ERROR_MESSAGE);
		}
	}
}
