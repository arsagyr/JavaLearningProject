package ru.javastudy.threads;

import ru.javastudy.collections.PersonList;
import ru.javastudy.models.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class NCounter {
    private NCounter() { }

    /* Подсчитывает вхождения target в list, используя upToThreads потоков.
     * Печатает результат в консоль и возвращает найденное количество.
    */
    public static int countAndPrintOccurrence(PersonList list, Person target, int upToThreads)
            throws InterruptedException, ExecutionException {

        if (list == null) {
            System.out.println("Коллекция равна null. Возвращаю 0.");
            return 0;
        }
        int size = list.size();
        if (size == 0 || target == null) {
            System.out.println("Коллекция пуста или target == null. Возвращаю 0.");
            return 0;
        }

        int threads = Math.max(1, upToThreads);
        int parts = Math.min(threads, size); // не больше, чем элементов

        int base = size / parts;
        int rem = size % parts;

        ExecutorService executor = Executors.newFixedThreadPool(parts);
        List<Callable<Integer>> tasks = new ArrayList<>(parts);

        int start = 0;
        for (int i = 0; i < parts; i++) {
            final int s = start;
            final int len = base + (i < rem ? 1 : 0);
            final int e = s + len; // exclusive
            tasks.add(() -> {
                int local = 0;
                for (int idx = s; idx < e; idx++) {
                    Person p = list.get(idx);
                    if (p != null && p.equals(target)) {
                        local++;
                    }
                }
                return local;
            });
            start += len;
        }

        List<Future<Integer>> futures = executor.invokeAll(tasks);

        int total = 0;
        for (Future<Integer> f : futures) {
            total += f.get();
        }

        executor.shutdown();

        System.out.println("Найдено вхождений элемента N: " + total);

        return total;
    }
}
