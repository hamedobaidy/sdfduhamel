 
package com.hamedapps.duhamel.ui.part;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.hamedapps.duhamel.Duhamel;
import com.hamedapps.duhamel.InputForce;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.ResourceManager;

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
	private Text textTmax;
	private Button btnInterpulateBetweenForce;
	private Button btnRecordsAreGround;
	private Text textGr;
	
	@Inject
	private Shell shell;
	
	@Inject
	public DataPart() {
		inputForces = new Vector<>();
		duhamel = new Duhamel(inputForces, kesi, m, omega, omega_D);
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setBounds(0, 0, 448, 548);
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);
		Composite mainComposite = new Composite(scrolledComposite_1, SWT.NONE);
		mainComposite.setLayout(new GridLayout(2, false));
		
		Label lblXi = new Label(mainComposite, SWT.NONE);
		lblXi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblXi.setText("xi: ");
		
		textXi = new Text(mainComposite, SWT.BORDER);
		textXi.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblK = new Label(mainComposite, SWT.NONE);
		lblK.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblK.setText("k: ");
		
		textK = new Text(mainComposite, SWT.BORDER);
		textK.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblM = new Label(mainComposite, SWT.NONE);
		lblM.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblM.setText("m: ");
		
		textM = new Text(mainComposite, SWT.BORDER);
		textM.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnInterpulateBetweenForce = new Button(mainComposite, SWT.CHECK);
		btnInterpulateBetweenForce.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnInterpulateBetweenForce.setText("Interpulate between force records");
		
		Label lblTimeStepdt = new Label(mainComposite, SWT.NONE);
		lblTimeStepdt.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTimeStepdt.setText("Time Step (dt) : ");
		
		textDt = new Text(mainComposite, SWT.BORDER);
		textDt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(mainComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		
		btnRecordsAreGround = new Button(mainComposite, SWT.CHECK);
		btnRecordsAreGround.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnRecordsAreGround.setText("Records are ground acceleration");
		
		Label lblGravity = new Label(mainComposite, SWT.NONE);
		lblGravity.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGravity.setText("Gravity : ");
		
		textGr = new Text(mainComposite, SWT.BORDER);
		textGr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFinalTime = new Label(mainComposite, SWT.NONE);
		lblFinalTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFinalTime.setText("Final Time : ");
		
		textTmax = new Text(mainComposite, SWT.BORDER);
		textTmax.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTime = new Label(mainComposite, SWT.NONE);
		lblTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTime.setText("Time: ");
		
		textTime = new Text(mainComposite, SWT.BORDER);
		textTime.setText("0.0");
		textTime.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textTime.selectAll();
			}
		});
		textTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblForce = new Label(mainComposite, SWT.NONE);
		lblForce.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblForce.setText("Force: ");
		
		textForce = new Text(mainComposite, SWT.BORDER);
		textForce.setText("0.0");
		textForce.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textForce.selectAll();
			}
		});
		textForce.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblForceRecords = new Label(mainComposite, SWT.NONE);
		lblForceRecords.setText("Force Records : ");
		new Label(mainComposite, SWT.NONE);
		
