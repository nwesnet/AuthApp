package authmicroservice.Controllers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CodeStore {
    private static final Map<String, String> codeMap = new ConcurrentHashMap<>();

    public static void saveCode(String code, String username) {
        codeMap.put(code, username);
    }

    public static String getUsernameByCode(String code) {
        return codeMap.get(code);
    }

    public static void removeCode(String code) {
        codeMap.remove(code);
    }
}
