package io.github.kgress.scaffold.extensions;

import com.saucelabs.saucerest.SauceREST;
import io.github.kgress.scaffold.TestContext;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class SauceExtension implements BeforeAllCallback, BeforeTestExecutionCallback, TestWatcher {

    private SauceREST sauce;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        try {
            var environment = SpringExtension.getApplicationContext(context).getEnvironment();
            var sauceUsername = environment.getProperty("desired-capabilities.sauce.user-name");
            var sauceAccessKey = environment.getProperty("desired-capabilities.sauce.access-key");
            sauce = new SauceREST(sauceUsername, sauceAccessKey);
        } catch (Exception e) {
            throw new Exception("Error initializing the Sauce Labs API: Please check your configuration.");
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        getStore(context).put("SESSION_ID", getSessionId());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        // TODO what to do with disabled test?
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        var sessionId = getStore(context).remove("SESSION_ID", String.class);
        sauce.jobPassed(sessionId);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        var sessionId = getStore(context).remove("SESSION_ID", String.class);
        sauce.jobFailed(sessionId);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        var sessionId = getStore(context).remove("SESSION_ID", String.class);
        sauce.jobFailed(sessionId);
    }

    private String getSessionId() {
        return TestContext.baseContext().getSetting(String.class, "SESSION_ID");
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(getClass(), context.getRequiredTestMethod()));
    }
}
