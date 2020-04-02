package man.dan.converter.unittest;

import com.sun.source.tree.Tree;
import man.dan.converter.lexer.Lexer;
import man.dan.converter.lexer.Num;
import man.dan.converter.parser.Parser;
import man.dan.converter.parser.SyntaxError;
import man.dan.converter.parser.TypeError;
import man.dan.converter.representation.Number;
import man.dan.converter.representation.*;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

class Trees {
    public static Node root1;
    public static Node root2;
    public static Node root3;
    public static Node root4;
}

class ParserTest {
    @BeforeAll
    public static void initTreeOne() throws TypeError {
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
    public static void initTreeTwo() throws TypeError {
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

    @BeforeAll
    public static void initTreeThree() throws TypeError {
        /*third tree element*element*element+15>-800*19*/
        Element el1 = new Element();
        Element el2 = new Element();
        Multiple mul1 = new Multiple(el1, el2);
        el1.setParent(mul1);
        el2.setParent(mul1);
        Element el3 = new Element();
        Multiple mul2 = new Multiple(mul1, el3);
        el3.setParent(mul2);
        mul1.setParent(mul2);
        Number fifteen = new Number(15);
        Plus plus = new Plus(mul2, fifteen);
        mul2.setParent(plus);
        fifteen.setParent(plus);

        Number minus800 = new Number(-800);
        Number nineteen = new Number(19);
        Multiple mul3 = new Multiple(minus800, nineteen);
        minus800.setParent(mul3);
        nineteen.setParent(mul3);

        Greater gr1 = new Greater(plus, mul3);
        plus.setParent(gr1);
        mul3.setParent(gr1);

        Trees.root3 = gr1;
        /*third tree*/
    }

    @BeforeAll
    public static void initTreeFour() throws TypeError {
        /*fourth tree element=element|-5*element>element*/
        Element el1 = new Element();
        Element el2 = new Element();
        Equal eq = new Equal(el1, el2);
        el1.setParent(eq);
        el1.setParent(eq);

        Number minusFive = new Number(-5);
        Element el3 = new Element();
        Multiple mul1 = new Multiple(minusFive, el3);
        el3.setParent(mul1);
        minusFive.setParent(mul1);

        Element el4 = new Element();
        Greater gr = new Greater(mul1, el4);
        el4.setParent(gr);
        mul1.setParent(gr);

        Or or = new Or(eq, gr);
        eq.setParent(or);
        gr.setParent(or);

        Trees.root4 = or;

        /*fourth tree*/
    }

    @Test
    public void testJUnit() {
        Assert.assertEquals(1, 1);
    }

    boolean compareNode(Node v1, Node v2) {
        if (v1 == null && v2 == null)
            return true;

        if ((v1 instanceof Number) && (v2 instanceof Number)) {
            if ((((Number) v1).getVal() == ((Number) v2).getVal()))
                return true;
            else
                return false;
        }

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
    public void treeTestZero() throws SyntaxError, TypeError {
        Node root = Trees.root1;
        ((Number)(((Operator)root).getRight())).setVal(-5);

        String expr = " map{ element+ 15*3- (element+  4)*10 - -5}";

        Lexer lex = new Lexer(expr);
        Parser parser = new Parser(lex);
        LinkedList<Call> clf = parser.analysis();

        Assert.assertEquals(clf.size(), 1);
        Assert.assertTrue(clf.element() instanceof MapCall);
        Node compVertex = clf.element().getVertex();

        Assert.assertTrue(compareSyntaxTrees(root, compVertex));

        ((Number)(((Operator)root).getRight())).setVal(5);
    }

    @Test
    public void treeTestOne() throws SyntaxError, TypeError {
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

    @Test
    public void treeTestNotEqual() throws SyntaxError, TypeError {
        Node root = Trees.root1;

        String expr = " map{ element+ -15*3- (element+  4)*10 - 5}";

        Lexer lex = new Lexer(expr);
        Parser parser = new Parser(lex);
        LinkedList<Call> clf = parser.analysis();

        Assert.assertEquals(clf.size(), 1);
        Assert.assertTrue(clf.element() instanceof MapCall);
        Node compVertex = clf.element().getVertex();

        Assert.assertFalse(compareSyntaxTrees(root, compVertex));
    }

    @Test
    public void treeTestTwo() throws SyntaxError, TypeError {
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

    @Test
    public void treesPairTest12() throws SyntaxError, TypeError {
        Node root1 = Trees.root1;
        Node root2 = Trees.root2;

        String expr = " filter{((element> -5)&3<element |3=-6)} %>% map{ ( element+ 15*3- (element+  4)*10 - 5 ) } ";

        Lexer lex = new Lexer(expr);
        Parser parser = new Parser(lex);
        LinkedList<Call> clf = parser.analysis();

        Assert.assertEquals(clf.size(), 2);
        Assert.assertTrue(clf.element() instanceof FilterCall);
        Assert.assertTrue(clf.get(1) instanceof MapCall);
        Node compVertex1 = clf.element().getVertex();
        Node compVertex2 = clf.get(1).getVertex();

        Assert.assertTrue(compareSyntaxTrees(root2, compVertex1));
        Assert.assertTrue(compareSyntaxTrees(root1, compVertex2));
    }

    @Test
    public void treesPairTest21() throws SyntaxError, TypeError {
        Node root1 = Trees.root1;
        Node root2 = Trees.root2;

        String expr = " map{ ((( element+ 15*3- (element+  4)*10 - 5 ) ))}%>%filter{(((element> -5)&3<element |3=(-6)))}";

        Lexer lex = new Lexer(expr);
        Parser parser = new Parser(lex);
        LinkedList<Call> clf = parser.analysis();

        Assert.assertEquals(clf.size(), 2);
        Assert.assertTrue(clf.element() instanceof MapCall);
        Assert.assertTrue(clf.get(1) instanceof FilterCall);
        Node compVertex1 = clf.element().getVertex();
        Node compVertex2 = clf.get(1).getVertex();

        Assert.assertTrue(compareSyntaxTrees(root1, compVertex1));
        Assert.assertTrue(compareSyntaxTrees(root2, compVertex2));
    }

    @Test
    public void treesQuadrupleTest1234() throws SyntaxError, TypeError {
        String expr = "map{ ((( element+ 15*3- (element+  4)*10 - 5 ) ))} %>% filter{(((element> -5)&3<element |3=(-6)))}%>% filter{element*element*element+15>-800*19}%>%filter{element=element|-5*element>element}";

        Lexer lex = new Lexer(expr);
        Parser parser = new Parser(lex);
        LinkedList<Call> clf = parser.analysis();

        Assert.assertEquals(clf.size(), 4);

        ListIterator<Call> iter = clf.listIterator();
        Call cl1 = iter.next();
        Call cl2 = iter.next();
        Call cl3 = iter.next();
        Call cl4 = iter.next();

        Assert.assertTrue(cl1 instanceof MapCall);
        Assert.assertTrue(cl2 instanceof FilterCall);
        Assert.assertTrue(cl3 instanceof FilterCall);
        Assert.assertTrue(cl4 instanceof FilterCall);

        Assert.assertTrue(compareSyntaxTrees(Trees.root1, cl1.getVertex()));
        Assert.assertTrue(compareSyntaxTrees(Trees.root2, cl2.getVertex()));
        Assert.assertTrue(compareSyntaxTrees(Trees.root3, cl3.getVertex()));
        Assert.assertTrue(compareSyntaxTrees(Trees.root4, cl4.getVertex()));
    }

    @Test
    public void treesSixEl422313() throws SyntaxError, TypeError {
        String expr = "filter{(element=element)|-5*element>element} %>% filter{ (element>-5)&3<element|3=-6 } %>% filter{ (element>-5)&3<element|3=-6 }%>%filter{(((element))*element*element+15>-800*19)} %>% map{ ((( element+ 15*3- (element+  4)*10 - 5 ) ))} %>% filter{((element)*(element)*element+15>(-800*19))}";

        Lexer lex = new Lexer(expr);
        Parser parser = new Parser(lex);
        LinkedList<Call> clf = parser.analysis();

        Assert.assertEquals(clf.size(), 6);

        ListIterator<Call> iter = clf.listIterator();
        Call cl1 = iter.next();
        Call cl2 = iter.next();
        Call cl3 = iter.next();
        Call cl4 = iter.next();
        Call cl5 = iter.next();
        Call cl6 = iter.next();

        Assert.assertTrue(cl1 instanceof FilterCall);
        Assert.assertTrue(cl2 instanceof FilterCall);
        Assert.assertTrue(cl3 instanceof FilterCall);
        Assert.assertTrue(cl4 instanceof FilterCall);
        Assert.assertTrue(cl5 instanceof MapCall);
        Assert.assertTrue(cl6 instanceof FilterCall);

        Assert.assertTrue(compareSyntaxTrees(Trees.root4, cl1.getVertex()));
        Assert.assertTrue(compareSyntaxTrees(Trees.root2, cl2.getVertex()));
        Assert.assertTrue(compareSyntaxTrees(Trees.root2, cl3.getVertex()));
        Assert.assertTrue(compareSyntaxTrees(Trees.root3, cl4.getVertex()));
        Assert.assertTrue(compareSyntaxTrees(Trees.root1, cl5.getVertex()));
        Assert.assertTrue(compareSyntaxTrees(Trees.root3, cl6.getVertex()));
    }

}