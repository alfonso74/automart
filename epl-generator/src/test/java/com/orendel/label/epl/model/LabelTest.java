package com.orendel.label.epl.model;

import java.util.List;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import com.orendel.epl.model.FontSize;
import com.orendel.epl.model.Label;
import com.orendel.epl.model.LabelElement;
import com.orendel.epl.model.OverflowMode;
import com.orendel.epl.model.TextLine;

public class LabelTest {

	@Test
	public void test_AddElementCentered_Happy() {
		Label label = new Label(450, 250);
		LabelElement textLine = TextLine.create(FontSize.THREE, "texto largo");
		label.addElementCentered(textLine, 10);
		List<LabelElement> elements = label.getElements();
		assertThat("La cantidad de líneas generadas no coincide con la esperada", 
				elements.size(), CoreMatchers.is(1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_AddElementCentered_Null() {
		Label label = new Label(450, 250);
		label.addElementCentered(null, 10);
	}
	
	@Test
	public void test_AddElementCentered_ElementWithEmptyString() {
		Label label = new Label(450, 250);
		LabelElement textLine = TextLine.create(FontSize.THREE, "");
		label.addElementCentered(textLine, 10);
		List<LabelElement> elements = label.getElements();
		assertThat("La cantidad de líneas generadas no coincide con la esperada", 
				elements.size(), CoreMatchers.is(1));
	}
	
	@Test
	public void test_AddElementCentered_Overflow() {
		Label label = new Label(450, 250);
		LabelElement textLine = TextLine.create(FontSize.FIVE, "SANDWICHCOMBO");
		label.addElementCentered(textLine, 10, OverflowMode.RESIZE);
		List<LabelElement> elements = label.getElements();
		assertThat("La cantidad de líneas generadas no coincide con la esperada", 
				elements.size(), CoreMatchers.is(1));
		assertThat("El tamaño de fuente auto-seleccionado es incorrecto", 
				((TextLine) textLine).getFontSize(), CoreMatchers.is(FontSize.FOUR));
	}
	
	@Test
	public void testSplitContent_Happy() {
		Label label = new Label(450, 250);
		String contentToSplit = "Texto largo que supera los 20 caracteres";
		List<LabelElement> lines = label.splitContent(contentToSplit, FontSize.THREE, 20);
		assertThat(lines.get(0).getContent(), CoreMatchers.is("Texto largo que"));
		assertThat(lines.get(1).getContent(), CoreMatchers.is("supera los 20"));
		assertThat(lines.get(2).getContent(), CoreMatchers.is("caracteres"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSplitContent_Null() {
		Label label = new Label(450, 250);
		String contentToSplit = null;
		label.splitContent(contentToSplit, FontSize.THREE, 20);
	}
	
	@Test
	public void testSplitContent_EmptyString() {
		Label label = new Label(450, 250);
		String contentToSplit = "";
		List<LabelElement> lines = label.splitContent(contentToSplit, FontSize.THREE, 20);
		assertThat(lines.get(0).getContent(), CoreMatchers.is(""));
	}
	
	@Test
	public void testSplitContent_OneLineWithMaxChars() {
		Label label = new Label(450, 250);
		String contentToSplit = "Línea de 18 letras";
		List<LabelElement> lines = label.splitContent(contentToSplit, FontSize.THREE, 18);
		assertThat(lines.get(0).getContent(), CoreMatchers.is("Línea de 18 letras"));
	}
	
	public void testSplitContent_testSplitContent_OneLineSplittedToTwoV1() {
		Label label = new Label(450, 250);
		String contentToSplit = "Línea de 18 letras";
		List<LabelElement> lines = label.splitContent(contentToSplit, FontSize.THREE, 11);
		assertThat(lines.get(0).getContent(), CoreMatchers.is("Línea de 18"));
		assertThat(lines.get(1).getContent(), CoreMatchers.is("letras"));
	}
	
	public void testSplitContent_testSplitContent_OneLineSplittedToTwoV2() {
		Label label = new Label(450, 250);
		String contentToSplit = "Línea de 18 letras";
		List<LabelElement> lines = label.splitContent(contentToSplit, FontSize.THREE, 12);
		assertThat(lines.get(0).getContent(), CoreMatchers.is("Línea de 18"));
		assertThat(lines.get(1).getContent(), CoreMatchers.is("letras"));
	}
	
	@Test
	public void testSplitContent_testSplitContent_OneContinuousLineToTwo() {
		Label label = new Label(450, 250);
		String contentToSplit = "Otorrinolaringología";
		List<LabelElement> lines = label.splitContent(contentToSplit, FontSize.THREE, 15);
		assertThat(lines.get(0).getContent(), CoreMatchers.is("Otorrinolaringo"));
		assertThat(lines.get(1).getContent(), CoreMatchers.is("logía"));
	}
	
	@Test
	public void testSplitContent_testSplitContent_OneContinuousLineToThree() {
		Label label = new Label(450, 250);
		String contentToSplit = "Otorrinolaringología tres";
		List<LabelElement> lines = label.splitContent(contentToSplit, FontSize.THREE, 9);
		assertThat(lines.get(0).getContent(), CoreMatchers.is("Otorrinol"));
		assertThat(lines.get(1).getContent(), CoreMatchers.is("aringolog"));
		assertThat(lines.get(2).getContent(), CoreMatchers.is("ía tres"));
	}
	
	@Test
	public void testAdjustFontSize_FontSizeOne() {
		Label label = new Label(450, 250);
		TextLine textLine = (TextLine) TextLine.create(FontSize.FIVE, "SANDWICHCOMBOSANDWICHCOMBOSANDWICHCOMBO");
		label.adjustFontSize(textLine);
		assertThat(textLine.getFontSize(), CoreMatchers.is(FontSize.ONE));
	}
	
	@Test
	public void testAdjustFontSize_FontSizeFour() {
		Label label = new Label(450, 250);
		TextLine textLine = (TextLine) TextLine.create(FontSize.FIVE, "SANDWICHCOMBO");
		label.adjustFontSize(textLine);
		assertThat(textLine.getFontSize(), CoreMatchers.is(FontSize.FOUR));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAdjustFontSize_TextTooLongForLabel() {
		Label label = new Label(450, 250);
		TextLine textLine = (TextLine) TextLine.create(FontSize.FIVE, "SANDWICHCOMBOSANDWICHCOMBOSANDWICHCOMBOSANDWICHCOMBO");
		label.adjustFontSize(textLine);
	}
}
