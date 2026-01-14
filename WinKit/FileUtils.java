import java.io.File;

public class FileUtils {

    // Чистая логика: берет файл и придумывает ему новое имя
    public static File generateOutputName(File source, String suffix, String newExtension) {
        if (source == null) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }
        
        String originalName = source.getName();
        int dotIndex = originalName.lastIndexOf('.');
        
        String baseName;
        if (dotIndex == -1) {
            baseName = originalName;
        } else {
            baseName = originalName.substring(0, dotIndex);
        }
        
        // Склеиваем новое имя
        return new File(source.getParent(), baseName + suffix + "." + newExtension.toLowerCase());
    }
}