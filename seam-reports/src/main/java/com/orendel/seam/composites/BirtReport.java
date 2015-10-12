package com.orendel.seam.composites;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;


public class BirtReport extends Composite {

	private String reportDirectory = "/reports/";
	private String reportFileName = "entregas.rptdesign";
	
	private String dbURL = "jdbc:sqlserver://192.168.222.128\\SQLEXPRESS:1433;databaseName=DemoGolf";
	private Date initialDate;
	private Date endDate;
	
	private Browser browser;
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public BirtReport(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		ScrolledComposite reportComposite = new ScrolledComposite(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		reportComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		reportComposite.setExpandHorizontal(true);
		reportComposite.setExpandVertical(true);
		
		browser = new Browser(reportComposite, SWT.NONE);
		reportComposite.setContent(browser);
		reportComposite.setMinSize(browser.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
//		previewProforma(browser, reportDirectory + reportFileName);
	}
	
	
	public void execute(Map<String, Serializable> parameters) {
		if (parameters != null) {
			Calendar calInitDate = (Calendar) parameters.get("initialDate");
			Calendar calEndDate = (Calendar) parameters.get("endDate");
			initialDate = new Date(calInitDate.getTime().getTime());
			endDate = new Date(calEndDate.getTime().getTime());
			dbURL = (String) parameters.get("dbURL");
		}
		previewProforma(browser, reportDirectory + reportFileName);
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	

	private void previewProforma(Browser browser, String rutaReporte) {
		IReportEngine engine = inicializarEngine();
		String rpt = getReportURL(rutaReporte);
		InputStream is = getReportIS(rutaReporte);
		RenderOption options = configurarRendererPDF(rpt);
		String reporteFileName = ejecutarReporte(engine, options, is);

		System.out.println("Archivo de salida: " + reporteFileName);

//		browser.setUrl("file://" + reporteFileName);
//		new File(MyClass.class.getProtectionDomain().getCodeSource().getLocation().getPath())
		System.out.println("Current path: " + System.getProperty("user.dir"));
		String currentPath = System.getProperty("user.dir");
		browser.setUrl(currentPath + "/" + reporteFileName);
		engine.destroy();
	}
	
	
	private IReportEngine inicializarEngine() {
		EngineConfig config = new EngineConfig();
		//add to the classpath-Set Parent Classloader		
//		config.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());
		try {
			Platform.startup(config);
		} catch (BirtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		IReportEngine engine = factory.createReportEngine( config );
		return engine;
	}
	
	
	/**
	 * Obtiene la ubicaci�n del reporte (dentro del bundle de eclipse)
	 * @param rutaReporte Ruta del reporte dentro del bundle de eclipse
	 * @return Ruta para accesar el reporte
	 */
	private String getReportURL(String rutaReporte) {		
		URL url = this.getClass().getResource(rutaReporte);
		String rpt = url.getPath();
		System.out.println("Ruta del reporte (bundle): " + rpt);
		return rpt;
	}
	
	
	private InputStream getReportIS(String rutaReporte) {
		InputStream is = this.getClass().getResourceAsStream(rutaReporte);
		return is;
	}

	
	/**
	 * Configura un renderer de excel para generar una hoja de c�lculo basada en el reporte indicado
	 * @param rpt Ruta del reporte usado como template
	 * @return RenderOption configurado para excel
	 */
	private RenderOption configurarRendererPDF(String rpt) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		String outputPDF = rpt.replaceFirst( ".rptdesign", ".pdf" ); outputPDF = "rr.pdf";
		PDFRenderOption options = new PDFRenderOption();
		options.setOutputStream(bos);
		options.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_PDF);
		options.setOutputFileName( outputPDF );
		System.out.println("Ruta del archivo a generar: " + outputPDF);
		return options;
	}
	
	
	/**
	 * Gnenera un archivo de acuerdo al reporte y formato suministrados
	 * @param engine Engine de BIRT
	 * @param options renderer configurado para generar el archivo en un formato espec�fico
	 * @param rpt ruta del reporte de BIRT a usar como template
	 * @return
	 */
	private String ejecutarReporte(IReportEngine engine, RenderOption options, String rpt) {
		// Pasamos los par�metros para el reporte y lo ejecutamos
		try {
			IReportRunnable design = engine.openReportDesign(rpt);
			IRunAndRenderTask task = engine.createRunAndRenderTask(design);
			task.setParameterValue("connectionURL", dbURL);
			System.out.println("Using DB URL: " + dbURL);
//			task.setParameterValue("Fecha inicial", initialDate);
//			task.setParameterValue("Fecha final", endDate);
			task.setParameterValue("Fecha inicial", new Date(2014, 3, 1));
			task.setParameterValue("Fecha final", new Date(2014, 4, 5));
			//task.setParameterValues(parametros);
			task.setRenderOption(options);
			task.run();
			task.close();
		} catch (EngineException e) {
//			MessageDialog.openError(getSite().getShell(), "Error en: " + this.getClass().getName(), "Error al generar el reporte: " + rpt + ".\n" +
//					"Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0] + "\n" + e.getStackTrace()[1]);
			e.printStackTrace();
		}
		return options.getOutputFileName();
	}
	
	
	private String ejecutarReporte(IReportEngine engine, RenderOption options, InputStream reportIS) {
		// Pasamos los par�metros para el reporte y lo ejecutamos
		try {
			IReportRunnable design = engine.openReportDesign(reportIS);
			IRunAndRenderTask task = engine.createRunAndRenderTask(design);
			task.setParameterValue("connectionURL", dbURL);
			System.out.println("Using DB URL: " + dbURL);
//			task.setParameterValue("Fecha inicial", initialDate);
//			task.setParameterValue("Fecha final", endDate);
			task.setParameterValue("Fecha inicial", new Date(2014, 3, 1));
			task.setParameterValue("Fecha final", new Date(2014, 4, 5));
			//task.setParameterValues(parametros);
			task.setRenderOption(options);
			task.run();
			task.close();
		} catch (EngineException e) {
//			MessageDialog.openError(getSite().getShell(), "Error en: " + this.getClass().getName(), "Error al generar el reporte: " + rpt + ".\n" +
//					"Error: " + e.toString() + "\n\nStack trace: " + e.getStackTrace()[0] + "\n" + e.getStackTrace()[1]);
			e.printStackTrace();
		}
		return options.getOutputFileName();
	}

}
