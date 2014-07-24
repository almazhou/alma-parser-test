import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.stream.Collectors;

public class DomainParser {


    private ParseTree tree;

    public DomainParser(ParseTree tree) {

        this.tree = tree;
    }

    public List<AmlParser.PropertyContext> getPropertyList() {
        return ((AmlParser.DataContext) tree.getChild(0)).property();
    }

    public List<Object> getPropertyValueList(int property_index) {
        List<ParseTree> propertyValues = getPropertyList().get(property_index).value().children;

        return propertyValues.stream().filter(parseTree -> !(parseTree instanceof AmlParser.CommaContext)).collect(Collectors.toList());
    }

    public String getExtendName() {
        return ((AmlParser.DataContext) tree.getChild(0)).extend_name.getText();
    }

    public String getDataName() {
        return ((AmlParser.DataContext) tree.getChild(0)).data_name.getText();
    }

    public String getKeyWord() {
        return ((AmlParser.DataContext) tree.getChild(0)).keyword.getText();
    }
}
