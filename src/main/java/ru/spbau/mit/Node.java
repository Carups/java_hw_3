package ru.spbau.mit;

import com.sun.corba.se.spi.ior.ObjectKey;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Queue;

public class Node {
    HashMap<Character, Node> next = new HashMap<>();
    int availableTerminals = 0;
    boolean isTerminal = false;
    private final String delim = "\t";
    Node(){ }
    Node(String source, Queue<Node> queue) {
        String[] sourceNode = source.split(delim);
        isTerminal = Boolean.parseBoolean(sourceNode[0]);
        availableTerminals = Integer.parseInt(sourceNode[1]);
        if (sourceNode.length > 2) {
            for (int key = sourceNode[2].length() - 1; key >= 0; --key) {
                next.put(sourceNode[2].charAt(key), queue.poll());
            }
        }
    }

    private String rendered = null;
    private String render() {
        StringBuilder resultString = new StringBuilder();
        resultString.append(Boolean.toString(isTerminal)).
                append(delim).
                append(Integer.toString(availableTerminals)).
                append(delim);
        Object[] keys = next.keySet().toArray();
        Arrays.sort(keys);
        for (Object c : keys) {
            resultString.append((char) c);
        }
        return resultString.toString();
    }
    @Override
    public String toString() {
        if (rendered == null) {
            rendered = render();
        }
        return rendered;
    }
}