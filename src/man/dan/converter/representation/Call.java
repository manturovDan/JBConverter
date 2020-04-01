package man.dan.converter.representation;

import man.dan.converter.parser.TypeError;

public abstract class Call {
    public abstract Node getVertex();

    public abstract void changeVertex(Node ver) throws TypeError;
}
