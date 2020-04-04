package man.dan.converter.unittest;

import java.util.ArrayList;
import java.util.Arrays;

public class ConvTestLst {
    public static ArrayList<String> lst = new ArrayList<>(Arrays.asList(
            "filter{(((5+3)<8)|(1=0))}",
            "filter{((((5+3)<8)|(1=0))&(element>-9))}",
            "filter{((((element+5)-18)>9)|(element>0))}",
            "filter{((9-((9+element)+4))=1)}",
            "filter{(((8*(element*3))*element)>(8-(15+element)))}",
            "filter{(((5+3)<(9*4))|(1=0))}",
            "filter{(((1*9)-(((3+6)+element)+(2+2)))=(2000000000*1000))}",
            "filter{(1=1)}",
            "map{(((3-(3+element))*12)*11)}",
            "map{((5*((element+element)*2))*4)}",
            "map{(3*1)}",
            "map{(((element+2)*38)-2)}",
            "map{((((10*element)*20)+15)*0)}",
            "map{0}",
            "map{element}",
            "map{((element+12)-(-2147483647-1))}"
    ));
}
