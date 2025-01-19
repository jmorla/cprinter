import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws IOException {
        if (Objects.isNull(args) || args.length == 0) {
            System.err.println("""
                Error: No arguments provided. Please pass the required arguments.
                Usage: Main [filename]""");
            System.exit(1); // Optionally exit the program with an error code
        }
        new ClassPrinter(args[0]).print();
    }

}