import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Random;

public class Server {
     Socket socket = null;
     ServerSocket server = null;
     DataInputStream in = null;
     DataOutputStream out = null;
     int mynumber;
     int attemptnumber;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client...");

            socket = server.accept();
            System.out.println("Client accepted");

            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());

            Random random = new Random();
            mynumber = random.nextInt(10) + 1;
            attemptnumber = 3;

            while (attemptnumber > 0) {
                try {
                    int userGuess = in.readInt();
                    System.out.println("Client guessed: " + userGuess);

                    if (userGuess == mynumber) {
                        System.out.println("Client won!");
                        out.writeUTF("Congratulations! You guessed the number. You won!");
                        out.writeUTF("Client won! The correct number was: " + mynumber);
                        break;
                    }

                    attemptnumber--;

                    if (userGuess > mynumber) {
                        System.out.println("Smaller than. Remaining attempts: " + attemptnumber);
                        out.writeUTF("Enter another number Remaining attempts: " + attemptnumber);
                    } else if (userGuess < mynumber) {
                        System.out.println("Greater than. Remaining attempts: " + attemptnumber);
                        out.writeUTF("Enter another number Remaining attempts: " + attemptnumber);
                    }

                    if (attemptnumber == 0) {
                        System.out.println("Client lost!!! Out of attempts. The correct number was: " + mynumber);
                        out.writeUTF("Game Over. You ran out of attempts. You lost!");
                        out.writeUTF("You lost! The correct number was: " + mynumber);
                        break; // Kazanma durumu kontrolüne gerek yok, çünkü kaybettik
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 10.");
                } catch (IOException e) {
                    System.out.println("Communication error with the client.");
                }
            }
            System.out.println("Closing connection");

            try {
                socket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        Server server = new Server(5000);
    }
}