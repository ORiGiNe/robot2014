package network.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indiquant � BaseDto si il doit transf�rer
 * le membre ou non
 * Par d�faut, il transf�re les champs publics
 * 
 * Cette anotation permet de ne pas transf�rer un attribut public
 * 
 * Sans param�tre : inclue dans l'envois
 * avec le param�tre false => retire de l'envois
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
