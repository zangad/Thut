package thut.core.common.world.utils;

import java.util.UUID;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import thut.api.world.utils.Info;

public class Info_Impl implements Info
{
    private static final long serialVersionUID = 4947657861031977837L;

    NBTTagCompound            nbt              = new NBTTagCompound();

    @Override
    public <T> T value(String key, Class<T> type)
    {
        if (type == UUID.class && nbt.hasUniqueId(key)) { return type.cast(nbt.getUniqueId(key)); }
        if (!nbt.hasKey(key)) return null;
        switch (type.getName())
        {
        case "C":
            return null;
//        case "Z":
//            return type.cast(nbt.getBoolean(key));
//        case "B":
//            return type.cast(nbt.getByte(key));
//        case "[B":
//            return type.cast(nbt.getByteArray(key));
//        case "D":
//            return type.cast(nbt.getDouble(key));
//        case "F":
//            return type.cast(nbt.getFloat(key));
//        case "I":
//            return type.cast(nbt.getInteger(key));
//        case "[I":
//            return type.cast(nbt.getIntArray(key));
//        case "J":
//            return type.cast(nbt.getLong(key));
//        case "S":
//            return type.cast(nbt.getShort(key));
        default:
            if (type == NBTTagCompound.class)
            {
                return type.cast(nbt.getCompoundTag(key));
            }
            else if (type == NBTTagList.class)
            {
                return type.cast(nbt.getTag(key));
            }
            else if (type == String.class) { return type.cast(nbt.getString(key)); }
            break;
        }
        return null;
    }

    @Override
    public <T> void set(String key, T value)
    {
        Class<?> type = value.getClass();
        if (type == UUID.class)
        {
            nbt.setUniqueId(key, (UUID) value);
        }
        switch (type.getName())
        {
        case "C":
            return;
//        case "Z":
//            nbt.setBoolean(key, (boolean) value);
//            return;
//        case "B":
//            nbt.setByte(key, (byte) value);
//            return;
//        case "[B":
//            nbt.setByteArray(key, (byte[]) value);
//            return;
//        case "D":
//            nbt.setDouble(key, (double) value);
//            return;
//        case "F":
//            nbt.setFloat(key, (float) value);
//            return;
//        case "I":
//            nbt.setInteger(key, (int) value);
//            return;
//        case "[I":
//            nbt.setIntArray(key, (int[]) value);
//            return;
//        case "J":
//            nbt.setLong(key, (long) value);
//            return;
//        case "S":
//            nbt.setShort(key, (short) value);
//            return;
        default:
            if (type == NBTTagCompound.class)
            {
                nbt.setTag(key, (NBTBase) value);
                return;
            }
            else if (type == NBTTagList.class)
            {
                nbt.setTag(key, (NBTBase) value);
                return;
            }
            else if (type == String.class)
            {
                nbt.setString(key, (String) value);
                return;
            }
            break;
        }
    }

    @Override
    public String serialize()
    {
        return nbt.toString();
    }

    @Override
    public void deserialize(String value)
    {
        try
        {
            nbt = JsonToNBT.getTagFromJson(value);
        }
        catch (NBTException e)
        {
            e.printStackTrace();
        }
    }

}
