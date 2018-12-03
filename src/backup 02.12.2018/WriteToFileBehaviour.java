package TrafiCarBehaviours;

import jade.core.*;
import jade.core.behaviours.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class WriteToFileBehaviour extends CyclicBehaviour {

		private int x;
		private Agent agent;
		public WriteToFileBehaviour(Agent a,int b) 
		{
			super(a);
			this.x=b;
		}
	public void action()
	{
		try
		{
			write();
		}
		catch(Exception ex)
		{
		}
		
	}
	public void write() throws InterruptedException, FileNotFoundException 
	{
	
		PrintWriter outputStream=new PrintWriter(new FileOutputStream(name, true));
		if(outputStream!=null)
		{			
			for(int i=0;i<10;i++)
			{
			x=x+1;
			outputStream.println(x + myAgent.getAID().getName());
			TimeUnit.SECONDS.sleep(1);
			}
			outputStream.close();
		}
		else
		{
			System.out.println("outputStream ==null");
		}
	}
	
	public PrintWriter GetPrintWriter(String name) throws FileNotFoundException
	{
			
			return outputStream;		
	}
}