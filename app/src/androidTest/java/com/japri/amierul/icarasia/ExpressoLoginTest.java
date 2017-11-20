package com.japri.amierul.icarasia;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ExpressoLoginTest {

    private final static String TAG = "ExpressoLoginTest";

    private String email;
    private String password;
    private String mobile;
    private String userType;
    private String firstName;
    private String lastName;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void initVariables(){

        email = "testing@gmail.com";
        password = "aaaaaaaa#";
        mobile = "60123333333";
        userType = "Broker";
        firstName = "Kato";
        lastName = "Shin";

        createDummy();
    }

    private void createDummy() {

        RealmEngine realmEngine = new RealmEngine(mainActivityActivityTestRule.getActivity());

        UserModel exist = realmEngine.checkIfExist(email);

        if(exist == null){
            UserModel userModel = new UserModel();
            userModel.setEmail(email);
            userModel.setPassword(password);
            userModel.setMobile(mobile);
            userModel.setUserType(userType);
            userModel.setFirstName(firstName);
            userModel.setLastName(lastName);

            realmEngine.createUserAcc(userModel,null,null);
        }
    }

    @Test
    public void test1CheckRegisterSuccess(){

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test();
    }

    private void test(){
        onView(withId(R.id.input_username)).perform(typeText(email));

        closeSoftKeyboard();

        onView(withId(R.id.input_password)).perform(typeText(password));

        closeSoftKeyboard();

        onView(withId(R.id.btn_login)).perform(click());

    }
}
