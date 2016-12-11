package com.orendel.transfer.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.orendel.common.config.AppConfig;
import com.orendel.transfer.exceptions.FTPException;
import com.orendel.transfer.util.FTPUtility;


/**
 * Service used to upload a file to a FTP server.
 * @author Admin
 */
public class FtpUploadService {
	
	private static final Logger logger = Logger.getLogger(FtpUploadService.class);
	
	private String host;
	private int port;
	private String username;
	private String password;
	private String destinationDir;
	
	
	public FtpUploadService() {
		host = AppConfig.INSTANCE.getValue("ftp.server.host");
		port = Integer.parseInt((String) AppConfig.INSTANCE.getValue("ftp.server.port"));
		username = AppConfig.INSTANCE.getValue("ftp.server.username");
		password = AppConfig.INSTANCE.getValue("ftp.server.password");
		destinationDir = AppConfig.INSTANCE.getValue("ftp.server.csv.upload.directory");
	}

	public FtpUploadService(String port, String host, String username, String password, String destinationDir) {
		this.host = host;
		this.port = Integer.parseInt(port);
		this.username = username;
		this.password = password;
		this.destinationDir = destinationDir;
	}
	
	
	public void uploadCsvFile(String csvPath) throws FTPException {
		if (host == null || host.isEmpty()) {
			return;
		}
		
		File uploadFile = new File(csvPath);		
		FTPUtility util = new FTPUtility(host, port, username, password);
		try {
			util.connect();
			util.uploadFile(uploadFile, destinationDir);

			FileInputStream inputStream = new FileInputStream(uploadFile);
			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				util.writeFileBytes(buffer, 0, bytesRead);
			}

			inputStream.close();

			util.finish();
		} catch (IOException e) {
			logger.error(e);
			throw new FTPException("Error leyendo el archivo csv: " + csvPath);
		} finally {
			util.disconnect();
		}
		
	}

}
