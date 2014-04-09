import java.net.*;
import java.io.*;

public class Client {

    private static final int SERVER_PORT = 7777;
    private static final String ADDRESS = "127.0.0.1";
    private static final int SIZE = 64 * 1024; // 64 KB
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
        client.serverUploads();
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
        //
        //добавить! ловить исключения
        //
    }

    private void serverFolder() throws IOException{
        // Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом.
        sin = socket.getInputStream();
        sout = socket.getOutputStream();

        // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
        in = new DataInputStream(sin);
        out = new DataOutputStream(sout);

        System.out.print("Введите название каталога с файлами на сервере: ");
        BufferedReader readerSaveFiles = new BufferedReader(new InputStreamReader(System.in));
        pathToFolder = readerSaveFiles.readLine();
        out.writeUTF(pathToFolder);
        String pathToFolder1 = in.readUTF();
        if (pathToFolder.equals(pathToFolder1)){
            System.out.println("Каталог " + pathToFolder1 + " создан на сервере.");
        } else {
            System.out.println("Ошибка при создании каталога на сервере");
            System.exit(0);
        }
    }

    private void serverUploads() throws IOException{

        File dir = new File(dataFolder);
        String[] fileNames = dir.list();
        System.out.println("Отправка " + fileNames.length + " файлов на сервер...");
        int i = 0;
        byte[] buffer = new byte[SIZE];
        for (String fileName:fileNames) {
            System.out.println("Передаю " + ++i + " файл");
            File f = new File(dataFolder + "/" + fileName);
            FileInputStream fis = new FileInputStream(f);
            long fileSize = f.length();
            out.writeUTF(fileName);
            out.writeLong(fileSize);
            int read = 0;
            while ((read = fis.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
        }
        System.out.println("Все файлы переданы");
        socket.close();
    }
}