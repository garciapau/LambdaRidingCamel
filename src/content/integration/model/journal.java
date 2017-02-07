package content.integration.model;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class journal {
    @XmlElement
    private article article[];
}
