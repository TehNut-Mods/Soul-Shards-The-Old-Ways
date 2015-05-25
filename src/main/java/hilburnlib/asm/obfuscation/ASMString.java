package hilburnlib.asm.obfuscation;

import net.minecraftforge.classloading.FMLForgePlugin;

public class ASMString
{
    public static boolean OBFUSCATED = FMLForgePlugin.RUNTIME_DEOBF;
    private String text;

    public ASMString(String text)
    {
        this.text = text;
    }

    @SuppressWarnings("rawtypes")
	public ASMString(Class clazz)
    {
        this.text = clazz.getCanonicalName();
    }

    public String getText()
    {
        return text;
    }

    public String getReadableText()
    {
        return text;
    }

    public String getASMClassName()
    {
        return text.replaceAll("\\.","/");
    }

    public String getObfASMClassName()
    {
        return getASMClassName();
    }

    public String getASMTypeName()
    {
        return "L" + getASMClassName() +";";
    }

    public String getObfASMTypeName()
    {
        return getASMTypeName();
    }

    public static class ASMObfString extends ASMString
    {
        private String obfText;
        public ASMObfString(String text, String obfText)
        {
            super(text);
            this.obfText = obfText;
        }

        @Override
        public String getObfASMTypeName()
        {
            return OBFUSCATED? "L" + obfText +";": getASMTypeName();
        }

        @Override
        public String getObfASMClassName()
        {
            return OBFUSCATED? obfText : getASMClassName();
        }

        @Override
        public String getText()
        {
            return OBFUSCATED ? obfText : super.getText();
        }
    }
}
