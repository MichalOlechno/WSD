package TraffiCarBehaviours;

import jade.core.*;
import jade.core.behaviours.*;
import java.util.concurrent.TimeUnit;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import TrafficarClasses.*;

public class IntersectionAgentBehaviour extends CyclicBehaviour 
{
	private DFAgentDescription trafficManagerTemplate;
	private MessageTemplate requestMessage;
	private MessageTemplate informMessage;
	private String lightColor;
	private double x;
	private double y;
	private long startTime;
	private long tempTime;
	private long currentTime;
	private long lightChangingRatio=30000;
	private AID trafficManagerAID;
	
	public IntersectionAgentBehaviour(Agent a,double X,double Y) 
	{
		super(a);
		x=X;
		y=Y;
		lightColor="red";
		trafficManagerTemplate = new DFAgentDescription();
		trafficManagerTemplate=setTemplate(trafficManagerTemplate,"TrafficManager");
		requestMessage=MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		informMessage=MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		startTime = System.currentTimeMillis();
		trafficManagerAID=GetAgents(trafficManagerTemplate)[0];
		
	}
	public void action()
	{
		startTime=System.currentTimeMillis();
		currentTime=System.currentTimeMillis();
		while((currentTime-startTime)<lightChangingRatio)
		{
			tempTime=System.currentTimeMillis();
			while((currentTime-tempTime)<1000)
			{
				//ACLMessage msg = ReceiveRequestMessage
				ACLMessage msg =null;
				if(msg!=null)
				{
					lightColor="red";
					startTime=System.currentTimeMillis();
				}
				currentTime=System.currentTimeMillis();
			}
			sendStatusMessage();
			currentTime=System.currentTimeMillis();
		}
		changeLights();
	}
	public void sendStatusMessage()
	{
		ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
		msg.setContent("intersection "+ x +" "+y +" null "+lightColor);
		msg.addReceiver(trafficManagerAID);
		myAgent.send(msg);
	}
	
	
	public void changeLights()
	{
		if(lightColor=="red")
			lightColor="green";
		else
			lightColor="red";
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
	public DFAgentDescription setTemplate(DFAgentDescription template,String AgentType)
	{
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentType);
		template.addServices(sd);
		return template;
	}		
}
		