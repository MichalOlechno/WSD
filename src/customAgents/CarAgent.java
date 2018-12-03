package TrafiCarAgents;

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
import customBehaviours.*;


public class CarAgent extends Agent {

	private int x;
	  
	protected void setup() {
		  x=0;
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("car");
		sd.setName("carAgent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		 
		System.out.println("Hallo! CarAgent "+getAID().getName()+" is ready.");
	}

	protected void takeDown() {
	    // Printout a dismissal message
	    System.out.println("CarAgent "+getAID().getName()+" terminating.");
	  }
	
}
