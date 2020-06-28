package project.common.test;


import project.common.DbUtils.DbField;
import project.common.DbUtils.DbFile;
import project.common.DbUtils.DbPrimary;

@DbFile("user")
public class UserBean {
    @DbPrimary
    @DbField("_id")
    private Integer id;
    @DbField("name")
    private String name;
    @DbField("_test")
    private String test;

    public UserBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserBean() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", test='" + test + '\'' +
                '}';
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
