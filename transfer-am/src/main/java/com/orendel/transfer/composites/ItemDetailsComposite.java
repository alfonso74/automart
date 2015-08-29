package com.orendel.transfer.composites;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.ImagesService;

import org.eclipse.swt.graphics.Image;
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
	
	private Text txtHeader;
	private Text txtItemCode;
	private Text txtDescription;
	
	private Image image;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ItemDetailsComposite(Composite parent, int style, String itemNo) {
		super(parent, style);
		
		this.itemNo = itemNo;
		this.controller = new CounterpointController();
		this.image = ImagesService.INSTANCE.getImage(parent.getDisplay(), IImageKeys.ITEM_24);
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 10;
		setLayout(gridLayout);
		
		Composite compositeTop = new Composite(this, SWT.NONE);
		compositeTop.setEnabled(false);
		GridData gd_compositeTop = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_compositeTop.heightHint = 35;
		gd_compositeTop.minimumHeight = 35;
		compositeTop.setLayoutData(gd_compositeTop);
		compositeTop.setBounds(0, 0, 64, 64);
		compositeTop.setLayout(new GridLayout(2, false));
		
		Label lblHeader = new Label(compositeTop, SWT.NONE);
		lblHeader.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true, 1, 1));
		lblHeader.setImage(image);
		
		txtHeader = new Text(compositeTop, SWT.READ_ONLY | SWT.WRAP);
		txtHeader.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		txtHeader.setForeground(SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
		txtHeader.setEditable(false);
		txtHeader.setText("Detalles del artículo");
		txtHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
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
