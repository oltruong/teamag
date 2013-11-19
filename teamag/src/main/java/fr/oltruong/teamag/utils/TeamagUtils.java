package fr.oltruong.teamag.utils;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class TeamagUtils {

    private TeamagUtils() {

    }

    public static String hashPassword(String passwordClear) {
        return Hashing.sha256().hashString(passwordClear, Charsets.UTF_8).toString();
    }

}