//		Button btnAdd = new Button(mainComposite, SWT.NONE);
//		btnAdd.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				addTimeForce();
//			}
//		});
//		btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//		btnAdd.setText("Add");
		
		Composite composite_1 = new Composite(mainComposite, SWT.NONE);
		composite_1.setLayout(new GridLayout(4, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		
		Button btnAdd = new Button(composite_1, SWT.NONE);
		btnAdd.setToolTipText("Add new record");
		btnAdd.setImage(ResourceManager.getPluginImage("com.hamedapps.duhamel.ui", "icons/list-add.png"));
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addTimeForce();
			}
		});
		
		Button btnRemove = new Button(composite_1, SWT.NONE);
		btnRemove.setToolTipText("Remove selected record");
		btnRemove.setImage(ResourceManager.getPluginImage("com.hamedapps.duhamel.ui", "icons/list-remove.png"));
		btnRemove.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSelectedForceRecord();
			}
		});
		
		Button btnEdit = new Button(composite_1, SWT.NONE);
		btnEdit.setToolTipText("Edit selected record");
		btnEdit.setImage(ResourceManager.getPluginImage("com.hamedapps.duhamel.ui", "icons/document-edit.png"));
		btnEdit.addSelectionListener(new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				editSelectedForceRecord();
			}
		});
		
		Button btnImport = new Button(composite_1, SWT.NONE);
		btnImport.setImage(ResourceManager.getPluginImage("com.hamedapps.duhamel.ui", "icons/document-import.png"));
		btnImport.setToolTipText("Import from file");
		btnImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importForceRecords();
			}
		});
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(mainComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
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
		
		TableViewerColumn tableViewerColumnTime = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnTimesec = tableViewerColumnTime.getColumn();
		tcl_composite.setColumnData(tblclmnTimesec, new ColumnPixelData(115, true, true));
		tblclmnTimesec.setText("Time (sec)");
		tableViewerColumnTime.setLabelProvider(new ColumnLabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				InputForce inf = (InputForce)element;
				return Double.toString(inf.getT());
			}
		});
		
		TableViewerColumn tableViewerColumnForce = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnForce = tableViewerColumnForce.getColumn();
		tcl_composite.setColumnData(tblclmnForce, new ColumnPixelData(150, true, true));
		tblclmnForce.setText("Force or Sup Acc");
		tableViewerColumnForce.setLabelProvider(new ColumnLabelProvider() {
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
		scrolledComposite_1.setContent(mainComposite);
		scrolledComposite_1.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
//		// Test
//		application.getContext().set("inputForces", inputForces);
		application.getContext().set("duhamel", duhamel);
		
	}

	/**
	 * 
	 */
	protected void editSelectedForceRecord() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	protected void removeSelectedForceRecord() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	protected void importForceRecords() {
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		String path = fileDialog.open();
		List<Double> input = new ArrayList<>();
		
		if(path != null) {
			try {
				StreamTokenizer data = new StreamTokenizer(new BufferedReader(new InputStreamReader(new FileInputStream(path))));
				
				while(data.nextToken() != StreamTokenizer.TT_EOF) {
					double d = (double) data.nval;
					input.add(d);
				}
				
				Vector<InputForce> inputForces = new Vector<>();
				double t = 0.0;
				double p = 0.0;
				for (int i = 0; i < input.size(); i++) {
					double x = input.get(i);
					if( (i%2) == 0 ) {
						t = x;
					} else {
						p = x;
						inputForces.add(new InputForce(t, p));
					}
				}
				
				for (InputForce inputForce : inputForces) {
					System.out.println("t = " + inputForce.getT() + " p = " + inputForce.getP());
				}
				
				this.inputForces = inputForces;
				tableViewer.setInput(inputForces);
				tableViewer.refresh();
			} catch(IOException ioex) {
				System.out.println(ioex.getMessage());
			}
		}
	}

	/**
	 * 
	 */
	private void updateDt() {
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
			duhamel.setK(k);
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
		InputForce inf = new InputForce(t, p);
		inputForces.add(inf);
		tableViewer.refresh();
		textTime.setFocus();
//		duhamel.setInputForces(inputForces);
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
		updateInerpolate();
		updateIsGroundAcceleration();
		updateTmax();
		updateGr();
		duhamel.setInputForces(inputForces);
		duhamel.setInterpolate(btnInterpulateBetweenForce.getSelection());
		duhamel.setForceIsGroundAcceleration(btnRecordsAreGround.getSelection());
	}

	/**
	 * 
	 */
	private void updateGr() {
		try{
			double gr = Double.parseDouble(textGr.getText());
			duhamel.setGr(gr);
		} catch(NumberFormatException nfex) {
			MessageDialog.openInformation(textGr.getShell(), "Error", "Enter a valid value for \"Gravity\" field.");
		}
	}

	/**
	 * 
	 */
	private void updateTmax() {
		try {
			double tMax = Double.parseDouble(textTmax.getText());
			duhamel.settMax(tMax);
		} catch (NumberFormatException nfex) {
			MessageDialog.openInformation(textTmax.getShell(), "Error", "Enter a valid value for \"Final Time\" filed.");
		}
	}

	/**
	 * 
	 */
	private void updateIsGroundAcceleration() {
		duhamel.setForceIsGroundAcceleration(btnRecordsAreGround.getSelection());
	}

	/**
	 * 
	 */
	private void updateInerpolate() {
		duhamel.setInterpolate(btnInterpulateBetweenForce.getSelection());
	}

	/**
	 * 
	 */
	public void clearFields() {
		textXi.setText("");
		textK.setText("");
		textM.setText("");
		textDt.setText("");
		textGr.setText("");
		textTmax.setText("");
		textTime.setText("");
		textForce.setText("");
		inputForces.clear();
		
	}
}