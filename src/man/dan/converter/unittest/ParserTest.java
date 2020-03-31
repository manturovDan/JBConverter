package man.dan.converter.unittest;

import com.sun.source.tree.AssertTree;
import man.dan.converter.lexer.Lexer;
import man.dan.converter.lexer.Num;
import man.dan.converter.parser.Parser;
import man.dan.converter.representation.*;
import man.dan.converter.representation.Number;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @Test
    public void testJUnit() {
        Assert.assertEquals(1, 1);
    }

    boolean compareNode(Node v1, Node v2) {
        if (v1 == null && v2 == null)
            return true;

        if (v1 instanceof Number && v2 instanceof Number && ((Number)v1).getVal() == ((Number)v2).getVal())
            return true;

        return v1 !=  null && v2 !=  null && v1.getClass() == v2.getClass();
    }

    boolean compareSyntaxTrees(Node v1, Node v2) {
        if(!compareNode(v1, v2))
            return false;

        if (v1 instanceof Operator && v2 instanceof Operator) {
            if(!compareNode(v1.getParent(), v2.getParent()))
                return false;

            return compareSyntaxTrees(((Operator) v1).getLeft(), ((Operator) v2).getRight());
        }

        return false;
    }

    @Test
    public void treeTestOne() throws Exception {
        Element el1 = new Element();
        Number fifteen = new Number(15);
        Number three = new Number(3);
        Multiple mul1 = new Multiple(fifteen, three);
        three.setParent(mul1);
        fifteen.setParent(mul1);
        Plus plus1 = new Plus(el1, mul1);
        el1.setParent(plus1);
        mul1.setParent(plus1);

        Element el2 = new Element();
        Number four = new Number(4);
        Plus plus2 = new Plus(el2, four);
        el2.setParent(plus2);
        four.setParent(plus2);
        Number ten = new Number(10);
        Multiple mul2 = new Multiple(plus2, ten);
        plus2.setParent(mul2);
        ten.setParent(mul2);

        Number five = new Number(5);
        Minus minus = new Minus(mul2, five);
        mul2.setParent(minus);
        five.setParent(minus);

        Minus root = new Minus(plus1, minus);
        plus1.setParent(root);
        minus.setParent(root);



        String expr = " map{element+15*3-(element+4)*10-5}";

        Lexer lex = new Lexer(expr);
        Parser parser = new Parser(lex);
        LinkedList<Call> clf = parser.analysis();

        Assert.assertEquals(clf.size(), 1);
        Assert.assertTrue(clf.element() instanceof MapCall);

        Node compVertex = clf.element().getVertex();

        Assert.assertTrue(compareSyntaxTrees(root, compVertex));
    }
}