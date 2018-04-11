package ru.spbau.mit;

import com.sun.corba.se.spi.ior.ObjectKey;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Arrays;

public class Node {
    HashMap<Character, Node> next = new HashMap<>();
    int availableTerminals = 0;
    boolean isTerminal = false;
    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();
        resultString.append(";");
        resultString.append(Boolean.toString(isTerminal) + ':');
        resultString.append(Integer.toString(availableTerminals) + ':');
        Object keys[] = next.keySet().toArray();
        Arrays.sort(keys);
        for (Object c :
                keys) {
            resultString.append((char) c);
        }
        return resultString.toString();
    }
}