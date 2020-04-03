package man.dan.converter.transformer;

import man.dan.converter.representation.*;

import java.util.LinkedList;

public class MakeString {
    public static String view(LinkedList<Call> callChain) {
        StringBuilder constructAns = new StringBuilder();
        boolean arrow = false;

        for(Call tree : callChain) {
            if (arrow)
                constructAns.append("%>%");
            else
                arrow = true;

            if (tree instanceof FilterCall)
                constructAns.append("filter{");
            else if (tree instanceof MapCall)
                constructAns.append("map{");

            if (tree.getVertex() instanceof Operator)
                constructAns.append("(");

            constructAns.append(printNode(tree.getVertex()));

            if (tree.getVertex() instanceof Operator)
                constructAns.append(")");

            constructAns.append("}");
        }

        return constructAns.toString();
    }

    public static StringBuilder printNode(Node vertex) {
        StringBuilder res = new StringBuilder();
        boolean isOperator = false;

        if (vertex instanceof Operator) {
            isOperator = true;
        }

        if (isOperator) {
            Node leftChild;
            boolean brackets = false;
            leftChild = ((Operator) vertex).getLeft();
            if (leftChild instanceof Operator)
                brackets = true;
            if(brackets)
                res.append("(");
            res.append(printNode(((Operator) vertex).getLeft()));
            if(brackets)
                res.append(")");
        }

        res.append(vertex);

        if (isOperator) {
            Node rightChild;
            boolean brackets = false;
            rightChild = ((Operator) vertex).getRight();
            if (rightChild instanceof Operator)
                brackets = true;
            if(brackets)
                res.append("(");
            res.append(printNode(((Operator) vertex).getRight()));
            if(brackets)
                res.append(")");
        }

        return res;
    }
}
