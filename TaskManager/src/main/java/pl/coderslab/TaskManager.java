package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {
    static String[][] tasksArray;
    static String filename = "tasks.csv";

    public static void main(String[] args) {
        showOptions();
        tasksArray = tasksFromCsvToArray(filename);

        try {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String input = scanner.next();
                switch (input) {
                    case "add":
                        addTask();
                        break;
                    case "remove":
                        removeTask(tasksArray,getNumber());
                        System.out.println("Task successfully deleted");
                        break;
                    case "list":
                        taskList();
                        break;
                    case "exit":
                        exit(filename);
                        System.out.println(ConsoleColors.RED+"Bye, bye.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println(ConsoleColors.BLUE+"Please select a correct option");
                }
                showOptions();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void showOptions() {
        String[] options = {"add", "remove", "list", "exit"};
        System.out.println(ConsoleColors.BLUE+"Please select an option"+ConsoleColors.RESET);
        for (int i = 0; i < options.length; i++) {
            System.out.println(options[i]);
        }
    }

    static String[][] tasksFromCsvToArray(String filename) {
        int counter = 0;
        String[][] tasksArray = null;
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                scanner.nextLine();
                counter++;
            }
            scanner.close();
            tasksArray = new String[counter][];
            scanner = new Scanner(file);
            for (int i = 0; i < counter; i++) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] values = line.split(",");
                    tasksArray[i] = values;
                }
            }
        } catch (FileNotFoundException a) {
            System.out.println("File not found");
        }
        return tasksArray;
    }

    private static void addTask() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please add task description");
            String description = scanner.nextLine();
            System.out.println("Please add task due date");
            String dueDate = scanner.nextLine();
            System.out.println("Is your task important: true/false");
            String isImportant = scanner.nextLine();
            String[] newTask = {description, dueDate, isImportant};
            tasksArray = Arrays.copyOf(tasksArray, tasksArray.length + 1);
            tasksArray[tasksArray.length - 1] = newTask;
        } catch (Exception e) {
            System.out.println("Couldn't read input");
        }

    }

    public static boolean graterOrEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static int getNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select a number to remove from the task list");
        String numberToBeRemoved = scanner.nextLine();
        while (!graterOrEqualZero(numberToBeRemoved)) {
            System.out.println("Not a number. Please enter a number greater or equal to zero");
            numberToBeRemoved = scanner.nextLine();
        }
        return Integer.parseInt(numberToBeRemoved);
    }

    private static void removeTask(String[][] tab,int index) {
        try {
            if (index <tab.length) {
              tasksArray= ArrayUtils.remove(tab,index);
            } else {
                System.out.println("Element does not exist in tasks");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element not exist in tasks");
        }
    }

    private static void taskList() {
        for (int i = 0; i < tasksArray.length; i++) {
            String taskLine = i+"."+"";
            for (int j = 0; j < tasksArray[i].length; j++) {
                taskLine += tasksArray[i][j] + " ";
            }
            System.out.println(taskLine);
        }
    }

    private static void exit(String file) {
        Path dir = Paths.get(file);
        String[] lines = new String[tasksArray.length];
        for (int i = 0; i < tasksArray.length; i++) {
            lines[i] = String.join(",", tasksArray[i]);
        }
        try {
            Files.write(dir, Arrays.asList(lines), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}