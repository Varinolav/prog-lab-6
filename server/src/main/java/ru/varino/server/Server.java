package ru.varino.server;

import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.exceptions.PermissionDeniedException;
import ru.varino.server.managers.*;
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
    private CommandListener commandListener;

    public Server(int port, RequestManager requestManager, FileManager fileManager, CollectionManager collectionManager, ParseManager parseManager, Console console, CommandListener commandListener) {
        this.port = port;
        this.requestManager = requestManager;
        this.fileManager = fileManager;
        this.collectionManager = collectionManager;
        this.parseManager = parseManager;
        this.console = console;
        this.commandListener = commandListener;
    }

    private ResponseEntity processRequest(RequestEntity request) {
        ResponseEntity response = requestManager.process(request);
        return response;
    }

    public void run() {
        Thread listener = commandListener.getListener();
        listener.start();
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
