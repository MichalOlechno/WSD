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
	private long lightChangingRatio=5000;
	
	public IntersectionAgentBehaviour(Agent a,double X,double Y) 
	{
		super(a);
		x=X;
		y=Y;
		lightColor="green";
		trafficManagerTemplate = new DFAgentDescription();
		trafficManagerTemplate=setTemplate(trafficManagerTemplate,"TrafficManager");
		requestMessage=MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		informMessage=MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		startTime = System.currentTimeMillis();
	}
	public void action()
	{
		startTime=System.currentTimeMillis();
		currentTime=System.currentTimeMillis();
		while((currentTime-startTime)<lightChangingRatio)
		{
			tempTime=System.currentTimeMillis();
			while((currentTime-tempTime)<500)
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
			//sendStatusMessage
			currentTime=System.currentTimeMillis();
		}
		changeLights();
	}
	
	public void changeLights()
	{
		if(lightColor=="red")
			lightColor="green";
		else
			lightColor="red";
	}
	public DFAgentDescription setTemplate(DFAgentDescription template,String AgentType)
	{
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentType);
		template.addServices(sd);
		return template;
	}		
}
		