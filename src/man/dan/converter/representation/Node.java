package man.dan.converter.representation;

public abstract class Node {
    protected Node parent; //maybe delete in the future if it will be unnecessary

    public void setParent(Node p) {
        parent = p;
    }
}
