package com.javigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    
}
