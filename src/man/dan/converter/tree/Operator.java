package man.dan.converter.tree;

public abstract class Operator extends Node {
    protected Node left;
    protected Node right;

    abstract protected int getPriority();
}
