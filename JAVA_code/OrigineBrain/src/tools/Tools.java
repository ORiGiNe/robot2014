package tools;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

import network.base.BaseDto;
import network.interfaces.IDto;


/**
 * Some utils functions
 * @author Jeremy
 *
 */
public final class Tools {
	
	/**
	 * return un byte en hexa
	 * @param b
	 * @return
	 */
	public static String toHex(byte b) {
		return String.format("%02x", b & 0xff);
	}
	
	/**
	 * return un octet en hexa
	 * @param b
	 * @return
	 */
	public static String toHex(int b) {
		return String.format("%08x", b);
	}
	
	/**
	 * Utilise la reflexivité pour afficher un objet
	 * @param o
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static String reflectDisplay(Object o) {
		if(o==null) { return "[null]"; }
		Class<?> cl = o.getClass();
		if(cl.isArray()) {
			StringBuilder builder = new StringBuilder();
			for(int i=0; i<Array.getLength(o); ++i) {
				if(i>0) {
					builder.append(",");
				} else {
					builder.append(Array.get(o, i).getClass().getSimpleName()+"{");
				}
				builder.append(reflectDisplay(Array.get(o, i)));
			}
			builder.append("}[");
			builder.append(Array.getLength(o));
			builder.append(" items]");
			return builder.toString();
		} else if (cl.isAssignableFrom(Byte.class) || cl.isAssignableFrom(byte.class)) {
			return toHex((byte) o);
		} else if (BaseDto.class.isAssignableFrom(cl)) {
			List<Field> fields = BaseDto.getTransfertFieldsList((IDto) o);
			StringBuilder builder = new StringBuilder(o.getClass().getSimpleName()+"{\n");
			for(Field f : fields) {
				builder.append("\t");
				builder.append(f.getName());
				builder.append(" = ");
				try {
					builder.append(reflectDisplay(f.get(o)));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					builder.append("Exception : "+e.getMessage());
				}
				builder.append("\n");
			}
			builder.append("}");
			return builder.toString();
		} else if (o.getClass() == String.class) {
			return "\""+o+"\"["+((String) o).length()+" char]";
		} else {
			return o.getClass().getSimpleName()+"("+o.toString()+")";
		}
	}
}
