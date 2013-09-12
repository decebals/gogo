package ro.fortsoft.gogo;

/**
 * @author Decebal Suiu
 */
public class DummyApplication {

	public static void main(String[] args) {
		System.out.println(">>>>> Hello from gogo!");
		
		// list all arguments
		System.out.println("-- listing arguments --");
		for (String arg : args) {
			System.out.println(arg);
		}
		
		// list all system properties
		System.getProperties().list(System.out);
	}
	
}
