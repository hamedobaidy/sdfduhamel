package com.hamedapps.duhamel.ui.handler;

import java.util.List;
import java.util.Vector;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import com.hamedapps.duhamel.Duhamel;
import com.hamedapps.duhamel.InputForce;
import com.hamedapps.duhamel.Response;
import com.hamedapps.duhamel.ui.part.AccelerationChartPart;
import com.hamedapps.duhamel.ui.part.DataPart;
import com.hamedapps.duhamel.ui.part.DisplacementChartPart;
import com.hamedapps.duhamel.ui.part.InputChartPart;
import com.hamedapps.duhamel.ui.part.ResponseDataPart;
import com.hamedapps.duhamel.ui.part.VelocityChartPart;

public class ComputeHandler {

	@Execute
	public void execute(MApplication application, IEclipseContext context,
			EModelService modelService, EPartService partService) {
		MPart data = partService
				.findPart("com.hamedapps.duhamel.ui.part.datapart");
		DataPart dataPart = (DataPart) data.getObject();
		dataPart.updateDuhamel();

		Duhamel duhamel = (Duhamel) context.get("duhamel");
		duhamel.compute();

		Vector<InputForce> inputForces = duhamel.getInputForces();
		for (InputForce inputForce : inputForces) {
			System.out.println(inputForce);
		}

		Vector<Response> responses = duhamel.getResponses();
		for (Response response : responses) {
			System.out.println("Time: " + response.getT() + ", Force: "
					+ response.getP() + ", U: " + response.getU());
		}

		List<MPart> parts = modelService
				.findElements(application,
						"com.hamedapps.duhamel.ui.part.responsedata",
						MPart.class, null);
		System.out.println("Found parts(s) : " + parts.size());
		ResponseDataPart responseDataPart = (ResponseDataPart) parts.get(0)
				.getObject();
		responseDataPart.updateTableViewer();

		List<MPart> parts1 = modelService.findElements(application,
				"com.hamedapps.duhamel.ui.part.inputchartpart", MPart.class,
				null);
		System.out.println("Found parts(s) : " + parts1.size());
		MPart ichp = parts1.get(0);
		partService.activate(ichp, false);
		InputChartPart inputChartPart = (InputChartPart) ichp.getObject();
		inputChartPart.updateChart();

		List<MPart> parts2 = modelService.findElements(application,
				"com.hamedapps.duhamel.ui.part.displacementchartpart",
				MPart.class, null);
		System.out.println("Found part(s) : " + parts2.size());
		MPart dcp = parts2.get(0);
		partService.activate(dcp);
		DisplacementChartPart displacementChartPart = (DisplacementChartPart) dcp
				.getObject();
		displacementChartPart.updateChart();

		List<MPart> parts3 = modelService.findElements(application,
				"com.hamedapps.duhamel.ui.part.velocitychartpart", MPart.class,
				null);
		System.out.println("Found part(s) : " + parts3.size());
		MPart vcp = parts3.get(0);
		partService.activate(vcp);
		VelocityChartPart velocityChartPart = (VelocityChartPart) vcp
				.getObject();
		velocityChartPart.updateChart();

		List<MPart> parts4 = modelService.findElements(application,
				"com.hamedapps.duhamel.ui.part.accelerationchartpart",
				MPart.class, null);
		System.out.println("Found part(s) : " + parts4.size());
		MPart acp = parts4.get(0);
		partService.activate(acp);
		AccelerationChartPart accelerationChartPart = (AccelerationChartPart) acp
				.getObject();
		accelerationChartPart.updateChart();
	}

}