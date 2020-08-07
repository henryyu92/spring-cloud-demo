package example.io.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;

public class FileTransfer {

    private static final Formatter formatter = new Formatter();

    public static void transfer(String src, String dest) throws Exception {

        final Path fp = Paths.get(src);
        final Path dp = Paths.get(dest);

        Files.copy(fp, dp);

    }

    public static void transfer(InputStream in, OutputStream out){

    }

    public static void main(String[] args) throws Exception {
        transfer("/home/yh/test.txt", "/home/yh/re.txt");
    }
}
