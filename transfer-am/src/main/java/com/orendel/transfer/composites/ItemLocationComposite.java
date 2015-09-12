package com.orendel.transfer.composites;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;

import com.orendel.counterpoint.domain.Inventory;
import com.orendel.counterpoint.domain.Item;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;

public class ItemLocationComposite extends Composite {
	
	private static final Logger logger = Logger.getLogger(ItemLocationComposite.class);
	private Text txtItemNo;
	private Text txtItemDescription;
	private Table tableLocations;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ItemLocationComposite(Composite parent, int style, Item item) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblItem = new Label(this, SWT.NONE);
		lblItem.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblItem.setText("Artículo:");
		
		txtItemNo = new Text(this, SWT.BORDER);
		txtItemNo.setEnabled(false);
		txtItemNo.setEditable(false);
		GridData gd_txtItemNo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_txtItemNo.widthHint = 70;
		txtItemNo.setLayoutData(gd_txtItemNo);
		
		Label lblDescription = new Label(this, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDescription.setText("Descripción:");
		
		txtItemDescription = new Text(this, SWT.BORDER);
		txtItemDescription.setEnabled(false);
		txtItemDescription.setEditable(false);
		txtItemDescription.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Group groupLocation = new Group(this, SWT.NONE);
		groupLocation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		groupLocation.setLayout(new GridLayout(1, false));
		
		tableLocations = new Table(groupLocation, SWT.BORDER | SWT.FULL_SELECTION);
		tableLocations.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableLocations.setHeaderVisible(true);
		tableLocations.setLinesVisible(true);
		
		TableColumn tblclmnLocation = new TableColumn(tableLocations, SWT.NONE);
		tblclmnLocation.setWidth(150);
		tblclmnLocation.setText("Bodega");
		
		TableColumn tblclmnOnhand = new TableColumn(tableLocations, SWT.NONE);
		tblclmnOnhand.setWidth(100);
		tblclmnOnhand.setText("On Hand");
		
		TableColumn tblclmnAvailable = new TableColumn(tableLocations, SWT.NONE);
		tblclmnAvailable.setWidth(100);
		tblclmnAvailable.setText("Disponible");
		
		TableColumn tblclmnCommited = new TableColumn(tableLocations, SWT.NONE);
		tblclmnCommited.setWidth(100);
		tblclmnCommited.setText("Commited");
		
		TableColumn tblclmnXferout = new TableColumn(tableLocations, SWT.NONE);
		tblclmnXferout.setWidth(100);
		tblclmnXferout.setText("Xfer Out");
		
		TableColumn tblclmnXferin = new TableColumn(tableLocations, SWT.NONE);
		tblclmnXferin.setWidth(100);
		tblclmnXferin.setText("Xfer In");
		
		Composite compositeButtons = new Composite(this, SWT.NONE);
		compositeButtons.setLayout(new GridLayout(1, false));
		compositeButtons.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
		
		Button btnOk = new Button(compositeButtons, SWT.NONE);
		GridData gd_btnOk = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnOk.widthHint = 70;
		btnOk.setLayoutData(gd_btnOk);
		btnOk.setBounds(0, 0, 75, 25);
		btnOk.setText("OK");
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getShell().close();
			}
		});
		
		displayItemLocationDetails(item);
		
		getShell().setDefaultButton(btnOk);
	}
	
	
	private void displayItemLocationDetails(Item item) {
		txtItemNo.setText(item.getItemNo());
		txtItemDescription.setText(item.getDescription());
		
		TableItem itemLine;
		
		tableLocations.removeAll();
		for (Inventory v : item.getInventory()) {
			itemLine = new TableItem(tableLocations, SWT.NONE);
			int column = 0;
			itemLine.setData(v);
			itemLine.setText(column++, " " + v.getLocationId());
			itemLine.setText(column++, " " + v.getQtyOnHand());
			itemLine.setText(column++, " " + v.getQtyAvailable());
			itemLine.setText(column++, " " + v.getQtyCommited());
			itemLine.setText(column++, " " + v.getQtyXferOut());
			itemLine.setText(column++, " " + v.getQtyXferIn());
		}		
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
