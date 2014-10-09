package network.base;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.interfaces.IConnectionStream;
import network.interfaces.IDto;
import tools.Tools;

/**
 * Impl�mentation de IDto vou�e � se transf�rer automatiquement
 * par le IConnexionStream utilis� et ses m�thodes send(Object) / receive(Class)
 * 
 * les champs publics non annot�s de AutoTransfert(false) sont
 * transf�r�s automatiquements
 *  
 * @author Jeremy
 *
 */
@SuppressWarnings("rawtypes")
public abstract class BaseDto implements IDto {

	//retient les recherches d�j� effectu�es
	private static Map<Class, List<Field>> fieldListMap = new HashMap<Class, List<Field>>();
	
	/**
	 * Calcule la liste des variables � transf�rer automatiquement
	 * Si d�j� calcul�, renvoit le r�sultat du calcul pr�c�dent
	 * @return
	 */
	public static List<Field> getTransfertFieldsList(IDto dto) {
		//Recherche un r�sultat d�j� calcul�
		List<Field> list = fieldListMap.get(dto.getClass());
		if(list != null)
			return list;
		
		Field[] allFields = dto.getClass().getDeclaredFields();
		List<Field> publicFields = new ArrayList<>();
	    for (Field field : allFields) {
	        if (Modifier.isPublic(field.getModifiers())) {
	        	AutoTransfert annot = field.getAnnotation(AutoTransfert.class);
	        	if(annot!=null) {
	        		if(annot.value())
	        			publicFields.add(field);
	        	} else {
	        		publicFields.add(field);
	        	}
	        }
	    }
	    fieldListMap.put(dto.getClass(), publicFields);
	    return publicFields;
	}

	@Override
	public void writeIn(IConnectionStream stream) throws IOException {
		for(Field field : getTransfertFieldsList(this)) {
			//System.out.println("Trying to send "+this.getClass().getCanonicalName()+"."+field.getName());
			try {
				stream.send(field.get(this));
			} catch (IOException | IllegalArgumentException | IllegalAccessException e) {
				throw new UnsupportedOperationException("Error sending "+this.getClass().getCanonicalName()+"."+field.getName()
						+ " : "+e.getClass().getSimpleName()+" "+e.getMessage(), e);
			}
		}
	}

	@Override
	public void readFrom(IConnectionStream stream) throws IOException {
		for(Field field : getTransfertFieldsList(this)) {
			//System.out.println("Trying to receive "+this.getClass().getCanonicalName()+"."+field.getName());
			try {
				field.set(this, stream.receiveObject(field.getType()));
			} catch (IOException | IllegalAccessException | IllegalArgumentException e) {
				throw new IOException("Error receiving "+this.getClass().getCanonicalName()+"."+field.getName()
						+ " : "+e.getClass().getSimpleName()+" "+e.getMessage(), e);
			}
		}
	}

	@Override
	public String toString() {
		return Tools.reflectDisplay(this);
	}
	
}
