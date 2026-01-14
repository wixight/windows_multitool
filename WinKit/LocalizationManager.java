import java.util.HashMap;
import java.util.Map;

public class LocalizationManager {
    private final Map<String, String[]> langMap = new HashMap<>();
    private boolean isRussian = true;

    public LocalizationManager() {
        langMap.put("btn.save", new String[]{"Save", "Сохранить"});
        langMap.put("msg.error", new String[]{"Error", "Ошибка"});
    }

    // Метод: переключить язык
    public void setRussian(boolean isRussian) {
        this.isRussian = isRussian;
    }

    // Метод: дай мне слово по ключу
    public String getString(String key) {
        String[] val = langMap.get(key);
        if (val == null) {
            return key;
        }
        return isRussian ? val[1] : val[0];
    }
}