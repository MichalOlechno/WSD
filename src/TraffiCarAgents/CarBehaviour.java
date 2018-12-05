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
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import TrafficarClasses.*;
import java.util.concurrent.TimeUnit;

public class CarBehaviour extends CyclicBehaviour 
{
	private String status;	
	private double x;
	private double y;
	private double x0;
	private double y0;
	private String currentDirection;
	private String mode;
	private AgentClass nextAgent;
	private String nextAgentmode;
	private String lightColor;
	private DFAgentDescription trafficManagerTemplate;
	private MessageTemplate requestMessage;
	private MessageTemplate informMessage;
	private long startTime;
	private long tempTime;
	private long measureTime;	
	private double v;
	private AID trafficManagerAID;
	private boolean messageSent=false;
	
	public CarBehaviour(Agent a,double X,double Y,String CurrentDirection)
	{
		super(a);
		x=X;
		y=Y;
		x0=X;
		y0=Y;
		currentDirection=CurrentDirection;
		mode="run";
		nextAgent=null;
		nextAgentmode="run";
		lightColor="green";
		trafficManagerTemplate = new DFAgentDescription();
		trafficManagerTemplate=setTemplate(trafficManagerTemplate,"TrafficManager");
		trafficManagerAID=GetAgents(trafficManagerTemplate)[0];
		requestMessage=MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		informMessage=MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		startTime = System.currentTimeMillis();
		v=1;
	}
		
	public void action()
	{ // funkcja do poruszania się samochodu
		tempTime=System.currentTimeMillis();
		measureTime=System.currentTimeMillis();
		sendStatusMessage();
		nextAgent=getNextAgent();
		while((measureTime-tempTime)<500)
		{
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
		measureTime=System.currentTimeMillis();
		}
	}
	
	
	public void move()
	{
		
		if(new String(nextAgent.type).equals("none"))
		{
			takeStep(currentDirection);
        }
		if(canTakeStep())
			takeStep(currentDirection);
		else{
		if(new String(nextAgent.type).equals("intersection"))
		{
			
			if(((new String(nextAgent.lightColor).equals("red")) && ((new String(currentDirection).equals("north")) || (new String(currentDirection).equals("south"))))
				|| ((new String(nextAgent.lightColor).equals("green")) && ((new String(currentDirection).equals("east")) || (new String(currentDirection).equals("west")))))
			{
				mode="stop";
			}
			if(((new String(nextAgent.lightColor).equals("green")) && ((new String(currentDirection).equals("north")) || (new String(currentDirection).equals("south"))))
				|| ((new String(nextAgent.lightColor).equals("red")) && ((new String(currentDirection).equals("east")) || (new String(currentDirection).equals("west")))))
			{
				if(canTakeStep())
					takeStep(currentDirection);
				else{
				mode="run";
				currentDirection=GetRandomDirection(currentDirection);
				x0=nextAgent.x;
				y0=nextAgent.y;
				x=x0;
				y=y0;
				System.out.println("next agent x= "+nextAgent.x);
				System.out.println("next agent y= "+nextAgent.y);
				startTime=System.currentTimeMillis();
				sendStatusMessage();
				try
			{
				TimeUnit.MILLISECONDS.sleep(500);
			}
			catch(Exception ex)
			{
			
			}
				nextAgent=getNextAgent();
				}
			}
		}
		if(new String(nextAgent.type).equals("car"))
		{
			if(canTakeStep())
			{
				takeStep(currentDirection);
			}
			else
			{
				mode="stop";
			}
		}
		}
	}
	public boolean canTakeStep()
	{
		boolean canTakeStep=false;
		switch(currentDirection)
		{
			case "north":
				canTakeStep=((nextAgent.y - y)>0.5);
				break;		
			case "south":
				canTakeStep=((nextAgent.y - y)<-0.5);
				break;
			case "east":
				canTakeStep=((nextAgent.x - x)>0.5);
				break;
			case "west":
				canTakeStep=((nextAgent.x - x)<-0.5);
				break;
			default:
			break;
		}			
return canTakeStep;
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
		return newDirection;
	}
	
	
	
	public AgentClass getNextAgent()
	{
		sendRequestMessage();
		System.out.println("Request sent");
		ACLMessage msg = myAgent.receive(informMessage);
		while(msg==null)
		{
			msg=myAgent.receive(informMessage);
		}
		System.out.println("Response received");
		
		return ParseAgent(msg);
		
	}
	
