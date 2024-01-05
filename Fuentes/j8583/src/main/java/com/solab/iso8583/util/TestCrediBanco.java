package com.solab.iso8583.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;


import com.credibanco.ssh.DBUtil;
import com.credibanco.ssh.Environment;
import com.credibanco.ssh.EnvironmentReader;
import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;

public class TestCrediBanco {
	
	private static DBUtil dbUtil;
	
	static String root = "D:\\AutomatizacionRPA_Mensajeria\\config\\";
	//Pruebas Locales
    //root =  "C:\\Data\\Assist\\2023\\Credibanco\\dist\\";
	
	static {
		
        Environment db = EnvironmentReader.getInfoByAlias("DB",root+"db.json");
        dbUtil =new DBUtil(db);
	}

	public static void main(String[] args) throws ParseException, IOException {
		//sample String[] arguments={trama,fecha,map.get("date"),path};
		 MessageFactory<IsoMessage> mf = new MessageFactory<>();
		 mf = new MessageFactory<>();
		 mf.setCharacterEncoding("UTF-8");
		 //mf.setConfigPath("config200.xml");
		 
		 File file = new File(root+"config200_dyn.xml");
		 
		 //File file = new File("C:\\Mensajeria\\Pruebas\\config200_dyn.xml");
		 
		 int[] intArray = new int[]{ 2,3,4,11,12,13,22,24,25,35,37,38,39,41,42,45,48,52,54,55,60,62,63,64 };
		 String[] names = new String[]{ "PAN","ProcessingCode","TxnAmount","SystemTraceNo","TxnTime","TxnDate","POSEntryMode","NII","POSConditionCode","Track2","RetRefNo","AuthID","ResponseCode","TerminalID","AcquirerID","Track1","PrivateAddData","PINData","AddAmounts","Field55","Field60","Field62","Field63","MsgAuthCode" };
		 
		 ConfigParser.configureFromReader(mf, new FileReader(file));
		 //  var input = "02003020058020C102060010000000050000000010220070000100374593174626388035D200620110000899000000303030413054355130302020303131353939303635202000243030303030303030303030303030303030303030303030300137820220009F3602027D9F26086434EBABA609A24E9F2701809F100706010A03A020009F3303E860089F350122950500000000009F3704CB544C199F02060000050000009F03060000000000005F25031910175F24032006305F3401009F3901079F1A0201705F2A0201709A031810199C01009F07023D009F1E0831323334353637389F6E04207000000006303030343939004830303030303030303030303030303030304150494230355F435949303039383134373738353930303030303030303030";
		 
		 //var input = "600107036A02003020058020C102060010000000022222000015370070010700374573210000097224D231222111592191000000303030444737353930302020303139393634383234202000363030303030303030343030303030303030303030303030303030303030303030313530300148820220009F3602006E9F2608C38292F377A60DDC9F2701809F34031F00009F100706011203A020009F3303E860089F350122950500000000009F370422C034729F02060000022222009F03060000000000005F25005F24005F3401009F3901079F1A0201708407A00000000320105F2A0201709A032306219C01009F0704000000009F1E0838313730323533309F6E04207000000006303031363437004830303030303030303030303030303030304146565331305F4330393332352D3631302D33333630303030303030303030";                                     // F9(BINARY)
		 //var input = "6001073D1002003020058020C904060030000000021747000000110022010700374005580000053692D21121011680019100000F303030415733574F3032202030313032303330343020207642343030353538303030303035333639325E5441524A455441204445205052554542412043522020202020205E32313132313031313638303030303030303030303030313931303030303030003630303030303032393130303030303030303030303030303030303030303031323236303000123030303030303232393730300006303030303130004830303030312041313032383337422030375046494230355F43303238313435333030313030303030303030303030303000000000000000000000";//200  Banda
		 
		 //var input = "600107036F02003020058020C102060010000000000200000000050070010700374116690000478942D27052201680119900000F303030444733393430302020303130323033303430202000123030303030303030333230300165820220009F3602007E9F2608FFE3A2D7D30089579F2701809F100706011203A028009F3303E860089F3501229505000000000057134116690000478942D27052201680119900000F9F21031539259F3704ABB5BBFC9F02060000000200009F03060000000000005F24032705315F3401009F3901079F1A0201705F2A0201709A032306219C01009F0702FF809F1E0832333731393435345F280201708407A00000000310100006303030303031004820203030303030303030303030303030305046495630355F433031393337323337313934353430303030303030303030";//200 sin contacto
		 
		 //var input = "60036901070210303801000EC00201003000000000005600000132153549062101073034303536383931353135313931353135313030303030415733574430322020303130323033303430202000000000000000000000";// 210 con 55
		 
		 //var input = "603D1001070210303801000EC00001003000000002174700000011110523090601073034343332383335343430313335343430313030303030415733574F3032202030313032303330343020200000000000000000";// 210 sin 55
		 
		 //var input = "600107035E08002020010000C0000292000000000101073030305351413035303020203031303230333034302020004820203030303030303030303030303030305046494830355F433031353139313339323130303530303030303030303030";// 800
		 
		 var input = "60035E0107081020380100028000069200000000011523440621010730303030305351413035008530303030303030303030303030303030446972656363696F6E20353420333320323220424F474F30303030303030303030303030303030303030303030304A554D424F2043414C4C45203830202020202020202020002000184B5003C818F64F2F3A48105CABDF99A525C8";// 810
		 
		 //var input = "600107036605002020010000C000129200000002620107303030415732513120202020303330303030303031202000063030303030360090303035303030303630303330303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030"; // 500
		 
		 //var input = "60036D01070510203801000A800002960000000160153730062101073034303536383939323134313030303030415733574D0090303039303030303939323439303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030"; //501
		 
		 //var input = "600107036C04003020058020C102060010000000022222000015370070010700374573210000097224D231222111592191000000303030444737353930302020303139393634383234202000363030303030303030343030303030303030303030303030303030303030303030313530300148820220009F3602006E9F2608C38292F377A60DDC9F2701809F34031F00009F100706011203A020009F3303E860089F350122950500000000009F370422C034729F02060000022222009F03060000000000005F25005F24005F3401009F3901079F1A0201708407A00000000320105F2A0201709A032306219C01009F0704000000009F1E0838313730323533309F6E04207000000006303031363437004830303030303030303030303030303030304146565331305F4330393332352D3631302D33333630303030303030303030000000000000000000000000000000";//400
		 
		 //var input = "60036B01070410303801000A80020000300000000000470000013015353506210107303430353638383632393731303030303041573357440000"; //410
		 
		 //var input = "600107036A01003020058020C10006253000000000005600000132005000010037379361713751708D26112012201834890000003030304157335744303220203031303230333034302020003630303030303030303038303030303030303030303030303030303030303030303034303000063030303036370048303030303030303030303030303030303053464E5630325F4338334E3836305731303031383330303030303030303030";//100
		 
		//var input = "60036A01070110303801000E80020325300000000000560000013215361806210001303430353638393135313531393135313531303030303041573357440000002600245141202020202020202020202020202020202020202020200000000000000000";//110
		 
		 //var input = "600107036D0320703805800CC1001616454405999185345902300100000861500000015109295506210022010700303030303030303030303433303039343335303030415733574D30332020303130323033303430202000363030303030303130303030303030303030303030303030303030303030303230303030300022303230303030303033393030303030303030303034330006303030303339004830303030312041313032383337422030375046494230355F433031383138353539383630303030303030303030303030";//320
		 
		 //var input = "60036D01070330303801000A800000023001000008615000000151092955062101073030303030303030303034333030303030415733574D";//330
		 
		 //var input = "600107036C04003020058020C102060010000000022222000015370070010700374573210000097224D231222111592191000000303030444737353930302020303139393634383234202000363030303030303030343030303030303030303030303030303030303030303030313530300148820220009F3602006E9F2608C38292F377A60DDC9F2701809F34031F00009F100706011203A020009F3303E860089F350122950500000000009F370422C034729F02060000022222009F03060000000000005F25005F24005F3401009F3901079F1A0201708407A00000000320105F2A0201709A032306219C01009F0704000000009F1E0838313730323533309F6E04207000000006303031363437004830303030303030303030303030303030304146565331305F4330393332352D3631302D33333630303030303030303030";//400
		 
		 //var input = "600107037201003020058020C902062530000000000200000000080050010700364099846580966117D220220136330493000030303044473339343030202031202020202020202020207642343039393834363538303936363131375E4E49264F2F4C454F4E4F522020202020202020202020202020205E323230323032303336333330303030303030303030303439333030303030300012303030303030303033323030018582025C009F360200619F2608B9A6C6418B079F119F2701809F34035E03009F100706010A03A0A0009F3303E8F8C89F350122950502400080009B02E80057124099846580966117D22022013633049300009F21031540179F37041F61BF2A9F02060000000200009F03060000000000005F25031602015F24032202285F3401009F3901009F1A0201705F2A0201709A032306219C01009F0702FF009F1E0832333731393435345F280201708407A00000000310109F0802008C0006303030303034004820203030303030303030303030303030305046495630355F433031393337323337313934353430303030303030303030";
		 // When
		// mf.parseMessage("060002000000000000000125213456".getBytes(), 0);
		 
		 String fecha = " fecha ";
		 
		 String date = " date ";
		 
		 String fileName = "HypercomSample.1";		 
		 String alias = "";
		 String node = "";
		 
		 if(args.length>0)
		 if (args[0]!=null) {
			 input = args[0];
			 fecha = args[1];
			 date  = args[2];
			 fileName = args[3];
			 alias = args[4].substring(0,args[4].indexOf("_"));
			 node = args[5];
		 }
	     final IsoMessage m = mf.parseMessage(input.getBytes(), 10);

	    var tx = input.substring(10,14);
	     System.out.println("*****  "+tx+"  *****");
	    if(tx.equalsIgnoreCase("0200")) {
	    
	     System.out.println("Bitmap           |"+ m.getField(1)+"|");
	     System.out.println("Processing Code  |"+ m.getField(3)+"|");
	     System.out.println("TxnAmount        |"+ m.getField(4)+"|");
	     System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
	     if(m.getField(12)!=null)
	    	 System.out.println("TxnTime          |"+ m.getField(12)+"|");
	     if(m.getField(13)!=null)
		     	System.out.println("TxnDate          |"+ m.getField(13)+"|");
	     System.out.println("POS Entry Mode   |"+ m.getField(22)+"|");
	     System.out.println("NII              |"+ m.getField(24)+"|");
	     System.out.println("POSConditionCode |"+ m.getField(25)+"|");
	     System.out.println("Track2           |"+ (m.getField(35).getValue()+"")+"|");
	     if(m.getField(37)!=null)
		     	System.out.println("RetRefNo         |"+ hexToAscii((m.getField(37).getValue()+""))+"|");
	     if(m.getField(38)!=null)
		     	System.out.println("AuthID           |"+ hexToAscii((m.getField(38).getValue()+""))+"|");
	     System.out.println("TerminalID       |"+hexToAscii( m.getField(41).getValue()+"")+"|"); 
	     System.out.println("AcquirerID       |"+ hexToAscii(m.getField(42).getValue()+"")+"|");
	     if(m.getField(45)!=null)
	    	 System.out.println("Track1           |"+ hexToAscii((m.getField(45).getValue()+"").substring(2))+"|");
	     if(m.getField(48)!=null)
	    	 System.out.println("PrivateAddData   |"+ hexToAscii((m.getField(48).getValue()+""))+"|");
	     if(m.getField(52)!=null)
	    	 System.out.println("PINData          |"+ (m.getField(52)+"")+"|");
	     if(m.getField(54)!=null)
	    	 System.out.println("Field54          |"+ hexToAscii((m.getField(54)+""))+"|");
	     if(m.getField(55)!=null)
	        System.out.println("Field55          |"+ (m.getField(55)+"")+"|");
	     System.out.println("Field62          |"+ hexToAscii((m.getField(62).getValue()+""))+"|");
	     System.out.println("Field63          |"+ hexToAscii((m.getField(63).getValue()+""))+"|");
	    } 
	    if(tx.equalsIgnoreCase("0210")) {
	    	System.out.println("Bitmap           |"+ m.getField(1)+"|");
		    System.out.println("Processing Code  |"+ m.getField(3)+"|");
		    System.out.println("TxnAmount        |"+ m.getField(4)+"|");
		    System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		    System.out.println("TxnTime          |"+ m.getField(12)+"|");
		    System.out.println("TxnDate          |"+ m.getField(13)+"|");
		    System.out.println(" NII             |"+ m.getField(24)+"|");
		    System.out.println("RetRefNo         |"+ hexToAscii((m.getField(37).getValue()+""))+"|");
		    System.out.println("AuthID           |"+ hexToAscii((m.getField(38).getValue()+""))+"|");
		    System.out.println("ResponseCode     |"+ hexToAscii((m.getField(39).getValue()+""))+"|");
		    System.out.println("TerminalID       |"+ hexToAscii((m.getField(41).getValue()+""))+"|");
		    System.out.println("AcquirerID       |"+ hexToAscii((m.getField(42).getValue()+""))+"|");
		    if(m.getField(55)!=null)
		        System.out.println("Field55          |"+ (m.getField(55)+"")+"|");
		    if(m.getField(63)!=null)
		    	System.out.println("Field63          |"+ hexToAscii((m.getField(63).getValue()+""))+"|");
		    System.out.println("MsgAuthCode      |"+ m.getField(64)+"|");
		    
	    }
	    if(tx.equalsIgnoreCase("0800")) {
	    	System.out.println("Bitmap           |"+ m.getField(1)+"|");
		    System.out.println("Processing Code  |"+ m.getField(3)+"|");
		    System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		    System.out.println(" NII             |"+ m.getField(24)+"|");
		    System.out.println("TerminalID       |"+ hexToAscii((m.getField(41).getValue()+""))+"|");
		    if(m.getField(42)!=null)
		    	System.out.println("AcquirerID       |"+ hexToAscii((m.getField(42).getValue()+""))+"|");
		    if(m.getField(63)!=null)
		    System.out.println("Field63          |"+ hexToAscii((m.getField(63).getValue()+""))+"|");
	    }
	    if(tx.equalsIgnoreCase("0810")) {
	    	System.out.println("Bitmap           |"+ m.getField(1)+"|");
		    System.out.println("Processing Code  |"+ m.getField(3)+"|");
		    System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		    System.out.println("TxnTime          |"+ m.getField(12)+"|");
		    System.out.println("TxnDate          |"+ m.getField(13)+"|");
		    System.out.println(" NII             |"+ m.getField(24)+"|");
		    System.out.println("ResponseCode     |"+ hexToAscii((m.getField(39).getValue()+""))+"|");
		    System.out.println("TerminalID       |"+ hexToAscii((m.getField(41).getValue()+""))+"|");
		    if(m.getField(62)!=null)
		    	System.out.println("Field62          |"+ hexToAscii((m.getField(62).getValue()+""))+"|");
		    if(m.getField(63)!=null)
		    	System.out.println("Field63          |"+ (m.getField(63).getValue()+"")+"|");
	    }
	    if(tx.equalsIgnoreCase("0500")) {
	    	System.out.println("Bitmap           |"+ m.getField(1)+"|");
		    System.out.println("Processing Code  |"+ m.getField(3)+"|");
		    System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		    System.out.println(" NII             |"+ m.getField(24)+"|");
		    System.out.println("TerminalID       |"+ hexToAscii((m.getField(41).getValue()+""))+"|");
		    System.out.println("AcquirerID       |"+ hexToAscii((m.getField(42).getValue()+""))+"|");
		    System.out.println("Field60          |"+ hexToAscii((m.getField(60).getValue()+""))+"|");
		    System.out.println("Field63          |"+ hexToAscii((m.getField(63).getValue()+""))+"|");
	    }
	    if(tx.equalsIgnoreCase("0510")) {
	    	System.out.println("Bitmap           |"+ m.getField(1)+"|");
		    System.out.println("Processing Code  |"+ m.getField(3)+"|");
		    System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		    System.out.println("TxnTime          |"+ m.getField(12)+"|");
		    System.out.println("TxnDate          |"+ m.getField(13)+"|");
		    System.out.println("NII              |"+ m.getField(24)+"|");
		    System.out.println("RetRefNo         |"+ hexToAscii((m.getField(37).getValue()+""))+"|");
		    System.out.println("ResponseCode     |"+ hexToAscii((m.getField(39).getValue()+""))+"|");
		    System.out.println("TerminalID       |"+ hexToAscii((m.getField(41).getValue()+""))+"|");
		    System.out.println("Field63          |"+ hexToAscii((m.getField(63).getValue()+""))+"|");
	    }
	    if(tx.equalsIgnoreCase("0400")) {
		    
		     System.out.println("Bitmap           |"+ m.getField(1)+"|");
		     System.out.println("Processing Code  |"+ m.getField(3)+"|");
		     System.out.println("TxnAmount        |"+ m.getField(4)+"|");
		     System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		     if(m.getField(12)!=null)
		    	 System.out.println("TxnTime          |"+ m.getField(12)+"|");
		     if(m.getField(13)!=null)
		     	System.out.println("TxnDate          |"+ m.getField(13)+"|");
		     System.out.println("POS Entry Mode   |"+ m.getField(22)+"|");
		     System.out.println("NII              |"+ m.getField(24)+"|");
		     System.out.println("POSConditionCode |"+ m.getField(25)+"|");
		     System.out.println("Track2           |"+ (m.getField(35).getValue()+"")+"|");
		     if(m.getField(37)!=null)
		     	System.out.println("RetRefNo         |"+ hexToAscii((m.getField(37).getValue()+""))+"|");
		     if(m.getField(38)!=null)
			     	System.out.println("AuthID           |"+ hexToAscii((m.getField(38).getValue()+""))+"|");
		     System.out.println("TerminalID       |"+hexToAscii( m.getField(41).getValue()+"")+"|"); 
		     System.out.println("AcquirerID       |"+ hexToAscii(m.getField(42).getValue()+"")+"|");
		     if(m.getField(45)!=null)
		     System.out.println("Track1           |"+ hexToAscii((m.getField(45).getValue()+"").substring(2))+"|");
		     
		     System.out.println("PrivateAddData   |"+ hexToAscii((m.getField(48).getValue()+""))+"|");
		     if(m.getField(54)!=null)
		    	 System.out.println("Field54          |"+ hexToAscii((m.getField(54)+""))+"|");
		     if(m.getField(55)!=null)
		        System.out.println("Field55          |"+ (m.getField(55)+"")+"|");
		     System.out.println("Field62          |"+ hexToAscii((m.getField(62).getValue()+""))+"|");
		     System.out.println("Field63          |"+ hexToAscii((m.getField(63).getValue()+""))+"|");
		    }
	    if(tx.equalsIgnoreCase("0410")) {
	    	System.out.println("Bitmap           |"+ m.getField(1)+"|");
		    System.out.println("Processing Code  |"+ m.getField(3)+"|");
		    System.out.println("TxnAmount        |"+ m.getField(4)+"|");
		    System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		    System.out.println("TxnTime          |"+ m.getField(12)+"|");
		    System.out.println("TxnDate          |"+ m.getField(13)+"|");
		    System.out.println("NII              |"+ m.getField(24)+"|");
		    System.out.println("RetRefNo         |"+ hexToAscii((m.getField(37).getValue()+""))+"|");
		    if(m.getField(38)!=null)
		    	 System.out.println("AuthID           |"+ hexToAscii((m.getField(38).getValue()+""))+"|");
		    System.out.println("ResponseCode     |"+ hexToAscii((m.getField(39).getValue()+""))+"|");
		    System.out.println("TerminalID       |"+ hexToAscii((m.getField(41).getValue()+""))+"|");
		    if(m.getField(55)!=null)
		        System.out.println("Field55          |"+ (m.getField(55)+"")+"|");
	    }
	    if(tx.equalsIgnoreCase("0100")) {
		    
		     System.out.println("Bitmap           |"+ m.getField(1)+"|");
		     System.out.println("Processing Code  |"+ m.getField(3)+"|");
		     System.out.println("TxnAmount        |"+ m.getField(4)+"|");
		     System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		     System.out.println("POS Entry Mode   |"+ m.getField(22)+"|");
		     System.out.println("NII              |"+ m.getField(24)+"|");
		     System.out.println("POSConditionCode |"+ m.getField(25)+"|");
		     System.out.println("Track2           |"+ (m.getField(35).getValue()+"")+"|");
		     System.out.println("TerminalID       |"+hexToAscii( m.getField(41).getValue()+"")+"|"); 
		     System.out.println("AcquirerID       |"+ hexToAscii(m.getField(42).getValue()+"")+"|");
		     if(m.getField(45)!=null)
		     System.out.println("Track1           |"+ hexToAscii((m.getField(45).getValue()+"").substring(2))+"|");
		     if(m.getField(48)!=null)
		     System.out.println("PrivateAddData   |"+ hexToAscii((m.getField(48).getValue()+""))+"|");
		     if(m.getField(54)!=null)
		    	 System.out.println("Field54          |"+ hexToAscii((m.getField(54)+""))+"|");
		     if(m.getField(55)!=null)
		        System.out.println("Field55          |"+ (m.getField(55)+"")+"|");
		     System.out.println("Field62          |"+ hexToAscii((m.getField(62).getValue()+""))+"|");
		     System.out.println("Field63          |"+ hexToAscii((m.getField(63).getValue()+""))+"|");
		  
		     
		    } 
	    if(tx.equalsIgnoreCase("0110")) {
	    	System.out.println("Bitmap           |"+ m.getField(1)+"|");
		    System.out.println("Processing Code  |"+ m.getField(3)+"|");
		    System.out.println("TxnAmount        |"+ m.getField(4)+"|");
		    System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		    System.out.println("TxnTime          |"+ m.getField(12)+"|");
		    System.out.println("TxnDate          |"+ m.getField(13)+"|");
		    System.out.println(" NII             |"+ m.getField(24)+"|");
		    System.out.println("RetRefNo         |"+ hexToAscii((m.getField(37).getValue()+""))+"|");
		    System.out.println("AuthID           |"+ hexToAscii((m.getField(38).getValue()+""))+"|");
		    System.out.println("ResponseCode     |"+ hexToAscii((m.getField(39).getValue()+""))+"|");
		    System.out.println("TerminalID       |"+ hexToAscii((m.getField(41).getValue()+""))+"|");
		    if(m.getField(42)!=null)
		    System.out.println("AcquirerID       |"+ hexToAscii(m.getField(42).getValue()+"")+"|");
		    if(m.getField(55)!=null)
		        System.out.println("Field55          |"+ (m.getField(55)+"")+"|");
		    if(m.getField(63)!=null)
		    System.out.println("Field63          |"+ hexToAscii((m.getField(63).getValue()+""))+"|");
		    System.out.println("MsgAuthCode      |"+ m.getField(64)+"|");
	    }
	    if(tx.equalsIgnoreCase("0320")) {
		    
		     System.out.println("Bitmap           |"+ m.getField(1)+"|");
		     System.out.println("PAN              |"+ (m.getField(2).getValue()+"").substring(2)+"|");
		     System.out.println("Processing Code  |"+ m.getField(3)+"|");
		     System.out.println("TxnAmount        |"+ m.getField(4)+"|");
		     System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		     System.out.println("TxnTime          |"+ m.getField(12)+"|");
			 System.out.println("TxnDate          |"+ m.getField(13)+"|");
		     System.out.println("POS Entry Mode   |"+ m.getField(22)+"|");
		     System.out.println("NII              |"+ m.getField(24)+"|");
		     System.out.println("POSConditionCode |"+ m.getField(25)+"|");
		     System.out.println("RetRefNo         |"+ hexToAscii((m.getField(37).getValue()+""))+"|");
			 System.out.println("AuthID           |"+ hexToAscii((m.getField(38).getValue()+""))+"|");
			 if(m.getField(39)!=null)
			 System.out.println("ResponseCode     |"+ hexToAscii((m.getField(39).getValue()+""))+"|");
		     System.out.println("TerminalID       |"+hexToAscii( m.getField(41).getValue()+"")+"|"); 
		     System.out.println("AcquirerID       |"+ hexToAscii(m.getField(42).getValue()+"")+"|");
		     if(m.getField(48)!=null)
		     System.out.println("PrivateAddData   |"+ hexToAscii((m.getField(48).getValue()+""))+"|");
		     if(m.getField(54)!=null)
		    	 System.out.println("Field54          |"+ hexToAscii((m.getField(54)+""))+"|");
		     System.out.println("Field60          |"+ hexToAscii((m.getField(60).getValue()+""))+"|");
		     System.out.println("Field62          |"+ hexToAscii((m.getField(62).getValue()+""))+"|");
		     System.out.println("Field63          |"+ hexToAscii((m.getField(63).getValue()+""))+"|");
		    } 
	    if(tx.equalsIgnoreCase("0330")) {
		    
		     System.out.println("Bitmap           |"+ m.getField(1)+"|");
		     System.out.println("Processing Code  |"+ m.getField(3)+"|");
		     System.out.println("TxnAmount        |"+ m.getField(4)+"|");
		     System.out.println("SystemTraceNo    |"+ m.getField(11)+"|");
		     System.out.println("TxnTime          |"+ m.getField(12)+"|");
			 System.out.println("TxnDate          |"+ m.getField(13)+"|");
		     System.out.println("NII              |"+ m.getField(24)+"|");
		     System.out.println("RetRefNo         |"+ hexToAscii((m.getField(37).getValue()+""))+"|");
		     System.out.println("ResponseCode     |"+ hexToAscii((m.getField(39).getValue()+""))+"|");
		     System.out.println("TerminalID       |"+hexToAscii( m.getField(41).getValue()+"")+"|");
		    } 
	    
	     Message m100 = new Message();
	     m100.setCode(tx);
	     HashMap<String,String> map = new HashMap<String,String>();
	     for (int i = 0; i < intArray.length; i++) {
	    	 if(m.getField(intArray[i])!=null) {
	    		 if (intArray[i]<36 || intArray[i]==52 || intArray[i]==55  || intArray[i]==64 || ( tx.equalsIgnoreCase("0810") && intArray[i]==63) )
	    			 map.put(names[i], m.getField(intArray[i])+"");
	    		 else
	    			 map.put(names[i], hexToAscii(m.getField(intArray[i]).getValue()+"")); 
	    		 
	    	 }
		}
	     map.put("DateLog", fecha);
	     map.put("TimeLog", date);
	     map.put("TxCode", tx);
	     map.put("FileLog", fileName);
	     map.put("Node", node);
	     System.out.println(map);
	     try {
	    	 
	    	 //System.out.println("");
	    	 dbUtil.insertData(map, "MESSAGE_LOG_"+alias);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static String hexToAscii(String hexStr) {
	    StringBuilder output = new StringBuilder("");
	    
	    for (int i = 0; i < hexStr.length(); i += 2) {
	        String str = hexStr.substring(i, i + 2);
	        output.append((char) Integer.parseInt(str, 16));
	    }
	    
	    return output.toString();
	}

}
