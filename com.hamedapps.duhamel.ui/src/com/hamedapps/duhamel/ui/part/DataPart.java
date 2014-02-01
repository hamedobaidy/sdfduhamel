 
package com.hamedapps.duhamel.ui.part;

import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hamedapps.duhamel.Duhamel;
import com.hamedapps.duhamel.InputForce;

public class DataPart {
	private Text textXi;
	private Text textK;
	private Text textM;
	private Table table;
	
	private Vector<InputForce> inputForces;
	private Text textTime;
	private Text textForce;
	private TableViewer tableViewer;
	
	private Duhamel duhamel;
	
	@Inject
	MApplication application;
	
	private double k = 0.0;
	private double kesi = 0.0;
	private double m = 0.0;
	private double omega = 0.0;
	private double omega_D = 0.0;
	private Text textDt;
	private double dt;
	
	@Inject
	public DataPart() {
		inputForces = new Vector<>();
		duhamel = new Duhamel(inputForces, kesi, m, omega, omega_D);
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		
//		// Test
//		application.getContext().set("inputForces", inputForces);
		application.getContext().set("duhamel", duhamel);
		
		Label lblXi = new Label(parent, SWT.NONE);
		lblXi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblXi.setText("xi: ");
		
		textXi = new Text(parent, SWT.BORDER);
		textXi.setText("0.2");
		textXi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateXi();
			}
		});
		textXi.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateXi();
			}
		});
		textXi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblK = new Label(parent, SWT.NONE);
		lblK.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblK.setText("k: ");
		
		textK = new Text(parent, SWT.BORDER);
		textK.setText("1000");
		textK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateK();
			}
		});
		textK.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateK();
			}
		});
		textK.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblM = new Label(parent, SWT.NONE);
		lblM.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblM.setText("m: ");
		
		textM = new Text(parent, SWT.BORDER);
		textM.setText("10000");
		textM.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateM();
			}
		});
		textM.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTimeStepdt = new Label(parent, SWT.NONE);
		lblTimeStepdt.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTimeStepdt.setText("Time Step (dt) : ");
		
		textDt = new Text(parent, SWT.BORDER);
		textDt.setText("0.01");
		textDt.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateDt();
			}
		});
		textDt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Label lblTime = new Label(parent, SWT.NONE);
		lblTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTime.setText("Time: ");
		
		textTime = new Text(parent, SWT.BORDER);
		textTime.setText("0.0");
		textTime.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textTime.selectAll();
			}
		});
		textTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblForce = new Label(parent, SWT.NONE);
		lblForce.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblForce.setText("Force: ");
		
		textForce = new Text(parent, SWT.BORDER);
		textForce.setText("1.0");
		textForce.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textForce.selectAll();
			}
		});
		textForce.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(parent, SWT.NONE);
		
		Button btnAdd = new Button(parent, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addTimeForce();
			}
		});
		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnAdd.setText("Add");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		TableColumnLayout tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTimesec = tableViewerColumn.getColumn();
		tcl_composite.setColumnData(tblclmnTimesec, new ColumnPixelData(50, true, true));
		tblclmnTimesec.setText("Time (sec)");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				InputForce inf = (InputForce)element;
				return Double.toString(inf.getT());
			}
		});
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnForce = tableViewerColumn_1.getColumn();
		tcl_composite.setColumnData(tblclmnForce, new ColumnPixelData(150, true, true));
		tblclmnForce.setText("Force");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				InputForce inf = (InputForce) element;
				return Double.toString(inf.getP());
			}
		});
		
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(inputForces);
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}

	/**
	 * 
	 */
	protected void updateDt() {
		try {
			dt = Double.parseDouble(textDt.getText());
			duhamel.setDt(dt);
		} catch (NumberFormatException nfex) {
			MessageDialog.openInformation(textDt.getShell(), "Error", "Enter valid number");
		}
	}

	/**
	 * 
	 */
	private void updateM() {
		try {
			m = Double.parseDouble(textM.getText());
			duhamel.setM(m);
			updateOmega();
		} catch(NumberFormatException nfex) {
			MessageDialog.openError(textM.getShell(), "Error", "Enter valid number ");
		}
	}

	/**
	 * 
	 */
	private void updateK() {
		try {
			k = Double.parseDouble(textK.getText());
			updateOmega();
		} catch (NumberFormatException nfex) {
			MessageDialog.openError(textK.getShell(), "Error", "Enter valid number ");
		}
	}

	/**
	 * 
	 */
	private void updateOmega() {
		if(m==0.0) {
			return;
		} else {
			omega = Math.sqrt(k/m);
			duhamel.setOmega(omega);
			omega_D= omega * Math.sqrt(1-kesi*kesi);
			duhamel.setOmega_D(omega_D);
		}
	}

	/**
	 * 
	 */
	private void updateXi() {
		try {
			kesi = Double.parseDouble(textXi.getText());
			duhamel.setKesi(kesi);
			updateOmega();
		} catch(NumberFormatException nfex) {
			textXi.setText("0.0");
			MessageDialog.openError(textXi.getShell(), "Error", "Enter valid number ");
		}
	}

	/**
	 * 
	 */
	private void addTimeForce() {
		double t = Double.parseDouble(textTime.getText());
		double p = Double.parseDouble(textForce.getText());
		double dt = Double.parseDouble(textDt.getText());
		InputForce inf = new InputForce(t, p);
		inputForces.add(inf);
		tableViewer.refresh();
		textTime.setText(Double.toString(t+dt));
		textForce.setFocus();
		duhamel.setInputForces(inputForces);
	}

	public Vector<InputForce> getInputForces() {
		return inputForces;
	}

	public void setInputForces(Vector<InputForce> inputForces) {
		this.inputForces = inputForces;
	}

	/**
	 * @return the duhamel
	 */
	public Duhamel getDuhamel() {
		return duhamel;
	}

	/**
	 * @param duhamel the duhamel to set
	 */
	public void setDuhamel(Duhamel duhamel) {
		this.duhamel = duhamel;
	}

	/**
	 * 
	 */
	public void updateDuhamel() {
		updateXi();
		updateK();
		updateM();
		updateDt();
	}
}