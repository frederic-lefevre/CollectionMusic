/*
 * MIT License

Copyright (c) 2017, 2024 Frederic Lefevre

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

package org.fl.collectionAlbum;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OsAction<T> {

	private static final Logger oLog = Control.getAlbumLog();
	
	private final String actionTitle;
	private final String actionCommand;
	private final OsActionCommandParameter<T> commandParameter;
	
	public OsAction(String t, String c, OsActionCommandParameter<T> a) {
		actionTitle   = t;
		actionCommand = c;
		commandParameter = a;
	}

	public String getActionTitle() {
		return actionTitle;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public OsActionCommandParameter<T> getCommandParameter() {
		return commandParameter;
	}

	public void runOsAction(T o) {
		
		StringBuilder fullCommand = new StringBuilder(getActionCommand());
		
		fullCommand.append(" ")
			.append(getCommandParameter().getParametersGetter().apply(o));
		
		String command = fullCommand.toString();
		
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			oLog.log(Level.SEVERE, "IOException executing command " + command, e) ;
		} catch (SecurityException e) {
			oLog.log(Level.SEVERE, "SecurityException executing command " + command, e) ;
		} catch (Exception e) {
			oLog.log(Level.SEVERE, "Exception executing command " + command, e) ;
		}

	}
}
