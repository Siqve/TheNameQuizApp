package com.example.adne.thenamequizapp.activities;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.adne.thenamequizapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    @Rule
    public ActivityTestRule<QuizActivity> mActivityRule
            = new ActivityTestRule<>(QuizActivity.class);

    @Test
    public void testScoring() {
        // Type text and then press the button.
        String correctGuess = mActivityRule.getActivity().getPersons().get(mActivityRule.getActivity().getCurrentIndex()-1).getName();


        onView(ViewMatchers.withId(R.id.guessEditText)).perform(ViewActions.typeText(correctGuess), ViewActions.closeSoftKeyboard());
        onView(ViewMatchers.withId(R.id.btnGuess)).perform(ViewActions.click());

        // Check that the text was changed.
        onView(ViewMatchers.withId(R.id.scoreTextView))
                .check(ViewAssertions.matches(ViewMatchers.withText("Score: 1/1")));
    }


}