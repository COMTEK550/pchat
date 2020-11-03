import org.junit.Test;
import org.junit.Assert;
import org.junit.Assume;
import junit.framework.TestCase;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.assertj.swing.testing.AssertJSwingTestCaseTemplate;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;

import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.assertj.swing.finder.WindowFinder.findFrame;


public class TestConnectForm extends AssertJSwingTestCaseTemplate {
  private FrameFixture window;

  @BeforeClass
  public static void setUpOnce() {
		//Assume.assumeFalse("Automated UI Test cannot be executed in headless environment", GraphicsEnvironment.isHeadless());
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp() {
    ConnectForm frame = GuiActionRunner.execute(() -> new ConnectForm());
    window = new FrameFixture(frame);
    window.show(); // shows the frame to test
  }

  @Test
  public void shouldCopyTextInLabelWhenClickingButton() {
    window.textBox("textToCopy").enterText("Some random text");
    window.button("copyButton").click();
    window.label("copiedText").requireText("Some random text");
  }

  @After
  public void tearDown() {
    window.cleanUp();
  }
}

/*
public class TestChatForm extends TestCase {
	protected int value1, value2;
	private FrameFixture window;

	protected void setUp() {
	}

	@Test
	public void testTests() {
		String lol = "hej";
		System.out.println(lol);
		Assert.assertEquals(1, 1);
	}

	public void testAssertJ() {
	}
}
*/
