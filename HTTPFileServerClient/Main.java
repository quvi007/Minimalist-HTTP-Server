import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String host = "localhost";
		int port = 5007;
		while (true) {
			String fileToUpload = scanner.nextLine();
			Thread worker = new Worker(host, port, fileToUpload);
			worker.start();
		}
	}
}
