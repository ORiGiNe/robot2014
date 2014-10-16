package network.serial;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tools.DelayExecution;

/**
 * Methode de recherche d'arduino par leur nom (case insensitive)
 * 
 * @author Bidouille
 * 
 */
public class ArduinoFinder {

	public static int TIME_OUT = 2000;

	private static Map<String, ArduinoSerialConnection> connectionMap = null;

	/**
	 * Obtient une connection d'arduino par le nom de la carte distante (La
	 * première execution de cette methode lance la détection d'arduino)
	 * 
	 * @param name
	 *            nom de la carte (case insensitive)
	 * @return la connection associée ou null si la carte n'a pas été détectée
	 * @throws Exception
	 */
	public static ArduinoSerialConnection getArduinoByName(String name)
			throws Exception {
		detectArduino();
		return connectionMap.get(name.toLowerCase());
	}

	/**
	 * Tente d'ouvrir des ArduinoSerialConnection sur tous les ports série
	 * disponibles, et ajoute les arduino trouvées à la map. Les nom des arduino
	 * sont case insensitive
	 * 
	 * @throws Exception
	 */
	private static void detectArduino() throws Exception {
		if (connectionMap == null) {
			System.out.println("# Detecting arduinos (TIME_OUT : "+TIME_OUT+" ms) :");
			connectionMap = new HashMap<String, ArduinoSerialConnection>();

			List<String> listPort = SerialConnection.getPortList();
			System.out.print("# - looking for ports :");
			for(String name : listPort) {
				System.out.print(" "+name);
			}
			System.out.println();
			final List<Tlisten> connectionThreads = new LinkedList<Tlisten>();

			// tente d'ouvrir une connection avec une arduino sur tous les ports
			// série
			for (String portName : listPort) {
				Tlisten t = new Tlisten(portName);
				connectionThreads.add(t);
				t.start();
			}

			DelayExecution delay = new DelayExecution(new Runnable() {
				@Override
				public void run() {
					for (Tlisten t : connectionThreads) {
						if (t.isAlive()) {
							t.interrupt();
						}
					}
				}
			});

			// coupe tous les thread d'écoutes après TIME OUT
			delay.start(TIME_OUT);

			// Attends la fin de tous les thread (connection ouverte ou
			// interrupted par time out)
			for (Tlisten t : connectionThreads) {
				if (t.isAlive()) {
					t.join();
				}
			}

			delay.stop();

			// ajoute les arduino trouvées à la map
			for (Tlisten t : connectionThreads) {
				if (t.connection != null) {
					connectionMap.put(t.connection.getName().toLowerCase(),
							t.connection);
				}
			}
			System.out.println("# Detection complete\n");
		}
	}

	public static void closeAllConnection() {
		if (connectionMap != null) {
			for (Map.Entry<String, ArduinoSerialConnection> entry : connectionMap
					.entrySet()) {
				try {
					entry.getValue().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class Tlisten extends Thread {
		private String portName;
		private ArduinoSerialConnection connection;

		public Tlisten(String portName) {
			this.portName = portName;
			this.connection = null;
		}

		@Override
		public void run() {
			try {
				this.connection = new ArduinoSerialConnection(this.portName);
				System.out.println("#    Arduino found on " + this.portName
						+ " : \"" + this.connection.getName() + "\"");
			} catch (IOException e) {
				System.out.println("#    No arduino found on " + this.portName);
			}
		}
	}

}
