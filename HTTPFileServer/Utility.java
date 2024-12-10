import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utility {
    public static boolean isFilePath(String path) {
        return new File(path).isFile();
    }
    public static boolean isDirPath(String path) {
        return new File(path).isDirectory();
    }
    public static String getExtension(String path) {
        String fileName = getFileName(path);
        String[] tokens = fileName.split("\\.");
        int n = tokens.length;
        return tokens[n - 1];
    }
    public static String getFileName(String path) {
        return new File(path).getName();
    }

    public static String getParentDirectory(String path) {
        if (path.equals("/")) return "";
        int i;
        for (i = path.length() - 1; i >= 0; --i) {
            if (path.charAt(i) == '/') break;
        }
        return path.substring(0, i);
    }

    public static byte[] readFromFile(String path) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
            byte[] data = bis.readAllBytes();
            bis.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeToFile(String path, String data) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
        bos.write(data.getBytes());
        bos.flush();
        bos.close();
    }

    public static String getLine(BufferedInputStream bis) throws IOException {
        StringBuilder sb = new StringBuilder();
        int data;
        while (true) {
            data = bis.read();
            if ((char) data == '\n' || data == -1)
                break;
            else if ((char) data == '\r') {
                bis.read();
                break;
            }
            sb.append((char) data);
        }
        String line = sb.toString();
        return line;
    }
}
