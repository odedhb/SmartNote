package snooze.ninja.SpeechDating;

import java.lang.reflect.Field;

/**
 * Created by oded on 8/12/14.
 */
public class ConstantIdentifier {

    private Class aClass;

    public ConstantIdentifier(Class aClass) {

        this.aClass = aClass;
    }

    public String name(int intCode) {
        String result = aClass.getSimpleName() + " : ";

        Field[] fields = aClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType() != int.class) {
                continue;
            }
            // note: get(null) for static field
            try {
                if (field.getInt(null) == intCode) {
                    return result + field.getName();
                }
            } catch (IllegalAccessException e) {
                return result + e.getMessage();
            }
        }
        return result + "unknown error in ";
    }
}
