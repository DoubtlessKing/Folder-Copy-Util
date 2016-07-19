package Copy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CopyFileVisitor extends SimpleFileVisitor<Path> {
    private File logFile;
    private String srcPath;
    private String destPath;
    private final String fileExt;
    private final ArrayList<String> log;
    
    CopyFileVisitor(String srcPath, String destPath, String fileExt){
        super();
        this.srcPath = srcPath;
        this.destPath = destPath;
        this.fileExt = fileExt;
        this.log = new ArrayList();
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }
    public void copyFile(File src, File dest){
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;

        try {
            inputChannel = new FileInputStream(src).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CopyFileVisitor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CopyFileVisitor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException ex) {
                Logger.getLogger(CopyFileVisitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
        
    }
@Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr){
        System.out.format("%s", file);
        System.out.println(" "+attr.size()+" bytes");
        if(file.getFileName().toString().contains(fileExt)){
            copyFile(file.toFile(), new File(destPath+"\\"+file.getFileName().toString()));
            log.add(file.getFileName().toString()+" copied from "+file.toString());
        }
        return CONTINUE;
    }
    public void publishLog(){
        logFile = new File(destPath+"\\log.txt");
        try (Writer writer = new BufferedWriter(new FileWriter(logFile))){
           for(String line : log){
               writer.write(line);
               writer.write("\r\n\r\n");
               System.out.print(line);
           }
        } catch (IOException ex) {
            Logger.getLogger(CopyFileVisitor.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }
}

