package man.dan.converter.representation;

import man.dan.converter.parser.TypeError;

public abstract class Operator extends Node {
    protected Node left;
    protected Node right;

    abstract protected int getPriority();

    public Operator(Node l, Node r) throws TypeError {
        if (this instanceof GetsNumeric) {
            if (!(l instanceof Numeric) || !(r instanceof Numeric))
                throw new TypeError();
        }
        if (this instanceof GetsLogic) {
            if (!(l instanceof Logic) || !(r instanceof Logic))
                throw new TypeError();
        }

        left = l;
        right = r;
    }

    public Node getLeft() { return left; }
    public Node getRight() { return right; }

    @Override
    public Node cloneTree(Node p) throws CloneNotSupportedException {
        Operator clone = (Operator) this.clone();
        clone.setParent(p);
        clone.left = left.cloneTree(clone);
        clone.right = right.cloneTree(clone);
        return clone;
    }
}
