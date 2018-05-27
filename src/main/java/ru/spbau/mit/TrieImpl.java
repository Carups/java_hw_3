package ru.spbau.mit;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class TrieImpl implements Trie, StreamSerializable {
    private Node root = new Node();
    private String rendered = null;
    private final String delim = "\n";
    private String render() {
        StringBuilder answerString = new StringBuilder();
        LinkedList<Node> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            answerString.append(delim).
                    append(currentNode.toString());
            Object[] keys = currentNode.next.keySet().toArray();
            Arrays.sort(keys);
            for (Object c : keys) {
                queue.add(currentNode.next.get(c));
            }
        }
        return answerString.toString();
    }
    @Override
    public String toString() {
        if (rendered == null) {
            rendered = render();
        }
        return rendered;
    }
    public void serialize(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeUTF(this.toString());
    }
    private void fromString(String source){
        /*Процесс обратный созданию строки. Собираем бор начиная с листьев бора.
          Так как у листьев нет ссылок, то они ничего не берут из очереди, а только кладут сами себя.
          Вершины же не листья будут вытаскивать вершины и класть себя. Порядок гарантируется тем, что
          при формирования строки был использован bfs.*/
        LinkedList<Node> queue = new LinkedList<>();
        String[] sourceForNodes = source.split(delim);
        for(int i = sourceForNodes.length - 1; i > 0; --i) {
            Node curNode = new Node(sourceForNodes[i], queue);
            queue.offer(curNode);
        }
        root = queue.peek();
    }
    public void deserialize(InputStream in) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        String inputString = dis.readUTF();
        fromString(inputString);
    }
    public boolean add (String element) {
        rendered = null;
        if (!this.contains(element)) {
            Node current = root;
            for (int index = 0; index < element.length(); ++index) {
                Node next = current.next.get(element.charAt(index));
                if (next == null) {
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
    public boolean remove (String element) {
        rendered = null;
        if (this.contains(element)){
            Node current = root;
            root.availableTerminals--;
            for (int index = 0; index < element.length(); ++index) {
                Node next = current.next.get(element.charAt(index));
                next.availableTerminals--;
                if (next.availableTerminals == 0) {
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
    public boolean contains (String element) {
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
    public int howManyStartsWithPrefix(String prefix) {
        Node current = root;
        for (int index = 0; index < prefix.length(); ++index) {
            current = current.next.get(prefix.charAt(index));
            if (current == null) {
                return 0;
            }
        }
        return current.availableTerminals;
    }
}
