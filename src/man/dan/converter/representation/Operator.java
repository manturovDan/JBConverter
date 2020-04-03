package man.dan.converter.representation;

import man.dan.converter.parser.Parser;
import man.dan.converter.parser.TypeError;

public abstract class Operator extends Node {
    protected Node left;
    protected Node right;

    public Operator(Node l, Node r) {
        if (this instanceof GetsNumeric) {
            if (!(l instanceof Numeric) || !(r instanceof Numeric))
                Parser.typeError();
        }
        if (this instanceof GetsLogic) {
            if (!(l instanceof Logic) || !(r instanceof Logic))
                Parser.typeError();
        }

        left = l;
        right = r;
    }

    public Node getLeft() { return left; }
    public Node getRight() { return right; }

    @Override
    public Node cloneTree(Node p) throws CloneNotSupportedException, TypeError {
        Operator clone = this.clone();
        clone.setParent(p);
        clone.setLeft(left.cloneTree(clone));
        clone.setRight(right.cloneTree(clone));
        return clone;
    }

    public void setLeft(Node l) {
        if (this instanceof GetsNumeric) {
            if (!(l instanceof Numeric))
                Parser.typeError();
        }
        if (this instanceof GetsLogic) {
            if (!(l instanceof Logic))
                Parser.typeError();
        }

        left = l;
    }

    public void setRight(Node r) {
        if (this instanceof GetsNumeric) {
            if (!(r instanceof Numeric))
                Parser.typeError();
        }
        if (this instanceof GetsLogic) {
            if (!(r instanceof Logic))
                Parser.typeError();
        }

        right = r;
    }

    @Override
    protected Operator clone() throws CloneNotSupportedException {
        return (Operator)super.clone();
    }
}