	public AgentClass ParseAgent(ACLMessage message)
	{
		String content=message.getContent();
		//System.out.println("CONTENT=" +content);
		String[] stringList = content.split(" ");
		for(String str:stringList)
		{
			System.out.println("STRING LIST ELEMENTS:" + str);
		}
		AgentClass agent=new AgentClass();
		agent.type=stringList[0];
		agent.x=Double.parseDouble(stringList[1]);
		agent.y=Double.parseDouble(stringList[2]);
		agent.direction=stringList[3];
		agent.lightColor=stringList[4];
		//agent.name=message.getSender().getLocalName();
		//agent.aid=message.getSender();
		return agent;
	}
	
	public boolean isNeedToMakePlaceForAmbulance(double x, double y, String currentDirection)
	{	
		//get type Request
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
		measureTime = System.currentTimeMillis();
		
		if (currentDirection == "north")
		{
			y =y0 + v*0.001*(measureTime-startTime);
		}
		if (currentDirection == "south")
		{
			y =y0 - v*0.001*(measureTime-startTime);
		}
		if (currentDirection == "east")
		{
			x = x0 + v*0.001*(measureTime-startTime);
		}
		if (currentDirection == "west")
		{
			x = x0 - v*0.001*(measureTime-startTime);
		}
	}
		
	void makePlaceForAmbulance(String currentDirection)
	{
		x0=x;
		y0=y;
		if (currentDirection == "north")
		{
			x = x0 + 1;
		}
		if (currentDirection == "south")
		{
			x = x0 - 1;
		}
		if (currentDirection == "east")
		{
			y = y0 + 1;
		}
		if (currentDirection == "west")
		{
			y = y0 - 1;
		}
	}
	void stop()
	{
		try
		{
			TimeUnit.MILLISECONDS.sleep(200);
		}
		catch(Exception ex)
		{
			
		}
		x0=x;
		y0=y;
		startTime=System.currentTimeMillis();
		mode="run";
		//ArrayList<ACLMessage> msgs=new ArrayList<ACLMessage>();
		//msgs = ReceiveMessages(requestMessage);
		//try
		//{
		//	msgs = ReceiveMessages(requestMessage);
		//}
		//catch(Exception ex)
		//{
			
		//}
		//if(msgs!=null && ParseAgent(msgs.get(1).getContent()).ambulance==null)
		//{
		//	x=x0;
		//	y=y0;
		//	startTime=System.currentTimeMillis();
		//	mode="run";
		//}
	}
	
	private ArrayList<ACLMessage> ReceiveMessages()
	{
		ArrayList<ACLMessage> msgs = new ArrayList<ACLMessage>();
		ACLMessage msg=myAgent.receive();
		while(msg!=null)
		{
		msgs.add(msg);
		msg = myAgent.receive();
		}
		return msgs;
	}
	private ArrayList<ACLMessage> ReceiveMessages(MessageTemplate template)
	{
		ArrayList<ACLMessage> msgs = new ArrayList<ACLMessage>();
		ACLMessage msg = myAgent.receive(template);
		while(msg!=null)
		{
		msgs.add(msg);
		msg = myAgent.receive(template);
		}
		return msgs;
	}
	public void sendStatusMessage()
	{
		ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
		msg.setContent("car "+ x +" "+y +" "+currentDirection + " null");
		msg.addReceiver(trafficManagerAID);
		myAgent.send(msg);
	}
	public void sendRequestMessage()
	{
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setContent("car "+ x +" "+y +" "+currentDirection + " null");
		msg.addReceiver(trafficManagerAID);
		myAgent.send(msg);
	}
	public void sendQueryIFMessage()
	{
		ACLMessage msg = new ACLMessage( ACLMessage.QUERY_IF);
		msg.setContent("car "+ x +" "+y +" "+currentDirection + " null");
		msg.addReceiver(trafficManagerAID);
		myAgent.send(msg);
	}
	public DFAgentDescription setTemplate(DFAgentDescription template,String AgentType)
	{
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentType);
		template.addServices(sd);
		return template;
	}		
	public AID[] GetAgents(DFAgentDescription template)
	{
		try
		{
			DFAgentDescription[] result = DFService.search(myAgent, template); 
			if(result==null)
				return new AID[0];
			AID[] tempAgents = new AID[result.length];
			for (int i = 0; i < result.length; ++i)
			{
				tempAgents[i] = result[i].getName();	
			}
			return tempAgents;
		}
		catch(FIPAException fe)
		{
			return null;
		}
	}
}