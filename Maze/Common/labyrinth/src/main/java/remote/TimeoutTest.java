package remote;

import java.io.IOException;
import java.util.concurrent.*;

public class TimeoutTest {

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();

        Callable<Integer> callableInt = () -> {
            Thread.sleep(5000);
            return 1;
        };

        int i = 0;
        try {
            i = service.submit(callableInt).get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            System.out.println("Timeout exception");
            throw new RuntimeException(e);
        }

        System.out.println(i);
        System.exit(1);
    }

    private static Callable<Integer> sleeper() {
        System.out.println("Entering sleeper");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Callable<Integer> callableInt = () -> {
            return 1;
        };
        System.out.println("About to return");
        return callableInt;
    }

}
