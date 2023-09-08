package YiChe_Demo.Dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;

public class DaoFather {
    //父类dao
    private Connection  conn= null;
    private Statement stmt = null;
    //sql操作 所需参数
    protected String driverName;
    protected String connectionUrl;
    protected String userPass;
    protected String userName;
    protected String primaryKey;
    protected String tableName;
    protected String beanName;

    public void MethodCreateSomeObject(){
        try{
            Class.forName(this.driverName);//注册驱动
            if (null==conn ||conn.isClosed()){
                conn = DriverManager.getConnection(this.connectionUrl, this.userName, this.userPass);//创建链接
            }
            if (null==stmt||stmt.isClosed()){
                stmt= conn.createStatement();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void MethodIUD(String str){
        MethodCreateSomeObject();
        try{
            stmt.executeUpdate(str.replace("\n","")
                    .replace("\t","").replace("\r",""));
            //替换插入语句中的特殊情况 例如换行等
            conn.close();
            stmt.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }



}
