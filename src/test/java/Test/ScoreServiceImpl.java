package Test;

import java.util.List;

public class ScoreServiceImpl implements ScoreService {
    @Override
    public int score(List<Person> pearsons) {
        int salariesSum = 0;

        for (Person pearson : pearsons) {
            salariesSum += pearson.getSalary();
        }

        return salariesSum;
    }
}
