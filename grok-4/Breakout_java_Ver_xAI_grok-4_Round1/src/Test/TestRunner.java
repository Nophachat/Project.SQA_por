package Test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({PaddleTest.class, BallTest.class, BrickTest.class, GamePanelTest.class})
public class TestRunner {
    // คลาสนี้ใช้สำหรับรัน test ทั้งหมดอัตโนมัติ
}