package simulator;

import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import network.interfaces.IConnectionStream;
import drivers.dtos.CommandDto;

public class ArduinoGuiServer extends ArduinoServeur {
	
	private Map<Integer, PinWidget> widgetMap;
	private JFrame frame;
	private JPanel content;
	private int row;

	public ArduinoGuiServer(IConnectionStream stream) {
		super(stream);
		
		widgetMap = new HashMap<Integer, PinWidget>();
		
		this.frame = new JFrame();
		frame.setTitle("Arduino Gui Server");
		frame.setSize(250, 400);
		
		frame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			
			@Override
			public void windowClosing(WindowEvent e) {
				ArduinoGuiServer.this.stopListening();
			}
			
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
		});
		
		content = new JPanel();
		frame.setContentPane(content);
		content.setLayout(new GridBagLayout());
		row = 0;
	}
	
	@Override
	public void run() {
		frame.setVisible(true);
		super.run();
	}

	@Override
	protected void addPin(int id, boolean isAnalogicReadable,
			boolean isAnalogicWritable) {
		super.addPin(id, isAnalogicReadable, isAnalogicWritable);
		PinWidget widget = new PinWidget(super.pinMap.get(id), content, row /2 , (row % 2)*3);
		row ++;
		widgetMap.put(id, widget);
	}

	@Override
	protected void handleDto(CommandDto dto) throws IOException {
		super.handleDto(dto);
		PinWidget widget = widgetMap.get(dto.pinId & 0xff);
		if(widget!=null) {
			widget.update();
		}
	}

	@Override
	public void stopListening() {
		super.stopListening();
		this.frame.setVisible(false);
		this.frame.dispose();
	}
}
