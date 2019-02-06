package com.example.adne.thenamequizapp.activities;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.adne.thenamequizapp.R;
import com.example.adne.thenamequizapp.data.Person;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;

@RunWith(AndroidJUnit4.class)
public class DatabaseActivityTest {

    @Rule
    public ActivityTestRule<DatabaseActivity> mActivityRule
            = new ActivityTestRule<>(DatabaseActivity.class);

    @Test
    public void testAddingPerson() {
        // Type text and then press the button.
        List<Person> personsBefore = new ArrayList<>(mActivityRule.getActivity().getPersons());
        onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.editTextName)).perform(ViewActions.typeText("Bob"),
                ViewActions.closeSoftKeyboard());

        onView(ViewMatchers.withId(R.id.btnSave)).perform(ViewActions.click());

        // Check that the text was changed.
        onView(ViewMatchers.withId(R.id.databaseList)).check(ViewAssertions.matches(ViewMatchers.hasChildCount(personsBefore.size() + 1)));
    }


}