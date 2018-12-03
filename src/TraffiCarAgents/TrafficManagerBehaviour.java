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
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import TrafficarClasses.*;

public class TrafficManagerBehaviour extends CyclicBehaviour 
{
	private AID[] carAgents;
	private AID[] intersectionAgents;
	private AID[] ambulanceAgents;
	private DFAgentDescription carTemplate;
	private DFAgentDescription intersectionTemplate;
	private DFAgentDescription ambulanceTemplate;
	private MessageTemplate requestMessage;
	private MessageTemplate informMessage;
	private MessageTemplate informIfMessage;
	private ArrayList<AgentClass> agents;
	private long startTime;
	private long tempTime;
	private long currentTime;
	
	public TrafficManagerBehaviour(Agent a) 
	{
		super(a);
		carTemplate = new DFAgentDescription();
		intersectionTemplate = new DFAgentDescription();
		ambulanceTemplate = new DFAgentDescription();
		carTemplate=setTemplate(carTemplate,"car");
		intersectionTemplate=setTemplate(intersectionTemplate,"intersection");
		ambulanceTemplate=setTemplate(ambulanceTemplate,"ambulance");
		requestMessage=MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		informMessage=MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		informIfMessage=MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF);
		startTime = System.currentTimeMillis();
	}
	public void action()
	{
		carAgents=GetAgents(carTemplate);
		intersectionAgents=GetAgents(intersectionTemplate);
		ambulanceAgents=GetAgents(ambulanceTemplate);
		agents=new ArrayList<AgentClass>();
		tempTime=System.currentTimeMillis();
		currentTime=System.currentTimeMillis();
		while((currentTime-tempTime)<1000)
		{
		agents=ReceiveAgentState();
		if(ambulanceAgents!=null)
		{
			//HandleAmbulance();
		}
		//GetCarsRequests();
		currentTime=System.currentTimeMillis();
		}
		
		SaveAgentsStates();
	}
	
	public void sendMessageToCarAgent(AID carAID, String type,int x,int y)
	{
		ACLMessage msg = new ACLMessage( ACLMessage.REQUEST );
		msg.setContent("CHANGE STATUS");
		
		for (int i = 0; i < carAgents.length; ++i)
		{
			msg.addReceiver(carAgents[i]);
		}
		myAgent.send(msg);
		
	}
	public DFAgentDescription setTemplate(DFAgentDescription template,String AgentType)
	{
		ServiceDescription sd = new ServiceDescription();
		sd.setType(AgentType);
		template.addServices(sd);
		return template;
	}
	public ArrayList<AgentClass> ReceiveAgentState()
	{
		//ReceiveInformMessages
		ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();
		for(ACLMessage message:messages)
		{
			for(int i=0;i<agents.size();i++)
			{
				if(agents.get(i+1).aid==message.getSender())
				{
					agents.set(i+1,ParseAgent(message.getContent()));
					break;
				}	
			}
		}
		return agents;
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
	
	public AgentClass ParseAgent(String content)
	{
		String[] stringList = content.split(" ");
		AgentClass agent=new AgentClass();
		agent.type=stringList[0];
		agent.x=Double.parseDouble(stringList[1]);
		agent.y=Double.parseDouble(stringList[2]);
		agent.lightColor=stringList[3];
		return agent;
	}
	
	public void SaveAgentsStates()
	{
		for(AgentClass agent:agents)
		{
			try
			{
				PrintWriter outputStream=new PrintWriter(new FileOutputStream((agent.aid.getName() +".txt"), true));
				if(outputStream!=null)
				{			
					outputStream.println(agent.type +" "+agent.x +" "+agent.y +" "+agent.lightColor);
				}
				outputStream.close();
			}
			catch(FileNotFoundException ex)
			{}
		}
	}
}
		