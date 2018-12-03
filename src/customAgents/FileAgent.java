package customAgents;

import jade.core.*;
import jade.core.behaviours.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;


public class FileAgent extends Agent {

	private String targetBookTitle;
	  // The list of known seller agents
	private AID[] sellerAgents;
	  
	  // Put agent initializations here
	protected void setup() {
		  // Printout a welcome message
		 WriteBahaviour writeBahaviour = new  WriteBahaviour(this);
			addBehaviour(writeBahaviour);
		 
		System.out.println("Hallo! FileAgent "+getAID().getName()+" is ready.");
	}
	public class WriteBahaviour extends CyclicBehaviour {

		public WriteBahaviour(Agent a) {
			super(a);
		}
	public void action() {
		try
		{
		write();
		}
		catch(Exception ex)
		{
			
		}
	}
	public void write() throws InterruptedException, FileNotFoundException {
		   File log = new File("log.txt");
    try{
    if(log.exists()==false){
            System.out.println("We had to make a new file.");
            log.createNewFile();
    }
    PrintWriter out = new PrintWriter(new FileWriter(log, true));
    for(int i=0;i<10;i++)
			{
				out.println(i);
			TimeUnit.SECONDS.sleep(2);
			}
			out.println("next cycle!");
			out.close();
    }catch(IOException e){
        System.out.println("COULD NOT LOG!!");
    }
	}
	
	public PrintWriter GetPrintWriter(String name) throws FileNotFoundException
	{

			PrintWriter outputStream=new PrintWriter(new FileOutputStream(name, true));
			return outputStream;		
	}
	}
	protected void takeDown() {
	    // Printout a dismissal message
	    System.out.println("FileAgent "+getAID().getName()+" terminating.");
	  }
	
}