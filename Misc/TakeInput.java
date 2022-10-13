package Misc;

import java.util.Scanner;

public class TakeInput {
    static Scanner scanner = new Scanner(System.in);

    public static <T> T getInput(){
        T output = (T) scanner.next();

        return output;
    }

}
