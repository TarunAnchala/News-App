package com.assigment.newsapp;

import androidx.lifecycle.ViewModelProviders;
import androidx.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class NewsJUnitTest {

    public static NewsViewModel newsViewModel;

    @ClassRule
    public static final ActivityTestRule<MainActivity> activity = new ActivityTestRule<>(MainActivity.class);

    @BeforeClass
    public static void initialize() {
        newsViewModel = ViewModelProviders.of(activity.getActivity()).get(NewsViewModel.class);
    }

    @Test
    public void testNewsLiveData() {
        assertNotNull(newsViewModel.getNewsData());
    }

    @Test
    public void testNewsList() {
        assertNotNull(newsViewModel.getListOfNews());
    }

    @Test
    public void testNewsCount() {
        if (newsViewModel.getNewsData().getValue() != null) {
            assertNotEquals(0, newsViewModel.getNewsData().getValue().size());
        }
    }

    @Test
    public void testNewsObj() {
        if (newsViewModel.getNewsData().getValue() != null && newsViewModel.getNewsData().getValue().size() > 0) {
            assertNotNull(newsViewModel.getNewsData().getValue().get(0));
        }
    }

    @Test
    public void validateSize() {
        if (newsViewModel.getNewsData().getValue() != null) {
            assertEquals(newsViewModel.getNewsData().getValue().size(), newsViewModel.getListOfNews().size());
        }
    }

    @AfterClass
    public static void tearDown() {
        newsViewModel = null;
    }

}
