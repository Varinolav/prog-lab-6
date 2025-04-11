package ru.varino.client;

import ru.varino.client.helpers.UserService;
import ru.varino.common.io.Console;
import ru.varino.common.io.StandartConsole;
import ru.varino.common.utility.ServerConfiguration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class RunClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Console console = new StandartConsole();
        UserService userService = new UserService(scanner, console);
        try {
            Client client = new Client(InetAddress.getByName(ServerConfiguration.HOST), ServerConfiguration.PORT,userService, console);
            client.run();
        } catch (UnknownHostException e) {
            System.out.println("Сервера с таким именем не найдено");
        }
    }
}
