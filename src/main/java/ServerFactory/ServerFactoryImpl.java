package ServerFactory;

import Request.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerFactoryImpl implements ServerFactory{
    @Override
    public void listen(int port, Object service) {
        try(ServerSocket server = new ServerSocket(port)) {
            ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
            while(true) {
                Socket client = server.accept();

                executorService.execute(() -> {
                    try {
                        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
                        ObjectInputStream input = new ObjectInputStream(client.getInputStream());

                        Request request = handleRequest(service, input);

                        var result = request.getMethod().invoke(service, request.getArgs());

                        output.writeObject(result);

                        closeConnectionWithClient(client, output, input);
                    } catch (IOException | NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                             ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Request handleRequest(Object service, ObjectInputStream input) throws IOException,
            ClassNotFoundException, NoSuchMethodException {
        String methodName = (String) input.readObject();
        Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
        Object[] args = (Object[]) input.readObject();

        Method method = service.getClass().getMethod(methodName, parameterTypes);
        method.setAccessible(true);

        return new Request(method, args);
    }

    private static void closeConnectionWithClient(Socket client, ObjectOutputStream output, ObjectInputStream input) {
        try {
            client.close();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
