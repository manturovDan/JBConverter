package man.dan.converter.representation;

import man.dan.converter.parser.TypeError;

public abstract class Node implements Cloneable {
    protected Node parent; //maybe delete in the future if it will be unnecessary

    public void setParent(Node p) {
        parent = p;
    }

    public Node getParent() { return parent; }

    public abstract Node cloneTree(Node p) throws CloneNotSupportedException, TypeError;

    @Override
    protected Node clone() throws CloneNotSupportedException {
        return (Node)super.clone();
    }
}
