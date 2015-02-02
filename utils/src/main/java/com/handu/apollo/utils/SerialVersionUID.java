package com.handu.apollo.utils;

/**
 * Created by markerking on 14-4-4.
 */
public interface SerialVersionUID {
    public static final long Base = 0x563F2F70 << 34; // 如果你猜到了这是什么，请告诉我，有小红心哦

    public static final long ApolloRuntimeException = Base | 0x1;
    public static final long InvalidParameterValueException = Base | 0x2;
    public static final long ApolloAuthenticationException = Base | 0x3;
    public static final long PermissionDeniedException = Base | 0x4;
    public static final long RequestLimitException = Base | 0x5;
    public static final long ParameterValidationException = Base | 0x6;
    public static final long AccountLimitException = Base | 0x7;

}
