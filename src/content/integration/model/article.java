package content.integration.model;

import javax.xml.bind.annotation.*;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
/**
 * Created by u6023035 on 27/01/2017.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@CsvRecord(separator = ",", crlf = "UNIX")
public class article {
    @XmlAttribute
    @DataField(pos = 1)
    private String journalId;
    @XmlAttribute
    @DataField(pos = 2)
    private String title;
    @XmlElement
    @DataField(pos = 3)
    private String category;
    @XmlElement
    @DataField(pos = 4)
    private String author;
    @XmlElement
    @DataField(pos = 5)
    private String city;
}
