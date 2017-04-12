package com.taobao.android.dexposed;


import android.app.Application;

import com.taobao.android.dexposed.annotations.Hook;
import com.taobao.android.dexposed.annotations.Hooks;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：zhangzhongping on 17/4/6 15:21
 * 邮箱：android_dy@163.com
 */
public class HookUtils {

    private static final Map<String, Class> abbreviationMap = new HashMap<String, Class>();

    static {
        abbreviationMap.put("int", int.class);
        abbreviationMap.put("boolean", boolean.class);
        abbreviationMap.put("float", float.class);
        abbreviationMap.put("long", long.class);
        abbreviationMap.put("short", short.class);
        abbreviationMap.put("byte", byte.class);
        abbreviationMap.put("double", double.class);
        abbreviationMap.put("char", char.class);
        abbreviationMap.put("void", void.class);
        abbreviationMap.put("int[]", int.class);
        abbreviationMap.put("boolean[]", boolean[].class);
        abbreviationMap.put("float[]", float[].class);
        abbreviationMap.put("long[]", long[].class);
        abbreviationMap.put("short[]", short[].class);
        abbreviationMap.put("byte[]", byte[].class);
        abbreviationMap.put("double[]", double[].class);
        abbreviationMap.put("char[]", char[].class);
    }

    public static Object[] ClassJX(Hook hook, Hooks hooks, Class arthook, Application application) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        XC_MethodReplacement xc_methodReplacement = (XC_MethodReplacement) arthook.newInstance();
        Class<?> clazz = Class.forName(hook == null ? hooks.Class() : hook.Class(), true, application.getClassLoader());
        String methodName = hook == null ? hooks.Name() : hook.Name();
        Class[] type = hook == null ? ClassLoad(hooks.Type(), application) : hook.Type();
        Object[] obj = new Object[type.length + 1];
        Type type1 = clazz.getDeclaredMethod(methodName, type).getGenericReturnType();
        String c = type1.toString().replace("class ", "");
        Class ret;
        if (abbreviationMap.get(c) != null) {
            ret = abbreviationMap.get(c);
        } else {
            ret = Class.forName(c, true, application.getClassLoader());
        }
        System.arraycopy(type, 0, obj, 1, type.length);
        obj[0] = ret;
        Object[] objects = new Object[obj.length + 2];
        System.arraycopy(obj, 0, objects, 1, obj.length);
        objects[0] = arthook;
        objects[objects.length - 1] = xc_methodReplacement;
        return objects;
    }

    private static Class[] ClassLoad(String[] strings, Application application) throws ClassNotFoundException {
        Class[] classes = new Class[strings.length];
        for (int i = 0; i < strings.length; i++) {
            if (abbreviationMap.get(strings[i]) != null) {
                classes[i] = abbreviationMap.get(strings[i]);
            } else {
                classes[i] = Class.forName(strings[i], true, application.getClassLoader());
            }
        }
        return classes;
    }
}
