import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.HexFormat;

public class PasswordAuthentication {

    private static final Scanner input = new Scanner(System.in);
    private static final Map<String, UserRecord> users = new HashMap<>();
    private static final SecureRandom random = new SecureRandom();

    private static final Path USERS_FILE = Path.of("users.txt");

    public static void main(String[] args) {

        try {
            loadUsers();
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
            return;
        }

        boolean running = true;

        while (running) {
            System.out.println("\n=== Password Authentication ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    register();
                    break;

                case "2":
                    login();
                    break;

                case "3":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        input.close();
    }

    private static void register() {

        System.out.print("Enter username: ");
        String username = input.nextLine().trim();

        if (username.isEmpty() || username.contains("|")) {
            System.out.println("Invalid username.");
            return;
        }

        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }

        System.out.print("Enter password: ");
        String password = input.nextLine();

        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        String salt = generateSalt();
        String passwordHash = hashPassword(password, salt);

        UserRecord user = new UserRecord(salt, passwordHash);

        String userData = username + "|" + salt + "|" + passwordHash
                + System.lineSeparator();

        try {
            Files.writeString(
                    USERS_FILE,
                    userData,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            users.put(username, user);

            System.out.println("Registration successful!");

        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    private static void login() {

        System.out.print("Enter username: ");
        String username = input.nextLine().trim();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        UserRecord user = users.get(username);

        if (user == null) {
            System.out.println("Invalid username or password.");
            return;
        }

        String enteredHash = hashPassword(password, user.salt);

        boolean authenticated = MessageDigest.isEqual(
                user.passwordHash.getBytes(StandardCharsets.US_ASCII),
                enteredHash.getBytes(StandardCharsets.US_ASCII)
        );

        if (authenticated) {
            System.out.println("Login successful! Welcome, " + username + ".");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static String generateSalt() {

        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        return HexFormat.of().formatHex(saltBytes);
    }

    private static String hashPassword(String password, String salt) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String passwordWithSalt = password + salt;

            byte[] hashBytes = digest.digest(
                    passwordWithSalt.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available.", e);
        }
    }

    private static void loadUsers() throws IOException {

        if (!Files.exists(USERS_FILE)) {
            return;
        }

        for (String line : Files.readAllLines(
                USERS_FILE, StandardCharsets.UTF_8)) {

            String[] parts = line.split("\\|", -1);

            if (parts.length == 3) {
                String username = parts[0];
                String salt = parts[1];
                String passwordHash = parts[2];

                users.put(username, new UserRecord(salt, passwordHash));
            }
        }
    }

    private static class UserRecord {

        private final String salt;
        private final String passwordHash;

        private UserRecord(String salt, String passwordHash) {
            this.salt = salt;
            this.passwordHash = passwordHash;
        }
    }
}