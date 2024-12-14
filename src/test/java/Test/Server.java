package Test;

import ServerFactory.ServerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import ServerFactory.ServerFactoryImpl;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException,
            InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        configureServer();
    }

    private static void configureServer() throws IOException, ClassNotFoundException,
            InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ServerFactory serverFactory = new ServerFactoryImpl();
        serverFactory.listen(8000, new ScoreServiceImpl());
    }
}
