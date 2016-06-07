package src.mas_lab;

import java.util.TreeMap;
import java.util.Vector;

import org.json.simple.JSONObject;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;

import src.people.Chose;
import src.people.Evaluate;
import src.people.Person;
import src.people.Search;

public class GlobalReceiver extends CyclicBehaviour {
	
	Vector<DFAgentDescription> allRestaurants;
	Vector<AID> allPeople;
	Vector<DFAgentDescription> currentRestaurant;
	Vector<AID> currentPeople;
	DFAgentDescription[] allRArray;
	int PeopleReceived;
	int RestaurantReceived;
	JSONObject peopleSetup;
	JSONObject restaurantsSetup;

	public GlobalReceiver(Agent a, DFAgentDescription[] allPeople, DFAgentDescription[] allRestaurants) {
		super(a);
		this.myAgent = a;
		this.allRestaurants = new Vector<DFAgentDescription>();
		this.allPeople = new Vector<AID>();
		
		allRArray = allRestaurants;
		
		for(DFAgentDescription dfd : allPeople)
			if(dfd != null)
				this.allPeople.add(dfd.getName());
		for(DFAgentDescription dfd : allRestaurants)
			if(dfd != null)
				this.allRestaurants.add(dfd);
		
		
		reset();
		
		peopleSetup = new JSONObject();
		restaurantsSetup = new JSONObject();
		
	}
	
	public GlobalReceiver(Agent a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			//System.out.println("Receive loop");
			block();
		} else {
			switch (msg.getPerformative()) {
				case (ACLMessage.INFORM):{
					//System.out.println("Received inform from " + msg.getSender().getLocalName());
					//System.out.println(currentTarget);
					
					if(currentPeople.contains(msg.getSender())){
						//System.out.println("Received inform");
						currentPeople.remove(msg.getSender());
						PeopleReceived ++;
						
						if(PeopleReceived == allPeople.size()){
							if(((Global)myAgent).turn < Main.MaxTurns){
								try{
									myAgent.addBehaviour(new Log(allRArray,	msg.getSender(),
											(TreeMap<AID, Double>)msg.getContentObject(), false));
									//block();
								} catch(Exception e) {
									e.printStackTrace();
								}
								reset();
							} else {
								try{
									myAgent.addBehaviour(new Log(allRArray, msg.getSender(), 
											(TreeMap<AID, Double>)msg.getContentObject(), true));
									//System.out.println("printing log");
									//block();
								} catch(UnreadableException e) {
									e.printStackTrace();
								}
								//System.exit(0);
							}
						}
						else{
							try{
								myAgent.addBehaviour(new Log(allRArray,	msg.getSender(),
										(TreeMap<AID, Double>)msg.getContentObject(), false));
								//block();
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
						
						
					}
					break;
				}
				
				case (ACLMessage.CONFIRM): {
					String sender = msg.getSender().getLocalName();
					JSONParser parser = new JSONParser();
					try {
						//JSONObject obj = (JSONObject)parser.parse(msg.getContent());
						Object obj = parser.parse(msg.getContent());
						if(sender.split(" ")[0].equals("Person")){
							peopleSetup.put(sender, obj);
						}
						else if (sender.split(" ")[0].equals("Restaurant")){
							restaurantsSetup.put(sender, obj);
						}
					} catch(ParseException e) {
						e.printStackTrace();
					}
					
					if(peopleSetup.size() == ((Global)myAgent).nPeople)
						System.out.println(peopleSetup);
					
					if(restaurantsSetup.size() == ((Global)myAgent).nRestaurants)
						System.out.println(restaurantsSetup);
					
					break;
				}
				
				default:
					block();
			}
		}
	}
	
	public void reset(){
		currentRestaurant = (Vector<DFAgentDescription>)allRestaurants.clone();
		currentPeople = (Vector<AID>)allPeople.clone();
		System.out.println(currentPeople.size());
		PeopleReceived=0;
		RestaurantReceived=0;
		((Global) myAgent).addBehaviour(new NewDayBCast(myAgent));
	
		//System.out.println(this.myAgent.getLocalName());
	}
	
}
