import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Demo {
    public static void main(String[] args) {

    }
    public static void function(){
        // 3 4 5
        Scanner scanner = new Scanner(System.in); // A
        int a = scanner.nextInt(); // A
        int b = scanner.nextInt(); // A
        int c = scanner.nextInt(); // A
        String type = "raznostran"; // A

        if ( a == b || a == c || b == c){ // B
            type = "ramnokrak"; // C
        }
        if ( a == b && b == c){ // D
            type = "ramnostran"; // E
        }
        if ( a >= b+c || b >= a+c || c >= a+b ){ // F
            type = "not a triangle"; // G
        }
        if ( a <=0 || b <= 0 || c <= 0 ){ // H
            type = "bad input"; // I
        }
        System.out.println(type); // J
    }

    public static void example() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); //A
        int hr1 = Integer.parseInt(reader.readLine()); //A
        int min1 = Integer.parseInt(reader.readLine()); //A
        String ampm1 = reader.readLine(); //A

        int hr2 = Integer.parseInt(reader.readLine()); //A
        int min2 = Integer.parseInt(reader.readLine()); //A
        String ampm2 = reader.readLine(); //A

        if ( hr1 == 12 ){ // A
            hr1 = 0; //B
        }

        if (hr2 == 12){ //C
            hr2 = 0; //D
        }

        if (ampm1.equals("pm")){ //E
            hr1 = hr1 + 12; //F
        }

        if ( ampm2.equals("pm") ){ //G
            hr2 = hr2 + 12; //H
        }

        if (min2 < min1 ){ //I
            min2 = min2 + 60; //J
        }

        hr2 = hr2-1; //K
        if (hr2 < hr1){ //K
            hr2 = hr2+24; //L
        }

        int elapsed = min2 - min1 + 60 * (hr2-hr1); //M
        System.out.println(elapsed); //M
    }

    /* TEST CASES
    * first test case: 12 00 pm 10 40 am
    * second test case: 9 57 am 12 40 pm
    * SO 2 TEST SLUCAJI JA POMINVAME CELATA PROGRAMA!
    * every statement
    *   T1  T2
    * A y   y
    * B y
    * C y   y
    * D     y
    * E y
    * F y
    * G y
    * H     y
    * I y
    * J     y
    * K y
    * L y
    * M y
    *
    * every branch:
    *       T1  T2
    * A-B   y
    * A-C       y
    * B-C   y
    * C-D       y
    * C-E   y
    * D-E       y
    * E-F   y
    * E-G       y
    * F-G   y
    * G-H       y
    * G-I   y
    * H-I       y
    * I-J       y
    * I-K   y
    * J-K       y
    * K-L   y
    * K-M       y
    * L-M   y
    *
    * */
}
