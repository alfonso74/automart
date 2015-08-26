package com.orendel.transfer.editors;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.controllers.CounterpointController;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ItemDetailsComposite extends Composite {
	private static final Logger logger = Logger.getLogger(ItemDetailsComposite.class);
	
	private CounterpointController controller;
	
	private String itemNo;
	
	private Item item;
	private Text txtItemCode;
	private Text txtDescription;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ItemDetailsComposite(Composite parent, int style, String itemNo) {
		super(parent, style);
		
		this.itemNo = itemNo;
		this.controller = new CounterpointController();
		
		setLayout(new GridLayout(1, false));
		
		Group groupGeneralInfo = new Group(this, SWT.NONE);
		groupGeneralInfo.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		groupGeneralInfo.setText("Información general");
		groupGeneralInfo.setLayout(new GridLayout(2, false));
		groupGeneralInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCode = new Label(groupGeneralInfo, SWT.NONE);
		lblCode.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Código:");
		
		txtItemCode = new Text(groupGeneralInfo, SWT.BORDER);
		txtItemCode.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		txtItemCode.setEditable(false);
		txtItemCode.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescripcin = new Label(groupGeneralInfo, SWT.NONE);
		lblDescripcin.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblDescripcin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescripcin.setText("Descripción:");
		
		txtDescription = new Text(groupGeneralInfo, SWT.BORDER);
		txtDescription.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		txtDescription.setEditable(false);
		txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		findItemDetails();
	}
	
	/**
	 * Busca un {@link Item} en base de datos para mostrar sus datos en la pantalla.
	 */
	private void findItemDetails() {
		item = controller.findItemByItemCode(itemNo);
		if (item != null) {
			refreshFieldsContents();
		}
	}
	
	/**
	 * Actualiza los campos con la información contenida en el {@link Item}
	 */
	private void refreshFieldsContents() {
		txtItemCode.setText(item.getItemNo());
		txtDescription.setText(item.getDescription());
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
