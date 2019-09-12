import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BlopEvent {

    final String blop;

    public BlopEvent(String value) {
        this.blop = value;
    }
}