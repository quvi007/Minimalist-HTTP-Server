import java.io.IOException;

public class Worker extends Thread {
	private String host;
	private int port;
	private String fileToUpload;
	
	@Override
	public void run() {
		try {
			Client client = new Client(host, port);
			client.upload(fileToUpload);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Worker(String host, int port, String fileToUpload) {
		this.host = host;
		this.port = port;
		this.fileToUpload = fileToUpload;
	}
}
