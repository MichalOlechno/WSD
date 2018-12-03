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

public class TrafficManagerBehaviour extends CyclicBehaviour 
{
	private AID[] carAgents;
	public TrafficManagerBehaviour(Agent a) 
	{
		super(a);
	}
		
	public void action()
	{
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("car");
		template.addServices(sd);
		System.out.println("Flag 1");
		try 
		{
			DFAgentDescription[] result = DFService.search(myAgent, template); 
			carAgents = new AID[result.length];
			System.out.println("Flag 2");
			for (int i = 0; i < result.length; ++i)
			{
				carAgents[i] = result[i].getName();
				System.out.println(carAgents[i].getName());	
			}
			//send message to car Agents
			ACLMessage msg = new ACLMessage( ACLMessage.REQUEST );
			msg.setContent("CHANGE STATUS");
			for (int i = 0; i < result.length; ++i)
			{
				msg.addReceiver(carAgents[i]);
			}
			myAgent.send(msg);
		}
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}
		try
		{
			TimeUnit.SECONDS.sleep(5);
		}
		catch(Exception ex)
		{
			
		}
	}
}
		