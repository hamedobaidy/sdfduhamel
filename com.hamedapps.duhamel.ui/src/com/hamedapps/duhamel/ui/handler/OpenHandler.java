 
package com.hamedapps.duhamel.ui.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class OpenHandler {
	@Execute
	public void execute(Shell shell) {
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		String path = fileDialog.open();
		if(path != null ) {
			System.out.println(path);
		}
	}
		
}