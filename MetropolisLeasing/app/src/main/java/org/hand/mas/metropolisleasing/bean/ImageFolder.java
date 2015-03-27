package org.hand.mas.metropolisleasing.bean;

/**
 * Created by gonglixuan on 15/3/26.
 */
public class ImageFolder {
    /**
     * 图片的文件夹路径
     */
    private String dir;
    /**
     * 第一张图片的路径
     */
    private String firstImagePath;
    /**
     * 文件夹名称
     *
     */
    private String name;
    /**
     * 图片数量
     *
     */
    private int count;

    public String getDir(){
        return dir;
    }

    public void setDir(String dir){
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf);
    }
    public String getFirstImagePath()
    {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath)
    {
        this.firstImagePath = firstImagePath;
    }

    public String getName()
    {
        return name;
    }
    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}
