import java.io.File;

public class FileUtility {
    public static String getExtension(String path) {
        String[] tokens = getFileName(path).split("\\.");
        return tokens[tokens.length - 1];
    }
    public static String getFileName(String path) {
        return new File(path).getName();
    }
}
