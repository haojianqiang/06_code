package pub.functions;

import java.io.Closeable;
import java.io.IOException;

public class IoFuncs {
    public static void tryClose(Closeable closable) {
        if (closable == null) {
            return;
        }
        try {
            closable.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
