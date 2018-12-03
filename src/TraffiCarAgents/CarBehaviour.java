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
import TrafficarClasses.*;

public class CarBehaviour extends CyclicBehaviour 
{
	private String status;	
	private double x;
	private double y;
	private String currentDirection;
	private String mode;
	private Agent nextAgent;
	private String nextAgentmode;
	private String lightColor;
	private DFAgentDescription trafficManagerTemplate;
	private MessageTemplate requestMessage;
	private MessageTemplate informMessage;
	private long startTime;
	private long measureTime;	
	private double v;
	private boolean messageSent=0;
	
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
		trafficManagerTemplate = new DFAgentDescription();
		trafficManagerTemplate=setTemplate(trafficManagerTemplate,"TrafficManager");
		requestMessage=MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		informMessage=MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		startTime = System.currentTimeMillis();
		v=1
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
		if(nextAgent.Type) == null) 
		{
			takeStep(currentDirection);
        }
		if(nextAgent.Type) == "intersection") 
		{
			
			if(nextAgent.LightColor=="red")
			{
				mode="stop";
			}
			if(nextAgent.LightColor=="green")
			{
				mode="run";
				currentDirection=GetRandomDirection(currentDirection);
				x0=nextAgent.x;
				y0=nextAgent.y;
				takeStep(currentDirection);
				startTime=System.currentTimeMillis();
			}
        }
		if(nextAgent.Type) == "car")
		{
			if(abs(nextAgent.x - x)>10)
			{
				takeStep(currentDirection);
			}
			else
			{
				mode="stop";
			}
		}
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
	
	
	
	public AgentClass getNextAgent(int x,int y,String currentDirection)
	{
		//implement message query-inform and wait for response.
		SendQueryInformMessage();
		ACLMessage msg = new Message.Receive(Response)
		while(msg==null)
		{
			msg=Message.Receive(Response);
		}
		agentClass = ParseAgent(msg.getContent();
		return null;
	}
	
	public AgentClass ParseAgent(String content)
	{
		List<String> stringList = Arrays.asList(content().split(" "));
		AgentClass agent=new AgentClass();
		agent.type=stringList.get(1);
		agent.x=Double.parseDouble(stringList.get(2));
		agent.y=Double.parseDouble(stringList.get(3));
		agent.lightColor=stringList.get(4);
		return agent;
			
	}
	
	public boolean isNeedToMakePlaceForAmbulance(int x, int y, String currentDirection)
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
			x = x0 + v*0.001*(measureTime-startTime)
		}
		if (currentDirection == "west")
		{
			x = x0 - v*0.001*(measureTime-startTime)
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
		//send queryIf
		ACLMessage[] msgs=new ACLMessage[]();
		msgs[] = ReceiveMessages(responseTemplate);
		try
		{
			msgs = ReceiveMessages(responseTemplate);
		}
		catch(Exception ex)
		{
			
		}
		if(msgm!==null && ParseAgent(msg.getContent()).ambulance==False)
		{
			x=x0;
			y=y0;
			startTime=System.currentTimeMillis();
			mode="run";
		}
	}
	
	private ACLMessage[] ReceiveMessages()
	{
		ACLMessage[] msgs = new ACLMessage[]();
		ACLMessage msg=myAgent.Receive();
		while(msg!=null)
		{
		msgs.add(msg);
		msg = myAgent.receive()
		}
		return msgs;
	}
	private ACLMessage[] ReceiveMessages(MessageTemplate template)
	{
		ACLMessage[] msgs = new ACLMessage[]();
		ACLMessage msg = myAgent.receive(template);
		while(msg!=null)
		{
		msgs.add(msg);
		msg = myAgent.receive(template)
		}
		return msgs;
	}
	public DFAgentDescription setTemplate(DFAgentDescription template,String AgentType)
	{
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentType);
		template.addServices(sd);
		return template;
	}		
}