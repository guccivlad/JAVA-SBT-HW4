package Request;

import java.io.Serializable;
import java.lang.reflect.Method;

public class Request implements Serializable {
    private final Method method;
    private final Object[] args;

    public Request(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }
}
