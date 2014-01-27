 
package com.hamedapps.duhamel.ui.part;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.swtchart.Chart;

public class InputChartPart {
	private Chart chart;

	@Inject
	public InputChartPart() {
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
		//TODO Your code here
	}

	/**
	 * 
	 */
	public void updateChart() {
		MessageDialog.openInformation(chart.getShell(), "Done", "Chart will be updated soon");
	}
	
	
	
	
}