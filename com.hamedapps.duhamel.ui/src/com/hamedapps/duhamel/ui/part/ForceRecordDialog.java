/**
 * 
 */
package com.hamedapps.duhamel.ui.part;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Hamed Mohammadi
 *
 */
public class ForceRecordDialog extends TitleAreaDialog {
	private Text textT;
	private Text textF;
	private double t;
	private double f;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ForceRecordDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblTime = new Label(container, SWT.NONE);
		lblTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTime.setText("Time : ");
		
		textT = new Text(container, SWT.BORDER);
		textT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textT.setText(Double.toString(t));
		
		Label lblForceOrBase = new Label(container, SWT.NONE);
		lblForceOrBase.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblForceOrBase.setText("Force or Base Acceleration : ");
		
		textF = new Text(container, SWT.BORDER);
		textF.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textF.setText(Double.toString(f));
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * @return the t
	 */
	public double getT() {
		return t;
	}

	/**
	 * @return the f
	 */
	public double getF() {
		return f;
	}

	/**
	 * @param t the t to set
	 */
	public void setT(double t) {
		this.t = t;
	}

	/**
	 * @param f the f to set
	 */
	public void setF(double f) {
		this.f = f;

	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		t = Double.parseDouble(textT.getText());
		f = Double.parseDouble(textF.getText());
		super.okPressed();
	}
}
