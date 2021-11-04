package ai.aimachineserver.utils;

import java.util.HashMap;
import java.util.Map;

public final class GeneralUtils {
    private GeneralUtils() {
    }

    public static Map<String, String> parseQueryParams(String queryString) {
        String[] split = queryString.split("[=&]");
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            if (i % 2 == 0) {
                params.put(split[i], split[i + 1]);
            }
        }
        return params;
    }
}
