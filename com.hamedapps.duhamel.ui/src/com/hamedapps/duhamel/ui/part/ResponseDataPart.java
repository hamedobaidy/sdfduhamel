 
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hamedapps.duhamel.Duhamel;
import com.hamedapps.duhamel.Response;

public class ResponseDataPart {
	private Table table;
	
	@Inject
	private IEclipseContext context;

	private TableViewer tableViewer;

	private DecimalFormat threeDigit = new DecimalFormat("0.000");
	private Duhamel duhamel;
	private Text textUMax;
	private Text textVMax;
	private Text textAMax;
	private Text textVsupMax;
	
	@Inject
	public ResponseDataPart() {
		//TODO Your code here
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Group grpMaximumValues = new Group(parent, SWT.NONE);
		grpMaximumValues.setLayout(new GridLayout(8, false));
		grpMaximumValues.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		grpMaximumValues.setText("Maximum Values");
		
		Label lblUMax = new Label(grpMaximumValues, SWT.NONE);
		lblUMax.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUMax.setText("U Max : ");
		
		textUMax = new Text(grpMaximumValues, SWT.BORDER);
		textUMax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblVMax = new Label(grpMaximumValues, SWT.NONE);
		lblVMax.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVMax.setText("V Max : ");
		
		textVMax = new Text(grpMaximumValues, SWT.BORDER);
		textVMax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblAMax = new Label(grpMaximumValues, SWT.NONE);
		lblAMax.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAMax.setText("A Max : ");
		
		textAMax = new Text(grpMaximumValues, SWT.BORDER);
		textAMax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblSupVMax = new Label(grpMaximumValues, SWT.NONE);
		lblSupVMax.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSupVMax.setText("Sup. V Max : ");
		
		textVsupMax = new Text(grpMaximumValues, SWT.BORDER);
		textVsupMax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
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
		tcl_composite.setColumnData(tblclmnTime, new ColumnPixelData(65, true, true));
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
		
		TableViewerColumn tableViewerColumnVsup = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnSupportReaction = tableViewerColumnVsup.getColumn();
		tcl_composite.setColumnData(tblclmnSupportReaction, new ColumnPixelData(150, true, true));
		tblclmnSupportReaction.setText("Support Reaction");
		tableViewerColumnVsup.setLabelProvider(new ColumnLabelProvider(){
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				Response r = (Response)element;
				return threeDigit.format(r.getvSup());
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
		
		textUMax.setText(threeDigit.format(duhamel.getuMax()));
		textVMax.setText(threeDigit.format(duhamel.getvMax()));
		textAMax.setText(threeDigit.format(duhamel.getaMax()));
		textVsupMax.setText(threeDigit.format(duhamel.getvSupMax()));
		
		tableViewer.setInput(duhamel.getResponses());
		tableViewer.refresh();
	}	
}