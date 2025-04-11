package ru.varino.client;

import ru.varino.client.helpers.UserService;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.io.Console;


import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;

import java.net.Socket;


public class Client {
    private InetAddress host;
    private int port;
    private UserService userService;
    private Console console;


    public Client(InetAddress host, int port, UserService userService, Console console) {
        this.port = port;
        this.host = host;
        this.userService = userService;
        this.console = console;
    }

    public void run() {
        boolean status = true;
        while (status) {
            try {
                status = requestToServer();
                continue;
            } catch (Exception exception) {
                console.printerr("Произошла ошибка");
            }
            console.println("Работа клиента завершена.");
            System.exit(0);
        }

    }

    private boolean requestToServer() {
        try {
            RequestEntity request = userService.handle();
            if (request.getCommand().isEmpty()) return true;
            try (Socket socket = new Socket(host, port)) {
                OutputStream out = socket.getOutputStream();
                out.write(serializeRequest(request));
                out.flush();

                if (request.getCommand().equals("save")) {
                    System.exit(0);
                }

                InputStream input = socket.getInputStream();

                ObjectInputStream ois = new ObjectInputStream(input);

                ResponseEntity response = (ResponseEntity) ois.readObject();
                console.printResponse(response);
            } catch (ClassNotFoundException e) {
                console.printerr("Класса с таким именем не существует");
            } catch (ConnectException e) {
                console.printerr("Сервер временно недоступен");
            } catch (Exception e) {
                console.printerr(e.toString());
            }
            return true;

        } catch (Exception e) {
            console.printerr(e.toString());
            return false;
        }

    }

    private static byte[] serializeRequest(RequestEntity request) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(request);
            return bos.toByteArray();
        }
    }


}
