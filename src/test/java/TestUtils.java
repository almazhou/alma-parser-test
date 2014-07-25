import java.io.*;

public class TestUtils {

    public static void writeToFile(String test, String filePath) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath), "utf-8"));
            writer.write(test);
        } catch (IOException ex) {
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }
}
