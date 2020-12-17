package com.javigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Utils {

    private static Log logger = LogFactory.getLog(Utils.class);

    public static <T> T findOneBy(Collection<T> findIn, Predicate<T> cond) {
        return findIn.stream().filter(cond).findAny().orElse(null);
    }

    public static <T> ArrayList<T> findOnesBy(Collection<T> findIn, Predicate<T> cond) {
        return  new ArrayList<T>(findIn.stream().filter(cond).collect(Collectors.toList()));
    }

    public static void info(Object o) {
        logger.info(o);
    }

    public static Color colorWithAlpha(Color color, float opacity) {
        System.out.println(Map(opacity, 0f,1f, 0f,255f));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) Map(opacity, 0f,1f, 255f,0f));
    }

    public static float Map( float value, float sourceFrom, float sourceTo, float targetFrom, float targetTo) {
        return ((value - sourceFrom) / (sourceTo - sourceFrom) * (targetTo - targetFrom) + targetFrom);
    }
    
}
