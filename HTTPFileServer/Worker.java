import java.io.*;
import java.net.Socket;

public class Worker extends Thread {
    private final Socket clientSocket;
    private final InputStream is;
    private final OutputStream os;
    @Override
    public void run() {
        Request request = new Request(is); // blocking call
        Response response = new Response(os);
        if (request.empty()) return;
        if (request.isGETRequest()) {
            String path = request.getPath();
            String localUrl = Server.getName() + path;
            if (path.equals("/favicon.ico"))
            	response.renderFavicon(Server.getFavicon());
            else if (Utility.isFilePath(localUrl)) {
                String ext = Utility.getExtension(localUrl);
                if (ext.equals("txt") || ext.equals("png") || ext.equals("jpg"))
                    response.renderFile(localUrl);
//                else if (ext.equals("html")) {
//                    response.renderHTML(localUrl);
//                }
                else
                    response.downloadFile(localUrl);
            } else if (Utility.isDirPath(localUrl))
                response.renderHTML(Renderer.render(path, request.getHost()));
            else response.renderError(Server.getErrorFile());
        }
        else if (request.isUPLOADRequest()) {
        	String requestHeader = request.getHeader();
        	String[] requestHeaderLines = requestHeader.split("\n");
        	if (requestHeaderLines.length > 1 && requestHeaderLines[1].startsWith("Error")) {
        		String errorMsg = requestHeaderLines[1];
        		System.out.println(errorMsg);
        		response.sendText("File not uploaded.");
        		System.out.println("File not uploaded.");
        	} else {
		        try {
		            request.saveUploadedFile(Server.getName() + "/uploaded");
		            response.sendText("File uploaded: " + request.getPath());
		            System.out.println("File uploaded: " + request.getPath());
		        } catch (IOException e) {
//	                e.printStackTrace();
		        }
            }
        } else {
            response.renderError(Server.getErrorFile());
        }
        
        try {
            clientSocket.close();
            Utility.writeToFile("logs/client-" + (++Server.clientCount) + ".txt", "Request Header:\n" + request.getHeader() + "\n\n" + "Response Header:\n" + response.getHeader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Worker(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.is = clientSocket.getInputStream();
        this.os = clientSocket.getOutputStream();
    }
}
