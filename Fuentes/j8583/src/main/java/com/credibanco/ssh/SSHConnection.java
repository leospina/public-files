package com.credibanco.ssh;

import com.jcraft.jsch.JSchException;
import com.solab.iso8583.util.TestFilesHypercom;
import java.io.IOException;
import java.sql.SQLException;


public class SSHConnection {
 
    //private static final String USERNAME = "ec2-user";
    //private static final String HOST 	 = "ec2-3-88-46-191.compute-1.amazonaws.com";
    //private static final String PASSWORD = "softux2023";
    
	//private static final String USERNAME = "svfe";
    //private static final String HOST 	 = "172.29.12.69";
    //private static final String PASSWORD = "N0dQA_2021*";
	//private static final int 	PORT 	 = 22;
	
    public static  String PATH 	 = "/home/svfe/output/archive";
 
    public static void main(String[] args) {
 
    	
    	String alias= "PRUEBAS_1";
		
		if(args.length>0)
			 if (args[0]!=null) {
				 alias = args[0];
			 }
        try {
        	boolean AIX= true;
        	
        	String root = "D:\\AutomatizacionRPA_Mensajeria\\config\\";
        	
        	//Pruebas Locales
        	//root = "C:\\Data\\Assist\\2023\\Credibanco\\dist\\";
        	
        	Environment env = EnvironmentReader.getInfoByAlias(alias,root+"environment.json");
        	
        	Environment db = EnvironmentReader.getInfoByAlias("DB",root+"db.json");
        	
        	DBUtil dbUtil =new DBUtil(db);
        	
        	SSHConnector sshConnector = new SSHConnector();
            //String lastFile = "202306211550.tar.gz";//Debe ser reemplazado por la BD
            String lastFile = dbUtil.getLastFile(alias);
            
            //String lastFile = "202312070943.tar.gz";
            sshConnector.connect(env.getUser(), env.getPasswd(), env.getHost(), Integer.parseInt(env.getPort()));
            System.out.println("Conectandose al Servidor");
            String result = "";//sshConnector.executeCommand("pwd");
            //System.out.println(result);
            String newFile = sshConnector.executeCommand("cd "+PATH+ "&&  sh next.sh "+lastFile).trim();
            System.out.println(newFile);
            dbUtil.insertTrace(alias, newFile, "INIT");
            System.out.println("copiando a ./Test");
            result = sshConnector.executeCommand("cd "+PATH+ "&& cp "+newFile +" "+PATH+"/Test");
            PATH = PATH +"/Test";
            System.out.println("descomprimiendo archivo "+ newFile);
            result = sshConnector.executeCommand("cd "+PATH+ "&& gzip -d "+newFile);
            String nextFile = newFile;
            newFile= newFile.replaceAll(".gz", "");
            System.out.println("desempaquetando archivos Hyper");
           
            // if(AIX)
           // 	result = sshConnector.executeCommand("cd "+PATH+ "&& tar xvfL "+newFile+" filter");// AIX
            //else
            	//result = sshConnector.executeCommand("cd "+PATH+ "&& tar -xf "+newFile+" --wildcards 'hyper*'");// LINUX
            	result = sshConnector.executeCommand("cd "+PATH+ "&& tar -xf "+newFile);// LINUX new
            
            result = sshConnector.executeCommand("cd "+PATH+ "&& rm -f "+newFile);
            result = sshConnector.executeCommand("cd "+PATH+ "&& ls hyper* > filter2");
            //result = sshConnector.executeCommand("cd "+PATH+ "&& tar -xzf "+newFile+" --wildcards 'hyper*'");
            //result = sshConnector.executeCommand("cd "+PATH+ "&& ls -lrt");
            System.out.println("Compactando archivos Hyper");
            String downloadFile = newFile.replaceAll(".tar", "")+"m.tar";
            
            if(AIX)
            	result = sshConnector.executeCommand("cd "+PATH+ "&& tar -cvf "+downloadFile+" -L filter2");// AIX
            else
            	result = sshConnector.executeCommand("cd "+PATH+ "&& tar -cvf "+downloadFile+" hypercom*");// LINUX
            
            
            result = sshConnector.executeCommand("cd "+PATH+ "&&  rm $(ls | grep -v *.tar | grep -v filter)");//-f hypercom*
            result = sshConnector.executeCommand("cd "+PATH+ "&& ls -lrt");
            result = sshConnector.executeCommand("cd "+PATH+ "&& rm -f filter2");
           
            
            result = sshConnector.executeCommand("cd "+PATH+ "&& gzip "+downloadFile);
            //result = sshConnector.executeCommand("cd "+PATH+ "&& rm -f "+downloadFile);
            downloadFile = downloadFile+".gz";
            //System.out.println(result);
            sshConnector.disconnect();
            dbUtil.updateTrace(alias, nextFile, "PROCESS");
            System.out.println("Descargando archivo "+ downloadFile);
            String path = new SFTP_Put_Get(env.getHost(),env.getUser(), env.getPort(), env.getPasswd()).getFile(downloadFile);
           
            String[] arguments={path,alias,nextFile};
			try {
				TestFilesHypercom.main(arguments);				
			
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
        } catch (JSchException ex) {
            ex.printStackTrace();
             
            System.out.println(ex.getMessage());
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
             
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
             
            System.out.println(ex.getMessage());
        }catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
