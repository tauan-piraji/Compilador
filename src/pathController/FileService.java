package pathController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileService{

    private FileMain fileMain;

    public boolean verifyExistence(FileMain file) {
        File f = new File(file.getAdress() + "\\" + file.getName());

        return f.exists();
    }

    public void createFile(FileMain entity) throws IOException {
        File file = new File(entity.getAdress() + "\\" + entity.getName());
        if (verifyExistence(entity)) {
            System.out.println("Arquivo já existe");
        } else {
            file.createNewFile();
            System.out.println("Arquivo criado");
        }
    }

    public void readEntireFile(FileMain entity) throws FileNotFoundException, IOException {
        InputStream is;
        if (entity.getAdress().equalsIgnoreCase("")) {
            is = new FileInputStream(entity.getName());
        }else{
            is = new FileInputStream(entity.getAdress() + "\\" + entity.getName());
        }

        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        while(s!=null){
            System.out.println(s);
            s = br.readLine();
        }
        br.close();
    }

    public void rewriteOnFile(FileMain entity, String text) throws FileNotFoundException, IOException{
        OutputStream os = new FileOutputStream(entity.getAdress() + "\\" + entity.getName());
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(text);
        bw.close();
    }

    public void writeOnFile(FileMain entity, String text) throws FileNotFoundException, IOException{
        OutputStream os = new FileOutputStream(entity.getAdress() + "\\" + entity.getName(), true);
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);

        bw.write(text);
        bw.close();
    }

    public void writeOnNextFileLine(FileMain entity, String text) throws FileNotFoundException, IOException{
        OutputStream os = new FileOutputStream(entity.getAdress() + "\\" + entity.getName(), true);
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.newLine();
        bw.write(text);
        bw.close();
    }

    public void moveFile(FileMain entity, String path){
        File f = new File(entity.getAdress() + "\\" + entity.getName());
        File des = new File(path + "\\" + entity.getName());

        f.renameTo(des);
    }

    public List<String> catchLineList(FileMain entity) throws FileNotFoundException, IOException{
        List<String> lines = new ArrayList();
        InputStream is;
        if (entity.getAdress().equalsIgnoreCase("")) {
            is = new FileInputStream(entity.getName());
        }else{
            is = new FileInputStream(entity.getAdress() + "\\" + entity.getName());
        }
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        while(s!=null){
            lines.add(s);
            s = br.readLine();
        }
        br.close();
        return lines;
    }

    public void createDirectory(String name){
        File file = new File(name);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public boolean verifyDirectoryExistence(String a){
        File file = new File(a);
        return file.exists();
    }

    public int verifyFileCount(String a){
        File file = new File(a);
        int c =0;
        for (File file2 : file.listFiles()) {
            if(file2.isFile()){
                c++;
            }
        }
        return c;
    }

    public List<File> catchAllFiles(String a){
        File file = new File(a);
        List<File> file3 = new ArrayList();
        for (File file2: file.listFiles()) {
            if(file2.isFile()){
                file3.add(file2);
            }
        }
        return file3;
    }

    public void deleteFile(FileMain arq){
        File file = new File(arq.getName(), arq.getAdress());
        file.delete();
    }

}
