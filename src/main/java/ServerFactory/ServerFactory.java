package ServerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface ServerFactory {
    void listen(int port, Object service) throws IOException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}