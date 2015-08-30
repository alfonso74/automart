package com.orendel.transfer.composites;

import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.counterpoint.domain.Inventory;
import com.orendel.counterpoint.domain.Item;
import com.orendel.transfer.controllers.CounterpointController;
import com.orendel.transfer.services.IImageKeys;
import com.orendel.transfer.services.ImagesService;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
	private String locationId;
	
	private Item item;
	
	private Text txtHeader;
	private Text txtItemCode;
	private Text txtDescription;
	
	private Image image;
	private Text txtLocation;
	private Text txtOnHand;
	private Text txtAvailable;
	private Text txtCommited;
	private Text txtXferIn;
	private Text txtXferOut;
	private Text txtQtyMin;
	private Text txtQtyMax;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ItemDetailsComposite(Composite parent, int style, String itemNo, String locationId) {
		super(parent, style);
		
		this.itemNo = itemNo;
		this.locationId = locationId;
		this.controller = new CounterpointController("ItemDetails" + new Date().getTime());
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
		txtHeader.setText("Detalles del artículo por bodega");
		txtHeader.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
		
		Group groupGeneralInfo = new Group(this, SWT.NONE);
		groupGeneralInfo.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		groupGeneralInfo.setLayout(new GridLayout(2, false));
		groupGeneralInfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCode = new Label(groupGeneralInfo, SWT.NONE);
		lblCode.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblCode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCode.setText("Código:");
		
		txtItemCode = new Text(groupGeneralInfo, SWT.BORDER);
		txtItemCode.setEnabled(false);
		txtItemCode.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		txtItemCode.setEditable(false);
		txtItemCode.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblDescripcin = new Label(groupGeneralInfo, SWT.NONE);
		lblDescripcin.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblDescripcin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescripcin.setText("Descripción:");
		
		txtDescription = new Text(groupGeneralInfo, SWT.BORDER);
		txtDescription.setEnabled(false);
		txtDescription.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		txtDescription.setEditable(false);
		txtDescription.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblLocation = new Label(groupGeneralInfo, SWT.NONE);
		lblLocation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLocation.setText("Bodega:");
		
		txtLocation = new Text(groupGeneralInfo, SWT.BORDER);
		txtLocation.setEnabled(false);
		txtLocation.setEditable(false);
		GridData gd_txtLocation = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtLocation.widthHint = 120;
		txtLocation.setLayoutData(gd_txtLocation);
		
		Group grpQty = new Group(this, SWT.NONE);
		GridLayout gl_grpQty = new GridLayout(4, false);
		grpQty.setLayout(gl_grpQty);
		grpQty.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpQty.setText("Cantidades");
		
		Label lblOnHand = new Label(grpQty, SWT.NONE);
		lblOnHand.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOnHand.setText("On Hand:");
		
		txtOnHand = new Text(grpQty, SWT.BORDER);
		txtOnHand.setEnabled(false);
		txtOnHand.setEditable(false);
		GridData gd_txtOnHand = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtOnHand.widthHint = 70;
		txtOnHand.setLayoutData(gd_txtOnHand);
		
		Label lblAvailable = new Label(grpQty, SWT.NONE);
		GridData gd_lblAvailable = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblAvailable.horizontalIndent = 40;
		lblAvailable.setLayoutData(gd_lblAvailable);
		lblAvailable.setText("Disponible:");
		
		txtAvailable = new Text(grpQty, SWT.BORDER);
		txtAvailable.setEnabled(false);
		txtAvailable.setEditable(false);
		GridData gd_txtAvailable = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_txtAvailable.widthHint = 70;
		txtAvailable.setLayoutData(gd_txtAvailable);
		
		Label lblCommited = new Label(grpQty, SWT.NONE);
		lblCommited.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCommited.setText("Commited:");
		
		txtCommited = new Text(grpQty, SWT.BORDER);
		txtCommited.setEnabled(false);
		txtCommited.setEditable(false);
		GridData gd_txtCommited = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtCommited.widthHint = 70;
		txtCommited.setLayoutData(gd_txtCommited);
		new Label(grpQty, SWT.NONE);
		new Label(grpQty, SWT.NONE);
		
		Label lblXferIn = new Label(grpQty, SWT.NONE);
		lblXferIn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblXferIn.setText("Xfer In:");
		
		txtXferIn = new Text(grpQty, SWT.BORDER);
		txtXferIn.setEnabled(false);
		txtXferIn.setEditable(false);
		GridData gd_txtXferIn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtXferIn.widthHint = 70;
		txtXferIn.setLayoutData(gd_txtXferIn);
		
		Label lblXferOut = new Label(grpQty, SWT.NONE);
		lblXferOut.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblXferOut.setText("Xfer Out:");
		
		txtXferOut = new Text(grpQty, SWT.BORDER);
		txtXferOut.setEnabled(false);
		txtXferOut.setEditable(false);
		GridData gd_txtXferOut = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtXferOut.widthHint = 70;
		txtXferOut.setLayoutData(gd_txtXferOut);
		
		Label lblMinqty = new Label(grpQty, SWT.NONE);
		lblMinqty.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMinqty.setText("Mínimo:");
		
		txtQtyMin = new Text(grpQty, SWT.BORDER);
		txtQtyMin.setEnabled(false);
		txtQtyMin.setEditable(false);
		GridData gd_txtQtyMin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtQtyMin.widthHint = 70;
		txtQtyMin.setLayoutData(gd_txtQtyMin);
		
		Label lblQtymax = new Label(grpQty, SWT.NONE);
		lblQtymax.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQtymax.setText("Máximo:");
		
		txtQtyMax = new Text(grpQty, SWT.BORDER);
		txtQtyMax.setEnabled(false);
		txtQtyMax.setEditable(false);
		GridData gd_txtQtyMax = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtQtyMax.widthHint = 70;
		txtQtyMax.setLayoutData(gd_txtQtyMax);
		
		addDisposeListener();
		
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
		
		Inventory inventory = null;
		for (Inventory v : item.getInventory()) {
			if (v.getLocationId().equalsIgnoreCase(locationId)) {
				inventory = v;
			}
		}
		
		txtLocation.setText(inventory.getLocationId());
		txtOnHand.setText(inventory.getQtyOnHand().toString());
		txtAvailable.setText(inventory.getQtyAvailable().toString());
		txtCommited.setText(inventory.getQtyCommited().toString());
		txtXferIn.setText(inventory.getQtyXferIn().toString());
		txtXferOut.setText(inventory.getQtyXferOut().toString());
	}
	
	private void addDisposeListener() {
		this.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				controller.finalizarSesion();
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
