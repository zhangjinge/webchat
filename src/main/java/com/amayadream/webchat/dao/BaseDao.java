package com.amayadream.webchat.dao;

import com.amayadream.webchat.annotation.ColumnName;
import com.amayadream.webchat.annotation.IDName;
import com.amayadream.webchat.annotation.TableName;
import com.amayadream.webchat.utils.DBCPUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacob Zhang on 2017/4/29.
 */
public class BaseDao<T> {
    DataSource dataSource= DBCPUtils.getDataSource();
    QueryRunner qr=new QueryRunner(dataSource);
    public Class<T> beanClass;

    public BaseDao(){
//        System.out.println(this);
        Type superclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType=(ParameterizedType)superclass;
        Type[] targs = parameterizedType.getActualTypeArguments();
        beanClass=(Class)targs[0];
    }

    public String getTableName(){
        TableName tableName = beanClass.getAnnotation(TableName.class);
        String _tableName = tableName.value().toUpperCase();
        return _tableName;
    }

    public int addItem(T t) throws Exception{
        Field[] fields = beanClass.getDeclaredFields();
        String tableName = getTableName();
        String sql="INSERT INTO "+tableName+"(";
        for (int i=0;i<=fields.length-1;i++){
            if(i<fields.length-1){
                sql+=fields[i].getName();
                sql+=",";
            }else if (i==fields.length-1){
                sql+=fields[i].getName();
            }
        }
        sql+=") VALUES(";
        for (int i=0;i<=fields.length-1;i++){
            if(i<fields.length-1){
                sql+="?";
                sql+=",";
            }else if (i==fields.length-1){
                sql+="?";
            }
        }
        sql+=");";
        System.out.println(sql);
        Object[] params=new Object[fields.length];
        for (int i=0;i<=fields.length-1;i++){
            Field f=fields[i];
            f.setAccessible(true);
            String value = f.get(this).toString();//this是传入的bean
//            System.out.println(value);
            if(i<=fields.length-1){
                params[i]=value;//+","不需要
            }
        }
        int i = qr.update(sql, params);
        return i;
    }

    public int deleteItem(String uuid) throws Exception{
        String tableName = getTableName();
        String primaryKey = getPrimaryKey();
        String sql="delete from "+tableName+" where "+primaryKey+"="+"\'"+uuid+"\'";
        int i = qr.update(sql);
        return i;
    }

    public int updateItem(String uuid,T t) throws Exception{//update `user` SET `password`='tom1' where userid='tom';
        String tableName = getTableName();
        String primaryKey = getPrimaryKey();
        Field[] fields = beanClass.getDeclaredFields();
        String sql="update "+tableName+" set ";

        for (int i=0;i<=fields.length-1;i++){
            if(fields[i].getAnnotation(ColumnName.class)!=null){
                String name = fields[i].getName();//password
                String _name = name.substring(0,1).toUpperCase() + name.substring(1);
                String name_="get"+_name;//getPassword
                Method method = beanClass.getMethod(name_);
                Object obt = method.invoke(t);
                String str=(String)obt;
                //如何获取一个类中有某个注解的字段或者方法的个数
                sql+=name+"="+"\'"+str+"\'";
                sql+=",";
            }
        }
        int index = sql.lastIndexOf(",");
        String sql_ = sql.substring(0, index);
        String _sql=sql_+" where "+primaryKey+"="+"\'"+uuid+"\'";
        System.out.println(_sql);
        int i = qr.update(_sql);
        return i;
    }

    public String getPrimaryKey(){
        String primaryKey="";
        Field[] fields = beanClass.getDeclaredFields();
        for (Field f:fields){
            if(f!=null && f.getAnnotation(IDName.class)!=null){
                IDName idName = f.getAnnotation(IDName.class);
                primaryKey = idName.value();
            }else{
                break;
            }
        }
        return primaryKey;
    }

    public T selectItem(String uuid) throws Exception{
        String tableName = getTableName();
        String primaryKey = getPrimaryKey();
        String sql="select * from "+tableName+" where "+primaryKey+"="+"\'"+uuid+"\'";
        Object ob = qr.query(sql, new BeanHandler(beanClass));
        T t=(T)ob;
        return t;
    }

    public List<T> listItem() throws Exception{
        String tableName = getTableName();
        String sql="select * from "+tableName+";";
        Object beans = qr.query(sql, new BeanListHandler(beanClass));
        List<T> beanList=(ArrayList<T>)beans;
        return beanList;
    }
}
