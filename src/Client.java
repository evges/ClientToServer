import java.net.*;
import java.io.*;

public class Client {

    private static final int SERVER_PORT = 7777;
    private static final String ADDRESS = "127.0.0.1";
    Socket socket = null;
    File folder = null;
    InputStream sin = null;
    OutputStream sout = null;
    DataInputStream in = null;
    DataOutputStream out = null;


    String dataFolder = null;
    String pathToFolder = null;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.serverConnect();
        client.clientFolder();
        client.serverFolder();
        client.serverDownloads();
    }

    private void serverConnect(){
        try {
            InetAddress ipAddress = InetAddress.getByName(ADDRESS); // создаем объект который отображает IP-адрес.
            System.out.println("Подключаемся к сокету " + ADDRESS + ":" + SERVER_PORT);
            socket = new Socket(ipAddress, SERVER_PORT);// создаем сокет используя IP-адрес и порт сервера.
        } catch (IOException e){
            System.out.println("Сервер недоступен");
            System.exit(0);
        }
    }

    private void clientFolder() throws IOException{
        BufferedReader readerDataFolder = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Введите полный путь к файлам на клиенте: ");
        dataFolder = readerDataFolder.readLine();
        folder = new File(dataFolder);
        while(!folder.exists()){
            System.out.println("Такого каталога не существует!");
            System.out.print("Введите полный путь к файлам на клиенте: ");
            dataFolder = readerDataFolder.readLine();
            folder = new File(dataFolder);
        }
        //ловить исключения
    }

    private void serverFolder() throws IOException{
        // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом.
        sin = socket.getInputStream();
        sout = socket.getOutputStream();

        // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
        in = new DataInputStream(sin);
        out = new DataOutputStream(sout);

        System.out.println("Введите название каталога с файлами на сервере");
        BufferedReader readerSaveFiles = new BufferedReader(new InputStreamReader(System.in));
        pathToFolder = readerSaveFiles.readLine();
        out.writeUTF(pathToFolder);
        pathToFolder = in.readUTF();
        System.out.println("Каталог " + pathToFolder + " создан на сервере");

        out.flush();

    }
    private void serverDownloads(){

    }
}