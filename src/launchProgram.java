public class launchProgram {
	
	public boolean launchProgramCommand(String application) {
		try {
			String command = "/Applications/"+ application + ".app/Contents/MacOS/" + application;
	    Runtime.getRuntime().exec(command);
	  	return true;	
		} catch(Exception exc){
			System.out.println(exc.toString());
		}

		return false;
	}
}
