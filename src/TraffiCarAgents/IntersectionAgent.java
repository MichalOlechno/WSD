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
import TraffiCarBehaviours2.*;
import TraffiCarBehaviours.*;


public class IntersectionAgent extends Agent {

	private int x;
	private int y;
	private String currentDirection;
	protected void setup() {
		x=0;
		y=10;
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("intersection");
		sd.setName("intersectionAgent");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		IntersectionAgentBehaviour intersectionBehaviour = new IntersectionAgentBehaviour(this,x,y);
		addBehaviour(intersectionBehaviour);
		 
		System.out.println("Hallo! IntersectionAgent "+getAID().getName()+" is ready.");
	}

	protected void takeDown() {
	    // Printout a dismissal message
	    System.out.println("IntersectionAgent "+getAID().getName()+" terminating.");
	  }
	
}