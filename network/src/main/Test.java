package main;

import java.util.ArrayList;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Interface main_interface = new Interface("https://ingredible.tech");

        ArrayList<String> toks = new ArrayList<>();
        toks.add("butter");

        Search s = main_interface.search(toks);
        int got = s.poll(5);

        System.out.println("polled 5 got " + got);
    }
}
