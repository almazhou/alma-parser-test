import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class DomainConverter<T> {

    private DomainParser domainParser;

    public DomainConverter(File file) throws IOException {
        readSource(file);
    }

    public void readSource(File file) throws IOException {
        InputStream fileInputStream = new FileInputStream(file);

        ANTLRInputStream input = new ANTLRInputStream(fileInputStream);
        AmlLexer lexer = new AmlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AmlParser parser = new AmlParser(tokens);
        ParseTree tree = parser.file();
        this.domainParser = new DomainParser(tree);
    }

    public List<T> convert(Class<T> aClass) {
        try {
            T instance = aClass.newInstance();
            Field[] fields = aClass.getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                String fieldName = field.getName();
                List<String> fieldValues;
                if(fieldName.equals("puid")) {
                    fieldValues = Arrays.asList(domainParser.getDataName());
                } else if(fieldName.equals("type")){
                    fieldValues = Arrays.asList(domainParser.getKeyWord());
                }
                else {
                    fieldValues = domainParser.getValues(fieldName, 0);
                }

                Class<?> fieldType = field.getType();
                if(fieldType.getName().equals("int")){
                    int value = Integer.parseInt(fieldValues.get(0));
                    field.set(instance,value);
                }
                else if(fieldType.getName().equals("double")){
                    double value = Double.parseDouble(fieldValues.get(0));
                    field.set(instance,value);
                } else {
                    field.set(instance,fieldValues.get(0));
                }
            }

            return Arrays.asList(instance);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return null;
    }
}
