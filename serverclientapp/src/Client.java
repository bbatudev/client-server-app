import java.net.*;
import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Client {
    Socket socket = null;
    DataOutputStream out = null;
    DataInputStream in = null;

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            Scanner scanner = new Scanner(System.in);
            int attemptnumber = 3;
            while (attemptnumber > 0) {
                try {
                    System.out.println("Guess a number between 1 and 10:");
                    int userGuess = scanner.nextInt();

                    if (userGuess < 1 || userGuess > 10) {
                        System.out.println("Invalid guess. Please enter a number between 1 and 10.");
                        continue;
                    }

                    out.writeInt(userGuess);
                    attemptnumber--;

                    String response = in.readUTF();
                    System.out.println(response);

                    if (response.contains("You won!") || response.contains("You lost!")) {
                        try {
                            String serverMessage = in.readUTF();
                            System.out.println(serverMessage);
                        } catch (EOFException e) {

                            break;
                        } catch (IOException e) {
                            System.out.println("Error receiving the final message from the server: " + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 10.");
                    scanner.next();
                } catch (EOFException e) {
                    break;
                } catch (IOException e) {
                    System.out.println("Communication error with the server: " + e.getMessage());
                    e.printStackTrace();
                    break;
                }
            }
            try {
                String finalMessage = in.readUTF();
                System.out.println(finalMessage);
            } catch (EOFException e) {
            } catch (IOException e) {
                System.out.println("Error receiving the final message from the server: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    out.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[]) {
        Client client = new Client("127.0.0.1", 5000);
    }
}