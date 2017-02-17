import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static Logger log = LoggerFactory.getLogger(VolumeZipper.class);

    public static File zip(List<String[]> srcFiles, String destination) throws IOException {
        // create byte buffer
        byte[] buffer = new byte[1024];
        File temp = new File(destination);
        FileOutputStream fos = new FileOutputStream(temp);
        ZipOutputStream zos = new ZipOutputStream(fos);
        int volumesNotFound = 0;

        for (String[] fl : srcFiles) {
            for (String f : fl){
                File srcFile = new File(f);
                if (!srcFile.exists()) {
                    log.error("Cannot find : " + f);
                    volumesNotFound ++;
                    continue;
                }
                FileInputStream fis = new FileInputStream(srcFile);
                // begin writing a new ZIP entry, positions the stream to the start of the entry data
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int length;
                while ((length = fis.read(buffer)) >= 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                // close the InputStream
                fis.close();
            }


        }

        log.info("Total volume count: " + srcFiles.size());
        log.info("Total volumes not found: " + volumesNotFound);
        // flush and close the ZipOutputStream
        zos.flush();
        zos.close();
        fos.close();
        return temp;

    }
}
