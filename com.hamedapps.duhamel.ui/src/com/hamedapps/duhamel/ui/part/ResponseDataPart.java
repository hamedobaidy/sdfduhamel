 
package com.hamedapps.duhamel.ui.part;

import javax.inject.Inject;
import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ColumnPixelData;

public class ResponseDataPart {
	private Table table;
	@Inject
	public ResponseDataPart() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		TableColumnLayout tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);
		
		TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTime = tableViewerColumn.getColumn();
		tcl_composite.setColumnData(tblclmnTime, new ColumnPixelData(150, true, true));
		tblclmnTime.setText("Time");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnForce = tableViewerColumn_1.getColumn();
		tcl_composite.setColumnData(tblclmnForce, new ColumnPixelData(150, true, true));
		tblclmnForce.setText("Force");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnU = tableViewerColumn_2.getColumn();
		tcl_composite.setColumnData(tblclmnU, new ColumnPixelData(150, true, true));
		tblclmnU.setText("Displacement");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnV = tableViewerColumn_3.getColumn();
		tcl_composite.setColumnData(tblclmnV, new ColumnPixelData(150, true, true));
		tblclmnV.setText("Velocity");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnA = tableViewerColumn_4.getColumn();
		tcl_composite.setColumnData(tblclmnA, new ColumnPixelData(150, true, true));
		tblclmnA.setText("Acceleration");
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		//TODO Your code here
	}
	
	public void updateTableViewer() {
		MessageDialog.openInformation(table.getShell(), "Compute Done", "End of computing");
	}
	
	
}