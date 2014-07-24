import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AmlParserTest {

    private ParseTree tree;
    private List<AmlParser.PropertyContext> propertyList;

    @Before
    public void setUp() throws Exception {
        String test = "group 5457_A3SB_A49A extends ProductGroup {\n" +
                "\tfirst = 1,4;\n" +
                "\tsecond = test,3.4;\n" +
                "}";

        File file = getFile(test, "classpath:test.aml");
        tree = getParseTree(file);
        propertyList = getPropertyList();

    }

    @Test
    public void should_return_keyword_data_name_and_extend_name() throws Exception {
        ParseTree child = tree.getChild(0);

        String keyword = ((AmlParser.DataContext) child).keyword.getText();
        String data_name = ((AmlParser.DataContext) child).data_name.getText();
        String extend_name = ((AmlParser.DataContext) child).extend_name.getText();

        assertThat(keyword, is("group"));
        assertThat(data_name, is("5457_A3SB_A49A"));
        assertThat(extend_name, is("ProductGroup"));
    }

    @Test
    public void should_return_property_name_and_property_value() throws Exception {

        assertThat(propertyList.size(), is(2));

        assertThat(propertyList.get(0).property_name.getText(), is("first"));
        assertThat(propertyList.get(1).property_name.getText(), is("second"));

    }

    @Test
    public void should_return_property_value_for_each_property() throws Exception {
        List<Object> first = getPropertyValueList(0);
        List<Object> second = getPropertyValueList(1);

        String first_1 = ((TerminalNode) first.get(0)).getText();
        String first_2 = ((TerminalNode) first.get(1)).getText();
        String second_1 = ((TerminalNode) second.get(0)).getText();
        String second_2 = ((TerminalNode) second.get(1)).getText();

        assertThat(first_1, is("1"));
        assertThat(first_2, is("4"));
        assertThat(second_1, is("test"));
        assertThat(second_2, is("3.4"));
    }

    private List<Object> getPropertyValueList(int property_index) {
        List<ParseTree> propertyValues = propertyList.get(property_index).value().children;

        return propertyValues.stream().filter(parseTree ->!(parseTree instanceof AmlParser.CommaContext)).collect(Collectors.toList());
    }

    private List<AmlParser.PropertyContext> getPropertyList() {
        return ((AmlParser.DataContext) tree.getChild(0)).property();
    }

    private File getFile(String test, String filePath) {
        writeToFile(test, filePath);

        return new File(filePath);
    }

    private void writeToFile(String test, String filePath) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath), "utf-8"));
            writer.write(test);
        } catch (IOException ex) {
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }
    }

    private ParseTree getParseTree(File file) throws IOException {

        InputStream fileInputStream = new FileInputStream(file);

        ANTLRInputStream input = new ANTLRInputStream(fileInputStream);
        AmlLexer lexer = new AmlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AmlParser parser = new AmlParser(tokens);
        return parser.file();
    }
}
