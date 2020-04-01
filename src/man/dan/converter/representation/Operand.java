package man.dan.converter.representation;

public abstract class Operand extends Node implements Numeric {
    @Override
    public Node cloneTree(Node p) throws CloneNotSupportedException {
        Operator clone = (Operator) this.clone();
        clone.setParent(p);
        return clone;
    }
}
