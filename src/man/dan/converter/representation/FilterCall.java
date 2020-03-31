package man.dan.converter.representation;

public class FilterCall extends Call {
    protected Logic vertex;

    public FilterCall(Logic v) {
        vertex = v;
    }

    @Override
    public Node getVertex() {
        return (Node)vertex;
    }
}