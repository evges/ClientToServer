import java.net.*;
import java.io.*;

public class Server {

    private static final int SERVER_PORT = 7777; // случайный порт (может быть любое число от 1025 до 65535)
    String pathToFolder = null;
    InputStream sin = null;
    OutputStream sout = null;
    DataInputStream in =null;
    DataOutputStream out = null;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.serverConnect();
        server.serverFolder();

    }

    private void serverConnect() throws IOException {
        ServerSocket ss = new ServerSocket(SERVER_PORT); // создаем сокет сервера и привязываем его к вышеуказанному порту
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
        out.writeUTF(pathToFolder);//отправка ответа о созданном каталоге
        out.flush();
    }
}