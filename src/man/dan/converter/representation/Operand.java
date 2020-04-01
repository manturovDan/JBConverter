package man.dan.converter.representation;

public abstract class Operand extends Node implements Numeric {
    @Override
    public Node cloneTree(Node p) throws CloneNotSupportedException {
        Node clone = (Node) this.clone();
        clone.setParent(p);
        return clone;
    }

    @Override
    protected Operand clone() throws CloneNotSupportedException {
        return (Operand)super.clone();
    }
}
