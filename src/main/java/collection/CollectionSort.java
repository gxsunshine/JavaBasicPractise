package collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @ClassName: CollectionSort
 * @Description: 集合自定义排序
 * @Author: gx
 * @Date: 2021/2/2 14:13
 * @Version: 1.0
 */
public class CollectionSort {

    /***
     * @Description: list的自定义排序
     * @Author: gx
     * @Date: 2021/2/2 14:16
     **/
    @Test
    public void listCustomSort(){
        List<Student> stuList = new ArrayList<>();

        String sortField = "maxScore";
        String sortDirection = "desc";

        Student stu1 = new Student("gx", 95, 100, 89);
        Student stu2 = new Student("kfy", 97, null, 90);
        Student stu3 = new Student("wry", 93, 99, 88);
        Student stu4 = new Student("zt", 96, null, 87);
        Student stu5 = new Student("lj", 92, 95, 86);

        stuList.add(stu1);
        stuList.add(stu2);
        stuList.add(stu3);
        stuList.add(stu4);
        stuList.add(stu5);

        System.out.println("排序前" + stuList.toString());

        Collections.sort(stuList, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                Integer sortValue1 = o1.getAvgScore();
                Integer sortValue2 = o2.getAvgScore();
                switch (sortField){
                    case "avgScore":
                        sortValue1 = o1.getAvgScore();
                        sortValue2 = o2.getAvgScore();
                        break;
                    case "maxScore":
                        sortValue1 = o1.getMaxScore();
                        sortValue2 = o2.getMaxScore();
                        break;
                    case "minScore":
                        sortValue1 = o1.getMinScore();
                        sortValue2 = o2.getMinScore();
                        break;
                }

                if(sortValue1 == sortValue2){
                    return 0;
                }else if(sortValue1 != null && sortValue2 == null){   // 处理为null的特殊情况
                    return -1;
                }else if(sortValue1 == null && sortValue2 != null){
                    return 1;
                } else{
                    if("asc".equals(sortDirection)){
                        return sortValue1 > sortValue2 ? 1 : -1;
                    }else {
                        return sortValue2 > sortValue1 ? 1 : -1;
                    }
                }
            }
        });

        System.out.println("排序后" + stuList.toString());
    }
}


class Student{

    private String name;

    private Integer avgScore;

    private Integer maxScore;

    private Integer minScore;

    public Student(String name, Integer avgScore, Integer maxScore, Integer minScore) {
        this.name = name;
        this.avgScore = avgScore;
        this.maxScore = maxScore;
        this.minScore = minScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(Integer avgScore) {
        this.avgScore = avgScore;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Integer getMinScore() {
        return minScore;
    }

    public void setMinScore(Integer minScore) {
        this.minScore = minScore;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", avgScore=" + avgScore +
                ", maxScore=" + maxScore +
                ", minScore=" + minScore +
                '}';
    }
}
