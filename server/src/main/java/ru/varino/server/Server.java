package ru.varino.server;

import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.exceptions.PermissionDeniedException;
import ru.varino.server.managers.CollectionManager;
import ru.varino.server.managers.FileManager;
import ru.varino.server.managers.ParseManager;
import ru.varino.server.managers.RequestManager;
import ru.varino.common.io.Console;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Server {
    private int port;
    private RequestManager requestManager;
    private FileManager fileManager;
    private CollectionManager collectionManager;
    private ParseManager parseManager;
    private Console console;

    public Server(int port, RequestManager requestManager, FileManager fileManager, CollectionManager collectionManager, ParseManager parseManager, Console console) {
        this.port = port;
        this.requestManager = requestManager;
        this.fileManager = fileManager;
        this.collectionManager = collectionManager;
        this.parseManager = parseManager;
        this.console = console;
    }

    private ResponseEntity processRequest(RequestEntity request) {
        ResponseEntity response = requestManager.process(request);
        return response;
    }

    public void run() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                try {
                    String stringInput = scanner.nextLine();
                    if (stringInput.trim().equals("save")) {
                        try {
                            fileManager.write(parseManager.getJsonFromHashTable(collectionManager.getCollection()));

                        } catch (PermissionDeniedException e) {
                            console.printerr(e.getMessage());
                        }
                    } else if (stringInput.trim().equals("exit")) {
                        console.println("Завершение работы сервера...");
                        System.exit(0);
                    } else {
                        console.println("Доступны только 2 команды: save и exit");
                    }
                } catch (NoSuchElementException exception) {
                    console.println("");
                    console.printerr("Работа программы прекращена!");
                }
            }
        }).start();
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                Socket sock = serverSocket.accept();
                ObjectInputStream input = new ObjectInputStream(sock.getInputStream());
                RequestEntity request = (RequestEntity) input.readObject();
                ResponseEntity response = processRequest(request);
                ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
                out.writeObject(response);
            } catch (Exception e) {
                System.out.println("Произошла ошибка");
            }
        }


    }

}
