package tehnut.lib.annot;

import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to automatically register Blocks with
 * {@link net.minecraftforge.fml.common.registry.GameRegistry#registerBlock(net.minecraft.block.Block, Class, String)}
 * <p/>
 * Uses {@code BlockClass.class.getSimpleName()} for {@link #name()} if one is not provided.
 * <p/>
 * Handles {@link ItemBlock} and {@link TileEntity} registration as well if values are
 * provided.
 * <p/>
 * Annotate any class that should be registered.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModBlock {
    String name();

    Class<? extends TileEntity> tileEntity() default TileEntity.class;

    Class<? extends ItemBlock> itemBlock() default ItemBlock.class;
}
