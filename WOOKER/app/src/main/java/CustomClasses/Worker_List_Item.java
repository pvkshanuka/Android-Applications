package CustomClasses;

import com.app.wooker.ClientMain;
import com.app.wooker.DBClasses.User;

public class Worker_List_Item implements Comparable {

    User worker;
    int job_count;
    double rating;

    public Worker_List_Item(User worker, int job_count, double rating) {
        this.worker = worker;
        this.job_count = job_count;
        this.rating = rating;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public int getJob_count() {
        return job_count;
    }

    public void setJob_count(int job_count) {
        this.job_count = job_count;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

//    @Override
//    public int compareTo(Worker_List_Item comparestu) {
//        double compareage=((Worker_List_Item)comparestu).getRating();
//        /* For Ascending order*/
//        return Double.compare(this.rating,compareage);
//
//        /* For Descending order do like this */
//        //return compareage-this.studentage;
//    }

    @Override
    public int compareTo(Object o) {
        double compareage = ((Worker_List_Item) o).getRating();
        /* For Ascending order*/
        if (ClientMain.mUser.getGender().equals("Female")) {

            if (((Worker_List_Item) o).getWorker().getGender().equals("Male") && this.getWorker().getGender().equals("Female")) {
                return -1;
            } else if (((Worker_List_Item) o).getWorker().getGender().equals("Female") && this.getWorker().getGender().equals("Male")) {
                return 1;
            } else if (((Worker_List_Item) o).getWorker().getGender().equals("Female") && this.getWorker().getGender().equals("Female")) {
                return Double.compare(compareage, this.rating);
            } else {
                return Double.compare(compareage, this.rating);
            }

        } else {
            return Double.compare(compareage, this.rating);
        }
    }
}
