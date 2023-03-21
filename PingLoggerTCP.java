import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;

public class PingLoggerTCP {

    private static final String HOST = "4chan.org";
    private static final int PORT = 80;
    private static final int NUM_PINGS = 10;

    public static void main(String[] args) {
        LinkedList<Long> responseTimes = new LinkedList<>();

        for (int i = 0; i < NUM_PINGS; i++) {
            long startTime = System.currentTimeMillis();
            boolean isSuccess;
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(HOST, PORT), 3000);
                isSuccess = true;
            } catch (IOException e) {
                isSuccess = false;
            }
            long endTime = System.currentTimeMillis();

            if (isSuccess) {
                long responseTime = endTime - startTime;
                responseTimes.add(responseTime);
                System.out.println("Pinging: " + HOST + " " + (i+1) + ": " + responseTime + "ms");
            } else {
                System.out.println("Ping " + (i+1) + ": failed");
            }
        }

        Collections.sort(responseTimes);

        try {
            FileWriter writer = new FileWriter("pinglog.txt");
            for (int i = 0; i < Math.min(NUM_PINGS, responseTimes.size()); i++) {
                writer.write("Ping " + (i+1) + ": " + responseTimes.get(i) + "ms\n");
            }
            if (responseTimes.size() >= NUM_PINGS) {
                writer.write("DONE!");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}