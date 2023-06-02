package ru.mrnightfury.queuemanager;

public class Util {
    public static String formatCount(int count) {
        return Integer.toString(count);
    }
    public static String formatCount(int count, int max) {
        return count + "/" + max;
    }
}
