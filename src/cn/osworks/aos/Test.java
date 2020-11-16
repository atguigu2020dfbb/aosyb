package cn.osworks.aos;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

class Person implements Comparable{
    public String name;

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Integer age;

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) &&
                Objects.equals(age, person.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Person){
            Person p=(Person)o;
            int i = this.name.compareTo(p.name);
            if(i==0){
                return Integer.compare(this.age,p.age);
            }else{
                return i;
            }

        }
           throw new RuntimeException("类型有错误");
    }
}

public class Test {
     @org.junit.Test
    public void Demo(){
//        TreeSet set=new TreeSet();
//        set.add(1);
//        set.add(7);
//        set.add(2);
//        set.add(5);
//        set.add(9);
         TreeSet set=new TreeSet();
         set.add(new Person("xiaoming",15));
         set.add(new Person("wangxiao",14));
         set.add(new Person("lishiying",45));
         set.add(new Person("lishiying",15));
         set.add(new Person("peixu",47));
         set.add(new Person("sunhongqiang",29));
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            Object obj = iterator.next();
            System.out.println(obj);
        }
    }
    @org.junit.Test
    public void Demo2(){
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof Person && o2 instanceof Person){
                    Person p1= (Person) o1;
                    Person p2= (Person) o2;
                    return Integer.compare(p1.getAge(),p2.getAge());
                }else{
                    throw new RuntimeException("类型有错误");
                }

            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
        TreeSet set=new TreeSet(comparator);
        set.add(new Person("xiaoming",15));
        set.add(new Person("wangxiao",14));
        set.add(new Person("lishiying",45));
        set.add(new Person("lishiying",15));
        set.add(new Person("peixu",47));
        set.add(new Person("sunhongqiang",29));
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            Object obj = iterator.next();
            System.out.println(obj);
        }
    }
    @org.junit.Test
    public void test4(){
        System.out.println(~-6);
        System.out.println(6&4);
        System.out.println(6|4);
        System.out.println(6^4);
        System.out.println(~-9);
        System.out.println(6>>2);
        System.out.println(6<<2);
        System.out.println(-6>>2);
        System.out.println(-6<<2);
        System.out.println(6>>2);
        System.out.println(-6>>>2);
        System.out.println(6>>>2);
    }
}
