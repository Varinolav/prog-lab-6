package ru.varino.client;

import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.io.Console;
import ru.varino.common.models.Movie;
import ru.varino.common.models.modelUtility.InteractiveMovieCreator;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

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
        RequestEntity request = userService.handle();
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(request);
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            ResponseEntity response = (ResponseEntity) input.readObject();
            console.printResponse(response);
            return true;

        } catch (Exception e) {
            console.printerr(e.toString());
            return false;
        }

    }

}
