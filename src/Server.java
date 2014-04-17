import java.net.*;
import java.io.*;

public class Server {

    private static final int SERVER_PORT = 7777; // случайный порт (может быть любое число от 1025 до 65535)
    private static final int SIZE = 64 * 1024;
    private String pathToFolder = null;
    private InputStream sin = null;
    private OutputStream sout = null;
    private DataInputStream in =null;
    private DataOutputStream out = null;
    private ServerSocket ss = null;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.serverConnect();
        server.serverFolder();
        server.serverDownloads();
    }

    private void serverConnect() throws IOException {
        ss = new ServerSocket(SERVER_PORT); // создаем сокет сервера и привязываем его к вышеуказанному порту
        System.out.println("Ожидание клиента.");
        Socket socket = ss.accept(); // заставляем сервер ждать подключений и выводим сообщение когда кто-то связался с сервером
        System.out.println("Клиент подключился");
        // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиенту.
        sin = socket.getInputStream();
        sout = socket.getOutputStream();
        // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
        in = new DataInputStream(sin);
        out = new DataOutputStream(sout);
    }

    private void serverFolder() throws IOException {
        pathToFolder = in.readUTF();
        File myPathToFolder = new File(pathToFolder);
        myPathToFolder.mkdirs();
        out.writeUTF(pathToFolder);
    }

    private void serverDownloads() throws IOException {
        byte[] buffer = new byte[SIZE];
        while(!ss.isClosed()) {
            String fileName = in.readUTF();
            long fileSize = in.readLong();
            FileOutputStream fos = new FileOutputStream(pathToFolder + "/" + fileName);
            long rounds = fileSize/buffer.length;
            long tail = fileSize%buffer.length;
            for (int i = 0; i < rounds; i++) {
                in.readFully(buffer);
                fos.write(buffer);
            }
            in.readFully(buffer, 0, (int) tail);
            fos.write(buffer,0, (int) tail);
            fos.close();
        }
    }
}
