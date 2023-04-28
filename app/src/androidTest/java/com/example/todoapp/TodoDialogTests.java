package com.example.todoapp;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import com.example.todoapp.utils.InterruptionViewActions;
import com.example.todoapp.utils.ViewMatcherUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TodoDialogTests {
    private static final String TEST_INPUT_VALUE = "Test todo";
    private static final String TEST_EDIT_VALUE = "Edited Test todo";

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void waitUiToBePreparedForTests() {
        onView(isRoot()).perform(InterruptionViewActions.waitFor(2000L));
    }

    @Test
    public void validate_add_dialog_shows() {
        onView(withId(R.id.todo_add)).perform(click());
        onView(withText(R.string.create_todo)).check(matches(isDisplayed()));
    }

    @Test
    public void validate_todo_displays_on_the_screen() {
        onView(withId(R.id.todo_add)).perform(click());
        onView(withId(R.id.todo_input)).perform(typeText(TEST_INPUT_VALUE));
        onView(withText(R.string.add)).perform(click());
        onView(ViewMatcherUtils.withIndex(withText(TEST_INPUT_VALUE), 1)).check(matches(isDisplayed()));
    }

    @Test
    public void validate_popup_on_todo_item() {
        onView(ViewMatcherUtils.withIndex(withId(R.id.recycler_view), 1))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withText(R.string.delete)).check(matches(isDisplayed()));
    }

    @Test
    public void validate_delete_todo_item() {
        onView(ViewMatcherUtils.withIndex(withId(R.id.recycler_view), 1))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withText(R.string.delete)).perform(click());
        onView(withText(R.string.yes)).perform(click());
        onView(ViewMatcherUtils.withIndex(withText(TEST_INPUT_VALUE), 1)).check(doesNotExist());
    }

    @Test
    public void validate_todo_item_editable() {
        onView(ViewMatcherUtils.withIndex(withId(R.id.recycler_view), 1))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withText(R.string.edit)).perform(click());
        onView(withId(R.id.todo_input))
                .perform(clearText())
                .perform(typeText(TEST_EDIT_VALUE));
        onView(withText(R.string.edit)).perform(click());
        onView(ViewMatcherUtils.withIndex(withText(TEST_EDIT_VALUE), 1)).check(matches(isDisplayed()));
    }
}
