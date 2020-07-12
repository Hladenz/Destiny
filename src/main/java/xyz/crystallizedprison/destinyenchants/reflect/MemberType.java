package xyz.crystallizedprison.destinyenchants.reflect;

import java.lang.reflect.AccessibleObject;
import java.util.Objects;

/**
 * Holds information about a type of member that a class can contain.
 */
public enum MemberType
{
    FIELD
            {
                @Override
                public AccessibleObject get( Class<?> holderClass,
                                             String name,
                                             Class<?> ... parameterTypes) throws NoSuchFieldException
                {
                    Objects.requireNonNull(name);
                    return holderClass.getDeclaredField(name);
                }
            },
    METHOD
            {
                @Override
                public AccessibleObject get( Class<?> holderClass,
                                             String name,
                                             Class<?> ... parameterTypes) throws ReflectiveOperationException
                {
                    Objects.requireNonNull(name);
                    return holderClass.getDeclaredMethod(name, parameterTypes);
                }
            },
    CONSTRUCTOR
            {
                @Override
                public AccessibleObject get( Class<?> holderClass,
                                             String name,
                                            Class<?> ... parameterTypes) throws ReflectiveOperationException
                {
                    return holderClass.getDeclaredConstructor(parameterTypes);
                }
            };

    /**
     * Get an {@link AccessibleObject} reflectively based on the parameters given
     *
     * @param holderClass    the class that holds this {@link AccessibleObject}
     * @param name           the name of the {@link AccessibleObject}, if any (Constructors are unnamed)
     * @param parameterTypes the parameter types of this {@link AccessibleObject}, if any (Fields do not require any)
     * @return an {@link AccessibleObject} if any is found, otherwise throw {@link ReflectiveOperationException}
     * @throws ReflectiveOperationException if the reflection could not be performed.
     */
    
    public abstract AccessibleObject get( final Class<?> holderClass,
                                          final String name,
                                          final Class<?>... parameterTypes) throws ReflectiveOperationException;
}
