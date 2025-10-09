package TestRound3;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({BallTest.class, BreakoutTest.class, BrickTest.class, GamePanelTest.class, PaddleTest.class})
public class TestRunner {
    // คลาสนี้ใช้สำหรับรัน test ทั้งหมดอัตโนมัติ
}