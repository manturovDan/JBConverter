package man.dan.converter.representation;

import man.dan.converter.parser.Parser;
import man.dan.converter.parser.TypeError;

public class FilterCall extends Call {
    protected Logic vertex;

    public FilterCall(Logic v) {
        vertex = v;
    }

    @Override
    public Node getVertex() {
        return (Node)vertex;
    }

    @Override
    public void setVertex(Node ver) throws TypeError {
        if ((ver instanceof Logic))
            vertex = (Logic)ver;
        else
            throw new TypeError();
    }

    @Override
    public void changeVertex(Node ver) throws TypeError {
        if (!(ver instanceof Logic))
            Parser.typeError();
        vertex = (Logic) ver;
    }
}
