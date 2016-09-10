package p455w0rd.p455w0rdsthings.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ReflectionUtils
{
  public static <T> MethodHandle findMethod(Class<T> clazz, String[] methodNames, Class<?>... methodTypes)
  {
    Method method = ReflectionHelper.findMethod(clazz, null, methodNames, methodTypes);
    try
    {
      return MethodHandles.lookup().unreflect(method);
    }
    catch (IllegalAccessException e)
    {
      throw new ReflectionHelper.UnableToFindMethodException(methodNames, e);
    }
  }
  
  public static MethodHandle findFieldGetter(Class<?> clazz, String... fieldNames)
  {
    Field field = ReflectionHelper.findField(clazz, fieldNames);
    try
    {
      return MethodHandles.lookup().unreflectGetter(field);
    }
    catch (IllegalAccessException e)
    {
      throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
    }
  }
  
  public static MethodHandle findFieldSetter(Class<?> clazz, String... fieldNames)
  {
    Field field = ReflectionHelper.findField(clazz, fieldNames);
    try
    {
      return MethodHandles.lookup().unreflectSetter(field);
    }
    catch (IllegalAccessException e)
    {
      throw new ReflectionHelper.UnableToAccessFieldException(fieldNames, e);
    }
  }
}


