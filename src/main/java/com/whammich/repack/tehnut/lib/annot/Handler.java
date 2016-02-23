package com.whammich.repack.tehnut.lib.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to automatically handle registration of EventHandlers for the
 * {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}.
 * <p/>
 * Annotate any class that should be subscribed.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Handler {
    boolean client() default false;
}
