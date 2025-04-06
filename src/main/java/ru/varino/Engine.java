package ru.varino;

import ru.varino.common.models.Movie;
import ru.varino.common.utility.RecursionDequeHandler;
import ru.varino.common.io.Console;
import ru.varino.common.io.StandartConsole;

import java.util.Hashtable;
import java.util.Scanner;


/**
 * Класс для инициализации программы
 */
public class Engine {
    /**
     * Метод, в котором инициализируются все менеджеры и запускается программа
     *
     * @param args аргументы командной строки (имя стартового файла)
     */
    public static void run(String[] args) {
        System.out.println("Initialization...");

        Console console = new StandartConsole();

        String fileName = "";
        try {
            fileName = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            console.println("Введите имя файла с помощью аргумента командной строки");
            System.exit(0);
        }

        FileManager fileManager = new FileManager(console);
        ParseManager parseManager = new ParseManager(console);
        CollectionManager collectionManager = new CollectionManager();

        String json = fileManager.read(fileName);
        Hashtable<Integer, Movie> initCollection = parseManager.getHashTableFromJson(json);
        collectionManager.setCollection(initCollection);

        Scanner scanner = new Scanner(System.in);
        InputManager.setScanner(scanner);


        CommandManager commandManager = new CommandManager();
        RecursionDequeHandler recursionDequeHandler = RecursionDequeHandler.getInstance();
        ScannerManager scannerManager = new ScannerManager();
        InputManager inputManager = new InputManager(console, commandManager, recursionDequeHandler, scannerManager);

        commandManager
                .add("show", new Show(collectionManager))
                .add("help", new Help(commandManager))
                .add("info", new Info(collectionManager))
                .add("insert", new Insert(collectionManager, scannerManager, console))
                .add("update", new Update(collectionManager, scannerManager, console))
                .add("remove_key", new RemoveKey(collectionManager))
                .add("clear", new Clear(collectionManager))
                .add("save", new Save(fileName, parseManager, fileManager, collectionManager))
                .add("exit", new Exit())
                .add("replace_if_greater", new ReplaceIf("greater", collectionManager, scannerManager, console))
                .add("replace_if_lower", new ReplaceIf("lower", collectionManager, scannerManager, console))
                .add("remove_lower_key", new RemoveLowerKey(collectionManager))
                .add("average_of_total_box_office", new AverageTotalBoxOffice(collectionManager))
                .add("min_by_director", new MinByDirector(collectionManager))
                .add("count_less_than_genre", new CountLessGenre(collectionManager))
                .add("execute_script", new ExecuteScript(inputManager));

        inputManager.interactiveRun();
    }
}
