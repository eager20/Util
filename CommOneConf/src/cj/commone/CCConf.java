package cj.commone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 * @author eager20
 *
 */
public class CCConf {

	static String CmdPath = "";
	static String CommOneInstalConfFileName = "CommOneInstall.conf";
	public static final HashMap<String, String> CommOneType = new HashMap<String, String>();
	
	public static final HashMap<String, String> ConfFileList = new HashMap<String, String>();

	
	public static void main(String[] args) 
	{
		CommOneType.put("file", "FILE");
		CommOneType.put("push", "PUSH");
		CommOneType.put("at", "AT");
		CommOneType.put("ft", "FT");
		CommOneType.put("msg", "MSG");
		CommOneType.put("email", "EMAIL");

		
		CmdPath = System.getProperty("user.dir");
		System.out.println("----------------------------------------------" );
		System.out.println("### CommOne Conf Setting PGM V.0.9" );
		System.out.println("### Your Current Path : "+CmdPath );
		System.out.println("----------------------------------------------" );
		
		
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String CmdLine = "";
		String Cmdtype = "";
		
		try {
			HashMap<String, String> list = setFileValue("file".toUpperCase());
			
			System.out.println(">> CommOneConf File Read Start");
			for( String key : list.keySet() ){
	            //System.out.println( String.format("[%s] FileName:%s [Exist:%b]", key, list.get(key), checkFileExist (list.get(key)))) ;
	            ConfFileList.put(key.replaceAll("FILE.", ""), list.get(key));
	        }
			System.out.println(">> CommOneConf File Read DONE");
			
			for( String key : ConfFileList.keySet() ){
				System.out.println( String.format("[%s] %s [Exist]:%b", key, ConfFileList.get(key) , checkFileExist (ConfFileList.get(key)) )) ;
			}


		} catch (Exception e) {
			System.out.println(e.toString());
		}
		pleaseEnterV();
		System.out.println("----------------------------------------------" );
		System.out.println("### Command List " );
		help();
		
		
		while(true)
		{
			try {
				CmdLine = in.readLine();
			} catch (IOException e) {
				System.out.println("CmdLine read error"+e.toString());
			}
			
			String[] cmdlist = CmdLine.split(" ");
			
			if ( cmdlist.length <1 || cmdlist.length >3 )
			{
				System.out.println("CmdLine arg 2 or 3 !!");
				continue;
			}
			
			if ( cmdlist.length == 3)
			{
				Cmdtype = cmdlist[2];
			}else {
				Cmdtype = "";
			}
			
			if (CmdLine.indexOf("help") > -1)
			{
				help();
			}
			
			if (CmdLine.indexOf("show file") > -1)
			{
				in = new BufferedReader(new InputStreamReader(System.in));
				CmdLine = "";
				Cmdtype = "";
				
				try {
					HashMap<String, String> list = setFileValue("file".toUpperCase());
					
					System.out.println(">> CommOneConf File Read Start");
					for( String key : list.keySet() ){
			            //System.out.println( String.format("[%s] FileName:%s [Exist:%b]", key, list.get(key), checkFileExist (list.get(key)))) ;
			            ConfFileList.put(key.replaceAll("FILE.", ""), list.get(key));
			        }
					System.out.println(">> CommOneConf File Read DONE");
					
					for( String key : ConfFileList.keySet() ){
						System.out.println( String.format("[%s] %s [Exist]:%b", key, ConfFileList.get(key) , checkFileExist (ConfFileList.get(key)) )) ;
					}


				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
			
			if (CmdLine.indexOf("show conf") > -1)
			{
				ShowFile(Cmdtype.toUpperCase());
				pleaseEnterV();
			}
			
			if(CmdLine.indexOf("show agconf") > -1)
			{
				LinkedHashMap<String, String> paramList = new LinkedHashMap<String, String>();
				Properties agentconfProp = null;
				
				try {
					HashMap<String, String> list = setFileValue(Cmdtype.toUpperCase());
					
					for( String key : list.keySet() ){
						paramList.put(key, "");
						//System.out.println("0"+key);
			            //System.out.println( String.format("[%s] FileName:%s [Exist:%b]", key, list.get(key), checkFileExist (list.get(key)))) ;
			        }
					agentconfProp= getProperties(ConfFileList.get(Cmdtype.toUpperCase()));
					//System.out.println(agentconfProp.toString());

					for( String key : paramList.keySet() )
					{
						try {
							paramList.put(key, (String) agentconfProp.get(key.replace(Cmdtype.toUpperCase()+".", "")));
							
							//System.out.println(key);
							//System.out.println((String) agentconfProp.get(key.replace(Cmdtype.toUpperCase()+".", "")));
							//System.out.println( String.format("[%s] %s ", key, agentconfProp.get(paramList.get(key.replace(Cmdtype.toUpperCase()+".", "")))))  ;
						}catch (Exception e)
						{
							e.printStackTrace();
						}
						
					}
					
					
					for( String key : paramList.keySet() )
					{
						try {
							System.out.println( String.format("[%s] %s ", key, paramList.get(key)))  ;
						}catch (Exception e)
						{
							e.printStackTrace();
						}
						
					}
							
					

				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
				
			if(CmdLine.equals("set file"))
			{
				System.out.println("set file");
			}
			if(CmdLine.equals("whereis conf"))
			{
				System.out.println("whereis Conf");
			}if(CmdLine.equals("exit"))
			{
				break;
			}
		}

	}

	
	/**
	 * CommOne Conf Setting File Read & Show
	 * @param grepword
	 */
	public static void ShowFile (String grepword)
	{
		String line = "";
		
		try{
            File file = new File(CmdPath+File.separator+CommOneInstalConfFileName);
            FileReader filereader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(filereader);
            line = "";
            while((line = bufReader.readLine()) != null){
            	if (grepword == null)
        		{
            		System.out.println(line);
        		}else {
        			if(line.indexOf(grepword) > -1)
        			{
        				System.out.println(line);
        			}
        		}
            }
            //.readLine()은 끝에 개행문자를 읽지 않는다.            
            bufReader.close();
		 }catch(Exception e){
        	System.out.println("ERR line >> " +line);
            System.out.println(e);
	     }
		
	}
	
	/**
	 * CommOne Conf Setting File show & select show 
	 * @param grepword
	 * @return
	 * @throws Exception
	 */
	public static LinkedHashMap setFileValue (String grepword) throws Exception
	{
		LinkedHashMap<String, String> rtn = new LinkedHashMap<String,String>();
		String line = "";
		try{
            File file = new File(CmdPath+File.separator+CommOneInstalConfFileName);
            FileReader filereader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(filereader);
            
            while((line = bufReader.readLine()) != null){
            	if (grepword == null)
        		{
            		rtn = ConvertStrKeyNValue (rtn, line);
        		}else {
        			if(line.indexOf(grepword) > -1)
        			{
        				rtn = ConvertStrKeyNValue (rtn, line);
        			}
        		}
            }
            bufReader.close();
        }catch(Exception e){
        	System.out.println("ERR line >> " +line);
            System.out.println(e);
        }
		return rtn;
	}
	
	/**
	 * key=value Strint Convert LinkedHashMap type
	 * @param src
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static LinkedHashMap<String, String> ConvertStrKeyNValue(LinkedHashMap<String, String> src, String value ) throws Exception
	{
		String[] temp = value.split("=");
		src.put(temp[0], temp[1]);
		
		return src;
	}
	
	
	public static boolean checkFileExist( String FilePath)
	{
		File file = new File(FilePath); 
		
		return file.exists(); 
	}
	
	public static void pleaseEnterV()
	{
		// System.out.println(">>> Display End <<<\n"
		         // +">>> Please Enter a Value <<<\n");
	}
	
	public static Properties getProperties(String filename)
	{

	    InputStream is = null;
		Properties p = null; 
		try { 
			is = new FileInputStream(filename);
			p = new Properties(); 
			p.load(is); 
		} catch (FileNotFoundException e) 
		{ 
			System.out.println( filename +"/"+e.toString() );
		} catch (IOException e) 
		{ 
			System.out.println( filename +"/"+e.toString() );
		} finally { 
			try {
				is.close();
			} catch (IOException e) {
			
			} 
		} 
		return p;
	}
	
	public static void help() 
	{
		System.out.println( 
				   "1. show agent conf file path & validate \n"
				 + "   ex) show flie [push, at, ft, msg, email]\n"
				 + "2. show CommOne conf file Data \n"
				 + "   ex) show conf  [push, at, ft, msg, email]\n"
				 + "3. show agent conf file Data \n"
				 + "   ex) show agconf [push, at, ft, msg, email]\n"
				 + "4. setting agent conf file from CommOne Conf\n"
				 + "   ex) set conf   [push, at, ft, msg, email]\n"
		         + "5. program exit \n"
		         + "   ex) exit\n"
		         +"---------------------------------------------\n"
		);
	}

}

