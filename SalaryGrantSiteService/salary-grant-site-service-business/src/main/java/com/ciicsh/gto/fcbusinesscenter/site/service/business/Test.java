package com.ciicsh.gto.fcbusinesscenter.site.service.business;

public class Test implements Cloneable{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String name;
    private int age;

    // 重写克隆方法子列才可以调用
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args){
        Test t1 = new Test();
        try {
            Test t2 = (Test) t1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

    }

}
