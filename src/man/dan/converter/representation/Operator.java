package man.dan.converter.representation;

public abstract class Operator extends Node {
    protected Node left;
    protected Node right;

    abstract protected int getPriority();

    public Operator(Node l, Node r) throws Exception {
        if (this instanceof GetsNumeric) {
            if (!(l instanceof Numeric) || !(r instanceof Numeric))
                throw new Exception("TYPE ERROR");
        }
        if (this instanceof GetsLogic) {
            if (!(l instanceof Logic) || !(r instanceof Logic))
                throw new Exception("TYPE ERROR");
        }

        left = l;
        right = r;
    }
}
