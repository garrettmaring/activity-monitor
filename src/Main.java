import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

// Sigar
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public class Main {

	// List of Applications in app folder
	private final static String appNames = "\"App Store\" Mail Notes Chess Safari safari Xcode Pages \"Google Chrome\" Finder Numbers Keynote Spotify iTunes AlinofTimer Automator Battle.net Calculator Contacts Dashboard Dictionary Messages FaceTime GarageBand iBooks iMovie iPhoto Launchpad Maps Notes TextEdit Calendar Reminders Preview";
	
	// Creating objects from Sigar, launchProgram, and Scanner classes
  private static Sigar UserInfo = new Sigar();
  private static launchProgram program = new launchProgram();
  private static Scanner input = new Scanner(System.in);

  private static Hashtable<String, Long> hashtable;
  
  
  public static void main(String[] args) throws Exception {

  	System.out.println("CPU/Memory Information");
  	System.out.println("*******************************************");
  	
  	String menu = "Select option:\n 1. System Information \n 2. Launch Program \n 3. Kill Program \n 4. Kill All Running Programs \n 5. Exit";
  	int option = 0;
  	
  	while(option != 5) {
  		System.out.println(menu);
  		option = input.nextInt();
  		
  		if (option == 1) {
  			// Iterates twice (one loop, one method call) to get more accurate percentages for Processes. Only prints when method is called. 
  			for(long pid: UserInfo.getProcList()) {
          String proc = formatProcInfo(pid);

          if (!proc.equals("")) {
        	  //System.out.println(proc);
          }
  	    }

  	    getInformationsAboutMemory();
  			System.out.println(ProcInfo());
  			
  		} else if(option == 2) {

  			programLauncher();
  		} else if(option == 3) {

  			// Eat previous input
  			input.nextLine();
  			programKiller();
  		}
  		else if(option == 4) {
  			
  			//Kill All, then launch. Can select which program to launch in programLauncher method.   
  			
  			System.out.println("This will close all other running applications. This will increase speed, and decrease memory usage.");
  			System.out.println("\n Do you wish to proceed? Y/N");

  			char y_n = input.next().charAt(0);

  			while(y_n == ('y'|'Y')) {
  				killAll();
  				break;
  			}
  			
  		}
  		else {
  			System.exit(0);
  		}
  	}
  }
        
  private static void programKiller() {
  	try {

  		System.out.println("Enter program name to terminate:");
			killProc(input.nextLine().toUpperCase());
  	} catch(Exception e) {
			System.out.println("That program does not exist!");
  	}
	}

	public static void killAll() {
		try {

			Runtime.getRuntime().exec("killall "+ appNames);
			System.out.println("All applications have been terminated.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
   
  public static void getInformationsAboutMemory() {

		//CPU
    try {
			System.out.println(UserInfo.getCpuPerc());
		} catch (SigarException e) {
			e.printStackTrace();
		}
        
        
    Mem mem = null;
    
    //Memory 
    try {
    	mem = UserInfo.getMem();
    } catch (SigarException se) {
      se.printStackTrace();
    }
    
    System.out.println("Actual total free system memory: "
            + mem.getActualFree() /1024/1024/1024 + " GB");
    
    System.out.println("Actual total used system memory: "
            + mem.getActualUsed()/1024/1024/1024 + " GB");
    
    System.out.println("System Random Access Memory....: " + mem.getRam()/1024
            + " GB");
    
    System.out.println("Total system memory............: " + mem.getTotal()/1024/1024/1024
           + " GB");
    
    System.out.println("Total used system memory.......: " + mem.getUsed()/1024/1024/1024
    		
            + " GB");

    System.out.println("\n**************************************\n");
  }

  public static void programLauncher() {
	 	//Line Eating
	   
		input.nextLine();
	   
    //Program Launcher
	   
	  try{
	  	System.out.println("Enter a program to launch!");
    	String application = input.nextLine().toUpperCase();
    	program.launchProgramCommand(application);
    } catch(Exception e) {
    	System.out.println("That program does not exist!");
    }
  }

  public static String ProcInfo() {
    try {
    	//Short hand iterations
    		
			for(long pid: UserInfo.getProcList()){
				String info=formatProcInfo(pid);
				
				if(!info.equals("")) {
					System.out.println(info);
				}
			}
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    return "";
  }

  public static String formatProcInfo(long pid) {
		
		try {
			return pid + ":\t"+UserInfo.getProcState(pid).getName()+"\t"+
					UserInfo.getProcCpu(pid).getPercent();
					
					/* + "\t" +
					UserInfo.getProcMem(pid).getResident()+ "\t" +
					UserInfo.getProcMem(pid).getSize()+ "\t";
					
					*/
					
		} catch (Exception e) {
			return "";
		}
	}

  public static void killProc(String procName) {

		//Storing in hashtable.  Cache
		hashtable=new Hashtable<String, Long>();
		long[] array;

		try {
			array = UserInfo.getProcList();
			for (int i = 0; i < array.length; i++) {
				long pid = array[i];
				String name = UserInfo.getProcState(pid).getName();
				hashtable.put(name, new Long(pid));
			}
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long killPid=hashtable.get(procName);
		
	  try {
			Runtime.getRuntime().exec("kill -9 " + killPid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}





