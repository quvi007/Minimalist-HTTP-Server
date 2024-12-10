import java.io.*;
import java.util.Date;

public class Response {
    private final OutputStream os;
    private String header = "";
    private byte[] body;
    public Response(OutputStream os) {
        this.os = os;
    }

    public String getHeader() {
        return header;
    }

    private void sendResponse() {
        BufferedOutputStream bos = new BufferedOutputStream(os);
        byte[] headerBytes = header.getBytes();
        try {
            bos.write(headerBytes);
            bos.write(body);
            bos.flush();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public void renderHTML(byte[] dataBytes) {
        body = dataBytes;
        header = "HTTP/1.1 200 OK\r\n";
        header += "Server: Java HTTP Server\r\n";
        header += "Date: " + new Date() + "\r\n";
        header += "Content-Type: text/html\r\n";
        header += "Content-Length: " + body.length + "\r\n";
        header += "\r\n";
        sendResponse();
    }

    public void renderHTML(String path) {
        body = Utility.readFromFile(path);
        header = "HTTP/1.1 200 OK\r\n";
        header += "Server: Java HTTP Server\r\n";
        header += "Date: " + new Date() + "\r\n";
        header += "Content-Type: text/html\r\n";
        header += "Content-Length: " + body.length + "\r\n";
        header += "\r\n";
        sendResponse();
    }

    public void renderFile(String path) {
        body = Utility.readFromFile(path);
        header = "HTTP/1.1 200 OK\r\n";
        header += "Server: Java HTTP Server\r\n";
        header += "Date: " + new Date() + "\r\n";
        String ext = Utility.getExtension(path);
        if (ext.equals("txt"))
            header += "Content-Type: text/plain\r\n";
        else header += "Content-Type: image/" + ext + "\r\n";
        header += "Content-Disposition: inline\r\n";
        header += "Content-Length: " + body.length + "\r\n";
        header += "\r\n";
        sendResponse();
    }

    public void renderError(String path) {
        body = Utility.readFromFile(path);
        header = "HTTP/1.1 404 Not Found\r\n";
        header += "Server: Java HTTP Server\r\n";
        header += "Date: " + new Date() + "\r\n";
        header += "Content-Type: text/html\r\n";
        header += "Content-Length: " + body.length + "\r\n";
        header += "\r\n";
        sendResponse();
    }

    public void downloadFile(String path) {
        header = "HTTP/1.1 200 OK\r\n";
        header += "Server: Java HTTP Server\r\n";
        header += "Date: " + new Date() + "\r\n";
		header += "Content-Type: application/octet-stream\r\n";
        header += "Content-Disposition: attachment; filename=\"" + Utility.getFileName(path) + "\"\r\n";
        header += "Transfer-Encoding: chunked\r\n\r\n";

        BufferedOutputStream bos = new BufferedOutputStream(os);
        BufferedInputStream bis;
        try {
            bis = new BufferedInputStream(new FileInputStream(path));
            bos.write(header.getBytes());
            bos.flush();
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
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    public void sendText(String s) {
        header = s;
        body = new byte[0];
        sendResponse();
    }
    
    public void renderFavicon(String path) {
    	body = Utility.readFromFile(path);
    	header = "HTTP/1.1 200 OK\r\n";
        header += "Server: Java HTTP Server\r\n";
        header += "Date: " + new Date() + "\r\n";
        header += "Content-Type: image/x-icon\r\n";
        header += "Content-Length: " + body.length + "\r\n";
        header += "\r\n";
        sendResponse();
    }
}
