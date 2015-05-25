package com.whammich.sstow.asm;

import hilburnlib.asm.Transformer;
import hilburnlib.asm.obfuscation.ASMString;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.Map;

public class SoulTransformer implements IClassTransformer, Opcodes
{
    private static ASMString hooks = new ASMString("com.whammich.sstow.asm.SoulHooks");
    private static ASMString onDeathUpdate = new ASMString.ASMObfString("onDeathUpdate", "aF");
    private static ASMString entityLivingBase = new ASMString.ASMObfString("net.minecraft.entity.EntityLivingBase", "sv");
    private static ASMString entityPlayer = new ASMString("net.minecraft.entity.player.EntityPlayer");

    private static Transformer.MethodTransformer deathUpdate = new Transformer.MethodTransformer(onDeathUpdate, "()V")
    {
        @Override
        protected void modify(MethodNode node)
        {
            AbstractInsnNode insnNode = node.instructions.getFirst();
            FieldInsnNode getField = null;
            while (insnNode != null)
            {
                if (insnNode.getOpcode() == GETFIELD) getField = (FieldInsnNode)insnNode;
                else if (insnNode.getOpcode() == ISTORE && ((VarInsnNode)insnNode).var == 1 && getField != null)
                {
                    node.instructions.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                    node.instructions.insertBefore(insnNode, new VarInsnNode(ALOAD, 0));
                    node.instructions.insertBefore(insnNode, new FieldInsnNode(GETFIELD, getField.owner, getField.name, getField.desc));
                    node.instructions.insertBefore(insnNode, new MethodInsnNode(INVOKESTATIC, hooks.getASMClassName(), "getModifiedXP", "(I" + entityLivingBase.getASMTypeName() + entityPlayer.getASMTypeName() + ")I", false));
                    return;
                }
                insnNode = insnNode.getNext();
            }
        }
    };


    private enum ClassTransformers
    {
        ENTITY_LIVING_BASE(new Transformer.ClassTransformer(entityLivingBase, deathUpdate));

        private Transformer.ClassTransformer transformer;

        ClassTransformers(Transformer.ClassTransformer transformer)
        {
            this.transformer = transformer;
        }

        public Transformer.ClassTransformer getTransformer()
        {
            return transformer;
        }

        public String getClassName()
        {
            return transformer.getClassName();
        }
    }

    private static Map<String, Transformer.ClassTransformer> classMap = new HashMap<String, Transformer.ClassTransformer>();

    static
    {
        for (ClassTransformers classTransformer : ClassTransformers.values())
            classMap.put(classTransformer.getClassName(), classTransformer.getTransformer());
        Transformer.log.info((ASMString.OBFUSCATED ? "O" : "Deo") + "bfuscated environment detected");
    }

    public static void register(Transformer.ClassTransformer transformer)
    {
        classMap.put(transformer.getClassName(), transformer);
    }

    @Override
    public byte[] transform(String className, String className2, byte[] bytes)
    {
        Transformer.ClassTransformer clazz = classMap.get(className);
        if (clazz != null)
        {
            bytes = clazz.transform(bytes);
            classMap.remove(className);
        }
        return bytes;
    }
}
