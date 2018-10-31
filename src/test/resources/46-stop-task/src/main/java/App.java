import com.excelsiorjet.api.cmd.*;
import com.excelsiorjet.api.log.*;
import java.io.*;

public class App {

    private static void failed() throws Exception {
        new File("failed").createNewFile();
        System.exit(10);
    }

    public static void main(String args[]) throws Exception {
        File workingDirectory = new File("../../..");
        try {
            // stop application using Gradle. 
            if (new CmdLineTool(args).workingDirectory(workingDirectory).withLog(new StdOutLog()).execute() != 0) {
                failed();
            }
        } catch (Exception e) {
            e.printStackTrace();
            failed();
        }
        Thread.sleep(5000);
        failed();
    }

}