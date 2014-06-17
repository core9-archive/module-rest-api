package io.core9.plugin.test;

import java.lang.reflect.Method;

public class ManualTest {
    public static void main(String[] args) {
        Method[] declaredMethods = ExampleMethods.class.getDeclaredMethods();

        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals("simpleMethod")) {
                System.out.println(Methods.getParameterNames(declaredMethod));
                break;
            }
        }
    }
}
