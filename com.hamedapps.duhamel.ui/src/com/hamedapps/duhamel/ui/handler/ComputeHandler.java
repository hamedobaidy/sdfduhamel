 
package com.hamedapps.duhamel.ui.handler;

import java.util.List;
import java.util.Vector;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.hamedapps.duhamel.Duhamel;
import com.hamedapps.duhamel.InputForce;
import com.hamedapps.duhamel.Response;
import com.hamedapps.duhamel.ui.part.DisplacementChartPart;
import com.hamedapps.duhamel.ui.part.InputChartPart;
import com.hamedapps.duhamel.ui.part.ResponseDataPart;

public class ComputeHandler {
	
	@Execute
	public void execute(MApplication application, IEclipseContext context, EModelService modelService) {
		
		Duhamel duhamel = (Duhamel) context.get("duhamel");
		duhamel.compute();
		
		Vector<InputForce> inputForces = duhamel.getInputForces();
		for (InputForce inputForce : inputForces) {
			System.out.println(inputForce);
		}
		
		Vector<Response> responses = duhamel.getResponses();
		for (Response response : responses) {
			System.out.println("Time: " + response.getT() + ", Force: " + response.getP() + ", U: " + response.getU());
		}
		
		List<MPart> parts = modelService.findElements(application, "com.hamedapps.duhamel.ui.part.responsedata",
		        MPart.class, null);
		System.out.println("Found parts(s) : " + parts.size());
		ResponseDataPart responseDataPart = (ResponseDataPart) parts.get(0).getObject();
		responseDataPart.updateTableViewer();
		
		List<MPart> parts1 = modelService.findElements(application, "com.hamedapps.duhamel.ui.part.inputchartpart",
		        MPart.class, null);
		System.out.println("Found parts(s) : " + parts1.size());
		InputChartPart inputChartPart = (InputChartPart) parts1.get(0).getObject();
		inputChartPart.updateChart();
		
		List<MPart> parts2 = modelService.findElements(application, "com.hamedapps.duhamel.ui.part.displacementchartpart", MPart.class, null);
		System.out.println("Found part(s) : " + parts2.size());
		DisplacementChartPart displacementChartPart = (DisplacementChartPart) parts2.get(0).getObject();
		displacementChartPart.updateChart();
	}
		
}