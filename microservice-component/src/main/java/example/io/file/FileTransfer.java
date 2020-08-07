package example.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.Formatter;

public class FileTransfer {

    private static final Formatter formatter = new Formatter();

    public static void transfer(String src, String dest) throws Exception {

        File sf = new File(src);
        if (!sf.exists()){
            throw new FileNotFoundException(formatter.format("file %s not found !", src).toString());
        }
        if (!sf.canRead()){
            throw new AccessDeniedException(formatter.format("file %s can't access", src).toString());
        }
        File df = new File(dest);

        Files.copy(sf.toPath(), df.toPath());

    }

    public static void main(String[] args) throws Exception {
        transfer("/home/yh/test.txt", "/home/yh/re.txt");
    }
}
