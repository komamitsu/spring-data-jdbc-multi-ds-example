package org.komamitsu.springtest.data.jdbc.multids;

public class DataSourceContextHolder {
    private static ThreadLocal<DataSourceType> CONTEXT
            = new ThreadLocal<>();

    public static void set(DataSourceType dataSourceType) {
        CONTEXT.set(dataSourceType);
    }

    public static DataSourceType dataSourceType() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
