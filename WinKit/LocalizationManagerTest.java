import org.junit.jupiter.api.Test; // Это библиотека JUnit
import static org.junit.jupiter.api.Assertions.*;

class LocalizationManagerTest {

    @Test
    void testRussianLanguage() {
        // 1. Создаем менеджера
        LocalizationManager manager = new LocalizationManager();
        // 2. Включаем русский
        manager.setRussian(true);
        // 3. Проверяем: если попросить "btn.save", он вернет "Сохранить"?
        assertEquals("Сохранить", manager.getString("btn.save"));
    }

    @Test
    void testEnglishLanguage() {
        LocalizationManager manager = new LocalizationManager();
        manager.setRussian(false); // Включаем английский
        // Проверяем: вернет ли он "Save"?
        assertEquals("Save", manager.getString("btn.save"));
    }

    @Test
    void testMissingWord() {
        LocalizationManager manager = new LocalizationManager();
        // Просим слово, которого нет в словаре
        String result = manager.getString("abracadabra");
        // Он должен вернуть саму абракадабру, а не сломаться
        assertEquals("abracadabra", result);
    }
}