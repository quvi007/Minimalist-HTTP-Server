import java.io.File;

public class Renderer {
    public static byte[] render(String path, String host) {
        File dir = new File(Server.getName() + path);
        String htmlData = "<html>\n" +
                "\t<head>\n" +
                "\t\t<title>Index of " + path + "</title>\n" +
                "\t</head>\n" +
                "\t\n" +
                "\t<body>\n" +
                "\t\t<h1>Index of " + path + "</h1>\n" +
                "\t\t<hr/>\n" +
                "\t\t<a href = \"http://" + host + Utility.getParentDirectory(path) + "\"><b><i>[parent directory]</i></b></a>\n" +
                "\t\t<table>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<th>Name</th>\n" +
                "\t\t\t</tr>\n";
        StringBuilder sb = new StringBuilder();
        for (File file : dir.listFiles()) {
            String s = path.equals("/") ? path : path + "/";
            if (file.isFile()) {
                sb.append("\t\t\t<tr>\n" +
                        "\t\t\t\t<td><a href = \"http://" + host + s + file.getName() + "\">" + file.getName() + "</a></td>\n" +
                        "\t\t\t</tr>\n");
            } else if (file.isDirectory()) {
                sb.append("\t\t\t<tr>\n" +
                        "\t\t\t\t<td><a href = \"http://" + host + s + file.getName() + "\"><b><i>" + file.getName() + "</i></b></a></td>\n" +
                        "\t\t\t</tr>\n");
            }
        }
        htmlData += sb.toString();
        htmlData += "\t\t</table>\n" +
                "\t</body>\t\n" +
                "</html>\n";
        return htmlData.getBytes();
    }
}
