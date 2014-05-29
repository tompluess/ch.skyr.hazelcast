package ch.skyr.hazelcast.hello;

import java.util.concurrent.ConcurrentMap;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class DistributedMap {
    private static final String MAP_NAME             = "my-distributed-map";
    private static final int    RUN_DURATION_SECONDS = 30;

    public static void main(final String[] args) throws InterruptedException {

        final String instanceName = "testInstance";
        final String groupName = "groupName";

        final Config config = new Config();
        // config.setInstanceName(instanceName );
        config.setGroupConfig(new GroupConfig(groupName, "lol"));
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
        config.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1");
        // config.getNetworkConfig().getInterfaces().clear();
        // config.getNetworkConfig().getInterfaces().addInterface("127.0.0.1");
        // config.getNetworkConfig().getInterfaces().setEnabled(true);

        final HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        final ConcurrentMap<String, String> map = h.getMap(MAP_NAME);
        map.put("key", "value");
        map.get("key");
        // Concurrent Map methods
        map.putIfAbsent("somekey", "somevalue");
        map.replace("key", "value", "newvalue");

        //
        map.put("ts-" + System.currentTimeMillis(), "bla");

        final long runDurationFinished = System.currentTimeMillis() + RUN_DURATION_SECONDS * 1000;
        while (runDurationFinished > System.currentTimeMillis()) {
            printMap(h);
            Thread.sleep(3000);
        }
        System.out.println("run duration finished.");
        h.shutdown();
    }

    private static void printMap(final HazelcastInstance h) {
        final ConcurrentMap<String, String> map = h.getMap(MAP_NAME);
        System.out.println("map: " + map);
        System.out.println("keys: " + map.keySet());
    }
}