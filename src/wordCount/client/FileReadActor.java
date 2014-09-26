package wordCount.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import akka.actor.UntypedActor;

public class FileReadActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof String) {
			String fileName = (String) message;
			System.out.println("fileName =" + fileName);
			// start to read file 
			long start = System.currentTimeMillis();
			
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(Thread.currentThread()
								.getContextClassLoader().getResource(fileName)
								.openStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					//System.out.println("File contents->" + line);
					getSender().tell(line);
				}
				System.out.println("All lines send !");
				// send the EOF message..
				getSender().tell(String.valueOf("EOF"));
				long end = System.currentTimeMillis();
				long timeSpent  = end - start;
				System.out.println("Read file time spent: " + timeSpent + " ms");
			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
			}
		} else
			throw new IllegalArgumentException("Unknown message [" + message
					+ "]");
	}
}