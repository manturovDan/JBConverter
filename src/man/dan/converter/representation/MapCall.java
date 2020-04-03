package man.dan.converter.representation;

import man.dan.converter.parser.Parser;
import man.dan.converter.parser.TypeError;

public class MapCall extends Call {
    protected Numeric vertex;

    public MapCall(Numeric v) {
        vertex = v;
    }

    @Override
    public Node getVertex() {
        return (Node)vertex;
    }

    @Override
    public void changeVertex(Node ver) throws TypeError {
        if (!(ver instanceof Numeric))
            Parser.typeError();
        vertex = (Numeric) ver;
    }
}
