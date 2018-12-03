package customAgents;

import jade.core.*;
import jade.core.behaviours.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;


public class test extends Agent {

	private int x;
	  // The list of known seller agents
	private AID[] sellerAgents;
	  
	  // Put agent initializations here
	protected void setup() {
		  // Printout a welcome message
		  x=0;
		 WriteBahaviour writeBahaviour = new  WriteBahaviour(this);
			addBehaviour(writeBahaviour);
		 
		System.out.println("Hallo! FileAgent "+getAID().getName()+" is ready.");
	}
	public class WriteBahaviour extends CyclicBehaviour {

		public WriteBahaviour(Agent a) {
			super(a);
		}
	public void action() {
		
		
		
		
	}
	public void write() throws InterruptedException, FileNotFoundException {
		//System.out.println("Writing started");
		PrintWriter outputStream=GetPrintWriter("tekst.txt");
		//System.out.println("Printer is acquired");
		if(outputStream!=null)
		{
			//System.out.println("outputStream !=null");
			
			for(int i=0;i<10;i++)
			{
				x=x+1;
				outputStream.println(x + getAID().getName());
				//System.out.println("line printed out");
			TimeUnit.SECONDS.sleep(1);
			}
			outputStream.println("next cycle!");
			outputStream.close();
		}
		else
		{
			System.out.println("outputStream ==null");
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
