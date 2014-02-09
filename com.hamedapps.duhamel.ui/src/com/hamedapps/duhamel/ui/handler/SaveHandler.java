package com.hamedapps.duhamel.ui.handler;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.hamedapps.duhamel.Duhamel;
import com.hamedapps.duhamel.Response;

public class SaveHandler {
	NumberFormat fd = new DecimalFormat("0.0000");

	@Execute
	public void execute(MApplication application, IEclipseContext context,
			Shell shell) {
		Duhamel duhamel = (Duhamel) context.get("duhamel");
		Vector<Response> responses = duhamel.getResponses();
		FileDialog d = new FileDialog(shell, SWT.SAVE);
		String path = d.open();
		if (path != null)
			try {
				BufferedWriter bufferedWriter = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(
								path)));
				for (Response r : responses) {
					bufferedWriter.write(fd.format(r.getT()) + "\t"
							+ fd.format(r.getP()) + "\t" + fd.format(r.getU())
							+ "\t" + fd.format(r.getV()) + "\t"
							+ fd.format(r.getA()) + "\t"
							+ fd.format(r.getvSup()) + "\n");
				}
				bufferedWriter.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}