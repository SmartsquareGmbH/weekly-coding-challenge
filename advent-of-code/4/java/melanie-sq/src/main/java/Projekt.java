import java.util.HashMap;
import java.util.Map;

public class Projekt {
    public static void main(String[] args) {

    }

    public int findPasswordInRange(int start, int end) {
        if (start < 100_000 || start > 1_000_000 || end > 1_000_000)
            throw new IllegalArgumentException("Not in range");

        int passwords = 0;

        for (int i = start; i <= end; i++) {
            if (checkIfPassword(i))
                passwords++;
        }

        System.out.println("Number of Passwords: " + passwords);
        return passwords;
    }

    public boolean checkIfPassword(int number) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>() {
            {
                put(0, 0);
                put(1, 0);
                put(2, 0);
                put(3, 0);
                put(4, 0);
                put(5, 0);
                put(6, 0);
                put(7, 0);
                put(8, 0);
                put(9, 0);
            }
        };
        int length = String.valueOf(number).length();
        int[] numbers = new int[length];

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Character.getNumericValue(Character.valueOf(String.valueOf(number).charAt(i)));
            int current = map.get(numbers[i]);
            map.put(numbers[i],current+1);
        }
        if (!map.values().contains(2)){
            return false;
        }

        int previous;
        int current;

        for (int i = 0; i < numbers.length - 1; i++) {
            previous = numbers[i];
            current = numbers[i + 1];
            if (current < previous) {
                return false;
            }
        }
        return true;
    }
}
