package man.dan.converter.unittest;

import man.dan.converter.lexer.Lexer;
import man.dan.converter.lexer.Num;
import man.dan.converter.parser.Parser;
import man.dan.converter.representation.Number;
import man.dan.converter.representation.*;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

class Trees {
    public static Node root1;
    public static Node root2;
}

class ParserTest {
    @BeforeAll
    public static void initTreeOne() throws Exception {
        /*first tree element+ 15*3- (element+  4)*10 - 5 */
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

        Minus minus1 = new Minus(plus1, mul2);
        plus1.setParent(minus1);
        mul2.setParent(minus1);

        Number five = new Number(5);
        Minus root = new Minus(minus1, five);
        five.setParent(root);
        minus1.setParent(root);

        Trees.root1 = root;
        /*first tree*/
    }

    @BeforeAll
    public static void initTreeTwo() throws Exception {
        /*second tree  (element>-5)&3<element|3=-6*/
        Element el1 = new Element();
        Number minusFive = new Number(-5);
        Greater gr1 = new Greater(el1, minusFive);
        minusFive.setParent(gr1);
        el1.setParent(gr1);

        Number three = new Number(3);
        Element el2 = new Element();
        Less ls1 = new Less(three, el2);
        three.setParent(ls1);
        el2.setParent(ls1);

        And and = new And(gr1, ls1);
        gr1.setParent(and);
        ls1.setParent(and);

        Number three2 = new Number(3);
        Number minusSix = new Number(-6);
        Equal eq = new Equal(three2, minusSix);
        three2.setParent(eq);
        minusSix.setParent(eq);

        Or or = new Or(and, eq);
        and.setParent(or);
        eq.setParent(or);

        Trees.root2 = or;
        /*second tree*/
    }

    @Test
    public void testJUnit() {
        Assert.assertEquals(1, 1);
    }

    boolean compareNode(Node v1, Node v2) {
        if (v1 == null && v2 == null)
            return true;

        if (v1 instanceof Number && v2 instanceof Number && ((Number)v1).getVal() == ((Number)v2).getVal())
            return true;

        if (v1 !=  null && v2 != null && v1.getClass() == v2.getClass())
            return true;
        else
            return false;
    }

    boolean compareSyntaxTrees(Node v1, Node v2) {
        if(!compareNode(v1, v2))
            return false;

        if (v1 instanceof Operator && v2 instanceof Operator) {
            if(!compareNode(v1.getParent(), v2.getParent()))
                return false;

            if (compareSyntaxTrees(((Operator) v1).getLeft(), ((Operator) v2).getLeft()) &&
                    compareSyntaxTrees(((Operator) v1).getRight(), ((Operator) v2).getRight()))
                return true;
            else
                return false;
        }

        return true;
    }

    @Test
    public void treeTestOne() throws Exception {
        Node root = Trees.root1;

        String expr = " map{ element+ 15*3- (element+  4)*10 - 5}";

        Lexer lex = new Lexer(expr);
        Parser parser = new Parser(lex);
        LinkedList<Call> clf = parser.analysis();

        Assert.assertEquals(clf.size(), 1);
        Assert.assertTrue(clf.element() instanceof MapCall);
        Node compVertex = clf.element().getVertex();

        Assert.assertTrue(compareSyntaxTrees(root, compVertex));
    }

    @Test public void treeTestTwo() throws Exception {
        Node root = Trees.root2;

        String expr = "filter{((element>-5)&3<element|3=-6)}";

        Lexer lex = new Lexer(expr);
        Parser parser = new Parser(lex);
        LinkedList<Call> clf = parser.analysis();

        Assert.assertEquals(clf.size(), 1);
        Assert.assertTrue(clf.element() instanceof FilterCall);
        Node compVertex = clf.element().getVertex();

        Assert.assertTrue(compareSyntaxTrees(root, compVertex));
    }
}