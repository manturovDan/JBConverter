package man.dan.converter.transformer;

import man.dan.converter.parser.TypeError;
import man.dan.converter.representation.And;
import man.dan.converter.representation.Call;
import man.dan.converter.representation.FilterCall;
import man.dan.converter.representation.MapCall;

import java.util.LinkedList;
import java.util.ListIterator;

public class Merger {
    LinkedList<Call> chain;

    public Merger(LinkedList<Call> ch) {
        chain = ch;
    }

    public void transform() throws Exception {
        ListIterator<Call> iter = chain.listIterator();

        //check empty list

        Call previous;
        Call current = iter.next();
        while (iter.hasNext()) {

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

                mergeMF((MapCall)previous, (FilterCall) current, chain.listIterator(iter.nextIndex())); //maybe optimize with own list
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

    protected void mergeMM(MapCall prev, MapCall cur) {
        //
    }

    protected void mergeMF(MapCall prev, FilterCall cur, ListIterator<Call> replIter) {
        System.out.println(".");
        while (replIter.hasNext()) {
            System.out.print(replIter.next() + " ");
        }
        System.out.println("\n.");
        //
    }
}
