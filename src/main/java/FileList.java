import java.io.File;
import java.util.Objects;

public class FileList {
    public static void main(String[] args) {
        String dirPath = "/media/prabeer-pc/New Volume2/videos/Dashcam/DCIMB/";

        File convertToDir = new File(dirPath);

        for (File videoName : Objects.requireNonNull(convertToDir.listFiles())) {
            if (convertToDir.isDirectory() && videoName.isFile()) {

                System.out.println("File Name : " + videoName.getName());
            } else {
                System.out.println("Dir : " + convertToDir.getName());
            }
        }

    }
}
