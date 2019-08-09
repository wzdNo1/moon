package com.mipo.db.config;


public class HandleDatasource {

    private static final ThreadLocal<String> holder = new ThreadLocal<String>();

    public static void addDatasource(String dataSource){
        holder.set(dataSource);
    }

    public static String getDatasource(){
        return holder.get();
    }

    public static void clear(){
        holder.remove();
    }

}
