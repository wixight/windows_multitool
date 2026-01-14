import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void testRenaming() {
        // Даем файл "cat.jpg"
        File source = new File("cat.jpg");
        // Просим добавить "_new" и сменить на "png"
        File result = FileUtils.generateOutputName(source, "_new", "png");
        
        // Проверяем: получилось ли имя "cat_new.png"?
        assertEquals("cat_new.png", result.getName());
    }

    @Test
    void testErrorOnNull() {
        // Проверяем ошибку: что будет, если сунуть пустоту вместо файла?
        // Код должен выбросить ошибку (Exception), это правильно
        assertThrows(IllegalArgumentException.class, () -> {
            FileUtils.generateOutputName(null, "_new", "png");
        });
    }
}