package remote;

import java.util.concurrent.*;

public class TimeoutTest2 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        final Runnable timeout = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Future future = executor.submit(timeout);
        executor.shutdown();

        try {
            future.get(6, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }

        if (!executor.isTerminated())
            executor.shutdownNow();

    }
}
