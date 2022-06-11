package users;

import java.util.HashMap;
import java.util.Map;

public class UserHandler {
    private static Map<String, String> usersData = new HashMap<>(){{
        put("login", "password");
        put("vasya", "123");
    }};

    public static boolean findUserByUsername(String userName) {
        return usersData.containsKey(userName);
    }

    public static boolean checkPassword(String userName, String password) {
        return usersData.get(userName) != null && usersData.get(userName).equals(password);
    }
}
