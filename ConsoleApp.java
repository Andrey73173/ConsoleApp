import java.io.*;
import java.time.*;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class ConsoleApp {
    private static final String FILE_NAME = "records.txt";
    private static List < Record > records = new ArrayList < > ();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static void main(String[] args) {
        loadRecordsFromFile();

        System.out.print("Доступные команды: \n #read - вывести содержимое файла \n #write - сделать запись в файл\n Ctrl+C - Закрыть программу \n ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду: \n > ");
            String command = scanner.nextLine();

            if (command.startsWith("#read")) {
                printRecords();
            } else if (command.startsWith("#write")) {
                writeRecord(scanner);
            } else {
                System.out.println("Неправильная команда. Попробуйте снова.");
            }
        }
    }

    private static void loadRecordsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("/");
                Record record = new Record(parts[0], LocalDateTime.parse(parts[1])); 
                records.add(record);
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    private static void printRecords() {
        System.out.println("================================"); 
        for (Record record: records) {
            System.out.println(record.toString());
        }
        System.out.println("================================");
    }

    private static void writeRecord(Scanner scanner) {
        System.out.print("Введите текст записи: ");
        String text = scanner.nextLine();
        Record record = new Record(text, LocalDateTime.now());
        records.add(record);
        saveRecordsToFile();
        System.out.println("Запись добавлена успешно.");
    }

    private static void saveRecordsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Record record: records) {
                writer.write(record.getText() + "/" + record.getDateTime().toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи файла: " + e.getMessage());
        }
    }

    private static class Record {
        private String text;
        private LocalDateTime dateTime;

        public Record(String text, LocalDateTime dateTime) {
            this.text = text;
            this.dateTime = dateTime;
        }

        public String getText() {
            return text;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        @Override
        public String toString() {
            return text + " (" + dateTime.format(formatter) + ")"; 
        }
    }
}