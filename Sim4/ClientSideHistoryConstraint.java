import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Author("Daniel")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ProgramPart
public @interface ClientSideHistoryConstraint {
    String[] value();
}
