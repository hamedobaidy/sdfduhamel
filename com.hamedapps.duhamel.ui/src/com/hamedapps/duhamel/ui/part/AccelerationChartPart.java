 
package com.hamedapps.duhamel.ui.part;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.swtchart.Chart;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;

import com.hamedapps.duhamel.Duhamel;

public class AccelerationChartPart {
	private Chart chart;
	private Duhamel duhamel;
	@Inject
	private IEclipseContext context;

	@Inject
	public AccelerationChartPart() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		chart = new Chart(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(chart);
		scrolledComposite.setMinSize(chart.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		chart.getTitle().setText("Acceleration Response Graph");
	}

	/**
	 * 
	 */
	private void refreshChart() {
		double[] ts = new double[duhamel.getResponses().size()];
		double[] as =  new double[duhamel.getResponses().size()];
		for(int i =0; i < ts.length; i++) {
			ts[i]=duhamel.getResponses().get(i).getT();
			as[i]=duhamel.getResponses().get(i).getA();
		}
		
		ISeriesSet seriesSet = chart.getSeriesSet();
		ILineSeries series = (ILineSeries) seriesSet.createSeries(SeriesType.LINE, "Acceleration");
		series.setYSeries(as);
		series.setXSeries(ts);
		IAxisSet axisSet = chart.getAxisSet();
		axisSet.getXAxis(0).getTitle().setText("t (sec)");
		axisSet.getYAxis(0).getTitle().setText("Acceleration");
		series.setSymbolType(PlotSymbolType.NONE);
		axisSet.adjustRange();
		chart.redraw();
	}

	/**
	 * 
	 */
	public void updateChart() {
		duhamel = (Duhamel) context.get("duhamel");
		if(duhamel!=null) 
			refreshChart();
	}	
}