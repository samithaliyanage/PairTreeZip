import com.beust.jcommander.JCommander;
import com.google.common.base.Joiner;
import gov.loc.repository.pairtree.Pairtree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        List<String> metZips = new ArrayList<String>();
        List<String> jsonZips = new ArrayList<String>();

        File tmp = Files.createTempDirectory("volumes").toFile();

        for (String vol : volumeList) {
            int indexOfFirstPeriod = vol.indexOf('.');
            String volId;
            if (!options.isCleanIds) {
                volId = pairtree.cleanId(vol.substring(indexOfFirstPeriod + 1));
            } else {
                volId = vol.substring(indexOfFirstPeriod + 1);
            }
            String library = vol.substring(0, indexOfFirstPeriod);
            Path volumeRoot;
            if(options.withPairtreeRoot){
                volumeRoot = Files.createDirectories(Paths.get(tmp.getAbsolutePath(),library, "pairtree_root",Joiner.on("/").join(volId.split("(?<=\\G.{2})")),volId));
            }else{
                volumeRoot = Files.createDirectory(Paths.get(tmp.getAbsolutePath(),volId));
            }



            Path volZip = Paths.get(String.format("%s/%s/pairtree_root/%s/%s/%s.zip", options.pairTreeRoot, library, Joiner.on("/").join(volId.split("(?<=\\G.{2})")), volId, volId));

            if (Files.exists(volZip)) {
                Files.copy(volZip, Paths.get(volumeRoot.toString(), String.format("%s.zip", volId)));
            }

            if (options.needMetsAndJsons) {
                Path mets = Paths.get(String.format("%s/%s/pairtree_root/%s/%s/%s.mets.xml", options.pairTreeRoot, library, Joiner.on("/").join(volId.split("(?<=\\G.{2})")), volId, volId));
                if (Files.exists(mets)) {
                    Files.copy(mets, Paths.get(volumeRoot.toString(), String.format("%s.mets.xml", volId)));
                }

                Path json = Paths.get(String.format("%s/%s/pairtree_root/%s/%s/%s.json", options.pairTreeRoot, library, Joiner.on("/").join(volId.split("(?<=\\G.{2})")), volId, volId));
                if (Files.exists(json)) {
                    Files.copy(json, Paths.get(volumeRoot.toString(), String.format("%s.json", volId)));
                }
            }

        }

        org.zeroturnaround.zip.ZipUtil.pack(tmp, new File(options.destinationPath));

        System.out.println("Done!");
    }
}
