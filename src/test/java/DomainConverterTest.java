import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DomainConverterTest {
    @Test
    public void should_return_object_for_given_class() throws Exception {
        String path = "classpath:puid.aml";
        String test = "puid 4567 extends Cable {\n" +
                "    length = 1.0;\n" +
                "    color = Gray;\n" +
                "    network = 10mm_sdl;\n" +
                "    fabric = TW;\n" +
                "}";
        TestUtils.writeToFile(test,path);
        DomainConverter domainConverter = new DomainConverter(new File(path));

        List<Cable> convert = domainConverter.convert(Cable.class);

        Cable cable = convert.get(0);
        assertThat(cable.getPuid(),is(4567));
//        assertThat(cable.getType(),is(AmlType.PUID));
        assertThat(cable.getlength(),is(1.0));
        assertThat(cable.getColor(),is("Gray"));
        assertThat(cable.getNetwork(),is("10mm_sdl"));
        assertThat(cable.getFabric(),is("TW"));
    }
}
