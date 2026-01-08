package de.husten1997.changesettings;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SettingEntryTest {

    @Test
    void testConstructorWithParameters() {
        SettingEntry entry = new SettingEntry(true, "testKey", "testValue", 10);

        assertThat(entry.isSettingActive()).isTrue();
        assertThat(entry.getSettingName()).isEqualTo("testKey");
        assertThat(entry.getSettingValue()).isEqualTo("testValue");
        assertThat(entry.getSettingLine()).isEqualTo(10);
    }

    @Test
    void testDefaultConstructor() {
        SettingEntry entry = new SettingEntry();

        assertThat(entry.isSettingActive()).isTrue();
        assertThat(entry.getSettingName()).isNull();
        assertThat(entry.getSettingValue()).isNull();
        assertThat(entry.getSettingLine()).isEqualTo(0);
    }

    @Test
    void testSetters() {
        SettingEntry entry = new SettingEntry();

        entry.setSettingActive(false);
        entry.setSettingName("newKey");
        entry.setSettingValue("newValue");
        entry.setSettingLine(5);

        assertThat(entry.isSettingActive()).isFalse();
        assertThat(entry.getSettingName()).isEqualTo("newKey");
        assertThat(entry.getSettingValue()).isEqualTo("newValue");
        assertThat(entry.getSettingLine()).isEqualTo(5);
    }

    @Test
    void testToString_whenActive() {
        SettingEntry entry = new SettingEntry(true, "testKey", "testValue", 0);

        assertThat(entry.toString()).isEqualTo("testKey: testValue");
    }

    @Test
    void testToString_whenInactive() {
        SettingEntry entry = new SettingEntry(false, "testKey", "testValue", 0);

        assertThat(entry.toString()).isEqualTo("# testKey: testValue");
    }

    @Test
    void testToString_withLineNumber() {
        SettingEntry entry = new SettingEntry(true, "testKey", "testValue", 10);

        assertThat(entry.toString()).isEqualTo("testKey: testValue(Line 10)");
    }

    @Test
    void testToString_inactiveWithLineNumber() {
        SettingEntry entry = new SettingEntry(false, "testKey", "testValue", 10);

        assertThat(entry.toString()).isEqualTo("# testKey: testValue(Line 10)");
    }

    @Test
    void testEquals_sameObject() {
        SettingEntry entry = new SettingEntry(true, "testKey", "testValue", 10);

        assertThat(entry.equals(entry)).isTrue();
    }

    @Test
    void testEquals_sameSettingName() {
        SettingEntry entry1 = new SettingEntry(true, "testKey", "testValue", 10);
        SettingEntry entry2 = new SettingEntry(false, "testKey", "differentValue", 20);

        assertThat(entry1.equals(entry2)).isTrue();
    }

    @Test
    void testEquals_differentSettingName() {
        SettingEntry entry1 = new SettingEntry(true, "testKey1", "testValue", 10);
        SettingEntry entry2 = new SettingEntry(true, "testKey2", "testValue", 10);

        assertThat(entry1.equals(entry2)).isFalse();
    }

    @Test
    void testEquals_nullObject() {
        SettingEntry entry = new SettingEntry(true, "testKey", "testValue", 10);

        assertThat(entry.equals(null)).isFalse();
    }

    @Test
    void testEquals_differentClass() {
        SettingEntry entry = new SettingEntry(true, "testKey", "testValue", 10);
        String differentClass = "testKey";

        assertThat(entry.equals(differentClass)).isFalse();
    }
}
