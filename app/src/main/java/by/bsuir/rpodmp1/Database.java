package by.bsuir.rpodmp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Database {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    private static Database instance = null;
    private static final String databaseFilePath = "/data/data/by.bsuir.feedthegarfieldcat/files/database.txt";

    private final Map<String, UserInfo> idToUser;

    private void createFileIfNecessary() {
        File file = new File(databaseFilePath);

        if (!file.exists()) {
            try (FileOutputStream fos = new FileOutputStream(databaseFilePath)) {
                fos.write("0".getBytes(StandardCharsets.UTF_8));
                System.err.println("CREATED DATABASE");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Database() throws Exception {
        createFileIfNecessary();
        idToUser = new HashMap<>();
        Scanner in = new Scanner(new File(databaseFilePath));

        int N = in.nextInt();
        for (int i = 0; i < N; ++i) {
            in.nextLine();
            String id = in.nextLine();
            String name = in.nextLine();
            int gameCount = in.nextInt();


            List<UserInfo.GameInfo> games = new ArrayList<>();
            for (int j = 0; j < gameCount; ++j) {
                in.nextLine();
                String date = in.nextLine();
                String time = in.nextLine();
                long score = in.nextLong();
                games.add(new UserInfo.GameInfo(dateFormat.parse(date + " " + time), score));
            }

            idToUser.put(id, new UserInfo(name, games));
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            try {
                instance = new Database();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public UserInfo getUser(String id) {
        return idToUser.containsKey(id) ? idToUser.get(id) : null;
    }

    public UserInfo putNewUser(String id, UserInfo user) {
        idToUser.put(id, user);
        return user;
    }

    public void saveDatabaseToFile() {
        try (PrintStream printStream = new PrintStream(databaseFilePath)) {
            printStream.println(idToUser.size());

            for (Map.Entry<String, UserInfo> pair : idToUser.entrySet()) {
                printStream.println(pair.getKey());
                UserInfo user = pair.getValue();

                printStream.println(user.getUsername());

                int gameCount = user.getUserGames().size();
                printStream.println(gameCount);

                for (int i = 0; i < gameCount; ++i) {
                    printStream.println(user.getGameAt(i));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
