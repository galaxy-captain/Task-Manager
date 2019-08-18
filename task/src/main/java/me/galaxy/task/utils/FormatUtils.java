package me.galaxy.task.utils;

import java.lang.reflect.Method;

public class FormatUtils {

    public static String methodParameterTypesToString(Method method) {

        Class<?>[] parameterTypes = method.getParameterTypes();

        int iMax = parameterTypes.length - 1;
        if (iMax == -1)
            return "()";

        StringBuilder b = new StringBuilder();
        b.append('(');
        for (int i = 0; ; i++) {
            b.append(String.valueOf(parameterTypes[i].getSimpleName()));
            if (i == iMax)
                return b.append(')').toString();
            b.append(", ");
        }

    }

}
