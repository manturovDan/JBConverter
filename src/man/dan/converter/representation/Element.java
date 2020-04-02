package man.dan.converter.representation;

public class Element extends Operand {
    @Override
    protected Element clone() throws CloneNotSupportedException {
        return (Element) super.clone();
    }

    @Override
    public String toString() {
        return "element";
    }
}
