import java.io.*;

public class Request {
    private final InputStream is;
    private String header;
    private BufferedInputStream bis;
    public Request(InputStream is) {
        this.is = is;
        try {
            bis = new BufferedInputStream(this.is);
            StringBuilder sb = new StringBuilder();
            String line;
            while (!(line = Utility.getLine(bis)).equals("")) {
                sb.append(line).append('\n');
            }
            header = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean empty() {
        return header == null;
    }

    public boolean isGETRequest() {
        return header.split("\n")[0].startsWith("GET");
    }

    public String getPath() {
        String[] headerSplitLines = header.split("\n");
        String[] headerFLS = headerSplitLines[0].split(" ");
        String path = headerFLS[1];
        if ((path = path.replaceAll("%20", " ")).length() != 1) {
            if (path.charAt(path.length() - 1) == '/') {
                path = path.substring(0, path.length() - 1);
            }
        }
        return path;
    }

    public String getHost() {
        for (String line : header.split("\n")) {
            if (line.startsWith("Host:")) {
                return line.split(" ")[1];
            }
        }
        return null;
    }

    public boolean isUPLOADRequest() {
        return header.split("\n")[0].startsWith("UPLOAD");
    }


    public String getHeader() {
        return header;
    }

    public void saveUploadedFile(String dirPath) throws IOException {
        String fileName = getPath();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dirPath + "/" + fileName));
        int chunkSize;
        while ((chunkSize = Integer.valueOf(Utility.getLine(bis), 16)) != 0) {
            bos.write(bis.readNBytes(chunkSize));
            bos.flush();
            bis.readNBytes(2);
        }
        bos.close();
    }
}
