package com.credibanco.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;

public class SFTP_Put_Get {

	 private String sftpHost = "172.29.12.69";
	 private String sftpUser = "svfe";
	 private String sftpPort = "22";
	 private String sftpPassword = "N0dQA_2021*";
	
		
     public SFTP_Put_Get() {

	}




	public SFTP_Put_Get(String sftpHost, String sftpUser, String sftpPort, String sftpPassword) {
		super();
		this.sftpHost = sftpHost;
		this.sftpUser = sftpUser;
		this.sftpPort = sftpPort;
		this.sftpPassword = sftpPassword;
	}




	public  String getFile(String downloadFile) {

		String newPath="";
        String localPath  = "D:\\AutomatizacionRPA_Mensajeria\\temp\\";
        
        //Pruebas Locales
        //localPath  = "C:\\Data\\Assist\\2023\\Credibanco\\Pruebas";
        
        String sftpPath = "/home/svfe/output/archive/Test/"+downloadFile;
       

        try{
            JSch jsch = new JSch();
            
            //Below 2 lines are not required if the SFTP connection doesn't require SSH
           // String privateKey = ".ssh/id_rsa";
            //jsch.addIdentity(privateKey);
            System.out.println("Obteniendo sesion SFTP");
            Session session = jsch.getSession(sftpUser, sftpHost, Integer.valueOf(sftpPort));
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(sftpPassword);
            session.connect();

            Channel channel = session.openChannel("sftp");
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.connect(60000);
            //sftpChannel.put(localPath, sftpPath);
            System.out.println("Descargando Archivo "+sftpPath+" "+ localPath);
            sftpChannel.get(sftpPath, localPath);

            sftpChannel.disconnect();
            session.disconnect();
            System.out.println("Archivo Descargado "+localPath);
            FileInputStream file = new FileInputStream(localPath+"\\"+downloadFile);
            newPath = localPath+"\\"+downloadFile.replaceAll("m.tar.gz", "");
            TarExtractor tar = new TarExtractor(file, true, Paths.get(newPath));
           
            System.out.println("Desempaquetando en Local "+newPath);
            tar.untar();
            System.out.println("Termina");
           
        } catch (IOException | SftpException | JSchException e) {
            e.printStackTrace();
        }
        return newPath; 
    }
}
