package licenta_imobiliare.util;

import java.util.Random;

public class IDGenerator {
    private static final Random random = new Random();

    public static String generateUniqueId() {
        int idLength = 8;
        StringBuilder idBuilder = new StringBuilder(idLength);

        for (int i = 0; i < idLength; i++) {
            idBuilder.append(random.nextInt(10));
        }

        return idBuilder.toString();
    }

    public static String generateCustomId(String prefix) {
        String uniquePart = generateUniqueId(); //
        return prefix + "-" + uniquePart;
    }

    public static String generateCustomIdWithProjectName(String projectName) {
        String sanitizedProjectName = projectName.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        return sanitizedProjectName + "-" + generateUniqueId();
    }
}
