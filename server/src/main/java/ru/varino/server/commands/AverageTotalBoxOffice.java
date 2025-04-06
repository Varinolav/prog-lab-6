package ru.varino.server.commands;

import ru.varino.server.managers.CollectionManager;
import ru.varino.common.models.Movie;
import ru.varino.common.communication.RequestEntity;
import ru.varino.common.communication.ResponseEntity;

/**
 * Класс команды AverageTotalBoxOffice
 */
public class AverageTotalBoxOffice extends Command {
    private final CollectionManager collectionManager;

    public AverageTotalBoxOffice(CollectionManager collectionManager) {
        super("average_of_total_box_office", "вывести среднее значение поля totalBoxOffice для всех элементов коллекции");
        this.collectionManager = collectionManager;
    }

    /**
     * {@inheritDoc}
     * @param req запрос для выполнения команды
     * @return {@link ResponseEntity}
     */
    @Override
    public ResponseEntity execute(RequestEntity req) {
        String args = req.getParams();
        if (!args.isEmpty()) return ResponseEntity.badRequest().body("Неверные аргументы");
        if (collectionManager.getCollection().isEmpty()) return ResponseEntity.badRequest().body("Коллекция пуста");
        long sum = 0;
        for (Movie m : collectionManager.getElements()) {
            sum += m.getTotalBoxOffice();
        }
        long res = sum / collectionManager.getCollection().size();
        return ResponseEntity.ok().body("Среднее значение totalBoxOffice: %s".formatted(res));
    }
}
