package ClientFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class ClientFactoryImpl implements ClientFactory{
    private final String ip;
    private final int port;

    public ClientFactoryImpl(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public <T> T newClient(Class<T> client) {
        return (T) Proxy.newProxyInstance(client.getClassLoader(), new Class<?>[]{client}, new Handler(ip, port));
    }
}

class Handler implements InvocationHandler {
    private final String ip;
    private final int port;

    public Handler(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Socket client = new Socket(ip, port);

        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());

        sendRequest(output, method, args);

        var result = input.readObject();

        closeConnectionWithServer(client, output, input);

        return result;
    }

    static private void sendRequest(ObjectOutputStream output, Method method, Object[] args) {
        try {
            output.writeObject(method.getName());
            output.writeObject(method.getParameterTypes());
            output.writeObject(args);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    static private void closeConnectionWithServer(Socket client, ObjectOutputStream output, ObjectInputStream input) {
        try {
            client.close();
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
