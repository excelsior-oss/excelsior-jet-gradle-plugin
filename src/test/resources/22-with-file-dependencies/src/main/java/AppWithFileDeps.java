import org.apache.commons.io.IOUtils;
import com.example.single.SingleDep;
import com.example.multi.first.FirstMultiDep;
import com.example.multi.second.SecondMultiDep;

import java.io.IOException;
import java.io.StringReader;
import java.lang.System;

public class AppWithFileDeps {

    public static void main(String args[]) throws IOException {
        System.out.println(IOUtils.toString(new StringReader("HelloWorld"))
                +
                ":" + SingleDep.getName()
                +
                ":" + FirstMultiDep.getName()
                +
                ":" + SecondMultiDep.getName()
        );
    }

}