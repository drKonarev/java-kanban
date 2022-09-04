package ru.practicum.yandex.tasktracker.managers;
import java.io.*;
import java.nio.file.Path;

public class competitve {
    public static Path filePath = Path.of("src/");
    public static String pathFile = filePath.toAbsolutePath().getParent() + "/resources/";
    public static File file = new File(pathFile, "input.txt");

    public void summ(){
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line = br.readLine();
            int A = Integer.parseInt(line.split(" ")[0]);
            int B = Integer.parseInt(line.split(" ")[1]);
            System.out.println(A*B);

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Произошла ошибка во время чтения файла.");
        }
    }

    public static void main(String[] args) {


    }

}
