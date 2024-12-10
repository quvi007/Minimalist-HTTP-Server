import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static String name = "FileServer";
    private static String errorFile = "res/error.html";
    private static String favicon = "res/favicon.ico";
    private static final int PORT = 5007;
    public static int clientCount = 0;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server up and running...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            Thread worker = new Worker(clientSocket);
            worker.start();
        }
    }

	public static String getErrorFile() {
		return errorFile;
	}
	
	public static String getFavicon() {
		return favicon;
	}

    public static String getName() {
        return name;
    }
}
