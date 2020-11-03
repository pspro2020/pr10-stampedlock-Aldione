import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.StampedLock;

public class Almacen {
    private final StampedLock stampedLock = new StampedLock();

    ArrayList<Integer> stock = new ArrayList<>(Arrays.asList(1, 2, 1, 1, 2, 1, 3));

    public void addProduct(int productId) {
        long stamp = stampedLock.writeLock();
        try {
            stock.add(productId);
            System.out.printf("%s The %s added the product %s on the stock\n", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName(), productId);
        } finally {
            stampedLock.unlock(stamp);
        }
    }

    public void checkStock(int productId) {
        int stock;
        long stamp = stampedLock.readLock();
        try {
            stock = countStock(productId);
            System.out.printf("%s The %s counted the stock of %s and the total is %s\n", DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(LocalTime.now()), Thread.currentThread().getName(), productId, stock);
        } finally {
            stampedLock.unlock(stamp);
        }
    }

    private int countStock(int productId) {
        int totalStock = 0;

        for (Integer integer : stock) {
            if (integer == productId) {
                totalStock++;
            }
        }
        return totalStock;
    }
}