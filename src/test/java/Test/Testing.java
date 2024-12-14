package Test;

import ClientFactory.ClientFactory;
import ClientFactory.ClientFactoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Testing {

    @Test
    public void testSingleClient() throws IOException {
        List<Person> personList = new ArrayList<>();
        int correctAnswer = 0;

        for(int i = 0; i < 1_000; ++i) {
            correctAnswer += i;
        }

        for(int i = 0; i < 1_000; ++i) {
            personList.add(new Person("a", "b", i));
        }

        ScoreService scoreService = createScoreClient();
        int result = scoreService.score(personList);

        Assertions.assertEquals(correctAnswer, result);
    }

    @Test
    public void testMultiClient() {
        int numberOfClient = 5;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfClient);

        for(int i = 0; i < numberOfClient; ++i) {
            executorService.submit(() -> {
                ScoreService scoreService = null;
                try {
                    scoreService = createScoreClient();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                List<Person> personList = new ArrayList<>();
                int correctAnswer = 0;

                for(int j = 0; j < 1_000; ++j) {
                    correctAnswer += j;
                }

                for(int j = 0; j < 1_000; ++j) {
                    personList.add(new Person("a", "b", j));
                }
                int result = scoreService.score(personList);

                Assertions.assertEquals(correctAnswer, result);
            });
        }
    }

    private static ScoreService createScoreClient() throws IOException {
        ClientFactory factory = new ClientFactoryImpl("127.0.0.1", 8000);
        return factory.newClient(ScoreService.class);
    }
}
