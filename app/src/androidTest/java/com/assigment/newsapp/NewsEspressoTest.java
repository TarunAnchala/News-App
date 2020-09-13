package com.assigment.newsapp;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class NewsEspressoTest {

    @Rule
    public final ActivityTestRule<MainActivity> activity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkVisibility() {
        onView(withId(R.id.newsList)).check(matches(isDisplayed()));
    }

    @Test
    public void scrollToPosition() {
        setDelay(6000);
        onView(withId(R.id.newsList)).perform(navigate(5));
    }

    private ViewAction navigate(int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "navigate";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView recyclerView = (RecyclerView) view;
                RecyclerView.Adapter adapter = ((RecyclerView) view).getAdapter();
                Assert.assertNotNull(adapter);
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    Assert.assertNotNull(recyclerView.getChildAt(i));
                    uiController.loopMainThreadForAtLeast(3000);
                    if (i == position) {
                        recyclerView.getChildAt(position).performClick();
                        uiController.loopMainThreadForAtLeast(3000);
                    }
                }
            }
        };
    }

    public ViewAction setDelay(int delay) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "navigate";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(delay);
            }
        };
    }
}
