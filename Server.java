import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 10001; // port number

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ServerThread(socket).start();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        ProcessBuilder shutdown = new ProcessBuilder("shutdown", "-s", "-t", "0");
        ProcessBuilder restart = new ProcessBuilder("shutdown", "-r", "-t", "0");

        // locks computer
        ProcessBuilder lock = new ProcessBuilder("rundll32.exe", "user32.dll,LockWorkStation");

        try (InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            String message;

            while ((message = reader.readLine()) != null) {
                System.out.println("Received: " + message);
                
                
                if(message.equals("1")){
                    try {
                        // Start the process
                        Process process = shutdown.start();
                        process.waitFor();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(message.equals("2")){
                    try {
                        // Start the process
                        Process process = restart.start();
                        process.waitFor();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(message.equals("3")){
                    try {
                        // Start the process
                        Process process = lock.start();
                        process.waitFor();
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
