package com.solab.iso8583.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.credibanco.ssh.DBUtil;
import com.credibanco.ssh.Environment;
import com.credibanco.ssh.EnvironmentReader;

public class TestFilesHypercom {
	
	public static void main(String[] args) throws IOException {
		
		String dir ="C:\\Data\\Assist\\2023\\Credibanco\\202306211550";
		String alias= "PRUEBAS_1";
		String nextFile= "202306211550.tar.gz";
		
		if(args.length>0)
			 if (args[0]!=null) {
				 dir 	  = args[0];
				 alias 	  = args[1];
				 nextFile = args[2];
			 }
		
		String root = "D:\\AutomatizacionRPA_Mensajeria\\config\\";
			//Pruebas Locales
			//root=  "C:\\Data\\Assist\\2023\\Credibanco\\dist\\";
		Environment env = EnvironmentReader.getInfoByAlias(alias,root+"environment.json");
		Environment db = EnvironmentReader.getInfoByAlias("DB",root+"db.json");
    	DBUtil dbUtil =new DBUtil(db);
		
		HashSet<String> list =  (HashSet<String>) listFilesUsingFilesList(dir);
		int index =1;
		for (String path : list) {
			
		//String path="C:\\Data\\Assist\\2023\\Credibanco\\202306211550\\hypercom_int_01580.Jun21.1";
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			System.out.println(index+" Archivo: "+path);
			index++;
			
			//if(index==50)
				//break;
			
		    inputStream = new FileInputStream(dir+"\\"+path);
		    
		    String fecha = getDateFromFile(dir+"\\"+path);
		    sc = new Scanner(inputStream, "UTF-8");
		    boolean isHexa= false;
		    boolean toSave= false;
		    ArrayList<HashMap<String, String>> messages = new ArrayList<HashMap<String,String>>();
		    String message="";
		    String date="";
		    while (sc.hasNextLine()) {
		        
		    	String line = sc.nextLine();
		        //if(line.indexOf("0.")>-1)
		        if(line.length()>38 && line.substring(15, 16).equalsIgnoreCase("|")) {
		        	
		         String tmp = line.substring(32,39);
		         long count =0;
		         String hexatemp = "";
		         if(line.length()>64) {
		         
		         hexatemp = line.substring(18,65);
		         count = hexatemp.chars().filter(ch -> ch == '.').count();
		         
		         List<String> codes = Arrays.asList(".01.00.", ".01.10.", ".02.00.", ".02.10.", ".03.20.", 
		        		    ".03.30.", ".04.00.", ".04.10.",".05.00.", ".05.10.", ".06.00.", ".08.00.", ".08.10.");
		         if(tmp.startsWith(".0") && tmp.endsWith("0.") && !tmp.equalsIgnoreCase(".00.00."))
		         if (!codes.contains(tmp))
		        	 ;//System.out.println("Error codigo de Tx no Identificado. "+tmp);
		        		 
		        		if (codes.contains(tmp) && !isHexa) {
		        			 //System.out.println(line);
		        			 System.out.println("         "+tmp);
		        			 //System.out.println("         "+ hexatemp);
		        			 toSave=true;
		        			 
		        			 date = line.substring(0, 15);
		        			 System.out.println("Fecha: "+fecha+" Hora: "+date+" Archivo: "+path+" ");
		        		}
		        		
		        		if(count==15) {
				        	 isHexa= true;
				        	 if(toSave)
				        	 {				        		
				        		 message+=hexatemp;				        		 
				        	 }
				         }
				         else
				         {
				        	 if(isHexa && toSave) {
				        		 //System.out.println("         "+ hexatemp);
				        	 	message+=hexatemp;
				        	 	}
				        	 isHexa= false;
				        	 toSave = false;
				        	 if(message.length()>0) {
				        		 HashMap<String, String> map = new HashMap<String,String>();
				        		 map.put("message", message.replace('.', ' ').replaceAll(" ", ""));
				        		 map.put("date", date);
				        	     messages.add(map);
				        	 }
				        	 message="";
				        	 date="";
				         }		        
		         	}
		       	}
		    }
		    if(messages.size() < 1)
		      System.out.println("Messages: Vacio 0");
		    for (HashMap<String,String> map : messages) {
		    	
		    	String trama = map.get("message");
		    	String[] arguments={trama,fecha,map.get("date"),path,alias,env.getNode()};
				try {
					TestCrediBanco.main(arguments);				
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		    
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
		}
		try {
			dbUtil.updateTrace(alias, nextFile, "OK");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Set<String> listFilesUsingFilesList(String dir) throws IOException {
	    try (Stream<Path> stream = Files.list(Paths.get(dir))) {
	        return stream
	          .filter(file -> !Files.isDirectory(file))
	          .map(Path::getFileName)
	          .map(Path::toString)
	          .collect(Collectors.toSet());
	    }
	}
	
	public static String getDateFromFile(String path) {
		String formattedDate =  "";
		Path file = Path.of(path);
	      //try {
	    	 String fecha = file.getParent().getFileName().toString().substring(0,8);
	    	 
	    	 
	    	 DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
	    	 DateTimeFormatter toFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    	 LocalDate dateTime = LocalDate.parse(fecha, format);
	    	 
	    	 formattedDate = dateTime.format(toFormat);
	         //FileTime lastModifiedTime = Files.getLastModifiedTime(file);
	         //Date date = new Date(lastModifiedTime.toMillis());
	         //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	          //formattedDate = formatter.format(dateTime.);
	         //System.out.println("Last modified date: " + formattedDate);
	      //} catch (IOException e) {
	        // System.out.println("File not found.");
	      //}
		return formattedDate;
	}

}
