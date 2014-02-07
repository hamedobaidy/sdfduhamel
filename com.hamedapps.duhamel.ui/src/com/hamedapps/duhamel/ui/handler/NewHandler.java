 
package com.hamedapps.duhamel.ui.handler;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import com.hamedapps.duhamel.Duhamel;
import com.hamedapps.duhamel.ui.part.DataPart;

public class NewHandler {
	@Execute
	public void execute(MApplication application, EPartService partService, IEclipseContext context) {
		DataPart dataPart = (DataPart) partService.findPart("com.hamedapps.duhamel.ui.part.datapart").getObject();
		dataPart.clearFields();
		
		Duhamel duhamel = (Duhamel) context.get("duhamel");
		duhamel.getInputForces().clear();
	}
		
}