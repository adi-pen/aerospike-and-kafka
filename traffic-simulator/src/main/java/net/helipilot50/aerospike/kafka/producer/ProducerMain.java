package net.helipilot50.aerospike.kafka.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.Value;
import com.aerospike.client.Operation;
import com.aerospike.client.AerospikeException.Connection;
import com.aerospike.client.cdt.MapOperation;
import com.aerospike.client.cdt.MapPolicy;

import net.helipilot50.aerospike.kafka.Constants;

/**
 * Producer of Aerospike operations
 *
 */
public class ProducerMain extends TimerTask {

    private static final List<String> users = new ArrayList<String>() {
        private static final long serialVersionUID = 408064932585810852L;

        {
            add("bob-123");
            add("mary-456");
            add("sue-789");
            add("james-189");
            add("sarah-289");
            add("mark-281");
            add("marius-928");
            add("katja-928");
            add("pierre-921");
            add("michal-921");
            add("tim-1999-2019");
            add("peter-1999-2019");
        }
    };

    private static final List<String> sites = new ArrayList<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("http://some.place.com/app");
            add("http://some.place.com/app/catalog");
            add("http://some.place.com/app/catalog/bike");
            add("http://some.place.com/app/catalog/scooter");
            add("http://some.place.com/app/catalog/doll");
            add("http://some.place.com/app/catalog/teddybare");
            add("http://some.place.com/app//catalog/banana");
            add("http://some.place.com/app/catalog/pare");
            add("http://some.place.com/app/catalog/lemon");
            add("http://some.place.com/app/catalog/pineapple");
            add("http://some.place.com/app/catalog/lego");
            add("http://some.place.com/app/catalog/paint");
            add("http://some.place.com/app/catalog/cloth");
            add("http://some.place.com/app/catalog/blocks");
            add("http://some.place.com/app/catalog/bread");
            add("http://some.place.com/app/catalog/glue");
            add("http://some.place.com/app/catalog/arduino");
            add("http://some.place.com/app/catalog/arduino/uno");
            add("http://some.place.com/app/catalog/arduino/due");
            add("http://some.place.com/app/catalog/arduino/Mega");
            add("http://some.place.com/app/catalog/arduino/Leonardo");
            add("http://some.place.com/app/catalog/arduino/lilypad");
            add("http://some.place.com/app/catalog/pi/4");
            add("http://some.place.com/app/catalog/pi/3Aplus");
            add("http://some.place.com/app/catalog/pi/3Bplus");
            add("http://some.place.com/app/catalog/pi/zeroW");
            add("http://some.place.com/app/catalog/pi/3");
            add("http://some.place.com/app/catalog/pi/zero");
            add("http://some.place.com/app/catalog/pi/2");
            add("http://some.place.com/app/catalog/pi/Aplus");
            add("http://some.place.com/app/catalog/pi/B");
        }
    };

    private static String randomUser() {
        int index = (int) (Math.random() * users.size());
        return users.get(index);
    }

    private static String randomSite() {
        int index = (int) (Math.random() * sites.size());
        return sites.get(index);
    }

    public static void main(String[] args) {
        System.out.println("Aerospike Kafka Producer");
        ProducerMain producer = new ProducerMain();
        Timer timer = new Timer();
        timer.schedule(producer, 0, 500);
    }

    public AerospikeClient asClient;

    public ProducerMain() {
        int attempts = 0;
        boolean attemptConnection = true;
        while (attemptConnection) {
            attempts += 1;
            try {
                System.out.println("Connect to aerospike, attempt: " + attempts);
                this.asClient = new AerospikeClient(Constants.AEROSPILE_HOST, 3000);
                attemptConnection = false;
            } catch (Connection conn) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (attempts > 2) {
                    attemptConnection = false;
                    System.out.println("Cannot connect to aerospike, attempted: " + attempts);
                    throw conn;
                }
            }
        }
    }

    @Override
    public void run() {
        String user = randomUser();
        String siteUrl = randomSite();
        System.out.println(String.format("Visit: %s to %s", user, siteUrl));
        addVisit(user, siteUrl);
    }

    public void addVisit(String userId, String siteUrl) {

        Key recordKey = new Key(Constants.NAMESPACE, Constants.RECORD_SET, userId);

        Bin nameBin = new Bin(Constants.NAME_BIN, userId);

        asClient.operate(null, recordKey, Operation.put(nameBin),
        MapOperation.increment(
            MapPolicy.Default, 
            Constants.VISIT_BIN, 
            Value.get(siteUrl),
            Value.get(1)
            ));
        return;
    }

    /**
     * close the Aerospike client on process termination
     */
    protected void finalize() {
        this.asClient.close();
        this.asClient = null;
    }

}
