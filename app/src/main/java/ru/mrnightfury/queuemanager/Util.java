package ru.mrnightfury.queuemanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Util {
    public static String formatCount(int count) {
        return Integer.toString(count);
    }
    public static String formatCount(int count, int max) {
        return count + "/" + max;
    }
    public static String parseUpdateString(String str, String login) {
        try {
            JSONObject json = new JSONObject(str);
            String name = json.getString("name");
            JSONArray array = json.getJSONArray("queuedPeople");
            for (int i = 0; i < array.length(); i++) {
                JSONObject u = array.getJSONObject(i);
                if (!u.getBoolean("frozen")) {
                    if (u.getString("login").equals(login)) {
                        return name;
                    }
                }
            }
            return "WTF";
        } catch (JSONException e) {
            return "Error parsing json";
        }
    }
}
