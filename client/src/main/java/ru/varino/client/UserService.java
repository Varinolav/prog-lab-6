package ru.varino.client;

import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;
import ru.varino.common.io.Console;
import ru.varino.common.models.Movie;
import ru.varino.common.models.modelUtility.InteractiveMovieCreator;

import java.util.NoSuchElementException;
import java.util.Scanner;


public class UserService {
    private Scanner scanner;
    private Console console;

    public UserService(Scanner usedScanner, Console console) {
        this.scanner = usedScanner;
        this.console = console;

    }

    public RequestEntity handle() {
        String[] userCommand;
        console.printf("~ ");
        userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
        String command = userCommand[0].toLowerCase();
        String params = userCommand[1].trim();
        RequestEntity request = RequestEntity.create(command, params);
        if (command.equals("exit")) {
            request = RequestEntity.create("save", "");
            console.println("Идет сохранение изменений...");
            console.println("Завершаем работу программы.");
            System.exit(0);
        }
        if (command.equals("insert") || command.equals("update") || command.contains("replace_if_")) {
            try {
                Movie m = InteractiveMovieCreator.create(console, scanner);
                request.body(m);
            } catch (InterruptedException e) {
                console.println("Ввод прекращен");
            }
        }
        return request;


    }

}
