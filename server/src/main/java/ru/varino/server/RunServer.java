package ru.varino.server;

import ru.varino.common.io.Console;
import ru.varino.common.io.StandartConsole;
import ru.varino.common.models.Movie;
import ru.varino.common.utility.RecursionDequeHandler;
import ru.varino.server.managers.*;
import ru.varino.server.commands.*;

import java.util.Hashtable;
import java.util.Scanner;

public class RunServer {
    public static void main(String[] args) {
        System.out.println("Initialization...");

        Console console = new StandartConsole();

        String fileName = "";
        try {
            fileName = "D:\\Java projects\\lab6\\src\\main\\java\\ru\\varino\\start.json";
        } catch (ArrayIndexOutOfBoundsException e) {
            console.println("Введите имя файла с помощью аргумента командной строки");
            System.exit(0);
        }

        FileManager fileManager = new FileManager(console, fileName);
        ParseManager parseManager = new ParseManager(console);
        CollectionManager collectionManager = new CollectionManager();

        String json = fileManager.read();
        Hashtable<Integer, Movie> initCollection = parseManager.getHashTableFromJson(json);
        collectionManager.setCollection(initCollection);




        CommandManager commandManager = new CommandManager();
        RecursionDequeHandler recursionDequeHandler = RecursionDequeHandler.getInstance();

        commandManager
                .add("show", new Show(collectionManager))
                .add("help", new Help(commandManager))
                .add("info", new Info(collectionManager))
                .add("insert", new Insert(collectionManager))
                .add("update", new Update(collectionManager))
                .add("remove_key", new RemoveKey(collectionManager))
                .add("clear", new Clear(collectionManager))
                .add("exit", new Exit())
                .add("replace_if_greater", new ReplaceIf("greater", collectionManager))
                .add("replace_if_lower", new ReplaceIf("lower", collectionManager))
                .add("remove_lower_key", new RemoveLowerKey(collectionManager))
                .add("average_of_total_box_office", new AverageTotalBoxOffice(collectionManager))
                .add("min_by_director", new MinByDirector(collectionManager))
                .add("count_less_than_genre", new CountLessGenre(collectionManager));

        RequestManager requestManager = new RequestManager(commandManager);
        try {
            Server server = new Server(8080, requestManager, fileManager, collectionManager, parseManager, console);
            server.run();

        } catch (Exception e) {
            console.printerr("Ошибка");
        }
    }
}
