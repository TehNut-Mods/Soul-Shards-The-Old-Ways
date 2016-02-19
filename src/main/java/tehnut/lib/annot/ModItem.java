package tehnut.lib.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to automatically register Items with
 * {@link net.minecraftforge.fml.common.registry.GameRegistry#registerItem(net.minecraft.item.Item, String)}
 * <p/>
 * Uses {@code ItemClass.class.getSimpleName()} for {@link #name()} if one is not provided.
 * <p/>
 * Annotate any class that should be registered.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModItem {
    String name();
}
