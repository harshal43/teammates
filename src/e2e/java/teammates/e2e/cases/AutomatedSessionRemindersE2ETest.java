package teammates.e2e.cases;

import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.util.Const;
import teammates.common.util.TimeHelper;
import teammates.common.util.TimeHelperExtension;
import teammates.e2e.util.TestProperties;

/**
 * SUT: {@link Const.CronJobURIs#AUTOMATED_FEEDBACK_OPENING_REMINDERS},
 *      {@link Const.CronJobURIs#AUTOMATED_FEEDBACK_CLOSING_REMINDERS},
 *      {@link Const.CronJobURIs#AUTOMATED_FEEDBACK_CLOSED_REMINDERS},
 *      {@link Const.CronJobURIs#AUTOMATED_FEEDBACK_PUBLISHED_REMINDERS}.
 */
public class AutomatedSessionRemindersE2ETest extends BaseE2ETestCase {

    @Override
    protected void prepareTestData() {
        testData = loadDataBundle("/AutomatedSessionRemindersE2ETest.json");

        // When running the test against a production server, email alerts will be sent
        // to the specified email address
        // The tester should manually check the email box after running the test suite
        // TODO check if we can automate this checking process

        String student1Email = TestProperties.TEST_EMAIL;
        testData.accounts.get("instructorWithEvals").setEmail(student1Email);
        testData.instructors.get("AutSesRem.instructor").setEmail(student1Email);
        testData.students.get("alice.tmms@AutSesRem.course").setEmail(student1Email);
        testData.feedbackSessions.get("closedSession").setCreatorEmail(student1Email);
        testData.feedbackSessions.get("closingSession").setCreatorEmail(student1Email);
        testData.feedbackSessions.get("openingSession").setCreatorEmail(student1Email);
        testData.feedbackSessions.get("publishedSession").setCreatorEmail(student1Email);

        // Set closing time of one feedback session to tomorrow
        FeedbackSessionAttributes closingFeedbackSession = testData.feedbackSessions.get("closingSession");
        closingFeedbackSession.setEndTime(TimeHelper.getInstantDaysOffsetFromNow(1));

        // Set closing time of one feedback session to 30 mins ago
        FeedbackSessionAttributes closedFeedbackSession = testData.feedbackSessions.get("closedSession");
        closedFeedbackSession.setEndTime(TimeHelperExtension.getInstantMinutesOffsetFromNow(-30));

        // Set opening time for one feedback session to yesterday
        FeedbackSessionAttributes openingFeedbackSession = testData.feedbackSessions.get("openingSession");
        openingFeedbackSession.setStartTime(TimeHelper.getInstantDaysOffsetFromNow(-1));

        // Published time for one feedback session already set to some time in the past.

        removeAndRestoreDataBundle(testData);
    }

    @Override
    protected void prepareBrowser() {
        // this test does not require any browser
    }

    @Test
    @Override
    public void testAll() {
        testFeedbackSessionOpeningReminders();
        testFeedbackSessionClosingReminders();
        testFeedbackSessionClosedReminders();
        testFeedbackSessionPublishedReminders();
    }

    private void testFeedbackSessionOpeningReminders() {
        BACKDOOR.executeGetRequest(Const.CronJobURIs.AUTOMATED_FEEDBACK_OPENING_REMINDERS, null);
    }

    private void testFeedbackSessionClosingReminders() {
        BACKDOOR.executeGetRequest(Const.CronJobURIs.AUTOMATED_FEEDBACK_CLOSING_REMINDERS, null);
    }

    private void testFeedbackSessionClosedReminders() {
        BACKDOOR.executeGetRequest(Const.CronJobURIs.AUTOMATED_FEEDBACK_CLOSED_REMINDERS, null);
    }

    private void testFeedbackSessionPublishedReminders() {
        BACKDOOR.executeGetRequest(Const.CronJobURIs.AUTOMATED_FEEDBACK_PUBLISHED_REMINDERS, null);
    }

}
