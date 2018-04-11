package ru.spbau.mit;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class TrieImpl implements Trie, StreamSerializable {
    private Node root = new Node();

    @Override
    public String toString() {
        StringBuilder answerString = new StringBuilder();
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(root);
        while (queue.size() != 0)
        {
            Node currentNode = queue.getFirst();
            queue.removeFirst();
            answerString.append(currentNode.toString());
            Object keys[] = currentNode.next.keySet().toArray();
            Arrays.sort(keys);
            for (Object c :
                    keys) {
                queue.add(currentNode.next.get(c));
            }
        }
        return answerString.toString();
    }

    public void serialize(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeChars(this.toString());
    }

    private void fromString(String source){
        LinkedList<Node> queue = new LinkedList<>();
        String sourceForNodes[] = source.split(";");

        for(int i = sourceForNodes.length - 1; i > 0; --i)
        {

            String sourceNode[] = sourceForNodes[i].split(":");
            Node curNode = new Node();
            curNode.isTerminal = Boolean.parseBoolean(sourceNode[0]);
            curNode.availableTerminals = Integer.parseInt(sourceNode[1]);
            if (sourceNode.length > 2) {
                for (int key = sourceNode[2].length() - 1; key >= 0; --key) {
                    curNode.next.put(sourceNode[2].charAt(key), queue.getFirst());
                    queue.removeFirst();
                }
            }
            queue.add(curNode);

        }
        root = queue.getFirst();
    }

    public void deserialize(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        char c;
        StringBuilder inputStringBuilder = new StringBuilder();
        try {
            while (true) {

                c = dis.readChar();
                inputStringBuilder.append(c);
            }
        } catch (EOFException e){

        }
        String inputString = inputStringBuilder.toString();
        fromString(inputString);
    }
    public boolean add (String element)
    {
        if (!this.contains(element)) {
            Node current = root;
            for (int index = 0; index < element.length(); ++index) {
                Node next = current.next.get(element.charAt(index));
                if (next == null)
                {
                    next = new Node();
                    current.next.put(element.charAt(index), next);
                }
                current.availableTerminals++;
                current = next;
            }
            current.availableTerminals++;
            current.isTerminal = true;
            return true;
        }
        else {
            return false;
        }
    }
    public boolean remove (String element)
    {
        if (this.contains(element)){
            Node current = root;
            root.availableTerminals--;
            for (int index = 0; index < element.length(); ++index) {
                Node next = current.next.get(element.charAt(index));

                next.availableTerminals--;
                if (next.availableTerminals == 0)
                {
                    current.next.remove(element.charAt(index));
                    return true;
                }
                current = next;
            }
            current.isTerminal = false;
            return true;
        }
        return false;
    }
    public boolean contains (String element)
    {
        Node current = root;
        for (int index = 0; index < element.length(); ++index) {
            current = current.next.get(element.charAt(index));
            if(current == null) {
                return false;
            }
        }
        return current.isTerminal;
    }
    public int size ()
    {
        return root.availableTerminals;
    }
    public int howManyStartsWithPrefix(String prefix)
    {
        Node current = root;
        for (int index = 0; index < prefix.length(); ++index) {
            current = current.next.get(prefix.charAt(index));
            if (current == null)
                return 0;
        }
        return current.availableTerminals;
    }

}
