package TraffiCarBehaviours2;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.Random;

public class CarBehaviour extends CyclicBehaviour 
{
	private String status;	
	private int x;
	private int y;
	private String currentDirection;
	private String mode;
	private Agent nextAgent;
	private String nextAgentmode;
	private String lightColor;
	
	public CarBehaviour(Agent a,int X,int Y,String CurrentDirection)
	{
		super(a);
		x=X;
		y=Y;
		currentDirection=CurrentDirection;
		mode="run";
		nextAgent=null;
		nextAgentmode="run";
		lightColor="green";
	}
		
	public void action()
	{ // funkcja do poruszania się samochodu
	    boolean isAmbulance = isNeedToMakePlaceForAmbulance(x,y,currentDirection); // w kazdej chwili pytamy managera ruchu czy trzeba stanąc na bok i sie zatrzymac bo jedzie ambulans
	    if (isAmbulance == true)
	    { 											// gdy jest ambulans, zjeżdzamy z drogi
	    	makePlaceForAmbulance(currentDirection);
	        mode = "stop";
	    }
	    if(mode=="run")
		{
	    	move(); // w przeciwnym wypadku jedziemy normlnie
		}
	    if(mode=="stop")
	    {
	    	stop();
	    }
	}
	
	public void move()
	{
		nextAgent=getNextAgent(x,y,currentDirection);
		if(GetAgentType(nextAgent) == null) 
		{
			takeStep(currentDirection);
        }
		if(GetAgentType(nextAgent) == "intersection") 
		{
			lightColor=GetLightColor();
			if(lightColor=="red")
			{
				mode="stop";
			}
			if(lightColor=="green")
			{
				mode="run";
				takeStep(currentDirection);
				currentDirection=GetRandomDirection(currentDirection);
				takeStep(currentDirection);
			}
        }
		if(GetAgentType(nextAgent) == "car")
		{
			nextAgentmode="run";
			if(nextAgentmode=="run")
			{
				mode="run";
				takeStep(currentDirection);
			}
			if(nextAgentmode=="stop")
			{
				mode="stop";
			}
		}
	}
	public void stop()
	{
		
	}
	public String GetRandomDirection(String currentDirection)
	{
		String newDirection=currentDirection;
		Random rand=new Random();
		int n;
		while(newDirection==currentDirection)
		{
			n=rand.nextInt(4);
			switch(n) {
			case 0:
				newDirection="north";
				break;
			case 1:
				newDirection="south";
				break;
			case 2:
				newDirection="east";
				break;
			case 3:
				newDirection="west";
				break;
			}
		}
		currentDirection=newDirection;
		return currentDirection;
	}
	
	//public Agent GetAgentDataFromContext()
	//{
	//	
	//}
	
	public String GetLightColor()
	{
		return "green";
	}
	
	
	public Agent getNextAgent(int x,int y,String currentDirection)
	{
		//implement message query-inform and wait for response.
		return null;
	}
	
	
	public boolean isNeedToMakePlaceForAmbulance(int x, int y, String currentDirection)
	{
		//ACLMessage msg=ACLMessage.Receive()
		boolean ambulanceIsComming=false; // implement message query-inform and wait for response.
		if(ambulanceIsComming)
		{
			return true;
		}
		return false;	
	}

	void takeStep(String currentDirection)
	{
		if (currentDirection == "north")
		{
			y = y + 1;
		}
		if (currentDirection == "south")
		{
			y = y - 1;
		}
		if (currentDirection == "east")
		{
			x = x + 1;
		}
		if (currentDirection == "west")
		{
			x = x - 1;
		}
	}
	
	String GetAgentType(Agent a)
	{
		return "car";
	}
	
	void makePlaceForAmbulance(String currentDirection)
	{
		if (currentDirection == "north")
		{
			x = x + 1;
		}
		if (currentDirection == "south")
		{
			x = x - 1;
		}
		if (currentDirection == "east")
		{
			y = y + 1;
		}
		if (currentDirection == "west")
		{
			y = y - 1;
		}
	}
	private boolean MessageReceived()
	{
		ACLMessage msg = myAgent.receive();
		if(msg != null) return true;
		else  return false;
	}
		
}