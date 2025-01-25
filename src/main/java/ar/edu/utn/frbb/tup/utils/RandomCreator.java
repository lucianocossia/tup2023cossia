package ar.edu.utn.frbb.tup.utils;
import java.util.Random;

public class RandomCreator {
    private static RandomCreator instance;
    private Random random;

    private RandomCreator() {
        random = new Random();
    }

    public static synchronized RandomCreator getInstance() {
        if (instance == null) {
            instance = new RandomCreator();
        }
        return instance;
    }

    public int generateRandomNumber (int maxNumber) {
        return random.nextInt(maxNumber);
    }
}

