package com.fido.tro.data.indices.entity.suffixtree;

import java.util.ArrayList;
import java.util.Collection;

public class Tree {
    private int last = 0;
    private final Node root = new Node();
    private Node activeLeaf = root;

    public Collection<Integer> search(String word) {
        Node tmpNode = searchNode(word);
        if (tmpNode == null) {
            return new ArrayList<>();
        }
        return tmpNode.getData(-1);
    }

    private Node searchNode(String word) {
        Node currentNode = root;
        Edge currentEdge;

        for (int i = 0; i < word.length(); ++i) {
            char ch = word.charAt(i);
            currentEdge = currentNode.getEdge(ch);
            if (null == currentEdge) {
                return null;
            } else {
                String label = currentEdge.getLabel();
                int lenToMatch = Math.min(word.length() - i, label.length());
                if (!word.regionMatches(i, label, 0, lenToMatch)) {
                    return null;
                }

                if (label.length() >= word.length() - i) {
                    return currentEdge.getDest();
                } else {
                    currentNode = currentEdge.getDest();
                    i += lenToMatch - 1;
                }
            }
        }

        return null;
    }

    public void put(String key, int index) throws IllegalStateException {
        if (index < last) {
            throw new IllegalStateException("The input index must not be less than any of the previously inserted ones. Got " + index + ", expected at least " + last);
        } else {
            last = index;
        }

        activeLeaf = root;

        String remainder = key;
        Node s = root;

        String text = "";
        for (int i = 0; i < remainder.length(); i++) {
            text += remainder.charAt(i);
            text = text.intern();

            Pair<Node, String> active = update(s, text, remainder.substring(i), index);
            active = canonize(active.getFirst(), active.getSecond());

            s = active.getFirst();
            text = active.getSecond();
        }

        if (null == activeLeaf.getSuffix() && activeLeaf != root && activeLeaf != s) {
            activeLeaf.setSuffix(s);
        }

    }

    private Pair<Boolean, Node> testAndSplit(final Node inputs, final String stringPart, final Character t, final String remainder, final int value) {
        Pair<Node, String> ret = canonize(inputs, stringPart);
        Node s = ret.getFirst();
        String str = ret.getSecond();

        if (!"".equals(str)) {
            Edge g = s.getEdge(str.charAt(0));

            String label = g.getLabel();
            if (label.length() > str.length() && label.charAt(str.length()) == t) {
                return new Pair<>(true, s);
            } else {
                String newLabel = label.substring(str.length());
                assert (label.startsWith(str));

                Node r = new Node();
                Edge newEdge = new Edge(str, r);

                g.setLabel(newLabel);

                r.addEdge(newLabel.charAt(0), g);
                s.addEdge(str.charAt(0), newEdge);

                return new Pair<>(false, r);
            }

        } else {
            Edge e = s.getEdge(t);
            if (null == e) {
                return new Pair<>(false, s);
            } else {
                if (remainder.equals(e.getLabel())) {
                    e.getDest().addRef(value);
                    return new Pair<>(true, s);
                } else if (remainder.startsWith(e.getLabel())) {
                    return new Pair<>(true, s);
                } else if (e.getLabel().startsWith(remainder)) {
                    Node newNode = new Node();
                    newNode.addRef(value);

                    Edge newEdge = new Edge(remainder, newNode);

                    e.setLabel(e.getLabel().substring(remainder.length()));

                    newNode.addEdge(e.getLabel().charAt(0), e);

                    s.addEdge(t, newEdge);

                    return new Pair<>(false, s);
                } else {
                    // they are different words. No prefix. but they may still share some common substr
                    return new Pair<>(true, s);
                }
            }
        }

    }

    private Pair<Node, String> canonize(final Node s, final String inputstr) {

        if ("".equals(inputstr)) {
            return new Pair<>(s, inputstr);
        } else {
            Node currentNode = s;
            String str = inputstr;
            Edge g = s.getEdge(str.charAt(0));
            // descend the tree as long as a proper label is found
            while (g != null && str.startsWith(g.getLabel())) {
                str = str.substring(g.getLabel().length());
                currentNode = g.getDest();
                if (str.length() > 0) {
                    g = currentNode.getEdge(str.charAt(0));
                }
            }

            return new Pair<>(currentNode, str);
        }
    }

    private Pair<Node, String> update(final Node inputNode, final String stringPart, final String rest, final int value) {
        Node s = inputNode;
        String tempstr = stringPart;
        Character newChar = stringPart.charAt(stringPart.length() - 1);

        Node oldroot = root;

        Pair<Boolean, Node> ret = testAndSplit(s, tempstr.substring(0, tempstr.length() - 1), newChar, rest, value);

        Node r = ret.getSecond();
        boolean endpoint = ret.getFirst();

        Node leaf;
        while (!endpoint) {
            Edge tempEdge = r.getEdge(newChar);
            if (null != tempEdge) {
                leaf = tempEdge.getDest();
            } else {
                leaf = new Node();
                leaf.addRef(value);
                Edge newedge = new Edge(rest, leaf);
                r.addEdge(newChar, newedge);
            }

            if (activeLeaf != root) {
                activeLeaf.setSuffix(leaf);
            }
            activeLeaf = leaf;

            if (oldroot != root) {
                oldroot.setSuffix(r);
            }

            oldroot = r;

            if (null == s.getSuffix()) {
                assert (root == s);
                tempstr = tempstr.substring(1);
            } else {
                Pair<Node, String> canret = canonize(s.getSuffix(), safeCutLastChar(tempstr));
                s = canret.getFirst();
                tempstr = (canret.getSecond() + tempstr.charAt(tempstr.length() - 1)).intern();
            }

            ret = testAndSplit(s, safeCutLastChar(tempstr), newChar, rest, value);
            r = ret.getSecond();
            endpoint = ret.getFirst();

        }

        if (oldroot != root) {
            oldroot.setSuffix(r);
        }
        oldroot = root;

        return new Pair<>(s, tempstr);
    }

    private String safeCutLastChar(String seq) {
        if (seq.length() == 0) {
            return "";
        }
        return seq.substring(0, seq.length() - 1);
    }
}
