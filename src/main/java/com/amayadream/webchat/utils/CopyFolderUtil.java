package com.amayadream.webchat.utils;

import java.io.*;

/**
 * Created by Jacob on 2017/5/3.
 * 将某个文件夹下的东西,是多级文件和文件夹的混合体,复制到另一个路径下
 * 1封装原始路径srcFile
 * 2封装要复制到的路径destFile
 * 3判断是文件还是文件夹
 * 3.1如果是文件夹,在目的地目录下创建该文件夹;获取该文件夹下所有文件和文件夹File数组;回到3;
 * 3.2如果是文件,使用字节流复制
 */
public class CopyFolderUtil {
    static int count=0;
    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis();
        File srcFile=new File("D:\\workspace\\project");
        File destFile=new File("C:\\");
        copyFolder(srcFile,destFile);
        long during = System.currentTimeMillis()-start;
        System.out.println("复制共耗时"+during+"毫秒,"+"共递归copyFolder:"+count+"次");
    }

    private static void copyFolder(File srcFile, File destFile) throws Exception {
        count++;
        System.out.println(srcFile+">>>>>>>>>"+destFile);
        if(srcFile.isDirectory()){//是文件夹
            String srcFileName = srcFile.getName();
            File newFile=new File(destFile,srcFileName);//c:\\demo\\srcFolderName
            newFile.mkdir();//创建此文件夹
            File[] files = srcFile.listFiles();
            for (File file:files){
                copyFolder(file,newFile);
            }
        }else {//是文件
            System.out.println(destFile+">>>>>>>>>"+srcFile.getName());
            File newFile=new File(destFile,srcFile.getName());//c:\\demo\\aa.java
            copy(srcFile,newFile);
        }
    }

    private static void copy(File srcFile, File destFile) throws Exception{
        BufferedInputStream bis=new BufferedInputStream(new FileInputStream(srcFile));
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(destFile));
        byte[] bytes=new byte[1024];
        int lens=0;
        while ((lens = bis.read(bytes))!=-1){
            bos.write(bytes,0,lens);
        }
        bos.close();
        bis.close();
    }

}
