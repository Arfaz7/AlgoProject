package HelperPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by arfaz on 15/05/2017.
 */
public class UnzipHelper {

    private static final String INPUT_FOLDER = "Documents/RATP_GTFS_LINES/";
    private static final String OUTPUT_FOLDER = "Documents/SUBWAY_LINES";


    public static void unzip() {

        ArrayList<String> zipFileList = new ArrayList<>();
        File folder = new File(INPUT_FOLDER);
        File[] fileList = folder.listFiles();

        File outputFolder = new File(OUTPUT_FOLDER);

        if(!outputFolder.isDirectory() || outputFolder.listFiles() == null) {
            for(int i=0; i<fileList.length; i++)
            {
                if (fileList[i].isFile()) {
                    String fileName = fileList[i].getName();
                    int fileNameLength = fileName.split("_").length-2;

                    if(fileNameLength > 1 && fileName.split("_")[fileNameLength].equals("METRO")) {
                        zipFileList.add(fileList[i].getName());

                        unzipFile(fileName);
                    }
                }

            }
        }
    }

    private static void unzipFile(String zipFile) {

        byte[] buffer = new byte[1024];

        try{

            String oldFolderName = zipFile.split("\\.")[0];
            String folderName  = oldFolderName.split("_")[2] + "_" + oldFolderName.split("_")[3];
            File folder = new File(OUTPUT_FOLDER + File.separator + folderName);
            if(!folder.exists()){
                folder.mkdir();
            }

            ZipInputStream zipIS = new ZipInputStream(new FileInputStream(INPUT_FOLDER + zipFile));

            ZipEntry zEntry = null;
            zEntry = zipIS.getNextEntry();


            while(zEntry != null){

                String fileName = zEntry.getName();
                File newFile = new File(OUTPUT_FOLDER + File.separator + folderName + File.separator + fileName);

                System.out.println("Unzipping file : "+ newFile.getAbsoluteFile());

                //create all non exists folders
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zipIS.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                zEntry = zipIS.getNextEntry();
            }

            zipIS.closeEntry();
            zipIS.close();

            System.out.println("Done");

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

}
