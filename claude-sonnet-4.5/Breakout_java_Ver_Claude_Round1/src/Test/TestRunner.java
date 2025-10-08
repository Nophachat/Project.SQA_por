package Test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test Suite สำหรับรัน tests ทั้งหมด
 */
@Suite
@SelectClasses({
    BallTest.class,
    PaddleTest.class,
    BrickTest.class,
    GamePanelTest.class,
    BreakoutGameTest.class
})
public class TestRunner {
}