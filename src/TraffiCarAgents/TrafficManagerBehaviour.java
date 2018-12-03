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
	private AgentClass[] agents;
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
		agents=new AgentClass[]();
		tempTime=System.currentTimeMillis();
		currentTime=System.currentTimeMillis();
		while((currentTime-tempTime)<1000)
		{
		agents=ReceiveAgentState();
		if(ambulance!=null)
			HandleAmbulance();
		GetCarsRequests();
		currentTime=System.currentTimeMillis();
		}
		
		SaveAgentsStates();
	}
	
	public void sendMessageToCarAgent(AID carAID, String type,int x,int y)
	{
		ACLMessage msg = new ACLMessage( ACLMessage.REQUEST );
		msg.setContent("CHANGE STATUS");
		for (int i = 0; i < result.length; ++i)
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
	public AgentClass[] ReceiveAgentState()
	{
		//ReceiveInformMessages
		foreach(message in messages)
		{
			foreach(int i=0;i<agents.length;i++)
			{
				if(agent[i].AID=message.AID)
				{
					agent[i]=ParseAgent(message.getContent())
					break;
				}	
			}
		}
		return agents;
	}
	

	public AID[] GetAgents(DFAgentDescription template); 
	{
		result = DFService.search(myAgent, template); 
		if(result==null)
			return new AID[0];
		agents = new AID[result.length];
		for (int i = 0; i < result.length; ++i)
		{
			agents[i] = result[i].getName();	
		}
		return agents;
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
	
	public void SaveAgentsStates()
	{
		foreach(agent in agents)
		{
			PrintWriter outputStream=new PrintWriter(new FileOutputStream((agent.getName() +".txt"), true));
			if(outputStream!=null)
			{			
				outputStream.println(agent.type +" "+agent.x +" "+agent.y +" "+agent.light);
			}
			outputStream.close();
		}
	}
}
		