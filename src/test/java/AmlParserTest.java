import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AmlParserTest {

    private ParseTree tree;
    private List<AmlParser.PropertyContext> propertyList;
    private DomainParser domainParser;
    private AmlParser parser;

    @Before
    public void setUp() throws Exception {
        String test = "group 5457_A3SB_A49A extends ProductGroup {\n" +
                "\tfirst = 1,4;\n" +
                "\tsecond = test,3.4;\n" +
                "}";

        File file = getFile(test, "classpath:test.aml");

        InputStream fileInputStream = new FileInputStream(file);

        ANTLRInputStream input = new ANTLRInputStream(fileInputStream);
        AmlLexer lexer = new AmlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        parser = new AmlParser(tokens);
        tree = parser.file();
        domainParser = new DomainParser(tree);
        propertyList = domainParser.getPropertyList();

    }

    @Test
    public void should_return_keyword_data_name_and_extend_name() throws Exception {

        String keyword = domainParser.getKeyWord();
        String data_name = domainParser.getDataName();
        String extend_name = domainParser.getExtendName();

        assertThat(keyword, is("group"));
        assertThat(data_name, is("5457_A3SB_A49A"));
        assertThat(extend_name, is("ProductGroup"));
    }

    @Test
    public void should_return_property_name_and_property_value() throws Exception {

        assertThat(propertyList.size(), is(2));

        int index = 0;
        assertThat(domainParser.getPropertyName(index), is("first"));
        assertThat(domainParser.getPropertyName(1), is("second"));

    }

    @Test
    public void should_return_property_value_for_each_property() throws Exception {

        String first_1 = domainParser.getPropertyValue(0, 0);
        String first_2 = domainParser.getPropertyValue(0, 1);
        String second_1 = domainParser.getPropertyValue(1, 0);
        String second_2 = domainParser.getPropertyValue(1, 1);

        assertThat(first_1, is("1"));
        assertThat(first_2, is("4"));
        assertThat(second_1, is("test"));
        assertThat(second_2, is("3.4"));
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

}
