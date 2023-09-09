package YiChe_Demo.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

//读文件工具类
public class ReadFileUtil {
    public String MethodReadFile(String path,String name){
        String content = "";
        try{
            File file = new File(path+name);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuffer SB = new StringBuffer();
            while ((line=br.readLine())!=null){
                SB.append(line).append("\n");
            }
            content=SB.toString();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return content;
    }
}
