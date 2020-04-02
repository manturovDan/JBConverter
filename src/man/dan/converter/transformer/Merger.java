package man.dan.converter.transformer;

import man.dan.converter.parser.TypeError;
import man.dan.converter.representation.*;

import java.util.LinkedList;
import java.util.ListIterator;

public class Merger {
    protected LinkedList<Call> chain;
    protected Node elementReplacement;

    public Merger(LinkedList<Call> ch) {
        chain = ch;
    }

    public void transform() throws Exception {
        ListIterator<Call> iter = chain.listIterator();

        //TODO check empty list

        Call previous;
        Call current = iter.next();

        while (iter.hasNext()) {
            if (elementReplacement != null && !(elementReplacement instanceof Element)) {
                gainTree(current.getVertex(), elementReplacement);
                if (current instanceof MapCall)
                    elementReplacement = null;
            }

            System.out.println(chain);

            previous = current;
            current = iter.next();

            if (previous instanceof FilterCall && current instanceof FilterCall) {
                mergeFF((FilterCall) previous, (FilterCall) current);
            }
            else if (previous instanceof MapCall && current instanceof MapCall) {
                mergeMM((MapCall)previous, (MapCall) current);
            }
            else if (previous instanceof MapCall && current instanceof FilterCall) {

                mergeMF((MapCall)previous, (FilterCall) current);
            }
            else if (previous instanceof FilterCall && current instanceof MapCall) {
                continue;
            }
            else
                throw new Exception("Something bad");

            iter.previous();
            iter.previous();
            iter.remove();
            if (iter.hasPrevious())
                iter.previous();

            if (iter.hasPrevious())
                iter.previous();
            current = iter.next();

        }

        System.out.println(chain);
    }

    protected void mergeFF(FilterCall prev, FilterCall cur) throws TypeError {
        And and = new And(prev.getVertex(), cur.getVertex());
        prev.getVertex().setParent(and);
        cur.getVertex().setParent(and);
        cur.changeVertex(and);
    }

    protected void mergeMM(MapCall prev, MapCall cur) throws TypeError, CloneNotSupportedException {
        elementReplacement = prev.getVertex();
    }

    protected void mergeMF(MapCall prev, FilterCall cur) {
        elementReplacement = prev.getVertex();
    }

    protected void gainTree(Node frame, Node growth) throws CloneNotSupportedException, TypeError { //growth only Numeric
        boolean freeLeft = true;
        boolean freeRight = true;

        if (frame.getLeft() instanceof Element) {
            growth = growth.cloneTree(frame);
            frame.setLeft(growth);
            freeLeft = false;
        }
        if (frame.getRight() instanceof Element) {
            growth = growth.cloneTree(frame);
            frame.setRight(growth);
            freeRight = false;
        }

        if (frame.getLeft() instanceof Operator && freeLeft)
            gainTree((Operator) frame.getLeft(), growth);

        if (frame.getRight() instanceof Operator && freeRight)
            gainTree((Operator) frame.getRight(), growth);
    }
}
