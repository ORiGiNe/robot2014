package network.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indiquant à BaseDto si il doit transférer
 * le membre ou non
 * Par défaut, il transfère les champs publics
 * 
 * Cette anotation permet de ne pas transférer un attribut public
 * 
 * Sans paramètre : inclue dans l'envois
 * avec le paramètre false => retire de l'envois
 * @author Jeremy
 *
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD })
public @interface AutoTransfert {
	boolean value() default true;
}
