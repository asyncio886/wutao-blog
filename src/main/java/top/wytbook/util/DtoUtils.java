package top.wytbook.util;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DtoUtils {
    public static <T> T convertObject(Object o, Class<T> cls) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T instance = cls.getDeclaredConstructor().newInstance();
        BeanUtils.copyProperties(o, instance);
        return instance;
    }

    public static <T,R> List<T> convertList(List<R> oList, Class<T> cls) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> res = new ArrayList<>();
        for (R o : oList) {
            res.add(convertObject(o, cls));
        }
        return res;
    }
}
