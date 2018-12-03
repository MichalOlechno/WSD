package TraffiCarAgents;

import jade.core.*;
import jade.core.behaviours.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import TraffiCarBehaviours.*;

public class TrafficManagerAgent extends Agent {

	private int x;
	  
	protected void setup()
	{
		TrafficManagerBehaviour trafficManager = new TrafficManagerBehaviour(this);
		addBehaviour(trafficManager);
	}

	protected void takeDown()
	{
	    // Printout a dismissal message
	    System.out.println("CarAgent "+getAID().getName()+" terminating.");
	}
	
}
