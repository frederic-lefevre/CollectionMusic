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

import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import org.fl.collectionAlbum.gui.DetailedAlbumAndDiscogsInfoPane.ReleasePanel;
import org.fl.collectionAlbum.utils.CollectionImage;
import org.fl.collectionAlbum.utils.CollectionUtils;
import org.fl.discogsInterface.Release;

public class DiscogsReleaseRequest extends SwingWorker<DiscogsReleaseRequest.ReleaseRequestResult, String> {

	private static final String ERROR_MESSAGE = "<html><body>Erreur de communication avec discogs .... Reéssayer</body></html>";
	
	private static final Logger logger = Logger.getLogger(DiscogsReleaseRequest.class.getName());
	
	private final DiscogsAlbumRelease discogsAlbumRelease;
	private final ReleasePanel releasePanel;
	
	public DiscogsReleaseRequest(DiscogsAlbumRelease discogsAlbumRelease, ReleasePanel releasePanel) {
		this.discogsAlbumRelease = discogsAlbumRelease;
		this.releasePanel = releasePanel;
	}
	
	record ReleaseRequestResult(Release release, CollectionImage collectionImage) {};
	
	@Override
	protected ReleaseRequestResult doInBackground() {
		
		if (discogsAlbumRelease == null) {
			return null;
		} else {
			Release release = discogsAlbumRelease.discogsRelease();
			return new ReleaseRequestResult(release, discogsAlbumRelease.coverImage());
		}
	}
	
	@Override
	public void done() {
		
		try {
			ReleaseRequestResult releaseRequestResult = get();
			if (releaseRequestResult.release() != null) {
				releasePanel.setReleaseTextInfo(CollectionUtils.getHtmlForDiscogsRelease(releaseRequestResult.release(), discogsAlbumRelease.inventoryCsvAlbum()));
			} else {
				releasePanel.setReleaseTextInfo(ERROR_MESSAGE);
			}
			releasePanel.setReleaseCoverImage(releaseRequestResult.collectionImage());
			releasePanel.configureImageBrowserButton(discogsAlbumRelease.getImageUriList());
		} catch (InterruptedException | ExecutionException e) {
			logger.log(Level.SEVERE, "DiscogsReleaseRequest exception", e);
			releasePanel.setReleaseTextInfo(ERROR_MESSAGE);
			releasePanel.setReleaseCoverImage(new CollectionImage((URL)null));
		}
	}
}
