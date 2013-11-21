package pubsub;

import java.util.StringTokenizer;

import siena.Filter;
import siena.Notification;
import siena.Op;
import siena.SienaException;
import siena.ThinClient;
import siena.comm.InvalidSenderException;

public class SienaPubSubEngine implements PubSubEngine{
	
	private ThinClient SienaClient;
	private String ClientID;
	private String BrokerID;
	private Filter AdvFilter;
	private SienaSubscriber Subscriber;
	
	public int start(String ClientID, String BrokerID) {
		this.ClientID = ClientID;
		this.BrokerID = BrokerID;
		
		return 0;
	}

	public int connect(String host, int port) {
		try {
			String uri = "ka:"+host+":"+port;
			SienaClient = new ThinClient (uri,ClientID);
		} catch (InvalidSenderException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	public void addConstraint (String predicates){
		
		StringTokenizer strtok = new StringTokenizer(predicates,",");
		AdvFilter = new Filter();
		
		while(strtok.hasMoreElements()){
			String key = strtok.nextToken().replace("[","").replace("]","").replace("'","");
			//System.out.println("Key: " + key);
			
			String operator = strtok.nextToken().replace("[","").replace("]","").replace("'","");
			//System.out.println("Op: " + operator);
			
			String value = strtok.nextToken().replace("[","").replace("]","").replace("'","");
			//System.out.println("Value: " + value);
				
				if(operator.equals("eq"))
					AdvFilter.addConstraint(key, Op.EQ, value);
				else if (operator.equals("<"))
					AdvFilter.addConstraint(key, Op.LT, value);
				else if (operator.equals(">"))
					AdvFilter.addConstraint(key, Op.GT, value);
				else if (operator.equals("<="))
					AdvFilter.addConstraint(key, Op.LE, value);
				else if (operator.equals(">="))
					AdvFilter.addConstraint(key, Op.GE, value);
				else
					AdvFilter.addConstraint(key,Op.ANY,value);
		}
	}

	public int advertise(String predicates) {
		
	//addConstraint (predicates);

//		try {
//			SienaClient.advertise(AdvFilter, ClientID);
//			System.out.println("AdvConstraints: " + AdvFilter.toString());
//		} catch (SienaException e) {
//			e.printStackTrace();
//		}
//		
		return 0;
	}

	public int unadvertise(int id) {
		
		return 0;
	}

	public void subscribe(String predicates) {

		addConstraint (predicates);
		try {
			Subscriber = new SienaSubscriber();
			SienaClient.subscribe(AdvFilter, Subscriber);
		} catch (SienaException e) {
			e.printStackTrace();
		}

	}

	public int unsubscribe(int id) {
		return 0;
	}

	public int publish(String predicates) {
		
		Notification n = new Notification();
		 
		StringTokenizer strtok = new StringTokenizer(predicates,",");
		
		while(strtok.hasMoreElements()){			
			String key = strtok.nextToken().replace("[","").replace("]","").replace("'","");
			String value = strtok.nextToken().replace("[","").replace("]","").replace("'","");
			n.putAttribute(key, value);
		}
		
		//System.out.println(n.toString());
			
		 try {
			SienaClient.publish(n);
		} catch (SienaException e) {
			e.printStackTrace();
		}
		    
		 return 0;
	}
	

	public int registerListener() {
		// TODO Auto-generated method stub
		return 0;
	}


	public int disconnect() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int stop() {
		// TODO Auto-generated method stub
		return 0;
	}

}
