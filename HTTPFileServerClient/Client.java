import java.io.*;
import java.net.Socket;

public class Client {
	private String host;
	private int port;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	
	public Client(String host, int port) throws IOException {
		this.host = host;
		this.port = port;
		connect();
	}
	
	private void connect() throws IOException {
		socket = new Socket(host, port);
		is = socket.getInputStream();
		os = socket.getOutputStream();
	}
	
	public void upload(String path) throws IOException {
		File file = new File(path);
		String ext = FileUtility.getExtension(path);		
		BufferedOutputStream bos = new BufferedOutputStream(os);
		bos.write(("UPLOAD " + file.getName().replaceAll(" ", "%20") + "\r\n").getBytes());
		String errorMsg = "";
		if (!file.isFile()) {
			errorMsg = "Error: file not found.";
		} else if (!(ext.equals("txt") || ext.equals("jpg") || ext.equals("png") || ext.equals("mp4"))) {
			errorMsg = "Error: file format is invalid.";
		}
		
		if (!errorMsg.equals("")) {
			System.out.println(errorMsg);
			bos.write((errorMsg + "\r\n\r\n").getBytes());
			bos.flush();
		} else {
			bos.write("\r\n".getBytes());
			bos.flush();
			
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] chunk;
			String chunkSizeStr, lineBreak = "\r\n";
			while ((chunk = bis.readNBytes(1024)).length != 0) {
				chunkSizeStr = Integer.toString(chunk.length, 16).toUpperCase();
				bos.write(chunkSizeStr.getBytes());
				bos.write(lineBreak.getBytes());
				bos.write(chunk);
				bos.write(lineBreak.getBytes());
				bos.flush();
			}
			chunkSizeStr = "0";
			bos.write(chunkSizeStr.getBytes());
			bos.write(lineBreak.getBytes());
			bos.write(lineBreak.getBytes());
			bos.flush();
			bis.close();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		System.out.println(br.readLine());
		socket.close();
	}
}
