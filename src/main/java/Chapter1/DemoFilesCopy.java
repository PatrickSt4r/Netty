package Chapter1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
* 多级目录拷贝
* */
public class DemoFilesCopy {
    public static void main(String[] args) throws IOException {
        String source = "D:\\123";
        String target = "D:\\123aa";

        Files.walk(Paths.get(source)).forEach(path->{
            String targetName = path.toString().replace(source,target);
            try {
                //是目录
                if(Files.isDirectory(path)){
                    Files.createDirectory(Paths.get(targetName));
                }

                //是普通文件
                else if (Files.isRegularFile(path)){
                    Files.copy(path,Paths.get(targetName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
