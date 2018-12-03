package customAgents;

import jade.core.*;
import jade.core.behaviours.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class cmdTest extends Agent {

	private String targetBookTitle;
	  // The list of known seller agents
	private AID[] sellerAgents;
	  
	  // Put agent initializations here
	protected void setup() {
		  // Printout a welcome message
		 WriteBahaviour writeBahaviour = new  WriteBahaviour(this);
			addBehaviour(writeBahaviour);
		 
		System.out.println("Hallo! CMDAgent "+getAID().getName()+" is ready.");
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
		//PrintWriter outputStream=GetPrintWriter("tekst.txt");
		//if(outputStream!=null)
		//{

			for(int i=0;i<10;i++)
			{
				System.out.println(i);
				TimeUnit.SECONDS.sleep(2);
			}
			System.out.println("nextCycle!");
		//}
	}
	
	public PrintWriter GetPrintWriter(String name) throws FileNotFoundException
	{
			PrintWriter outputStream=new PrintWriter(new FileOutputStream(name, true));
			return outputStream;		
	}
	}
	protected void takeDown() {
	    // Printout a dismissal message
	    System.out.println("CMDAgent "+getAID().getName()+" terminating.");
	  }
	
}
