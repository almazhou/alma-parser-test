import com.sun.javafx.fxml.PropertyNotFoundException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

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

    public String getPropertyName(int index) {
        return getPropertyList().get(index).property_name.getText();
    }

    public String getPropertyValue(int propertyIndex, int valueIndex) {
        return ((TerminalNode) getPropertyValueList(propertyIndex).get(valueIndex)).getText();
    }

    public List<String> getValues(String propertyName) {
        List<AmlParser.PropertyContext> findProperty = getPropertyList().stream().filter(property -> property.property_name.getText().equals(propertyName)).collect(Collectors.toList());
        if(findProperty.size() == 0) {
            throw new PropertyNotFoundException("Perperty named " + propertyName + " not found" );
        }

        if(findProperty.size() > 1){
            throw new MultiplePropertyException("There is more than one property have the save name " + propertyName);
        }

        AmlParser.PropertyContext propertyContext = findProperty.get(0);

        List<ParseTree> values = propertyContext.value().children;

        List<ParseTree> findValues = values.stream().filter(value -> !(value instanceof AmlParser.CommaContext)).collect(Collectors.toList());
        List<String> gotValues = findValues.stream().map(gotValue -> gotValue.getText()).collect(Collectors.toList());
        return gotValues;
    }
}
