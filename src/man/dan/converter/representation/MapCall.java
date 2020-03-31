package man.dan.converter.representation;

public class MapCall extends Call {
    protected Numeric vertex;

    public MapCall(Numeric v) {
        vertex = v;
    }

    @Override
    public Node getVertex() {
        return (Node)vertex;
    }
}