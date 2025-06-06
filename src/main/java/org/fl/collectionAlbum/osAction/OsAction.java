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

package org.fl.collectionAlbum.osAction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OsAction<T> {

	private static final Logger oLog = Logger.getLogger(OsAction.class.getName());
	
	private final String actionTitle;
	private final OsCommandAndOption osCommandAndOption;
	private final OsActionCommandParameter<T> commandParameter;
	
	public OsAction(String t, OsCommandAndOption osCommandAndOption, OsActionCommandParameter<T> a) {
		actionTitle   = t;
		this.osCommandAndOption = osCommandAndOption;
		commandParameter = a;
	}

	public String getActionTitle() {
		return actionTitle;
	}

	public OsActionCommandParameter<T> getCommandParameter() {
		return commandParameter;
	}

	public void runOsAction(T o) {
		
		List<String> cmdAndParams = new ArrayList<>();
		cmdAndParams.add(osCommandAndOption.getActionCommand());
		if (osCommandAndOption.hasOptions()) {
			cmdAndParams.addAll(osCommandAndOption.getActionOptions());
		}
		cmdAndParams.addAll(getCommandParameter().getParametersGetter().apply(o));
		
		try {
			Runtime.getRuntime().exec(cmdAndParams.toArray(new String[0]));
		} catch (IOException e) {
			oLog.log(Level.SEVERE, "IOException executing command " + cmdAndParams.toString(), e) ;
		} catch (SecurityException e) {
			oLog.log(Level.SEVERE, "SecurityException executing command " + cmdAndParams.toString(), e) ;
		} catch (Exception e) {
			oLog.log(Level.SEVERE, "Exception executing command " + cmdAndParams.toString(), e) ;
		}
	}
}
