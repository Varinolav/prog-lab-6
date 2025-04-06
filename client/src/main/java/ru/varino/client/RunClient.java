package ru.varino.client;

import ru.varino.common.io.Console;
import ru.varino.common.io.StandartConsole;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class RunClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Console console = new StandartConsole();
        UserService userService = new UserService(scanner, console);
        try {
            Client client = new Client(InetAddress.getByName("0.0.0.0"), 8080, userService, console);
            client.run();
        } catch (UnknownHostException e) {
            System.out.println("Сервера с таким именем не найдено");
        }


    }
}
