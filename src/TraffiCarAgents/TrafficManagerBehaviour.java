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
	private double writeTime;
	
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
		agents=new ArrayList<AgentClass>();
	}
	public void action()
	{
		carAgents=GetAgents(carTemplate);
		intersectionAgents=GetAgents(intersectionTemplate);
		ambulanceAgents=GetAgents(ambulanceTemplate);
		tempTime=System.currentTimeMillis();
		currentTime=System.currentTimeMillis();
		while((currentTime-tempTime)<2000)
		{
		agents=ReceiveAgentState();
		if(ambulanceAgents!=null)
		{
			//HandleAmbulance();
		}
		HandleCarsRequests();
		currentTime=System.currentTimeMillis();
		}
		System.out.println("TrafficManager lives, agent size ="+agents.size());
		SaveAgentsStates();
	}
	
	public void sendMessageToCarAgent(AgentClass carAgent,AgentClass requestedAgent)
	{
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent(requestedAgent.aid +" "+ requestedAgent.type + " " + requestedAgent.x +" "+requestedAgent.y +" "+requestedAgent.direction + " null");
		msg.addReceiver(carAgent.aid);
		myAgent.send(msg);
		System.out.println("message sent to" + carAgent.aid);
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
		ArrayList<ACLMessage> messages = GetMessages(informMessage);
		boolean isNewElement=true;
		for(ACLMessage message:messages)
		{
			
			for(int i=0;i<agents.size();i++)
			{

				if(new String(agents.get(i).name).equals(message.getSender().getLocalName()))
				{
					//System.out.println("Existing agent in list i=" +i);
					agents.set(i,ParseAgent(message));
					
					isNewElement=false;
					break;
				}
			}
			if(isNewElement)
			{
				agents.add(ParseAgent(message));
				//System.out.println("Added");
			}
			isNewElement=true;
		}
		return agents;
	}
	public ArrayList<ACLMessage> GetMessages(MessageTemplate template)
	{
		ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();
		ACLMessage msg = myAgent.receive(template);
		
		while(msg!=null)
		{
			//System.out.println("TrafficManager gets a message !!!");
			messages.add(msg);
			msg=myAgent.receive(requestMessage);
		}
		return messages;
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
	
	public AgentClass ParseAgent(ACLMessage message)
	{
		String content=message.getContent();
		//System.out.println("CONTENT=" +content);
		String[] stringList = content.split(" ");
		//for(String str:stringList)
		//{
		//	System.out.println("STRING LIST ELEMENTS:" + str);
		//}
		AgentClass agent=new AgentClass();
		agent.type=stringList[9];
		agent.x=Double.parseDouble(stringList[10]);
		agent.y=Double.parseDouble(stringList[11]);
		agent.direction=stringList[12];
		agent.lightColor=stringList[13];
		agent.name=message.getSender().getLocalName();
		agent.aid=message.getSender();
		return agent;
	}
	public void HandleCarsRequests()
	{
		ArrayList<ACLMessage> messages=GetMessages(requestMessage);
		ArrayList<AgentClass> carAgents=GetAgentsFromMessage(messages);
		AgentClass tempAgent;
		for(AgentClass carAgent:carAgents)
		{
			System.out.println("someCar");	
			double value=1000;
			AgentClass requestingAgent=new AgentClass();
			requestingAgent.type="none";
			requestingAgent.x=carAgent.x;
			requestingAgent.y=carAgent.y;
			requestingAgent.direction=carAgent.direction;
			requestingAgent.lightColor=carAgent.lightColor;
			
			for(AgentClass agent:agents)
			{
				if(new String(carAgent.direction).equals(agent.direction))
				{
					tempAgent=calculateRelevantDistance(carAgent,agent,value);
					if(tempAgent!=null)
						requestingAgent=tempAgent;
				}
			}
			sendMessageToCarAgent(carAgent,requestingAgent);
			
		}	
	}
	public AgentClass calculateRelevantDistance(AgentClass carAgent,AgentClass agent,double value)
	{
		double temp;
		if(((new String(agent.direction).equals("north")) || (new String(agent.direction).equals("null"))) &&  carAgent.x==agent.x)
		{
			if(agent.y >carAgent.y && (agent.y - carAgent.y)<value)
			{
				value=(agent.y - carAgent.y);
				return agent;
			}
		}
		if(((new String(agent.direction).equals("south")) || (new String(agent.direction).equals("null"))) &&  carAgent.x==agent.x)
		{
			if(agent.y <carAgent.y && (carAgent.y - agent.y)<value)
			{
				value=(carAgent.y - agent.y);
				return agent;
			}
		}
		if(((new String(agent.direction).equals("east")) || (new String(agent.direction).equals("null"))) &&  carAgent.y==agent.y)
		{
			if(agent.x >carAgent.x && (agent.x - carAgent.x)<value)
			{
				value= (agent.x - carAgent.x);
				return agent;
			}
		}
		if(((new String(agent.direction).equals("west")) || (new String(agent.direction).equals("null"))) &&  carAgent.y==agent.y)
		{
			if(agent.x <carAgent.x && (carAgent.x - agent.x)<value)
			{
				value= (carAgent.x - agent.x);
				return agent;
			}
		}
		return null;
		
	}
	
	public ArrayList<AgentClass> GetAgentsFromMessage(ArrayList<ACLMessage> messages)
	{
		ArrayList<AgentClass> carAgents=new ArrayList<AgentClass>();
		boolean isNewElement=true;
		for(ACLMessage message:messages)
		{
			for(int i=0;i<carAgents.size();i++)
			{

				if(new String(carAgents.get(i).name).equals(message.getSender().getLocalName()))
				{
					carAgents.set(i,ParseAgent(message));
					
					isNewElement=false;
					break;
				}
			}
			if(isNewElement)
			{
				carAgents.add(ParseAgent(message));
				//System.out.println("Added");
			}
			isNewElement=true;
		}
		return carAgents;
	}
	
	public void SaveAgentsStates()
	{
		writeTime=(double)((System.currentTimeMillis()-startTime))/1000;
		for(AgentClass agent:agents)
		{
			try
			{
				PrintWriter outputStream=new PrintWriter(new FileOutputStream((agent.name +".txt"), true));
				//System.out.println("File names:" +agent.name);
				if(outputStream!=null)
				{			
					outputStream.println(writeTime +" " + agent.type +" "+agent.x +" "+agent.y +" "+agent.direction +" "+agent.lightColor);
				}
				outputStream.close();
			}
			catch(FileNotFoundException ex)
			{}
		}
	}
}
		