package com.hamedapps.duhamel.ui.handler;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import com.hamedapps.duhamel.Duhamel;
import com.hamedapps.duhamel.ui.part.AccelerationChartPart;
import com.hamedapps.duhamel.ui.part.DataPart;
import com.hamedapps.duhamel.ui.part.DisplacementChartPart;
import com.hamedapps.duhamel.ui.part.InputChartPart;
import com.hamedapps.duhamel.ui.part.ResponseDataPart;
import com.hamedapps.duhamel.ui.part.SupportReactionPart;
import com.hamedapps.duhamel.ui.part.VelocityChartPart;

public class ComputeHandler {

	@Execute
	public void execute(MApplication application, IEclipseContext context,
			EPartService partService) {
		MPart data = partService
				.findPart("com.hamedapps.duhamel.ui.part.datapart");
		DataPart dataPart = (DataPart) data.getObject();
		dataPart.updateDuhamel();

		
		Duhamel duhamel = (Duhamel) context.get("duhamel");
		
		duhamel.compute();
		

		MPart response = partService
				.findPart("com.hamedapps.duhamel.ui.part.responsedata");
		ResponseDataPart responseDataPart = (ResponseDataPart) response
				.getObject();
		partService.activate(response);
		responseDataPart.updateTableViewer();

		MPart inputChart = partService
				.findPart("com.hamedapps.duhamel.ui.part.inputchartpart");
		partService.activate(inputChart, false);
		InputChartPart inputChartPart = (InputChartPart) inputChart.getObject();
		inputChartPart.updateChart();

		MPart displacement = partService
				.findPart("com.hamedapps.duhamel.ui.part.displacementchartpart");
		partService.activate(displacement, false);
		DisplacementChartPart displacementChartPart = (DisplacementChartPart) displacement
				.getObject();
		displacementChartPart.updateChart();

		MPart velocity = partService
				.findPart("com.hamedapps.duhamel.ui.part.velocitychartpart");
		partService.activate(velocity, false);
		VelocityChartPart velocityChartPart = (VelocityChartPart) velocity
				.getObject();
		velocityChartPart.updateChart();

		MPart acceleration = partService
				.findPart("com.hamedapps.duhamel.ui.part.accelerationchartpart");
		partService.activate(acceleration, false);
		AccelerationChartPart accelerationChartPart = (AccelerationChartPart) acceleration
				.getObject();
		accelerationChartPart.updateChart();
		
		MPart supportR = partService.findPart("com.hamedapps.duhamel.ui.part.supreactionpart");
		partService.activate(supportR, false);
		SupportReactionPart supportReactionPart = (SupportReactionPart) supportR.getObject();
		supportReactionPart.updateChart();
		
		partService.bringToTop(response);
	}

}