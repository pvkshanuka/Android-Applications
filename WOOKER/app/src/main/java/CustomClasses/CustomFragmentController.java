package CustomClasses;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.app.wooker.R;

public class    CustomFragmentController {

    private FragmentManager fragmentManager;

    public CustomFragmentController(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setupFragment(Fragment fragment, boolean addToBack) {
        fragmentManager.popBackStack();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right);
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

        if (addToBack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.main_cons_lay, fragment);
        transaction.commit();
    }

    public void setupClientFragment(Fragment fragment, boolean addToBack) {
        fragmentManager.popBackStack();
//        FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.cons_lay_client, fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBack) {
            transaction.addToBackStack(null);
        }
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.cons_lay_client, fragment);
        transaction.commit();
    }

    public void setupWorkerFragment(Fragment fragment, boolean addToBack) {
        System.out.println("setupWorkerFragment");
        fragmentManager.popBackStack();
//        FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.cons_lay_client, fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBack) {
            transaction.addToBackStack(null);
        }
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.cons_lay_worker, fragment);
        transaction.commit();
    }

    public void setupDialog(DialogFragment fragment) {
        fragment.show(fragmentManager, null);
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

}
