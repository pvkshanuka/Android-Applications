package CustomClasses;

import com.app.wooker.DBClasses.Job;

import java.util.ArrayList;

public class JobCategoryDetails {

    String cat_name;
    ArrayList<Job> list_jobs;

    public JobCategoryDetails(String cat_name, ArrayList<Job> list_jobs) {
        this.cat_name = cat_name;
        this.list_jobs = list_jobs;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public ArrayList<Job> getList_jobs() {
        return list_jobs;
    }

    public void setList_jobs(ArrayList<Job> list_jobs) {
        this.list_jobs = list_jobs;
    }
}
