package com.orendel.locator.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.mihalis.opal.opalDialog.Dialog;
import org.mihalis.opal.opalDialog.Dialog.CenterOption;
import org.mihalis.opal.opalDialog.Dialog.OpalDialogType;


public class MessagesUtil {
	
	
	public MessagesUtil() {
	}
	
	
	public static int showConfirmation(String title, String message) {
		final Dialog dialog = new Dialog(Display.getCurrent().getActiveShell());
		dialog.setCenterPolicy(CenterOption.CENTER_ON_DIALOG);
		dialog.setTitle("Confirmación");
		dialog.setMinimumWidth(400);
		dialog.getMessageArea()
			.setTitle(title)
			.setIcon(Display.getCurrent().getSystemImage(SWT.ICON_QUESTION))
			.setText(message);
		dialog.setButtonType(OpalDialogType.OK_CANCEL);
		return dialog.show();
//		Dialog.isConfirmed(title, message, 10);
	}
	
	
	public static void showWarning(String title, String message) {
		final Dialog dialog = new Dialog(Display.getCurrent().getActiveShell());
		dialog.setCenterPolicy(CenterOption.CENTER_ON_DIALOG);
		dialog.setTitle("Aviso");
		dialog.setMinimumWidth(400);
		dialog.getMessageArea()
			.setTitle(title)
			.setIcon(Display.getCurrent().getSystemImage(SWT.ICON_WARNING))
			.setText(message);
		dialog.setButtonType(OpalDialogType.OK);
		dialog.show();
	}
	
	
	public static void showError(String title, String message) {
		final Dialog dialog = new Dialog(Display.getCurrent().getActiveShell());
		dialog.setCenterPolicy(CenterOption.CENTER_ON_DIALOG);
		dialog.setTitle("Error");
		dialog.setMinimumWidth(400);
		dialog.getMessageArea()
			.setTitle(title)
			.setIcon(Display.getCurrent().getSystemImage(SWT.ICON_ERROR))
			.setText(message);
		dialog.setButtonType(OpalDialogType.OK);
		dialog.show();
	}
	
	
	public static void showInformation(String title, String message) {
		final Dialog dialog = new Dialog(Display.getCurrent().getActiveShell());
		dialog.setCenterPolicy(CenterOption.CENTER_ON_DIALOG);
		dialog.setTitle("Información");
		dialog.setMinimumWidth(400);
		dialog.getMessageArea()
			.setTitle(title)
			.setIcon(Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION))
			.setText(message);
		dialog.setButtonType(OpalDialogType.OK);
		dialog.show();
	}
	

	public static void displayLargeText() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (int t = 0; t < 20; t++) {
			stringBuilder.append("A <b>very</b> <size=10>long text (10)</size> " + t + "");
			stringBuilder.append("A <b>very</b> <size=+12>long text (+12)</size> " + t + "");
			stringBuilder.append("A <b>very</b> <size=-4>long text (-4)</size> " + t + "");
			stringBuilder.append("A <b>very</b> <color=#088A29>long text</color> " + t + "");
			stringBuilder.append("A <b>very</b> <color=255,0,255>long text</color> " + t + "");
			stringBuilder.append("A <b>very</b> <color=navy>long text</color> " + t + "");
			stringBuilder.append("A <b>very</b> <backgroundcolor=255,0,0>long text</backgroundcolor> " + t + "");
			stringBuilder.append("A <b>very</b> <backgroundcolor=#FFFFCC>long text</backgroundcolor> " + t + "");
			stringBuilder.append("A <b>very</b> <backgroundcolor=lavender>long text</backgroundcolor> " + t + "");
			stringBuilder.append("A very long text " + t + "<br/>");
			stringBuilder.append("..." + "<br/>");
		}

		final Dialog dialog = new Dialog(true);
		dialog.getMessageArea().setVerticalScrollbar(true);
		dialog.getMessageArea().setHeight(200);
		dialog.getMessageArea().setText(stringBuilder.toString());
		dialog.setButtonType(OpalDialogType.OK);
		dialog.show();
	}
	
}
