 
package com.hamedapps.duhamel.ui.part;

import java.text.DecimalFormat;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.hamedapps.duhamel.Duhamel;
import com.hamedapps.duhamel.Response;

public class ResponseDataPart {
	private Table table;
	
	@Inject
	private IEclipseContext context;

	private TableViewer tableViewer;

	private DecimalFormat threeDigit = new DecimalFormat("0.000000");
	private DecimalFormat twoDiFormat = new DecimalFormat("0.00");
	
	private Duhamel duhamel;
	
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
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableViewerColumn tableViewerColumnT = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTime = tableViewerColumnT.getColumn();
		tcl_composite.setColumnData(tblclmnTime, new ColumnPixelData(50, true, true));
		tblclmnTime.setText("Time");
		tableViewerColumnT.setLabelProvider(new ColumnLabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				Response r = (Response)element;
				return threeDigit.format(r.getT());
			}
		});
		
		TableViewerColumn tableViewerColumnForce = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnForce = tableViewerColumnForce.getColumn();
		tcl_composite.setColumnData(tblclmnForce, new ColumnPixelData(150, true, true));
		tblclmnForce.setText("Force");
		tableViewerColumnForce.setLabelProvider(new ColumnLabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				Response r = (Response)element;
				return threeDigit.format(r.getP());
			}
		});
		
		TableViewerColumn tableViewerColumnDis = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnU = tableViewerColumnDis.getColumn();
		tcl_composite.setColumnData(tblclmnU, new ColumnPixelData(150, true, true));
		tblclmnU.setText("Displacement");
		tableViewerColumnDis.setLabelProvider(new ColumnLabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				Response r = (Response)element;
				return threeDigit.format(r.getU());
			}
		});
		
		TableViewerColumn tableViewerColumnVel = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnV = tableViewerColumnVel.getColumn();
		tcl_composite.setColumnData(tblclmnV, new ColumnPixelData(150, true, true));
		tblclmnV.setText("Velocity");
		tableViewerColumnVel.setLabelProvider(new ColumnLabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				Response r = (Response)element;
				return threeDigit.format(r.getV());
			}
		});
		
		TableViewerColumn tableViewerColumnAcc = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnA = tableViewerColumnAcc.getColumn();
		tcl_composite.setColumnData(tblclmnA, new ColumnPixelData(150, true, true));
		tblclmnA.setText("Acceleration");
		tableViewerColumnAcc.setLabelProvider(new ColumnLabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				Response r = (Response)element;
				return threeDigit.format(r.getA());
			}
		});
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		if(duhamel!= null)
			tableViewer.setInput(duhamel.getResponses());
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		//TODO Your code here
	}
	
	public void updateTableViewer() {
		duhamel = (Duhamel) context.get("duhamel");
		
		tableViewer.setInput(duhamel.getResponses());
		tableViewer.refresh();
	}
	
	
}