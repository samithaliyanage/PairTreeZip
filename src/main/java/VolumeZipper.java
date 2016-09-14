import com.beust.jcommander.JCommander;
import com.google.common.base.Joiner;
import gov.loc.repository.pairtree.Pairtree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class VolumeZipper {
    private static Logger log = LoggerFactory.getLogger(VolumeZipper.class);
    private static Pairtree pairtree = new Pairtree();

    public static void main(String[] args) throws IOException {
        Options options = new Options();
        new JCommander(options, args);

        log.info("Starting pairtree-zip with pairtree root: " + options.pairTreeRoot + " and volume list: " + options.volumeList);

        Charset charset = Charset.defaultCharset();
        List<String> volumeList = Files.readAllLines(new File(options.volumeList).toPath(), charset);

        List<String> volumeZips = new ArrayList<String>();

        for (String vol : volumeList) {
            int indexOfFirstPeriod = vol.indexOf('.');
            String volId = pairtree.cleanId(vol.substring(indexOfFirstPeriod + 1));
            String library = vol.substring(0, indexOfFirstPeriod);

            volumeZips.add(String.format("%s/%s/pairtree_root/%s/%s/%s.zip", options.pairTreeRoot, library, Joiner.on("/").join(volId.split("(?<=\\G.{2})")), volId, volId));
        }

        ZipUtil.zip(volumeZips,options.destinationPath);

    }
}
